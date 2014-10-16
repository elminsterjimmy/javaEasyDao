package com.elminster.easydao.db.mapping;

import java.lang.reflect.Field;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.ISqlAnalyzer;
import com.elminster.easydao.db.analyze.ORMFetchAnalyzer;
import com.elminster.easydao.db.analyze.ORMInsertAnalyzer;
import com.elminster.easydao.db.analyze.AnalyzeException;
import com.elminster.easydao.db.analyze.data.MappingSqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
import com.elminster.easydao.db.annotation.Mapping;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.id.IdGenerator;
import com.elminster.easydao.db.id.IdGeneratorHelper;
import com.elminster.easydao.db.id.NoneIdGenerator;
import com.elminster.easydao.db.manager.DAOSupportSession;

public class IntermediaryMappingInsertProcessor implements IMappingProcessor {
  
  private static final Log logger = LogFactory.getLog(IntermediaryMappingInsertProcessor.class);

  @SuppressWarnings("rawtypes")
  @Override
  public MappingSqlStatementInfo processMapping(DAOSupportSession session,
      Field mappingField, Object obj) throws MappingException {
    MappingSqlStatementInfo msi = new MappingSqlStatementInfo();
    try {
      Object value = ReflectUtil.getFieldValue(obj, mappingField);
      if (null != value) {
        if (value instanceof Collection) {
          // collection
          Collection c = (Collection) value;
          for (Object o : c) {
            msi.addMappingStatementInfo(dealMapping(session, mappingField, o));
          }
        } else if (mappingField.getType().isArray()) {
          // array
        } else {
          // object
          msi.addMappingStatementInfo(dealMapping(session, mappingField, value));
        }
      } else {
        logger.warn("The field " + mappingField.getName() + " of " + obj + " is null.");
      }
    } catch (Exception e) {
      throw new MappingException("cannot mapping bacause:" + e);
    }
    return msi;
  }

  private MappingSqlStatementInfo dealMapping(DAOSupportSession session,
      Field mappingField, Object obj) throws MappingException {
    MappingSqlStatementInfo msi = null;
    Mapping mapping = mappingField.getAnnotation(Mapping.class);
    ISqlAnalyzer mappingAnalyzer = new ORMInsertAnalyzer(session);
    Class<?> mappingClass = mapping.entity();
    // mapping statement info
    SqlStatementInfo mappingSqlStatement;
    try {
      mappingSqlStatement = mappingAnalyzer.parser(null, obj);
    } catch (AnalyzeException e) {
      throw new MappingException(e);
    }
    msi = new MappingSqlStatementInfo(mappingSqlStatement);
    msi.setMappingClass(mappingClass);
    msi.setMappingField(mappingField);
    String sql = upateIntermediary(mapping);
    // intermediary statement info
    MappingSqlStatementInfo intermediary = new MappingSqlStatementInfo();
    intermediary.setAnalyzedSqlStatement(sql);
    intermediary.setAnalyzedSqlType(SqlType.UPDATE);
    intermediary.setIdGenerator(new NoneIdGenerator());
    msi.addUpdateIntermediarySql(intermediary);
    
    InsertPolicy insertPolicy = mapping.insertPolicy();
    if (InsertPolicy.SELECT_INSERT == insertPolicy) {
      SqlStatementInfo selectSqlStatInfo;
      ISqlAnalyzer fetchAnalyzer = new ORMFetchAnalyzer(session);
      try {
        selectSqlStatInfo = fetchAnalyzer.parser(null, obj);
        selectSqlStatInfo = modifySql(selectSqlStatInfo, obj);
        IdGenerator idGenerator = msi.getIdGenerator();
        idGenerator = IdGeneratorHelper.toSelectInsertGenerator(idGenerator, selectSqlStatInfo);
        msi.setIdGenerator(idGenerator);
      } catch (AnalyzeException e) {
        throw new MappingException(e);
      }
    }
    return msi;
  }

  private SqlStatementInfo modifySql(SqlStatementInfo selectSqlStatInfo, Object obj) throws AnalyzeException {
    String sql = selectSqlStatInfo.getAnalyzedSqlStatement();
    // replace select all field to only select keys
    int start = sql.indexOf("SELECT ") + "SELECT ".length();
    int end = sql.indexOf(" FROM ");
    StringBuilder sb = new StringBuilder();
    Field[] allFields = ReflectUtil.getAllField(obj.getClass());
    boolean first = true;
    for (Field field : allFields) {
      if (AnnotationUtil.isColumn(field)) {
        if (AnnotationUtil.isKey(field)) {
          if (first) {
            first = false;
          } else {
            sb.append(", ");
          }
          String columnName = AnnotationUtil.getColumnName(field, AnnotationUtil.getColumnConverter(obj));
          sb.append(columnName);
        }
      }
    }
    sql = sql.replace(sql.subSequence(start, end), sb.toString());
    selectSqlStatInfo.setAnalyzedSqlStatement(sql);
    return selectSqlStatInfo;
  }

  protected String upateIntermediary(Mapping mapping) throws MappingException {
    String intermediaryTable = mapping.intermediaryTable();
    String[] intermediaryControlColumn = mapping.intermediaryControlColumn();
    String[] intermediaryUncontrolColumn = mapping
        .intermediaryUncontrolColumn();
    StringBuilder sql = new StringBuilder();
    sql.append("INSERT INTO ");
    sql.append(intermediaryTable);
    sql.append(" (");
    boolean first = true;
    for (String c : intermediaryControlColumn) {
      if (!first) {
        sql.append(", ");
      } else {
        first = false;
      }
      sql.append(c);
    }
    sql.append(", ");
    first = true;
    for (String c : intermediaryUncontrolColumn) {
      if (!first) {
        sql.append(", ");
      } else {
        first = false;
      }
      sql.append(c);
    }
    sql.append(") VALUES (");
    int size = intermediaryControlColumn.length
        + intermediaryUncontrolColumn.length;
    for (int i = 0; i < size; i++) {
      if (0 != i) {
        sql.append(", ");
      }
      sql.append("?");
    }
    sql.append(")");
    return sql.toString();
  }
}
