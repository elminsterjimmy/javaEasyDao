package com.elminster.easydao.db.manager;

import java.lang.reflect.Method;

import com.elminster.easydao.db.analyze.ISqlAnalyzer;
import com.elminster.easydao.db.analyze.ISqlAnalyzerFactory;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.executor.ISqlExecutor;
import com.elminster.easydao.db.executor.ISqlExecutorFactory;

/**
 * Proxy pattern for DAO classes
 * 
 * @author Gu
 * @version 1.0
 *
 */
public class DAOInvokeHandler extends InterfaceInvocationHandler {
	
	private ISqlAnalyzerFactory sqlAnalyzerFactory;
	private ISqlExecutorFactory sqlExecutorFactory;
  
  public DAOInvokeHandler() {
  }
	
	public void setSqlAnalyzerFactory(ISqlAnalyzerFactory sqlAnalyzerFactory) {
		this.sqlAnalyzerFactory = sqlAnalyzerFactory;
	}
	
	public void setSqlExecutorFactory(ISqlExecutorFactory sqlExecutorFactory) {
		this.sqlExecutorFactory = sqlExecutorFactory;
	}
	
	@Override
	protected Object override(Object proxy, Method method, Object[] args) throws Throwable {
		ISqlAnalyzer sqlAnalyzer = sqlAnalyzerFactory.getSqlAnalyzer(method, args);
		sqlAnalyzer.setOriginalClass(this.getOriginalClass());
		SqlStatementInfo sqlStatementInfo = sqlAnalyzer.parser(method, args);
		
		ISqlExecutor sqlExecutor = sqlExecutorFactory.getSQLExecutor(sqlStatementInfo);
		sqlExecutor.setOriginalClass(this.getOriginalClass());
		return sqlExecutor.execute(sqlStatementInfo, method, args);
	}
}
