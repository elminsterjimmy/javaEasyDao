package com.elminster.easydao.db.mapping;

import java.lang.reflect.Field;

import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.ISqlAnalyzer;
import com.elminster.easydao.db.analyze.ORMFetchAnalyzer;
import com.elminster.easydao.db.analyze.ORMInsertAnalyzer;
import com.elminster.easydao.db.analyze.data.MappingSqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.annotation.Mapping;
import com.elminster.easydao.db.manager.DAOSupportSession;

public class DirectMappingInsertProcessor implements IMappingProcessor {

  @Override
  public MappingSqlStatementInfo processMapping(DAOSupportSession session,
      Field mappingField, Object obj) throws MappingException {
    MappingSqlStatementInfo msi = new MappingSqlStatementInfo();
    try {
      Object value = ReflectUtil.getFieldValue(obj, mappingField);
      if (null != value) {
        Mapping mapping = mappingField.getAnnotation(Mapping.class);
        ISqlAnalyzer mappingAnalyzer = new ORMInsertAnalyzer(session);
        Class<?> mappingClass = mapping.entity();
        SqlStatementInfo mappingSqlStatement = mappingAnalyzer.parser(null, obj);
        
        msi = new MappingSqlStatementInfo(mappingSqlStatement);
        msi.setMappingClass(mappingClass);
        msi.setMappingField(mappingField);
        
        InsertPolicy insertPolicy = mapping.insertPolicy();
        if (InsertPolicy.SELECT_INSERT == insertPolicy) {
          SqlStatementInfo selectSqlStatInfo;
          ISqlAnalyzer fetchAnalyzer = new ORMFetchAnalyzer(session);
          selectSqlStatInfo = fetchAnalyzer.parser(null, obj);
          
        }
      }
    } catch (Exception e) {
      throw new MappingException("cannot mapping bacause:" + e);
    }
    return msi;
  }

}
