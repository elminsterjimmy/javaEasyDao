package com.elminster.easydao.db.annotation.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.AnalyzeException;
import com.elminster.easydao.db.annotation.Column;
import com.elminster.easydao.db.annotation.ColumnConverter;
import com.elminster.easydao.db.annotation.CustomerType;
import com.elminster.easydao.db.annotation.Entity;
import com.elminster.easydao.db.annotation.Key;
import com.elminster.easydao.db.annotation.Mapping;
import com.elminster.easydao.db.converter.IColumnNameConverter;
import com.elminster.easydao.db.converter.ICustomerTypeConverter;
import com.elminster.easydao.db.id.KeyPolicy;

abstract public class AnnotationUtil {

  public static boolean isColumn(Field field) {
    Column column = field.getAnnotation(Column.class);
    return null != column;
  }

  public static boolean isKey(Field field) {
    Key key = field.getAnnotation(Key.class);
    return null != key;
  }
  
  public static boolean isMapping(Field field) {
    Mapping mapping = field.getAnnotation(Mapping.class);
    return null != mapping;
  }

  public static KeyPolicy getKeyPolicy(Field field) {
    if (isKey(field)) {
      Key key = field.getAnnotation(Key.class);
      return key.policy();
    }
    return null;
  }
  
  public static Field[] getKeyField(Object obj) {
    Class<?> clazz = obj.getClass();
    Field[] fields = ReflectUtil.getAllField(clazz);
    List<Field> keyFields = new ArrayList<Field>();
    for (Field field : fields) {
      if (isKey(field)) {
        keyFields.add(field);
      }
    }
    return keyFields.toArray(new Field[0]);
  }
  
  public static Object[] getKeyValue(Object obj) {
    Field[] keyFields = getKeyField(obj);
    Object[] keyValue = new Object[keyFields.length];
    try {
      for (int i = 0; i < keyValue.length; i++) {
        keyValue[i] = ReflectUtil.getFieldValue(obj, keyFields[i]);
      }
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    return keyValue;
  }
  
  public static Object getCustomerDBValue(Field field, Object value) throws AnalyzeException {
    CustomerType customerData = field.getAnnotation(CustomerType.class);
    if (null != customerData) {
      Class<?> clazz = customerData.className();
      Object customerType;
      try {
        customerType = clazz.newInstance();
        if (customerType instanceof ICustomerTypeConverter) {
          ICustomerTypeConverter converter = (ICustomerTypeConverter) customerType;
          value = converter.convert2DBData(value);
        } else {
          throw new AnalyzeException("The specified Customer Data Converter Class: " + clazz.getName() + " is not an instance of ICustomerDataConverter.");
        }
      } catch (InstantiationException e) {
        throw new AnalyzeException("Can not initalize Customer Data Converter Class: " + clazz.getName());
      } catch (IllegalAccessException e) {
        throw new AnalyzeException("Can not initalize Customer Data Converter Class: " + clazz.getName());
      }
    }
    return value;
  }
  
  public static Object getCustomerBeanValue(Field field, Object value) throws AnalyzeException {
    CustomerType customerData = field.getAnnotation(CustomerType.class);
    if (null != customerData) {
      Class<?> clazz = customerData.className();
      Object customerType;
      try {
        customerType = clazz.newInstance();
        if (customerType instanceof ICustomerTypeConverter) {
          ICustomerTypeConverter converter = (ICustomerTypeConverter) customerType;
          value = converter.convert2BeanData(value);
        } else {
          throw new AnalyzeException("The specified Customer Data Converter Class: " + clazz.getName() + " is not an instance of ICustomerDataConverter.");
        }
      } catch (InstantiationException e) {
        throw new AnalyzeException("Can not initalize Customer Data Converter Class: " + clazz.getName());
      } catch (IllegalAccessException e) {
        throw new AnalyzeException("Can not initalize Customer Data Converter Class: " + clazz.getName());
      }
    }
    return value;
  }
  
  public static Class<?> getDBType(Field field) throws AnalyzeException {
    CustomerType customerData = field.getAnnotation(CustomerType.class);
    if (null != customerData) {
      Class<?> clazz = customerData.className();
      Object customerType;
      try {
        customerType = clazz.newInstance();
        if (customerType instanceof ICustomerTypeConverter) {
          ICustomerTypeConverter converter = (ICustomerTypeConverter) customerType;
          return converter.getJavaType();
        } else {
          throw new AnalyzeException("The specified Customer Data Converter Class: " + clazz.getName() + " is not an instance of ICustomerDataConverter.");
        }
      } catch (InstantiationException e) {
        throw new AnalyzeException("Can not initalize Customer Data Converter Class: " + clazz.getName());
      } catch (IllegalAccessException e) {
        throw new AnalyzeException("Can not initalize Customer Data Converter Class: " + clazz.getName());
      }
    }
    return field.getType();
  }
  
  public static String getTableName(Object obj) throws AnalyzeException {
    Entity entry = obj.getClass().getAnnotation(Entity.class);
    if (null == entry) {
      throw new AnalyzeException("");
    }
    return entry.tableName();
  }

  public static String getColumnConverter(Object obj) {
    ColumnConverter pattern = obj.getClass().getAnnotation(
        ColumnConverter.class);
    if (null == pattern) {
      return null;
    }
    return pattern.convertClass();
  }

  public static String getColumnName(Field field, String converterClass) throws AnalyzeException {
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

  public static String convertPattern(Field field, String converterClass) throws AnalyzeException {
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
        throw new AnalyzeException("Conlumn name converter :" + converterClass + " cannot be instanted.");
      } catch (IllegalAccessException e) {
        throw new AnalyzeException("Conlumn name converter :" + converterClass + " cannot be accessed.");
      } catch (ClassNotFoundException e) {
        throw new AnalyzeException("Conlumn name converter :" + converterClass + " is not found.");
      }
    }
    return rst;
  }
}
