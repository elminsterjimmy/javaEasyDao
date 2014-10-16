package com.elminster.easydao.db.id;

import java.io.Serializable;

import com.elminster.easydao.db.manager.DAOSupportSession;

/**
 * The id generator.
 * 
 * @author jgu
 * @version 1.0
 */
public interface IdGenerator {

  Serializable[] generate(DAOSupportSession session, Object obj) throws IdGenerateException;
  
  Serializable[] lastId() throws IdGenerateException;
}
