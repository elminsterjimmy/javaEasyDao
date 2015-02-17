package com.elminster.easydao.db.transaction;

import java.sql.SQLException;

public interface IUnfinishedTransactionHandler {

  public void handleUnfinishedTransaction(Transaction transaction) throws SQLException;
}
