package com.elminster.easydao.db.dialect;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLServerDialect extends Dialect {

  @Override
  public String getLimitSql(String sql, boolean hasOffset) {
    if (hasOffset) {
      throw new UnsupportedOperationException(
          "query result offset is not supported.");
    }
    return new StringBuffer(sql.length() + 8).append(sql).insert(
        getAfterSelectInsertPoint(sql), " top ?").toString();
  }

  private static int getAfterSelectInsertPoint(String sql) {
    int selectIndex = sql.toLowerCase().indexOf("select");
    final int selectDistinctIndex = sql.toLowerCase()
        .indexOf("select distinct");
    return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
  }

  @Override
  public boolean supportOffset() {
    return false;
  }

  @Override
  public boolean supportPaged() {
    return true;
  }

  @Override
  public String getTestConnectionSql() {
    return "select count(*) from sysusers";
  }

  public int registerResultSetOutParameter(CallableStatement statement, int col)
      throws SQLException {
    return col; // sql server just returns automatically
  }

  public ResultSet getResultSet(CallableStatement ps) throws SQLException {
    boolean isResultSet = ps.execute();
    // This assumes you will want to ignore any update counts
    while (!isResultSet && ps.getUpdateCount() != -1) {
      isResultSet = ps.getMoreResults();
    }
    // You may still have other ResultSets or update counts left to process here
    // but you can't do it now or the ResultSet you just got will be closed
    return ps.getResultSet();
  }
}
