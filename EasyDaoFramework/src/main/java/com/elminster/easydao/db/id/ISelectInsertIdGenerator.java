package com.elminster.easydao.db.id;

import java.io.Serializable;

import com.elminster.easydao.db.manager.DAOSupportSession;

public interface ISelectInsertIdGenerator extends IdGenerator {

  Serializable[] selectId(DAOSupportSession session) throws IdGenerateException;
}
