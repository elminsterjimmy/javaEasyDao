package com.elminster.easydao.generator;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.elminster.common.constants.Constants.StringConstants;
import com.elminster.common.util.CollectionUtil;
import com.elminster.common.util.FileUtil;
import com.elminster.common.util.ObjectUtil;
import com.elminster.common.util.StringUtil;
import com.elminster.easydao.data.ClassData;
import com.elminster.easydao.db.converter.ITableNameConverter;
import com.elminster.easydao.db.schema.IColumn;
import com.elminster.easydao.db.schema.ITable;
import com.elminster.easydao.db.util.JavaSqlTypeMap;
import com.elminster.easydao.exception.UnknownTypeException;
import com.elminster.easydao.util.Utils;

/**
 * The Entity generator.
 * 
 * @author jgu
 * @version 1.0
 */
public class EntityGenerator {

  public static final String INDENT = "  ";

  private static final String KEY = INDENT + "@Key\n";

  private static final String METHODS_TEMPLATE = "Methods.template";
  private static final String FIELDS_TEMPLATE = "Fields.template";
  private static final String ENTITY_TEMPLATE = "Entity.template";
  public static final String ENTITY = "Entity";
  private static String ENTITY_TEMPLATE_STRING;

  private static final Log logger = LogFactory.getLog(EntityGenerator.class);

  private ITableNameConverter tableNameConverter;

  /** the variable list. */
  private List<Variable> variables = new ArrayList<Variable>();
  private List<String> importPackages = new ArrayList<String>();

  private Properties methods = new Properties();
  private Properties fields = new Properties();

  static {
    try {
      ENTITY_TEMPLATE_STRING = FileUtil.readFile2String(
          EntityGenerator.class.getClassLoader().getResourceAsStream(ENTITY_TEMPLATE), false);
    } catch (IOException e) {
      // should not happen
      e = null;
    }
  }

  public EntityGenerator() {
    try {
      methods.load(this.getClass().getClassLoader().getResourceAsStream(METHODS_TEMPLATE));
      fields.load(this.getClass().getClassLoader().getResourceAsStream(FIELDS_TEMPLATE));
    } catch (IOException e) {
      // should not happen
      e = null;
    }
  }

  public ClassData generatEntity(ITable table, String packageName) throws UnknownTypeException {
    String tableName = table.getName();
    List<IColumn> columns = table.getColumns();
    String className = tableName;
    if (null != tableNameConverter) {
      className = tableNameConverter.convertTableName(className);
    }
    className = Utils.normalizeName(className);
    className = className + ENTITY;
    importPackages.clear();
    String fields = generateFileds(table, columns);
    String getSetMethods = generateGetSetMethods();
    StringBuilder sb = new StringBuilder(fields.length() + getSetMethods.length() + 1);
    sb.append(fields).append(StringConstants.LF).append(getSetMethods);
    StringBuilder ipsb = new StringBuilder();
    if (CollectionUtil.isNotEmpty(importPackages)) {
      for (String importPackage : importPackages) {
        ipsb.append("import ");
        ipsb.append(importPackage);
        ipsb.append(StringConstants.SEMICOLON);
        ipsb.append(StringConstants.LF);
      }
      ipsb.delete(ipsb.length() - 1, ipsb.length());
    }
    String generated = MessageFormat.format(ENTITY_TEMPLATE_STRING, packageName, tableName, className, sb.toString(),
        ipsb.toString());
    logger.debug("table [" + table.getName() + "] generated source:\n" + generated);
    ClassData cd = new ClassData();
    cd.setClassName(className);
    cd.setPackageName(packageName);
    cd.setContent(generated);
    return cd;
  }

  private String generateGetSetMethods() {
    StringBuilder sb = new StringBuilder();
    String setMethod = methods.getProperty("setMethod");
    String getMethod = methods.getProperty("getMethod");
    String isMethod = methods.getProperty("isMethod");
    for (Variable variable : variables) {
      String varName = variable.getName();
      String varDataType = variable.getDataType();
      sb.append(MessageFormat.format(setMethod, INDENT, StringUtil.capitalize(varName), varDataType, varName));
      if (Boolean.class.getSimpleName().equals(varDataType)) {
        sb.append(MessageFormat.format(isMethod, INDENT, StringUtil.capitalize(varName), varDataType, varName));
      } else {
        sb.append(MessageFormat.format(getMethod, INDENT, StringUtil.capitalize(varName), varDataType, varName));
      }
    }
    return sb.toString();
  }

  private String generateFileds(ITable table, List<IColumn> columns) throws UnknownTypeException {
    variables.clear();
    StringBuilder sb = new StringBuilder();
    IColumn[] pks = table.getPrimaryKeys();
    for (IColumn column : columns) {
      String fieldType = getFieldType(column.getType());
      String fieldName = StringUtil.uncapitalize(Utils.normalizeName(column.getName()));
      String fieldDef = (String) fields.get("entityFiledDef");
      String defString = MessageFormat.format(fieldDef, INDENT, fieldType, fieldName);
      if (ObjectUtil.contains(pks, column)) {
        defString = KEY + defString;
      }
      sb.append(defString);
      Variable var = new Variable();
      var.setDataType(fieldType);
      var.setName(fieldName);
      variables.add(var);
    }
    return sb.toString();
  }

  private String getFieldType(int type) throws UnknownTypeException {
    Class<?> clazz = JavaSqlTypeMap.JAVA_SQL_MAPPING.get(type);
    if (null != clazz) {
      String packageName = clazz.getPackage().getName();
      if (!"java.lang".equals(packageName)) {
        importPackages.add(clazz.getName());
      }
      return clazz.getSimpleName();
    } else {
      throw new UnknownTypeException("Unknown Type: " + type);
    }
  }

  public void setTableNamePatternConverter(ITableNameConverter tableNameConverter) {
    this.tableNameConverter = tableNameConverter;
  }

  class Variable {
    private String name;
    private String dataType;

    /**
     * @return the name
     */
    public String getName() {
      return name;
    }

    /**
     * @param name
     *          the name to set
     */
    public void setName(String name) {
      this.name = name;
    }

    /**
     * @return the dataType
     */
    public String getDataType() {
      return dataType;
    }

    /**
     * @param dataType
     *          the dataType to set
     */
    public void setDataType(String dataType) {
      this.dataType = dataType;
    }
  }
}
