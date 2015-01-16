package com.elminster.easydao.db.executor;

import com.elminster.easydao.db.analyze.data.SqlStatementInfo;

public interface ISqlExecutorFactory {

	public ISqlExecutor getSQLExecutor(SqlStatementInfo sqlStatementInfo);
}
