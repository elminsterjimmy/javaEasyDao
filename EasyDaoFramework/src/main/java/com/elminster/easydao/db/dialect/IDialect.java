package com.elminster.easydao.db.dialect;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDialect {

  public String getLimitSql(String sql, boolean hasOffset);
  
  public boolean supportPaged();
  
  public boolean supportOffset();
  
  public boolean useMaxForLimit();
  
  public int convertToFirstRowValue(int zeroBasedFirstResult);
  
  public int registerResultSetOutParameter(CallableStatement statement, int position) throws SQLException;
  
  public ResultSet getResultSet(CallableStatement statement) throws SQLException;
  
  public String getTestConnectionSql();
  
  public String getCurrentTimestampSelectString();
  
  public String getIdentitySelectString();
}
