package com.elminster.easydao.db.mapping;

import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.executor.ExecuteException;

public interface IMappingExecutor {

  public Object executeQuery(SqlStatementInfo sqlStatementInfo,
      Object parentResult) throws ExecuteException;

  public int executeExecute(
      SqlStatementInfo sqlStatementInfo, Object parentResult)
      throws ExecuteException;
}
