package com.elminster.easydao.test;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.elminster.common.util.CollectionUtil;
import com.elminster.easydao.db.converter.ICustomerTypeConverter;

public class SplitListConverter implements ICustomerTypeConverter {

  @Override
  public Object convert2BeanData(Object dbData) {
    String stringData = (String) dbData;
    if (null != stringData) {
      String[] tels = stringData.split(",");
      if (null != tels && tels.length > 0) {
        List<Integer> list = new ArrayList<Integer>();
        for (String tel : tels) {
          try {
            list.add(Integer.parseInt(tel));
          } catch (NumberFormatException nfe) {
            throw new RuntimeException(nfe);
          }
        }
        return list;
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object convert2DBData(Object beanData) {
    List<Integer> list = (List<Integer>) beanData;
    StringBuilder sb = new StringBuilder();
    if (CollectionUtil.isNotEmpty(list)) {
      for (Integer i : list) {
        sb.append(String.valueOf(i) + ",");
      }
      if (sb.length() > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      return sb.toString();
    } else {
      return null;
    }
  }

  @Override
  public int getSQLType() {
    return Types.VARCHAR;
  }

  @Override
  public Class<?> getJavaType() {
    return String.class;
  }

}
