package com.elminster.easydao.db.transaction;

import java.sql.SQLException;

import com.elminster.easydao.db.session.DAOSupportSession;

abstract public class TransactionTemplate {

  public void workWithTransaction(DAOSupportSession session) throws TransactionException {
    Transaction transaction = TransactionManager.INASTANCE.getTransaction(session);
    try {
      transaction.beginTransaction();
      doTransaction(session);
      transaction.commitTransaction();
    } catch (Exception e) {
      try {
        transaction.rollbackTransaction();
      } catch (SQLException sqle) {
        throw new TransactionException("Error on rollback transaction.", sqle);
      }
      throw new TransactionException(e);
    } finally {
      try {
        transaction.close();
      } catch (SQLException sqle) {
        throw new TransactionException("Error on close transaction.", sqle);
      }
    }
    
  }

  abstract public void doTransaction(DAOSupportSession session) throws Exception;
}
