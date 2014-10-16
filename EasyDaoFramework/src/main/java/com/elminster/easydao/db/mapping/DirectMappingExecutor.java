package com.elminster.easydao.db.mapping;

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
import com.elminster.easydao.db.manager.DAOSupportSession;

public class DirectMappingExecutor implements IMappingExecutor {
  
  private ResultSetHandlerFactory resultSetHandlerFactory = ResultSetHandlerFactory
      .getInstance();

  private ISqlExecutor parentExecutor;
  private DAOSupportSession session;

  public DirectMappingExecutor(ISqlExecutor parentExecutor,
      DAOSupportSession session) {
    this.parentExecutor = parentExecutor;
    this.session = session;
  }

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
    // TODO Auto-generated method stub
    return 0;
  }

}
