package com.elminster.easydao.db.annotation.util;

import java.lang.reflect.Field;

import com.elminster.easydao.db.annotation.Column;
import com.elminster.easydao.db.annotation.CustomerType;
import com.elminster.easydao.db.annotation.Key;
import com.elminster.easydao.db.converter.ICustomerTypeConverter;
import com.elminster.easydao.db.exception.SqlAnalyzeException;

abstract public class AnnotationUtil {

  public static boolean isColumn(Field field) {
    Column column = field.getAnnotation(Column.class);
    return null != column;
  }

  public static boolean isKey(Field field) {
    Key key = field.getAnnotation(Key.class);
    return null != key;
  }

  public static Object getCustomerDBValue(Field field, Object value) {
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
          throw new SqlAnalyzeException("The specified Customer Data Converter Class: " + clazz.getName() + " is not an instance of ICustomerDataConverter.");
        }
      } catch (InstantiationException e) {
        throw new SqlAnalyzeException("Can not initalize Customer Data Converter Class: " + clazz.getName());
      } catch (IllegalAccessException e) {
        throw new SqlAnalyzeException("Can not initalize Customer Data Converter Class: " + clazz.getName());
      }
    }
    return value;
  }
  
  public static Object getCustomerBeanValue(Field field, Object value) {
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
          throw new SqlAnalyzeException("The specified Customer Data Converter Class: " + clazz.getName() + " is not an instance of ICustomerDataConverter.");
        }
      } catch (InstantiationException e) {
        throw new SqlAnalyzeException("Can not initalize Customer Data Converter Class: " + clazz.getName());
      } catch (IllegalAccessException e) {
        throw new SqlAnalyzeException("Can not initalize Customer Data Converter Class: " + clazz.getName());
      }
    }
    return value;
  }
  
  public static Class<?> getDBType(Field field) {
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
          throw new SqlAnalyzeException("The specified Customer Data Converter Class: " + clazz.getName() + " is not an instance of ICustomerDataConverter.");
        }
      } catch (InstantiationException e) {
        throw new SqlAnalyzeException("Can not initalize Customer Data Converter Class: " + clazz.getName());
      } catch (IllegalAccessException e) {
        throw new SqlAnalyzeException("Can not initalize Customer Data Converter Class: " + clazz.getName());
      }
    }
    return field.getType();
  }
}
