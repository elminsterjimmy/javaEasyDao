package com.elminster.easydao.db.manager;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.elminster.easydao.db.analyze.DefaultSqlAnalyzerFactory;
import com.elminster.easydao.db.analyze.ISqlAnalyzerFactory;
import com.elminster.easydao.db.annotation.DAO;
import com.elminster.easydao.db.executor.DefaultSqlExecutorFactory;
import com.elminster.easydao.db.executor.ISqlExecutorFactory;

/**
 * The DAO support manager.
 * 
 * @author jgu
 * @version 1.0
 */
public class DAOSupportManager {
  
  /** the instance. */
  private static DAOSupportManager instance =  new DAOSupportManager();
	/** the DAO cache. */
	private Map<Class<?>, Object> daoCache = new HashMap<Class<?>, Object>();
	/** the the SQL analyzer factory. */
	private ISqlAnalyzerFactory sqlAnalyzerFactory;
	/** the the SQL executor factory. */
	private ISqlExecutorFactory sqlExecutorFactory;
	/** the DAO support session. */
	private DAOSupportSession session;
	
	/**
	 * Constructor.
	 */
	private DAOSupportManager() {
		this.sqlAnalyzerFactory = DefaultSqlAnalyzerFactory.getInstance();
		this.sqlExecutorFactory = DefaultSqlExecutorFactory.getInstance();
	}
	
	/**
	 * Get the DAO support manager instance.
	 * @return the instance
	 */
	public static DAOSupportManager getInstance() {
	  return instance;
	}
	
	/**
	 * Get the session.
	 * @return the session
	 */
	protected DAOSupportSession getSession() {
    return session;
  }

	/**
	 * Set the session.
	 * @param session the session
	 */
  public void setSession(DAOSupportSession session) {
    this.session = session;
  }

  /**
	 * Set the SQL analyzer factory.
	 * @param sqlAnalyzerFactory the SQL analyzer factory
	 */
	public void setSqlAnalyzerFactoryClass(ISqlAnalyzerFactory sqlAnalyzerFactory) {
		this.sqlAnalyzerFactory = sqlAnalyzerFactory;
	}
	
	/**
	 * Set the SQL executor factory.
   * @param sqlExecutorFactory the SQL executor factory
   */
  public void setSqlExecutorFactory(ISqlExecutorFactory sqlExecutorFactory) {
    this.sqlExecutorFactory = sqlExecutorFactory;
  }
	
	/**
	 * Get the DAO proxy.
	 * @param clazz the DAO class
	 * @return the DAO proxy
	 */
	public Object getDAO(Class<?> clazz) {
		if (null == clazz) {
			return null;
		}
		if (!isDAOClass(clazz)) {
			return null;
		}
		Object obj = daoCache.get(clazz);
		if (null == obj) {
			DAOInvokeHandler handler = new DAOInvokeHandler(this);
			handler.setSqlAnalyzerFactory(sqlAnalyzerFactory);
			handler.setSqlExecutorFactory(sqlExecutorFactory);
			handler.setOriginalClass(clazz);
			obj = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, handler);
			daoCache.put(clazz, obj);
		}
		return obj;
	}

	/**
	 * Check the class is DAO class or not.
	 * @param clazz the class
	 * @return is DAO class or not
	 */
	private boolean isDAOClass(Class<?> clazz) {
		return null != clazz.getAnnotation(DAO.class);
	}
}
