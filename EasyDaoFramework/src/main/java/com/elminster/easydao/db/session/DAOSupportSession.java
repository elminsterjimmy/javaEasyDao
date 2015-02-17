package com.elminster.easydao.db.session;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.elminster.easydao.db.config.IConfiguration;
import com.elminster.easydao.db.dialect.DialectFactory;
import com.elminster.easydao.db.dialect.IDialect;
import com.elminster.easydao.db.query.IQuery;
import com.elminster.easydao.db.query.Query;
import com.elminster.easydao.db.transaction.Transaction;

public class DAOSupportSession {

  private static Log logger = LogFactory.getLog(DAOSupportSession.class);

  private DialectFactory dialectFactory = DialectFactory.getFactory();
  private Connection conn;
  private IDialect dialect;
  private Long id;
  private IConfiguration config;
  private IQuery query;
  private Transaction transaction;
  private long createdTime;
  private long updatedTime;

  public DAOSupportSession() {
    this.createdTime = System.currentTimeMillis();
    this.updatedTime = createdTime;
  }

  public void setConnection(Connection conn) {
    this.conn = conn;
    this.dialect = dialectFactory.getDialect(conn);
    this.query = new Query(conn);
    query.setConfiguration(config);
  }

  public void setId(Long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public IDialect getDialect() {
    return dialect;
  }

  /**
   * Test the connection.
   * 
   * @return the connection is fine?
   */
  public boolean testConnection() {
    try {
      return this.query.testConnection();
    } catch (SQLException e) {
      return false;
    }
  }

  private void assertConnectionNotNull() {
    if (null == conn)
      throw new NullPointerException("Connection can not be null.");
  }

  public Connection getConnection() {
    assertConnectionNotNull();
    return conn;
  }
  
  public void close() throws SQLException {
    clearup();
    if (null != conn) {
      try {
        conn.close();
      } catch (SQLException e) {
        logger.error("The DB Connection cannot be closed. Cause: " + e.getMessage());
        throw e;
      }
    }
  }
  
  public void clearup() throws SQLException {
    // commit unfinished transaction.
    if (null != transaction) {
      transaction.close();
      transaction = null;
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

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  public boolean isInTransaction() {
    boolean inTransaction = false;
    if (null != this.transaction) {
      inTransaction = true;
    }
    return inTransaction;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public long getCreatedTime() {
    return createdTime;
  }

  public long getUpdatedTime() {
    return updatedTime;
  }

  public void update() {
    this.updatedTime = System.currentTimeMillis();
  }
}
