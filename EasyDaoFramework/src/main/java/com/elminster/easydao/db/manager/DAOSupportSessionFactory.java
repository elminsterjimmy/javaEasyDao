package com.elminster.easydao.db.manager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.elminster.easydao.db.config.BaseConfiguration;
import com.elminster.easydao.db.config.IConfiguration;

public class DAOSupportSessionFactory {

  private Log logger = LogFactory.getLog(DAOSupportSessionFactory.class);
  
  private String factoryId;

  private static int nextSessionId = 0;

  private LinkedList<DAOSupportSession> freeSessions = new LinkedList<DAOSupportSession>();

  private Map<Integer, DAOSupportSession> usedSessions = new HashMap<Integer, DAOSupportSession>();
  
  private IConfiguration configuraton;

  private DataSource ds;
  
  public DAOSupportSessionFactory(DataSource ds) {
    this.ds = ds;
    this.configuraton = new BaseConfiguration();
    this.factoryId = generateFactoryId();
  }

  /**
   * FIXME
   * Generate the factory id.
   * @return the factory id
   */
  private String generateFactoryId() {
    return UUID.randomUUID().toString();
  }

  public synchronized DAOSupportSession popDAOSupportSession() throws SQLException {
    DAOSupportSession session;
    do {
      try {
        session = freeSessions.removeLast();
      } catch (NoSuchElementException nse) {
        createNewSession();
        session = freeSessions.removeLast();
      }
    } while (!testOrCloseConnection(session));
    usedSessions.put(session.getId(), session);
    ThreadSessionMap.INSTANCE.putSessionPerThread(Thread.currentThread(), session);
    return session;
  }

  public synchronized void pushDAOSupportSession(DAOSupportSession session)
      throws SQLException {
    usedSessions.remove(session.getId());
    freeSessions.add(session);
    ThreadSessionMap.INSTANCE.removeSessionPerThread(Thread.currentThread(), session);
  }

  public boolean clearFreeSession() {
    if (!freeSessions.isEmpty()) {
      DAOSupportSession session = freeSessions.removeLast();
      session.clearup();
      return true;
    } else {
      return false;
    }
  }
  
  private boolean testOrCloseConnection(DAOSupportSession session) {
    boolean rst = session.testConnection();
    if (!rst) {
      session.clearup();
    }
    return rst;
  }

  private void createNewSession() throws SQLException {
    DAOSupportSession session = new DAOSupportSession();
    session.setConnection(ds.getConnection());
    session.setId(++nextSessionId);
    session.setConfiguraton(configuraton);
    freeSessions.add(session);
  }

  public void shutdown() {
    if (!usedSessions.isEmpty()) {
      logger.warn("some sessions are still used:");
      for (Integer id : usedSessions.keySet()) {
        DAOSupportSession session = usedSessions.remove(id);
        logger.warn("session " + session.getId());
        session.clearup();
      }
    }
    while (clearFreeSession())
      ;
  }

  public IConfiguration getConfiguraton() {
    return configuraton;
  }

  public void setConfiguraton(IConfiguration configuraton) {
    this.configuraton = configuraton;
    updateConfiguration();
  }

  private void updateConfiguration() {
    for (DAOSupportSession session : usedSessions.values()) {
      session.setConfiguraton(configuraton);
    }
  }

  public String getFactoryId() {
    return factoryId;
  }
}
