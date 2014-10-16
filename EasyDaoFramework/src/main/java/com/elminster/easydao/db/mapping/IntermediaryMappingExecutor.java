package com.elminster.easydao.db.mapping;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.elminster.common.util.CollectionUtil;
import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.data.MappingSqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.executor.ExecuteException;
import com.elminster.easydao.db.executor.ISqlExecutor;
import com.elminster.easydao.db.handler.IResultSetHandler;
import com.elminster.easydao.db.handler.ResultSetHandlerFactory;
import com.elminster.easydao.db.id.ISelectInsertIdGenerator;
import com.elminster.easydao.db.id.IdGenerateException;
import com.elminster.easydao.db.id.IdGenerator;
import com.elminster.easydao.db.manager.DAOSupportSession;

public class IntermediaryMappingExecutor implements IMappingExecutor {

  private ResultSetHandlerFactory resultSetHandlerFactory = ResultSetHandlerFactory
      .getInstance();

  private ISqlExecutor parentExecutor;
  private DAOSupportSession session;

  public IntermediaryMappingExecutor(ISqlExecutor parentExecutor,
      DAOSupportSession session) {
    this.parentExecutor = parentExecutor;
    this.session = session;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public Object executeQuery(SqlStatementInfo sqlStatementInfo,
      Object parentResult) throws ExecuteException {
    Object rtn = parentResult;
    List<MappingSqlStatementInfo> mappingStats = sqlStatementInfo
        .getMappingSqlStatementInfo();
    if (CollectionUtil.isNotEmpty(mappingStats)) {
      for (MappingSqlStatementInfo mappingStat : mappingStats) {
        List<MappingSqlStatementInfo> mappingInfos = mappingStat
            .getMappingStatementInfo();
        if (CollectionUtil.isNotEmpty(mappingInfos)) {
          try {
            for (MappingSqlStatementInfo stat : mappingInfos) {
              if (parentResult instanceof Collection) {
                Collection collection = (Collection) parentResult;
                for (Object o : collection) {
                  Object[] keyValues = AnnotationUtil.getKeyValue(o);
                  stat.setAnalyzedSqlParameters((List<Object>) CollectionUtil
                      .array2List(keyValues));
                  IResultSetHandler resultSetHandler = resultSetHandlerFactory
                      .getResultSetHandler(stat.getMappingClass(),
                          stat.getGenericTypes());
                  Object mappingObj = parentExecutor.executeQuery(stat,
                      resultSetHandler);
                  ReflectUtil.setField(o, stat.getMappingField(), mappingObj);
                }
              }
            }
          } catch (Exception e) {
            throw new ExecuteException(e);
          }
        }
      }
    }
    return rtn;
  }

  @Override
  public int executeExecute(SqlStatementInfo sqlStatementInfo,
      Object parentResult) throws ExecuteException {
    int updateCount = 0;
    Serializable[] controlId = null;
    try {
      IdGenerator generator = sqlStatementInfo.getIdGenerator();
      if (null != generator) {
        controlId = generator.lastId();
      }
      List<MappingSqlStatementInfo> mappingStats = sqlStatementInfo
          .getMappingSqlStatementInfo();
      if (CollectionUtil.isNotEmpty(mappingStats)) {
        for (MappingSqlStatementInfo mappingStat : mappingStats) {
          List<MappingSqlStatementInfo> mappingInfos = mappingStat
              .getMappingStatementInfo();
          if (CollectionUtil.isNotEmpty(mappingInfos)) {
            for (MappingSqlStatementInfo stat : mappingInfos) {

              Serializable[] uncontrolId = null;
              generator = stat.getIdGenerator();
              if (null != generator) {
                if (generator instanceof ISelectInsertIdGenerator) {
                  // try to select it first.
                  uncontrolId = ((ISelectInsertIdGenerator) generator)
                      .selectId(session);
                  if (null == uncontrolId) {
                    updateCount += executeStatInfoData(stat);
                    uncontrolId = generator.lastId();
                  }
                } else {
                  updateCount += executeStatInfoData(stat);
                  uncontrolId = generator.lastId();
                }
              } else {
                updateCount += executeStatInfoData(stat);
              }

              List<MappingSqlStatementInfo> updateIntermediary = stat
                  .getUpdateIntermediaryStatInfo();
              if (CollectionUtil.isNotEmpty(updateIntermediary)) {
                for (SqlStatementInfo info : updateIntermediary) {
                  if (CollectionUtil.isEmpty(info.getAnalyzedSqlParameters())) {
                    if (null == controlId || null == uncontrolId) {
                      throw new IllegalStateException("controlId or uncontrolId is null.");
                    }
                    info.setAnalyzedSqlParameters(controlId, uncontrolId);
                  }
                  updateCount += parentExecutor.executeUpdate(info);
                }
              }
            }
          }
          List<MappingSqlStatementInfo> updateStats = mappingStat.getUpdateIntermediaryStatInfo();
          if (CollectionUtil.isNotEmpty(updateStats)) {
            for (MappingSqlStatementInfo updateStat : updateStats) {
              updateCount += parentExecutor.executeUpdate(updateStat);
            }
          }
        }
      }
    } catch (IdGenerateException e) {
      throw new ExecuteException(e);
    }
    return updateCount;
  }

  private int executeStatInfoData(MappingSqlStatementInfo stat)
      throws ExecuteException {
    return parentExecutor.executeUpdate(stat);
  }
}
