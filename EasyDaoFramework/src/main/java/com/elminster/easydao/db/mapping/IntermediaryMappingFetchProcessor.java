package com.elminster.easydao.db.mapping;

import java.lang.reflect.Field;

import com.elminster.common.constants.Constants.StringConstants;
import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.ISqlAnalyzer;
import com.elminster.easydao.db.analyze.ORMFetchAnalyzer;
import com.elminster.easydao.db.analyze.data.MappingSqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.annotation.Mapping;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.manager.DAOSupportSession;

public class IntermediaryMappingFetchProcessor implements IMappingProcessor {

  @Override
  public MappingSqlStatementInfo processMapping(DAOSupportSession session, Field mappingField, Object obj) throws MappingException {
    MappingSqlStatementInfo mappingInfo = new MappingSqlStatementInfo();
    ISqlAnalyzer mappingAnalyzer = new ORMFetchAnalyzer(session);
    Mapping mapping = mappingField.getAnnotation(Mapping.class);
    Class<?> mappingClass = mapping.entity();
    try {
      // mapping statement info
      Object newInstance = ReflectUtil.newInstanceViaReflect(mappingClass);
      fillMockKey(newInstance);
      SqlStatementInfo mappingSqlStatement = 
          mappingAnalyzer.parser(null, newInstance);
      MappingSqlStatementInfo msi = new MappingSqlStatementInfo(mappingSqlStatement);
      msi.setMappingClass(mappingField.getType());
      msi.setGenericTypes(ReflectUtil.getGenericFieldType(mappingField));
      msi.setMappingField(mappingField);
      msi.setMappingPolicy(mapping.mappingPolicy());
      mappingInfo.addMappingStatementInfo(msi);
      
      modifyMappingSqlStatement(mapping, msi);
    } catch (Exception e) {
      throw new MappingException("cannot mapping bacause:" +  e);
    }
    return mappingInfo;
  }

  private void modifyMappingSqlStatement(Mapping mapping, SqlStatementInfo mappingSqlStatement) throws MappingException {
    String intermediaryTable = mapping.intermediaryTable();
    String[] intermediaryControlColumn = mapping.intermediaryControlColumn();
    String[] intermediaryUncontrolColumn = mapping.intermediaryUncontrolColumn();
    String sql = mappingSqlStatement.getAnalyzedSqlStatement();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < intermediaryUncontrolColumn.length; i++) {
      sb.delete(0, sb.length());
      sb.append(" IN (SELECT ");
      sb.append(intermediaryUncontrolColumn[i]);
      sb.append(" FROM ");
      sb.append(intermediaryTable);
      boolean first = true;
      for (int j = 0; j < intermediaryControlColumn.length; j++) {
        if (first) {
          sb.append(" WHERE ");
          first = false;
        } else {
          sb.append(" AND ");
        }
        sb.append(intermediaryControlColumn[j]);
        sb.append(" = ?");
      }
      sb.append(")");
      sql = sql.replaceFirst(" = \\?", sb.toString());
    }
    mappingSqlStatement.setAnalyzedSqlStatement(sql);
  }

  private void fillMockKey(Object newInstance) throws MappingException {
    Field[] keyFields = AnnotationUtil.getKeyField(newInstance);
    for (Field field : keyFields) {
      try {
        // assert key only will be short, int, long and String
        Class<?> keyFieldType = field.getType();
        if (Integer.class.isAssignableFrom(keyFieldType)
            || int.class.isAssignableFrom(keyFieldType)
            || Short.class.isAssignableFrom(keyFieldType)
            || short.class.isAssignableFrom(keyFieldType)
            || Long.class.isAssignableFrom(keyFieldType)
            || long.class.isAssignableFrom(keyFieldType)) {
            ReflectUtil.setField(newInstance, field, 0);
        } else if (String.class.isAssignableFrom(keyFieldType)) {
          ReflectUtil.setField(newInstance, field, StringConstants.EMPTY_STRING);
        } else {
          throw new MappingException("key type is not supported key=" + field.getName() + " keyType=" + field.getType());
        }
      } catch (IllegalArgumentException e) {
        throw new MappingException("cannot set key." + e);
      } catch (IllegalAccessException e) {
        throw new MappingException("cannot set key." + e);
      }
    }
  }
}
