package com.elminster.easydao.db.dialect;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLDialect extends Dialect {

  @Override
  public String getLimitSql(String sql, boolean hasOffset) {
    return new StringBuffer(sql.length() + 20).append(sql).append(
        hasOffset ? " limit ?, ?" : " limit ?").toString();
  }

  @Override
  public String getTestConnectionSql() {
    return "select user()";
  }

  @Override
  public boolean supportOffset() {
    return true;
  }

  @Override
  public boolean supportPaged() {
    return true;
  }

  @Override
  public int registerResultSetOutParameter(CallableStatement statement, int col)
      throws SQLException {
    return col;
  }

  @Override
  public ResultSet getResultSet(CallableStatement ps) throws SQLException {
    boolean isResultSet = ps.execute();
    while (!isResultSet && ps.getUpdateCount() != -1) {
      isResultSet = ps.getMoreResults();
    }
    return ps.getResultSet();
  }

  @Override
  public String getCurrentTimestampSelectString() {
    return "select now()";
  }

  @Override
  public String getIdentitySelectString() {
    return "select last_insert_id()";
  }
}
