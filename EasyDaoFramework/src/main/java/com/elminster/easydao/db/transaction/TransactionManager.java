package com.elminster.easydao.db.transaction;

import com.elminster.easydao.db.session.DAOSupportSession;
import com.elminster.easydao.id.IdGenerator;
import com.elminster.easydao.id.internal.InternalIdGenerator;

public class TransactionManager {

  public static final TransactionManager INASTANCE = new TransactionManager();
  
  private static final IdGenerator transactionIdGenerator = new InternalIdGenerator();
  
  public Transaction getTransaction(DAOSupportSession session) {
    return new Transaction((Long) transactionIdGenerator.nextId(), session);
  }
}
