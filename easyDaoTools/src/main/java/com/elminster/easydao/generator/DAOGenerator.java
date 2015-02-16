package com.elminster.easydao.generator;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.elminster.common.util.FileUtil;
import com.elminster.easydao.data.ClassData;

/**
 * The DAO generator.
 * 
 * @author jgu
 * @version 1.0
 */
public class DAOGenerator {

  private static final String DAO_TEMPLATE = "DAO.template";
  private static final String BASE_DAO_TEMPLATE = "BaseDAO.template";
  private static final String DAO = "DAO";
  private static String DAO_TEMPLATE_STRING;
  private static String BASE_DAO_TEMPLATE_STRING;
  
  private static final Log logger = LogFactory.getLog(DAOGenerator.class);
  private static final String BASE_DAO_NAME = "BaseDAO";
  
  static {
    try {
      BASE_DAO_TEMPLATE_STRING = FileUtil.readFile2String(
          DAOGenerator.class.getClassLoader().getResourceAsStream(BASE_DAO_TEMPLATE), false);
      DAO_TEMPLATE_STRING = FileUtil.readFile2String(
          DAOGenerator.class.getClassLoader().getResourceAsStream(DAO_TEMPLATE), false);
    } catch (IOException e) {
   // should not happen
      e = null;
    }
  }
  
  public DAOGenerator() {
  }
  
  public ClassData generatDAO(ClassData entityClassData, String packageName) {
    String entityName = entityClassData.getClassName();
    String className = entityName + DAO;
    String fullName = entityClassData.getFullName();
    String generated = MessageFormat.format(DAO_TEMPLATE_STRING, packageName, fullName, className, entityName);
    logger.debug("entity [" + entityName + "] generated source:\n" + generated);
    ClassData cd = new ClassData();
    cd.setClassName(className);
    cd.setPackageName(packageName);
    cd.setContent(generated);
    return cd;
  }
  
  public ClassData geterateBaseDAO(String packageName) {
    String generated = MessageFormat.format(BASE_DAO_TEMPLATE_STRING, packageName);
    logger.debug("base DAO generated source:\n" + generated);
    ClassData cd = new ClassData();
    cd.setClassName(BASE_DAO_NAME);
    cd.setPackageName(packageName);
    cd.setContent(generated);
    return cd;
  }
}
