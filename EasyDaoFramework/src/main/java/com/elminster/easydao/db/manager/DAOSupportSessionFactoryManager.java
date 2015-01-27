package com.elminster.easydao.db.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.elminster.common.util.CollectionUtil;
import com.elminster.easydao.db.context.DataSourceInfo;
import com.elminster.easydao.db.context.IContext;
import com.elminster.easydao.db.ds.DataSourceFactory;

public class DAOSupportSessionFactoryManager {
  
  private static final DAOSupportSessionFactoryManager INSTANCE = new DAOSupportSessionFactoryManager();

  private Map<String, DAOSupportSessionFactory> sessionFactorys = new HashMap<String, DAOSupportSessionFactory>();

  private DAOSupportSessionFactory defaultSessionFactory;
  
  private final DataSourceFactory datasourceFactory = DataSourceFactory.INSTANCE;

  private DAOSupportSessionFactoryManager() {
  }
  
  public static DAOSupportSessionFactoryManager getSessionManager() {
    return INSTANCE;
  }

  public static DAOSupportSessionFactoryManager getSessionManager(IContext context) {
    INSTANCE.init(context);
    return INSTANCE;
  }
  
  private void init(IContext context) {
    if (null != context) {
      List<DataSourceInfo> dsil = context.getDataSourceInforamtion();
      if (CollectionUtil.isNotEmpty(dsil)) {
        for (DataSourceInfo dsi : dsil) {
          String dsName = dsi.getName();
          DataSource ds = datasourceFactory.getDataSource(context, dsName);
          DAOSupportSessionFactory factory = new DAOSupportSessionFactory(dsName, ds);
          this.putSessionFactory(factory);
        }
      }
    }
  }
  
  /**
   * @return the defaultSessionFactory
   */
  public DAOSupportSessionFactory getDefaultSessionFactory() {
    return defaultSessionFactory;
  }

  /**
   * @param defaultSessionFactory
   *          the defaultSessionFactory to set
   */
  public void setDefaultSessionFactory(DAOSupportSessionFactory defaultSessionFactory) {
    if (null != this.defaultSessionFactory) {
      throw new IllegalStateException("default session factory cannot be set twice.");
    }
    this.defaultSessionFactory = defaultSessionFactory;
  }

  public void putSessionFactory(DAOSupportSessionFactory factory) {
    sessionFactorys.put(factory.getFactoryId(), factory);
  }

  public DAOSupportSessionFactory getSessionFactory(String factoryId) {
    return sessionFactorys.get(factoryId);
  }

  public void removeSessionFactory(DAOSupportSessionFactory factory) {
    sessionFactorys.remove(factory.getFactoryId());
  }
}
