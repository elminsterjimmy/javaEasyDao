package com.elminster.easydao.db.executor;

import com.elminster.easydao.db.analyze.data.SqlStatementInfo;

public class DefaultSqlExecutorFactory implements ISqlExecutorFactory {

	private static DefaultSqlExecutorFactory instance = new DefaultSqlExecutorFactory();
	
	private DefaultSqlExecutorFactory() {}
	
	public static DefaultSqlExecutorFactory getInstance() {
		return instance;
	}
	
	public ISqlExecutor getSQLExecutor(SqlStatementInfo sqlStatementInfo) {
		ISqlExecutor executor = null;
		executor = new SqlExecutor();
		return executor;
	}
}
