package com.elminster.easydao.db.transaction;

import java.sql.SQLException;

import com.elminster.easydao.db.session.DAOSupportSession;

public class Transaction {

  private final DAOSupportSession session;
  private Transaction parentTransaction;
  private TransactionState state;
  private IUnfinishedTransactionHandler unfinishedTransactionHandler;
  private Long id;
  
  Transaction(Long id, DAOSupportSession session) {
    this.id = id;
    this.session = session;
    if (session.isInTransaction()) {
      this.parentTransaction = session.getTransaction();
    }
    session.setTransaction(this);
    unfinishedTransactionHandler = new DefaultUnfinishedTransactionHandler();
    setTransactionState(TransactionState.TRANSACTION_INIT);
  }
  
  public void beginTransaction() throws SQLException {
    session.getConnection().setAutoCommit(false);
    setTransactionState(TransactionState.TRANSACTION_START);
  }
  
  public void commitTransaction() throws SQLException {
    session.getConnection().commit();
    setTransactionState(TransactionState.TRANSACTION_END);
  }
  
  public void rollbackTransaction() throws SQLException {
    session.getConnection().rollback();
    setTransactionState(TransactionState.TRANSACTION_ROLLBACK);
  }
  
  public Long getId() {
    return id;
  }

  private void setTransactionState(TransactionState state) {
    this.state = state;
  }

  public TransactionState getTransactionState() {
    return state;
  }
  
  public void close() throws SQLException {
    if (TransactionState.TRANSACTION_START == this.state) {
      unfinishedTransactionHandler.handleUnfinishedTransaction(this);
    }
  }
  
  class DefaultUnfinishedTransactionHandler implements IUnfinishedTransactionHandler {

    @Override
    public void handleUnfinishedTransaction(Transaction transaction) throws SQLException {
      throw new IllegalStateException("Unfinished Transaction: " + transaction);
    }
  }
}
