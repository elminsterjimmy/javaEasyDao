package com.elminster.easydao.db.executor;

import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.handler.IResultSetHandler;

public interface ISqlExecutor {

	public Object executeQuery(SqlStatementInfo sqlStatementInfo,
      IResultSetHandler resultHandler) throws ExecuteException;
	
	public int executeUpdate(SqlStatementInfo sqlStatementInfo) throws ExecuteException;
}
