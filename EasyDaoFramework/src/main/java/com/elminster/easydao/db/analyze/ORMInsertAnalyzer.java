package com.elminster.easydao.db.analyze;

import java.lang.reflect.Field;

import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
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
      Field field = fields[i];
      if (AnnotationUtil.isKey(field)) {
        KeyPolicy keyPolicy = AnnotationUtil.getKeyPolicy(field);
        // auto inc policy is not necessary to set the key
        if (KeyPolicy.AUTO_INC_POLICY == keyPolicy) {
          continue;
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
          Object value = ReflectUtil.getFieldValue(obj, field);
          value = AnnotationUtil.getCustomerDBValue(field, value);
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
