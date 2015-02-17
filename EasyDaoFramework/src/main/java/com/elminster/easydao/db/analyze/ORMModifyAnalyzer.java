package com.elminster.easydao.db.analyze;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.exception.SqlAnalyzeException;

public class ORMModifyAnalyzer extends ORMSqlAnalyzer {

  public ORMModifyAnalyzer() {
    super();
  }

  @Override
  public SqlType getSqlType(String s) {
    return SqlType.UPDATE;
  }

  @Override
  protected AnalyzedSqlData mapping(Object obj) {
    StringBuilder builder = new StringBuilder();

    builder.append("UPDATE ");
    builder.append(getTableName(obj));
    builder.append(" SET ");
    Field[] fields = ReflectUtil.getAllField(obj.getClass());
    boolean first = true;
    List<Object> analyzedSqlParameters = new ArrayList<Object>();
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      if (!AnnotationUtil.isKey(field)) {
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
          if (null == value) {
            continue;
          }
          if (!first) {
            builder.append(" , ");
          } else {
            first = false;
          }
          builder.append(getColumnName(field, getColumnConverter(obj)));
          builder.append(" = ?");
          analyzedSqlParameters .add(value);
        }
      }
    }
    first = true;
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      if (AnnotationUtil.isKey(field)) {
        if (AnnotationUtil.isColumn(field)) {
          Object value = null;
          try {
            value = ReflectUtil.getFieldValue(obj, field);
          } catch (IllegalArgumentException e) {
            throw new SqlAnalyzeException(e);
          } catch (IllegalAccessException e) {
            throw new SqlAnalyzeException(e);
          }
          if (null == value) {
            continue;
          }
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
    String analyzedSql = builder.toString();
    AnalyzedSqlData data = new AnalyzedSqlData();
    data.setAnalyzedSql(analyzedSql);
    data.setAnalyzedSqlParameters(analyzedSqlParameters);
    return data;
  }
}