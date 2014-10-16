package com.elminster.easydao.db.analyze;

import java.lang.reflect.Field;

import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.exception.SqlAnalyzeException;
import com.elminster.easydao.db.manager.DAOSupportSession;

public class ORMDeleteAnalyzer extends ORMSqlAnalyzer {

  public ORMDeleteAnalyzer(DAOSupportSession session) {
    super(session);
  }

  @Override
  protected SqlType getSqlType() {
    return SqlType.UPDATE;
  }

  @Override
  protected void mapping(Object obj) {
    StringBuilder builder = new StringBuilder();

    builder.append("DELETE FROM ");
    builder.append(getTableName(obj));
    Field[] fields = ReflectUtil.getAllField(obj.getClass());
    boolean first = true;
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      if (AnnotationUtil.isKey(field)) {
        if (AnnotationUtil.isColumn(field)) {
          Object value = null;
          try {
            value = ReflectUtil.getFieldValue(obj, field);
            value = AnnotationUtil.getCustomerDBValue(field, value);
          } catch (IllegalArgumentException e) {
            throw new SqlAnalyzeException(e);
          } catch (IllegalAccessException e) {
            throw new SqlAnalyzeException(e);
          }
          if (null != value) {
            if (!first) {
              builder.append(" AND ");
            } else {
              builder.append(" WHERE ");
              first = false;
            }
            builder.append(getColumnName(field, getColumnConverter(obj)));
            builder.append(" = ?");
            analyzedSqlParameters.add(value);
          }

        }
      }
    }
    analyzedSql = builder.toString();
  }
}
