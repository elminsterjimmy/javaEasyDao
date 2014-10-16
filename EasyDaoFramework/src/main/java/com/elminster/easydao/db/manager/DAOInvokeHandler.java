package com.elminster.easydao.db.manager;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.elminster.common.util.ExceptionUtil;
import com.elminster.easydao.db.analyze.ISqlAnalyzer;
import com.elminster.easydao.db.analyze.ISqlAnalyzerFactory;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.executor.ExecutorProxy;
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
  
  private static Log logger = LogFactory.getLog(DAOInvokeHandler.class);
	
	private ISqlAnalyzerFactory sqlAnalyzerFactory;
	private ISqlExecutorFactory sqlExecutorFactory;
	private DAOSupportManager manager;
  
  public DAOInvokeHandler(DAOSupportManager manager) {
    this.manager = manager;
  }
	
	public void setSqlAnalyzerFactory(ISqlAnalyzerFactory sqlAnalyzerFactory) {
		this.sqlAnalyzerFactory = sqlAnalyzerFactory;
	}
	
	public void setSqlExecutorFactory(ISqlExecutorFactory sqlExecutorFactory) {
		this.sqlExecutorFactory = sqlExecutorFactory;
	}
	
	public DAOSupportSession getDAOSupportSession() {
	  return manager.getSession();
  }
	
	@Override
	protected Object override(Object proxy, Method method, Object[] args) throws Throwable {
	  DAOSupportSession session = getDAOSupportSession();
	  if (null == session) {
      throw new IllegalStateException("Session is NULL!");
    }
	  try {
	    ISqlAnalyzer sqlAnalyzer = sqlAnalyzerFactory.getSqlAnalyzer(method, args, session);
	    sqlAnalyzer.setOriginalClass(this.getOriginalClass());
	    SqlStatementInfo sqlStatementInfo = sqlAnalyzer.parser(method, args);
	    
	    ISqlExecutor sqlExecutor = sqlExecutorFactory.getSQLExecutor(sqlStatementInfo, session);
	    ExecutorProxy exeProxy = new ExecutorProxy(sqlExecutor);
	    exeProxy.setOriginalClass(this.getOriginalClass());
	    return exeProxy.execute(sqlStatementInfo, method, args);
	  } catch (Exception e) {
	    logger.debug(ExceptionUtil.getFullStackTrace(e));
	    throw e;
	  }
	}
}
