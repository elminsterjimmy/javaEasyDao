package com.elminster.easydao.db.dialect;

import java.sql.Connection;

public interface IDialectResolver {

  /**
   * Determine the {@link IDialect} to use based on the given JDBC {@link DatabaseMetaData}.  Implementations are
   * expected to return the {@link IDialect} instance to use, or null if the {@link DatabaseMetaData} does not match
   * the criteria handled by this impl.
   * 
   * @param metaData The JDBC metadata.
   * @return The dialect to use, or null.
   * @throws Exception Indicates a 'non transient connection problem', which indicates that
   * we should stop resolution attempts.
   */
  public IDialect resolveDialect(Connection conn) throws Exception;
}
