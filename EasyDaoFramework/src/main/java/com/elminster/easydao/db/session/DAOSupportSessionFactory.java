package com.elminster.easydao.db.session;

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
import com.elminster.easydao.id.IdGenerator;
import com.elminster.easydao.id.internal.InternalIdGenerator;

public class DAOSupportSessionFactory {

  private Log logger = LogFactory.getLog(DAOSupportSessionFactory.class);
  
  private String factoryId;
  
  private static final IdGenerator sessionIdGenerator = new InternalIdGenerator();
  
  private LinkedList<DAOSupportSession> freeSessions = new LinkedList<DAOSupportSession>();

  private Map<Long, DAOSupportSession> usedSessions = new HashMap<Long, DAOSupportSession>();
  
  private IConfiguration configuraton;

  private DataSource ds;
  
  public DAOSupportSessionFactory(String factoryId, DataSource ds) {
    this.ds = ds;
    this.configuraton = new BaseConfiguration();
    this.factoryId = factoryId;
  }
  
  public DAOSupportSessionFactory(DataSource ds) {
    this(UUID.randomUUID().toString(), ds);
  }

  public synchronized DAOSupportSession popDAOSupportSession() throws SQLException {
    DAOSupportSession session;
    do {
      try {
        session = freeSessions.removeLast();
        session.update();
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
    session.clearup();
    usedSessions.remove(session.getId());
    freeSessions.add(session);
    ThreadSessionMap.INSTANCE.removeSessionPerThread(Thread.currentThread(), session);
  }

  public boolean clearFreeSession() throws SQLException {
    if (!freeSessions.isEmpty()) {
      DAOSupportSession session = freeSessions.removeLast();
      session.close();
      return true;
    } else {
      return false;
    }
  }
  
  private boolean testOrCloseConnection(DAOSupportSession session) throws SQLException {
    boolean rst = session.testConnection();
    if (!rst) {
      session.close();
    }
    return rst;
  }

  private void createNewSession() throws SQLException {
    DAOSupportSession session = new DAOSupportSession();
    session.setConnection(ds.getConnection());
    session.setId((Long)sessionIdGenerator.nextId());
    session.setConfiguraton(configuraton);
    freeSessions.add(session);
  }

  public void shutdown() throws SQLException {
    if (!usedSessions.isEmpty()) {
      logger.warn("some sessions are still used:");
      for (Long id : usedSessions.keySet()) {
        DAOSupportSession session = usedSessions.remove(id);
        logger.warn("session " + session.getId());
        session.close();
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
