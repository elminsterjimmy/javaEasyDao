package com.elminster.easydao.db.manager;

import java.util.HashMap;
import java.util.Map;

public class DAOSupportSessionFactoryManager {

  private static DAOSupportSessionFactoryManager manager = new DAOSupportSessionFactoryManager();
  
  private Map<String, DAOSupportSessionFactory> sessionFactorys = new HashMap<String, DAOSupportSessionFactory>();
  
  private DAOSupportSessionFactory defaultSessionFactory;
  
  private DAOSupportSessionFactoryManager() {
  }
  
  public static DAOSupportSessionFactoryManager getSessionManager() {
    return manager;
  }
  
  /**
   * @return the defaultSessionFactory
   */
  public DAOSupportSessionFactory getDefaultSessionFactory() {
    return defaultSessionFactory;
  }

  /**
   * @param defaultSessionFactory the defaultSessionFactory to set
   */
  public void setDefaultSessionFactory(
      DAOSupportSessionFactory defaultSessionFactory) {
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
