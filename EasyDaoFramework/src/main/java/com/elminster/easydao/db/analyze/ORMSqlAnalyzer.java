package com.elminster.easydao.db.analyze;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.elminster.easydao.db.annotation.Column;
import com.elminster.easydao.db.annotation.ColumnConverter;
import com.elminster.easydao.db.annotation.Entity;
import com.elminster.easydao.db.converter.IColumnNameConverter;
import com.elminster.easydao.db.exception.SqlAnalyzeException;

abstract public class ORMSqlAnalyzer extends BaseSqlAnalyzer implements
    ISqlAnalyzer {

  public ORMSqlAnalyzer() {
    super();
  }

  @Override
  public void analyzeSql(Method invokedMethod, Object... methodArguments) {
    boolean hasEntry = false;
    for (Object obj : methodArguments) {
      if (null == obj.getClass().getAnnotation(Entity.class)) {
        continue;
      }
      hasEntry = true;
      mapping(obj);
      break;
    }
    if (!hasEntry) {
      throw new SqlAnalyzeException("No Entiry defined.");
    }
  }

  abstract protected void mapping(Object obj);

  protected String getTableName(Object obj) {
    Entity entry = obj.getClass().getAnnotation(Entity.class);
    if (null == entry) {
      throw new SqlAnalyzeException("");
    }
    return entry.tableName();
  }

  protected String getColumnConverter(Object obj) {
    ColumnConverter pattern = obj.getClass().getAnnotation(
        ColumnConverter.class);
    if (null == pattern) {
      return null;
    }
    return pattern.convertClass();
  }

  protected String getColumnName(Field field, String converterClass) {
    String columnName = null;
    Column column = field.getAnnotation(Column.class);
    if (null != column) {
      columnName = column.name();
    }
    if ("".equals(columnName)) {
      columnName = convertPattern(field, converterClass);
    }
    return columnName;
  }

  protected String convertPattern(Field field, String converterClass) {
    String rst = "";
    String fieldName = field.getName();
    rst = fieldName;
    if (null != converterClass) {
      Object converter;
      try {
        converter = Class.forName(converterClass).newInstance();
        if (converter instanceof IColumnNameConverter) {
          rst = ((IColumnNameConverter) converter).convert(fieldName);
        }
      } catch (InstantiationException e) {
        throw new SqlAnalyzeException("Conlumn name converter :" + converterClass + " cannot be instanted.");
      } catch (IllegalAccessException e) {
        throw new SqlAnalyzeException("Conlumn name converter :" + converterClass + " cannot be accessed.");
      } catch (ClassNotFoundException e) {
        throw new SqlAnalyzeException("Conlumn name converter :" + converterClass + " is not found.");
      }
    }
    return rst;
  }
}
