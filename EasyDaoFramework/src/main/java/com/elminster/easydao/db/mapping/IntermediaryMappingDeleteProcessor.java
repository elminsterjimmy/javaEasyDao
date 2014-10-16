package com.elminster.easydao.db.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.elminster.common.constants.Constants.StringConstants;
import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.AnalyzeException;
import com.elminster.easydao.db.analyze.ISqlAnalyzer;
import com.elminster.easydao.db.analyze.ORMDeleteAnalyzer;
import com.elminster.easydao.db.analyze.data.MappingSqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
import com.elminster.easydao.db.annotation.Entity;
import com.elminster.easydao.db.annotation.Mapping;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.manager.DAOSupportSession;

public class IntermediaryMappingDeleteProcessor implements IMappingProcessor {

  @Override
  public MappingSqlStatementInfo processMapping(DAOSupportSession session,
      Field mappingField, Object obj) throws MappingException {
    MappingSqlStatementInfo mappingInfo = new MappingSqlStatementInfo();
    Mapping mapping = mappingField.getAnnotation(Mapping.class);    
    
    try {
      Field[] keyFields = AnnotationUtil.getKeyField(obj);
      List<Object> keyValues = new ArrayList<Object>();
      for (Field keyField : keyFields) {
        keyValues.add(ReflectUtil.getFieldValue(obj, keyField));
      }
      
      MappingSqlStatementInfo updateIntermediaryInfo = new MappingSqlStatementInfo();
      updateIntermediaryInfo.setAnalyzedSqlStatement(createUpdateIntermediarySql(mapping));
      updateIntermediaryInfo.setAnalyzedSqlType(SqlType.UPDATE);
      updateIntermediaryInfo.setAnalyzedSqlParameters(keyValues);
      mappingInfo.addUpdateIntermediarySql(updateIntermediaryInfo);
      
      UpdatePolicy deletePolicy = mapping.updatePolicy();
      if (UpdatePolicy.CASCADE == deletePolicy) {
        MappingSqlStatementInfo cascadeInfo = createCascadeSql(mapping, mappingField, session);
        cascadeInfo.setAnalyzedSqlType(SqlType.UPDATE);
        cascadeInfo.setAnalyzedSqlParameters(keyValues);
        mappingInfo.addMappingStatementInfo(cascadeInfo);
      }
    } catch (IllegalArgumentException e) {
      throw new MappingException(e);
    } catch (IllegalAccessException e) {
      throw new MappingException(e);
    }
    return mappingInfo;
  }

  private MappingSqlStatementInfo createCascadeSql(Mapping mapping, Field mappingField, DAOSupportSession session) throws MappingException {
    try {
      Class<?> actualType = mapping.entity();
      Entity entity = actualType.getAnnotation(Entity.class);
      if (null == entity) {
        throw new MappingException("mapping type " + actualType + "is not an entity type.");
      }
      // delete from t2 where k2 in (select k2 from t12 where k1 = ?)
      Object obj = ReflectUtil.newInstanceViaReflect(actualType);
      ISqlAnalyzer deleteAnalyzer = new ORMDeleteAnalyzer(session);
      fillMockKey(obj);
      SqlStatementInfo sqlStatInfo = deleteAnalyzer.parser(null, obj);
      modifyMappingSqlStatement(mapping, sqlStatInfo);
      
      MappingSqlStatementInfo msi = new MappingSqlStatementInfo(sqlStatInfo);
      return msi;
    } catch (NoSuchMethodException e) {
      throw new MappingException(e);
    } catch (SecurityException e) {
      throw new MappingException(e);
    } catch (InstantiationException e) {
      throw new MappingException(e);
    } catch (IllegalAccessException e) {
      throw new MappingException(e);
    } catch (IllegalArgumentException e) {
      throw new MappingException(e);
    } catch (InvocationTargetException e) {
      throw new MappingException(e);
    } catch (AnalyzeException e) {
      throw new MappingException(e);
    }
  }

  private String createUpdateIntermediarySql(Mapping mapping) {
    StringBuilder sb = new StringBuilder();
    sb.append("DELETE FROM ");
    sb.append(mapping.intermediaryTable());
    boolean first = true;
    String[] intermediaryControlColumns = mapping.intermediaryControlColumn();
    for (String column : intermediaryControlColumns) {
      if (first) {
        sb.append(" WHERE ");
        first = false;
      } else {
        sb.append(" AND ");
      }
      sb.append(column);
      sb.append(" = ?");
    }
    return sb.toString();
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
