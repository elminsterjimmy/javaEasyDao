package com.elminster.easydao.bean.util;

import java.lang.reflect.Field;
import java.util.Map;

import com.elminster.common.util.CollectionUtil;
import com.elminster.common.util.ReflectUtil;

/**
 * Utility class for copy properties.
 * 
 * @author jgu
 * @version 1.0
 */
abstract public class DXOUtil {
  
  public static Object copyProperties(Object src, Object dest) throws Exception {
    return copyProperties(src, dest, null, null);
  }
  
  public static Object copyProperties(Object src, Object dest, Map<String, String> fieldNameMapping) throws Exception {
    return copyProperties(src, dest, fieldNameMapping, null);
  }
  
  public static Object copyProperties(Object src, Object dest, Map<String, String> fieldNameMapping, Map<String, IDXOValueConverter> fieldValueConverter) throws Exception {
    Class<?> srcClazz = src.getClass();
    Class<?> destClazz = dest.getClass();
    
    Field[] fields = ReflectUtil.getAllField(srcClazz);
    boolean fieldNameMappingAvaliable = CollectionUtil.isNotEmpty(fieldNameMapping);
    boolean fieldValueConverterAvaliable = CollectionUtil.isNotEmpty(fieldValueConverter);
    for (Field field : fields) {
      String srcFieldName = field.getName();
      String destFieldName = field.getName();
      if (fieldNameMappingAvaliable) {
        String mappingName = fieldNameMapping.get(srcFieldName);
        if (null != mappingName) {
          destFieldName = mappingName;
        }
      }
      Field destField = ReflectUtil.getDeclaredField(destClazz, destFieldName);
      if (null != destField) {
        Object value = ReflectUtil.getFieldValue(src, field);
        if (fieldValueConverterAvaliable) {
          IDXOValueConverter converter = fieldValueConverter.get(srcFieldName);
          if (null != converter) {
            value = converter.convert(value);
          }
        }
        ReflectUtil.setField(dest, destField, value);
      }
    }
    return dest;
  }
}
