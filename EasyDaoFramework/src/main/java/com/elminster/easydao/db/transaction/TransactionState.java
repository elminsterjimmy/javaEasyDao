package com.elminster.easydao.db.transaction;

public enum TransactionState {
  TRANSACTION_INIT,
  TRANSACTION_START,
  TRANSACTION_END,
  TRANSACTION_ROLLBACK
}
