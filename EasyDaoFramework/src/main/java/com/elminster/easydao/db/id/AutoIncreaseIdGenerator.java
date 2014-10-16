package com.elminster.easydao.db.id;

import java.io.Serializable;

import com.elminster.easydao.db.manager.DAOSupportSession;
import com.elminster.easydao.db.query.IQuery;

public class AutoIncreaseIdGenerator implements IdGenerator {
  
  private DAOSupportSession session;

  @Override
  public Serializable[] generate(DAOSupportSession session, Object obj) throws IdGenerateException {
    this.session = session;
    return new Serializable[1];
  }

  @Override
  public Serializable[] lastId() throws IdGenerateException {
    String sql = session.getDialect().getIdentitySelectString();
    int lastId;
    try {
      session.beginTransaction();
      IQuery query = session.getQuery();
      lastId = query.sqlSelectSingleInteger(sql, 0);
      session.endTransaction();
    } catch (Exception e) {
      throw new IdGenerateException(e);
    }
    return new Serializable[] {lastId};
  }

}
