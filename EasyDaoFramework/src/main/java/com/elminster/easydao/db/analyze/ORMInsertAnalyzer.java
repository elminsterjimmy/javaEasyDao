package com.elminster.easydao.db.analyze;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.elminster.common.util.ReflectUtil;
import com.elminster.common.util.StringUtil;
import com.elminster.easydao.db.analyze.data.GUIDSelectValue;
import com.elminster.easydao.db.analyze.data.SeqenceSelectValue;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
import com.elminster.easydao.db.annotation.Key;
import com.elminster.easydao.db.annotation.KeyPolicy;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.exception.SqlAnalyzeException;
import com.elminster.easydao.id.IdGeneratorManager;
import com.elminster.easydao.id.IdGeneratorNotFoundException;
import com.elminster.easydao.id.UUIDGenerator;

public class ORMInsertAnalyzer extends ORMSqlAnalyzer {

  public ORMInsertAnalyzer() {
    super();
  }

  @Override
  public SqlType getSqlType(String s) {
    return SqlType.UPDATE;
  }

  @Override
  protected AnalyzedSqlData mapping(Object obj) {
    StringBuilder builder = new StringBuilder();

    builder.append("INSERT INTO ");
    builder.append(getTableName(obj));
    builder.append(" (");
    Field[] fields = ReflectUtil.getAllField(obj.getClass());
    boolean first = true;
    List<Object> analyzedSqlParameters = new ArrayList<Object>();
    for (int i = 0; i < fields.length; i++) {
      Object generatedValue = null;
      Field field = fields[i];
      if (AnnotationUtil.isKey(field)) {
        Key key = field.getAnnotation(Key.class);
        KeyPolicy keyPolicy = key.policy();
        // auto inc policy is not necessary to set the key
        if (KeyPolicy.AUTO_INC_POLICY == keyPolicy) {
          continue;
        } else if (KeyPolicy.UUID_POLICY == keyPolicy) {
          try {
            generatedValue = IdGeneratorManager.INSTANCE.getIdGenerator(UUIDGenerator.class.getName()).nextId();
          } catch (IdGeneratorNotFoundException e) {
            throw new SqlAnalyzeException("UUID Id Generator not found.");
          }
        } else if (KeyPolicy.SEQUENCE_POLICY == keyPolicy) {
          String sequenceName = key.sequenceName();
          if (StringUtil.isEmpty(sequenceName)) {
            throw new SqlAnalyzeException("Sequence Name is required for SEQUENCE_POLICY key.");
          }
          generatedValue = new SeqenceSelectValue(getDialect(), sequenceName);;
        } else if (KeyPolicy.GUID_POLICY == keyPolicy) {
          generatedValue = new GUIDSelectValue(getDialect());
        } else if (KeyPolicy.CUSTOM_ID_POLICY == keyPolicy) {
          String customIdGeneratorClass = key.customIdGeneratorClass();
          if (StringUtil.isEmpty(customIdGeneratorClass)) {
            throw new SqlAnalyzeException("Custom Id Generator is required for CUSTOM_ID_POLICY key.");
          }
          try {
            generatedValue = IdGeneratorManager.INSTANCE.getIdGenerator(customIdGeneratorClass).nextId();
          } catch (IdGeneratorNotFoundException e) {
            throw new SqlAnalyzeException("Custom Id Generator not found.", e);
          }
        }
      }
      if (AnnotationUtil.isColumn(field)) {
        if (!first) {
          builder.append(", ");
        } else {
          first = false;
        }
        builder.append(getColumnName(field, getColumnConverter(obj)));
        try {
          Object value;
          if (null != generatedValue) {
            value = generatedValue;
          } else {
            value = ReflectUtil.getFieldValue(obj, field);
            value = AnnotationUtil.getCustomerDBValue(field, value);
          }
          analyzedSqlParameters .add(value);
        } catch (IllegalArgumentException e) {
          throw new SqlAnalyzeException(e);
        } catch (IllegalAccessException e) {
          throw new SqlAnalyzeException(e);
        }
      }
    }
    builder.append(") VALUES (");
    for (int i = 0; i < analyzedSqlParameters.size(); i++) {
      if (0 != i) {
        builder.append(", ");
      }
      builder.append("?");
    }
    builder.append(")");
    String analyzedSql = builder.toString();
    AnalyzedSqlData data = new AnalyzedSqlData();
    data.setAnalyzedSql(analyzedSql);
    data.setAnalyzedSqlParameters(analyzedSqlParameters);
    return data;
  }
}
