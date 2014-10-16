package com.elminster.easydao.db.manager;

import java.util.HashMap;
import java.util.Map;

public class DAOSupportSessionFactoryManager {

  private static DAOSupportSessionFactoryManager manager = new DAOSupportSessionFactoryManager();
  
  private Map<String, DAOSupportSessionFactory> sessionFactorys = new HashMap<String, DAOSupportSessionFactory>();
  
  private DAOSupportSessionFactoryManager() {
  }
  
  public static DAOSupportSessionFactoryManager getSessionManager() {
    return manager;
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
