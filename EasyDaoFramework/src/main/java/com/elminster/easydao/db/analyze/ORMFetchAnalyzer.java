package com.elminster.easydao.db.analyze;

import java.lang.reflect.Field;

import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.constants.SqlConstants;
import com.elminster.easydao.db.exception.SqlAnalyzeException;

public class ORMFetchAnalyzer extends ORMSqlAnalyzer {

  public ORMFetchAnalyzer() {
    super();
  }

  @Override
  public SqlType getSqlType() {
    return SqlType.QUERY;
  }

  @Override
  protected void mapping(Object obj) {
    StringBuilder builder = new StringBuilder();
    StringBuilder whereClause = new StringBuilder();

    builder.append("SELECT ");
    Field[] fields = ReflectUtil.getAllField(obj.getClass());
    boolean firstValue = true;
    boolean firstKey = true;
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      if (AnnotationUtil.isColumn(field)) {
        if (firstKey) {
          firstKey = false;
        } else {
          builder.append(", ");
        }
        String columnName = getColumnName(field, getColumnConverter(obj));
        builder.append(columnName);

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
          if (firstValue) {
            whereClause.append(" WHERE ");
            firstValue = false;
          } else {
            whereClause.append(" AND ");
          }
          if (null != value) {
            whereClause.append(columnName);
            if (value instanceof String) {
              String s = (String) value;
              if (s.contains(SqlConstants.LIKE)) {
                whereClause.append(" like ?");
                value = s.replaceAll(SqlConstants.LIKE, "%");
              } else if (s.contains(SqlConstants.NOT)) {
                whereClause.append(" <> ?");
                value = s.replace(SqlConstants.NOT, "");
              } else {
                whereClause.append(" = ?");
              }
            } else {
              whereClause.append(" = ?");
            }
          }
          analyzedSqlParameters.add(value);
        }
      }
    }
    builder.append(" FROM ");
    builder.append(getTableName(obj));
    builder.append(whereClause.toString());
    analyzedSql = builder.toString();
  }
}
