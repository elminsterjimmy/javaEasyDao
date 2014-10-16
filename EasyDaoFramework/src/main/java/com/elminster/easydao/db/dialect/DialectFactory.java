package com.elminster.easydao.db.dialect;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DialectFactory {
  
  private static Log log = LogFactory.getLog(DialectFactory.class);
  private static DialectFactory factory = new DialectFactory();
  private IDialectResolver resolver = new BaseDialectResolver();
  
  public static DialectFactory getFactory() {
    return factory;
  }
  
  public IDialect getDialect(Connection conn) {
    IDialect dialect = null;
    try {
      dialect = resolver.resolveDialect(conn);
    } catch (Exception e) {
      log.warn("Exception at resolveDialect(). Cause: " + e.getMessage());
    }
    if (null == dialect) {
      dialect = new Dialect();
    }
    return dialect;
  }
}
