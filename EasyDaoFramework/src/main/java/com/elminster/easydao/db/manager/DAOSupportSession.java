package com.elminster.easydao.db.manager;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.elminster.easydao.db.config.IConfiguration;
import com.elminster.easydao.db.dialect.DialectFactory;
import com.elminster.easydao.db.dialect.IDialect;
import com.elminster.easydao.db.query.IQuery;
import com.elminster.easydao.db.query.Query;

public class DAOSupportSession {

  private static Log logger = LogFactory.getLog(DAOSupportSession.class);

  private DialectFactory dialectFactory = DialectFactory.getFactory();
  private Connection conn;
  private IDialect dialect;
  private int id;
  private IConfiguration config;
  private IQuery query;

  public DAOSupportSession() {
  }

  public void setConnection(Connection conn) {
    this.conn = conn;
    this.dialect = dialectFactory.getDialect(conn);
    this.query = new Query(conn);
    query.setConfiguration(config);
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public IDialect getDialect() {
    return dialect;
  }

  public void beginTransaction() throws Exception {
    if (assertConnectionNotNull()) {
      conn.setAutoCommit(false);
    }
  }

  public void endTransaction() throws Exception {
    if (assertConnectionNotNull()) {
      conn.commit();
    }
  }

  public void rollbackTransaction() throws Exception {
    if (assertConnectionNotNull()) {
      conn.rollback();
    }
  }

  /**
   * Test the connection.
   * @return the connection is fine?
   */
  public boolean testConnection() {
    try {
      return this.query.testConnection();
    } catch (SQLException e) {
      return false;
    }
  }

  private boolean assertConnectionNotNull() {
    if (null == conn) {
      throw new NullPointerException("Connection can not be null.");
    } else {
      return true;
    }
  }

  public Connection getConnection() {
    return conn;
  }

  public void clearup() {
    if (null != conn) {
      try {
        conn.close();
      } catch (SQLException e) {
        logger.error("The DB Connection cannot be closed. Cause: "
            + e.getMessage());
        throw new RuntimeException(e);
      }
    }
  }

  public IConfiguration getConfiguraton() {
    return config;
  }

  public void setConfiguraton(IConfiguration configuraton) {
    this.config = configuraton;
    this.query.setConfiguration(configuraton);
  }
  
  /** {@inheritDoc} */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Connection:\n");
    if (null != conn) {
      try {
        sb.append(conn.getClientInfo());
      } catch (SQLException e) {
        // ignore
        e = null;
      }
    } else {
      sb.append("null");
    }
    sb.append("\ndialect:\n");
    if (null != dialect) {
      sb.append(dialect.toString());
    } else {
      sb.append("null");
    }
    return sb.toString();
  }

  public IQuery getQuery() {
    return query;
  }

  public void setQuery(IQuery query) {
    this.query = query;
  }
}
