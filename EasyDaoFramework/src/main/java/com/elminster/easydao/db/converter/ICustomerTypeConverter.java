package com.elminster.easydao.db.converter;

public interface ICustomerTypeConverter {

  public int getSQLType();
  
  public Class<?> getJavaType();
  
  public Object convert2BeanData(Object dbData);
  
  public Object convert2DBData(Object beanData);
}
