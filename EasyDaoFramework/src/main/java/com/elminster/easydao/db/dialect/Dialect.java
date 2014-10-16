package com.elminster.easydao.db.dialect;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dialect implements IDialect {

  /**
   * Given a limit and an offset, apply the limit clause to the query.
   * 
   * @param query
   *          The query to which to apply the limit.
   * @param offset
   *          The offset of the limit
   * @param limit
   *          The limit of the limit ;)
   * @return The modified query statement with the limit applied.
   */
  @Override
  public String getLimitSql(String sql, boolean hasOffset) {
    throw new UnsupportedOperationException(
        "query result paged is not supported.");
  }

  /**
   * Get test connection query.
   * 
   * @return The test connection query
   */
  @Override
  public String getTestConnectionSql() {
    throw new UnsupportedOperationException(
        "query test connection is not supported.");
  }

  /**
   * Does this dialect's LIMIT support (if any) additionally support specifying
   * an offset?
   * 
   * @return True if the dialect supports an offset within the limit support.
   */
  @Override
  public boolean supportOffset() {
    return false;
  }

  /**
   * Does this dialect support some form of limiting query results via a SQL
   * clause?
   * 
   * @return True if this dialect supports some form of LIMIT.
   */
  @Override
  public boolean supportPaged() {
    return false;
  }

  /**
   * Does the <tt>LIMIT</tt> clause take a "maximum" row number instead of a
   * total number of returned rows?
   * <p/>
   * This is easiest understood via an example. Consider you have a table with
   * 20 rows, but you only want to retrieve rows number 11 through 20.
   * Generally, a limit with offset would say that the offset = 11 and the limit
   * = 10 (we only want 10 rows at a time); this is specifying the total number
   * of returned rows. Some dialects require that we instead specify offset = 11
   * and limit = 20, where 20 is the "last" row we want relative to offset (i.e.
   * total number of rows = 20 - 11 = 9)
   * <p/>
   * So essentially, is limit relative from offset? Or is limit absolute?
   * 
   * @return True if limit is relative from offset; false otherwise.
   */
  public boolean useMaxForLimit() {
    return false;
  }

  /**
   * setFirstResult() should be a zero-based offset. Here we allow the Dialect a
   * chance to convert that value based on what the underlying db or driver will
   * expect.
   * <p/>
   * NOTE: what gets passed into {@link #getLimitString(String,int,int)} is the
   * zero-based offset. Dialects which do not {@link #supportsVariableLimit}
   * should take care to perform any needed {@link #convertToFirstRowValue}
   * calls prior to injecting the limit values into the SQL string.
   * 
   * @param zeroBasedFirstResult
   *          The user-supplied, zero-based first row offset.
   * 
   * @return The corresponding db/dialect specific offset.
   */
  public int convertToFirstRowValue(int zeroBasedFirstResult) {
    return zeroBasedFirstResult;
  }

  /**
   * Registers an OUT parameter which will be returing a
   * {@link java.sql.ResultSet}. How this is accomplished varies greatly from DB
   * to DB, hence its inclusion (along with {@link #getResultSet}) here.
   * 
   * @param statement
   *          The callable statement.
   * @param position
   *          The bind position at which to register the OUT param.
   * @return The number of (contiguous) bind positions used.
   * @throws SQLException
   *           Indicates problems registering the OUT param.
   */
  public int registerResultSetOutParameter(CallableStatement statement,
      int position) throws SQLException {
    throw new UnsupportedOperationException(getClass().getName()
        + " does not support resultsets via stored procedures");
  }

  /**
   * Given a callable statement previously processed by
   * {@link #registerResultSetOutParameter}, extract the
   * {@link java.sql.ResultSet} from the OUT parameter.
   * 
   * @param statement
   *          The callable statement.
   * @return The extracted result set.
   * @throws SQLException
   *           Indicates problems extracting the result set.
   */
  public ResultSet getResultSet(CallableStatement statement)
      throws SQLException {
    throw new UnsupportedOperationException(getClass().getName()
        + " does not support resultsets via stored procedures");
  }
  
  /**
   * Retrieve the command used to retrieve the current timestamp from the
   * database.
   *
   * @return The command.
   */
  @Override
  public String getCurrentTimestampSelectString() {
    throw new UnsupportedOperationException( "Database not known to define a current timestamp function" );
  }
  
  /**
   * Get the select command to use to retrieve the last generated IDENTITY
   * value.
   *
   * @return The appropriate select command
   * @throws MappingException If IDENTITY generation is not supported.
   */
  @Override
  public String getIdentitySelectString() {
    throw new UnsupportedOperationException(getClass().getName() + " does not support identity key generation" );
  }
}
