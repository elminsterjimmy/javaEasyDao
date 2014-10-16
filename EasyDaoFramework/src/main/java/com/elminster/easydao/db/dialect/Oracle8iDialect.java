package com.elminster.easydao.db.dialect;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Oracle8iDialect extends Dialect {

  public static final String ORACLE_TYPES_CLASS_NAME = "oracle.jdbc.OracleTypes";
  public static final String DEPRECATED_ORACLE_TYPES_CLASS_NAME = "oracle.jdbc.driver.OracleTypes";
  public static final int INIT_ORACLETYPES_CURSOR_VALUE = -99;
  // not final-static to avoid possible classcast exceptions if using different
  // oracle drivers.
  private int oracleCursorTypeSqlType = INIT_ORACLETYPES_CURSOR_VALUE;

  public int getOracleCursorTypeSqlType() {
    if (oracleCursorTypeSqlType == INIT_ORACLETYPES_CURSOR_VALUE) {
      // todo : is there really any reason to kkeep trying if this fails once?
      oracleCursorTypeSqlType = extractOracleCursorTypeValue();
    }
    return oracleCursorTypeSqlType;
  }

  protected int extractOracleCursorTypeValue() {
    Class<?> oracleTypesClass;
    try {
      oracleTypesClass = Class.forName(ORACLE_TYPES_CLASS_NAME);
    } catch (ClassNotFoundException cnfe) {
      try {
        oracleTypesClass = Class.forName(DEPRECATED_ORACLE_TYPES_CLASS_NAME);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException("Unable to locate OracleTypes class", e);
      }
    }

    try {
      return oracleTypesClass.getField("CURSOR").getInt(null);
    } catch (Exception se) {
      throw new RuntimeException("Unable to access OracleTypes.CURSOR value",
          se);
    }
  }

  @Override
  public String getTestConnectionSql() {
    return "select user from dual";
  }

  /**
   * Map case support to the Oracle DECODE function. Oracle did not add support
   * for CASE until 9i.
   * 
   * @return The oracle CASE -> DECODE fragment
   */
  @Override
  public String getLimitSql(String sql, boolean hasOffset) {
    sql = sql.trim();
    boolean isForUpdate = false;
    if (sql.toLowerCase().endsWith(" for update")) {
      sql = sql.substring(0, sql.length() - 11);
      isForUpdate = true;
    }

    StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
    if (hasOffset) {
      pagingSelect
          .append("select * from ( select row_.*, rownum rownum_ from ( ");
    } else {
      pagingSelect.append("select * from ( ");
    }
    pagingSelect.append(sql);
    if (hasOffset) {
      pagingSelect.append(" ) row_ ) where rownum_ <= ? and rownum_ > ?");
    } else {
      pagingSelect.append(" ) where rownum <= ?");
    }

    if (isForUpdate) {
      pagingSelect.append(" for update");
    }

    return pagingSelect.toString();
  }

  @Override
  public boolean supportOffset() {
    return true;
  }

  @Override
  public boolean supportPaged() {
    return true;
  }

  public boolean useMaxForLimit() {
    return true;
  }

  public int registerResultSetOutParameter(CallableStatement statement, int col)
      throws SQLException {
    // register the type of the out param - an Oracle specific type
    statement.registerOutParameter(col, getOracleCursorTypeSqlType());
    col++;
    return col;
  }

  public ResultSet getResultSet(CallableStatement ps) throws SQLException {
    ps.execute();
    return (ResultSet) ps.getObject(1);
  }
}
