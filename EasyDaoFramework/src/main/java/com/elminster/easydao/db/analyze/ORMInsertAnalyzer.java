package com.elminster.easydao.db.analyze;

import java.lang.reflect.Field;
import java.util.UUID;

import com.elminster.common.util.ReflectUtil;
import com.elminster.common.util.StringUtil;
import com.elminster.easydao.db.analyze.data.GUIDSelectValue;
import com.elminster.easydao.db.analyze.data.SeqenceSelectValue;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
import com.elminster.easydao.db.annotation.Key;
import com.elminster.easydao.db.annotation.KeyPolicy;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.exception.SqlAnalyzeException;
import com.elminster.easydao.db.manager.DAOSupportSession;

public class ORMInsertAnalyzer extends ORMSqlAnalyzer {

  public ORMInsertAnalyzer(DAOSupportSession session) {
    super(session);
  }

  @Override
  public SqlType getSqlType() {
    return SqlType.UPDATE;
  }

  @Override
  protected void mapping(Object obj) {
    StringBuilder builder = new StringBuilder();

    builder.append("INSERT INTO ");
    builder.append(getTableName(obj));
    builder.append(" (");
    Field[] fields = ReflectUtil.getAllField(obj.getClass());
    boolean first = true;
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
          generatedValue = UUID.randomUUID().toString();
        } else if (KeyPolicy.SEQUENCE_POLICY == keyPolicy) {
          String sequenceName = key.sequenceName();
          if (StringUtil.isEmpty(sequenceName)) {
            throw new SqlAnalyzeException("Sequence Name is required for SEQUENCE_POLICY key.");
          }
          generatedValue = new SeqenceSelectValue(getDialect(), sequenceName);;
        } else if (KeyPolicy.GUID_POLICY == keyPolicy) {
          generatedValue = new GUIDSelectValue(getDialect());
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
          analyzedSqlParameters.add(value);
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
    analyzedSql = builder.toString();
  }
}
