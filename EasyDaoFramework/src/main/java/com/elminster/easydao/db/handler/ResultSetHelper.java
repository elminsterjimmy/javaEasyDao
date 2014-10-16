package com.elminster.easydao.db.handler;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.annotation.Column;
import com.elminster.easydao.db.annotation.ColumnConverter;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.converter.IColumnNameConverter;

public class ResultSetHelper {

  @SuppressWarnings("unchecked")
  public static <T> T mapRow(ResultSet rs, Class<T> clazz) throws Exception {
    Object obj;
    if (Integer.class.isAssignableFrom(clazz)
        || int.class.isAssignableFrom(clazz)) {
      obj = rs.getInt(1);
    } else if (Short.class.isAssignableFrom(clazz)
        || short.class.isAssignableFrom(clazz)) {
      obj = rs.getShort(1);
    } else if (Long.class.isAssignableFrom(clazz)
        || long.class.isAssignableFrom(clazz)) {
      obj = rs.getLong(1);
    } else if (Float.class.isAssignableFrom(clazz)
        || float.class.isAssignableFrom(clazz)) {
      obj = rs.getFloat(1);
    } else if (Double.class.isAssignableFrom(clazz)
        || double.class.isAssignableFrom(clazz)) {
      obj = rs.getDouble(1);
    } else if (Byte.class.isAssignableFrom(clazz)
        || byte.class.isAssignableFrom(clazz)) {
      obj = rs.getByte(1);
    } else if (String.class.isAssignableFrom(clazz)) {
      obj = rs.getString(1);
    } else if (Date.class.isAssignableFrom(clazz)) {
      obj = rs.getDate(1);
    } else if (BigDecimal.class.isAssignableFrom(clazz)) {
      obj = rs.getBigDecimal(1);
    } else if (Blob.class.isAssignableFrom(clazz)) {
      obj = rs.getBlob(1);
    } else if (Clob.class.isAssignableFrom(clazz)) {
      obj = rs.getClob(1);
    } else if (Timestamp.class.isAssignableFrom(clazz)) {
      obj = rs.getTimestamp(1);
    } else if (Time.class.isAssignableFrom(clazz)) {
      obj = rs.getTime(1);
    } else {
      obj = clazz.newInstance();
      Field[] fields = ReflectUtil.getAllField(clazz);
      for (int i = 0; i < fields.length; i++) {
        Field field = fields[i];
        if (isColumn(field)) {
          Object value = ResultSetHelper.getValue(rs, getColumnName(field,
              getColumnConverter(clazz)), AnnotationUtil.getDBType(field));
          value = AnnotationUtil.getCustomerBeanValue(field, value);
          ReflectUtil.setField(obj, field, value);
        }
      }
    }
    return (T) obj;
  }

  protected static String getColumnConverter(Class<?> clazz) {
    ColumnConverter pattern = clazz.getAnnotation(ColumnConverter.class);
    if (null == pattern) {
      return null;
    }
    return pattern.convertClass();
  }

  public static boolean isColumn(Field field) {
    Column column = field.getAnnotation(Column.class);
    return null != column;
  }

  protected static String getColumnName(Field field, String converterClass) {
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

  protected static String convertPattern(Field field, String converterClass) {
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
      } catch (IllegalAccessException e) {
      } catch (ClassNotFoundException e) {
      }
    }
    return rst;
  }
  
  public static Object getValue(ResultSet rs, String label,
      Class<?> expectedType) throws SQLException {
    Object value = null;
    if (Integer.class.isAssignableFrom(expectedType)
        || int.class.isAssignableFrom(expectedType)) {
      value = rs.getInt(label);
    } else if (Short.class.isAssignableFrom(expectedType)
        || short.class.isAssignableFrom(expectedType)) {
      value = rs.getShort(label);
    } else if (Long.class.isAssignableFrom(expectedType)
        || long.class.isAssignableFrom(expectedType)) {
      value = rs.getLong(label);
    } else if (Float.class.isAssignableFrom(expectedType)
        || float.class.isAssignableFrom(expectedType)) {
      value = rs.getFloat(label);
    } else if (Double.class.isAssignableFrom(expectedType)
        || double.class.isAssignableFrom(expectedType)) {
      value = rs.getDouble(label);
    } else if (Byte.class.isAssignableFrom(expectedType)
        || byte.class.isAssignableFrom(expectedType)) {
      value = rs.getByte(label);
    } else if (String.class.isAssignableFrom(expectedType)) {
      value = rs.getString(label);
    } else if (Date.class.isAssignableFrom(expectedType)) {
      value = rs.getDate(label);
    } else if (BigDecimal.class.isAssignableFrom(expectedType)) {
      value = rs.getBigDecimal(label);
    } else if (Blob.class.isAssignableFrom(expectedType)) {
      value = rs.getBlob(label);
    } else if (Clob.class.isAssignableFrom(expectedType)) {
      value = rs.getClob(label);
    } else if (Timestamp.class.isAssignableFrom(expectedType)) {
      value = rs.getTimestamp(label);
    } else if (Time.class.isAssignableFrom(expectedType)) {
      value = rs.getTime(label);
    }
    return value;
  }

}
