package com.elminster.easydao.db.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseDialectResolver implements IDialectResolver {

  private static Log log = LogFactory.getLog(BaseDialectResolver.class);

  @Override
  public IDialect resolveDialect(Connection conn) throws Exception {
    DatabaseMetaData metaData = conn.getMetaData();
    
    String databaseName = metaData.getDatabaseProductName();
    int databaseMajorVersion = metaData.getDatabaseMajorVersion();

    if ("HSQL Database Engine".equals(databaseName)) {
      // return new HSQLDialect();
    }

    if ("H2".equals(databaseName)) {
      // return new H2Dialect();
    }

    if ("MySQL".equals(databaseName)) {
      return new MySQLDialect();
    }

    if ("PostgreSQL".equals(databaseName)) {
      // return new PostgreSQLDialect();
    }

    if ("Apache Derby".equals(databaseName)) {
      // return new DerbyDialect();
    }

    if ("ingres".equalsIgnoreCase(databaseName)) {
      // return new IngresDialect();
    }

    if (databaseName.startsWith("Microsoft SQL Server")) {
      return new SQLServerDialect();
    }

    if ("Sybase SQL Server".equals(databaseName)
        || "Adaptive Server Enterprise".equals(databaseName)) {
      // return new SybaseDialect();
    }

    if ("Informix Dynamic Server".equals(databaseName)) {
      // return new InformixDialect();
    }

    if (databaseName.startsWith("DB2/")) {
      // return new DB2Dialect();
    }

    if ("Oracle".equals(databaseName)) {
      switch (databaseMajorVersion) {
      case 11:
        log.warn("Oracle 11g is not yet fully supported; using 10g dialect");
        return new Oracle10gDialect();
      case 10:
        return new Oracle10gDialect();
      case 9:
        return new Oracle9iDialect();
      case 8:
        return new Oracle8iDialect();
      default:
        log.warn("unknown Oracle major version [" + databaseMajorVersion + "]");
      }
    }
    return null;
  }
}
