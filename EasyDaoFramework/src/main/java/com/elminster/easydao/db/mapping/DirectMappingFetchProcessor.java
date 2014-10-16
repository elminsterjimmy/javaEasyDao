package com.elminster.easydao.db.mapping;

import java.lang.reflect.Field;

import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.ISqlAnalyzer;
import com.elminster.easydao.db.analyze.ORMFetchAnalyzer;
import com.elminster.easydao.db.analyze.data.MappingSqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.annotation.Mapping;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.manager.DAOSupportSession;

public class DirectMappingFetchProcessor implements IMappingProcessor {

  @Override
  public MappingSqlStatementInfo processMapping(DAOSupportSession session,
      Field mappingField, Object obj) throws MappingException {
    MappingSqlStatementInfo mappingInfo = new MappingSqlStatementInfo();
    try {
      String columnName = AnnotationUtil.getColumnName(mappingField, AnnotationUtil.getColumnConverter(obj));
      if (null == columnName) {
        throw new MappingException("DIRECT MAPPING NEEDS THE REF COLUMN.");
      }
      Mapping mapping = mappingField.getAnnotation(Mapping.class);
      String directRefColumn = mapping.directRefColumn();
      Class<?> mappingClass = mapping.entity();
      
      ISqlAnalyzer mappingAnalyzer = new ORMFetchAnalyzer(session);
      Object newInstance = ReflectUtil.newInstanceViaReflect(mappingClass);
      ReflectUtil.setField(obj, directRefColumn, ReflectUtil.getFieldValue(obj, mappingField));
      SqlStatementInfo mappingSqlStatement = mappingAnalyzer.parser(null, newInstance);
      MappingSqlStatementInfo msi = new MappingSqlStatementInfo(mappingSqlStatement);
      msi.setMappingClass(mappingField.getType());
      msi.setGenericTypes(ReflectUtil.getGenericFieldType(mappingField));
      msi.setMappingField(mappingField);
      msi.setMappingPolicy(mapping.mappingPolicy());
      mappingInfo.addMappingStatementInfo(msi);
      
    } catch (Exception e) {
      throw new MappingException(e);
    }
    return mappingInfo;
  }

}
