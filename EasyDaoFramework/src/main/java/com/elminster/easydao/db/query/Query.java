package com.elminster.easydao.db.query;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.BatchUpdateException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.elminster.common.constants.Constants.StringConstants;
import com.elminster.common.util.DateUtil;
import com.elminster.common.util.ObjectUtil;
import com.elminster.easydao.db.config.IConfiguration;
import com.elminster.easydao.db.dialect.DialectFactory;
import com.elminster.easydao.db.dialect.IDialect;
import com.elminster.easydao.db.dialect.Oracle8iDialect;
import com.elminster.easydao.db.handler.IResultSetHandler;

/**
 * The implementation of Query.
 * 
 * @author jgu
 * @version 1.0
 */
public class Query implements IQuery {
  /** the batch size. */
  private static final int BATCH_SIZE = 100;
  /** the date format. */
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  /** the logger. **/
  private static Log logger = LogFactory.getLog(Query.class);
  /** the dialect factory. */
  private DialectFactory dialectFactory = DialectFactory.getFactory();
  /** the connection. */
  private Connection conn;
  /** the dialect. */
  private IDialect dialect;
  /** the last statement. */
  private String lastStmt;
  /** the configuration. */
  private IConfiguration config;

  /**
   * Constructor.
   * 
   * @param conn
   *          the connection
   */
  public Query(Connection conn) {
    this.conn = conn;
    this.dialect = dialectFactory.getDialect(conn);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setConfiguration(IConfiguration config) {
    this.config = config;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean testConnection() throws SQLException {
    String sql = dialect.getTestConnectionSql();
    boolean rst;
    try {
      String result = this.sqlSelectSingleString(sql, "error");
      if ("error".equals(result)) {
        rst = false;
      } else {
        rst = true;
      }
    } catch (SQLException sqle) {
      logger.warn("SQL Exception in testConnection(). " + sqle.getMessage());
      rst = false;
    }
    return rst;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commit() throws SQLException {
    if (logger.isDebugEnabled()) {
      logger.debug("commit");
    }
    try {
      conn.commit();
    } catch (SQLException sqle) {
      logger.error("commit:" + buildErrorMessage(sqle, null));
      throw sqle;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void rollback() throws SQLException {
    if (logger.isDebugEnabled()) {
      logger.debug("rollback");
    }
    try {
      conn.rollback();
    } catch (SQLException sqle) {
      logger.error("rollback:" + buildErrorMessage(sqle, null));
      throw sqle;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int sqlExecNoResult(String stm) throws SQLException {
    int rst = 0;
    this.lastStmt = buildQuerySql(stm);
    if (config.isShowSql()) {
      logger.info(lastStmt);
    }
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      rst = stmt.executeUpdate(stm);
    } catch (SQLException sqle) {
      logger.error("sqlExecNoResult:" + buildErrorMessage(sqle, stm));
      throw sqle;
    } finally {
      if (null != stmt) {
        try {
          stmt.close();
        } catch (SQLException sqle) {
          logger.error("Error closing result set and statement", sqle);
        }
      }
    }
    return rst;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long sqlSelectSingleLong(String stm, Long nullReplace, Object[] data,
      Calendar cal) throws SQLException {
    Long rst = nullReplace;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    HostVariableTypes types = null;
    try {
      pstmt = conn.prepareStatement(stm);
      if (ObjectUtil.isArrayNotEmpty(data)) {
        types = analysisTypes(data, null);
        setHostVariables(pstmt, types, 0, data, cal);
      }
      this.lastStmt = buildQuerySql(stm, types, 0, data);
      if (config.isShowSql()) {
        logger.info(lastStmt);
      }
      rs = pstmt.executeQuery();
      if (null != rs) {
        if (rs.next()) {
          long value = rs.getLong(1);
          if (!rs.wasNull()) {
            rst = Long.valueOf(value);
          }
        }
      }
    } catch (SQLException sqle) {
      logger.error("sqlSelectSingleLong:"
          + buildErrorMessage(sqle, stm, types, 0, data));
      throw sqle;
    } finally {
      if (null != rs) {
        try {
          rs.close();
        } catch (SQLException sqle) {
          logger.error("Error closing result set and statement", sqle);
        }
      }
      if (null != pstmt) {
        try {
          pstmt.close();
        } catch (SQLException sqle) {
          logger.error("Error closing result set and statement", sqle);
        }
      }
    }
    return null == rst ? nullReplace : rst;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer sqlSelectSingleInteger(String stm, Integer nullReplace,
      Object[] data, Calendar cal) throws SQLException {
    Integer rst = nullReplace;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    HostVariableTypes types = null;
    try {
      pstmt = conn.prepareStatement(stm);
      if (ObjectUtil.isArrayNotEmpty(data)) {
        types = analysisTypes(data, null);
        setHostVariables(pstmt, types, 0, data, cal);
      }
      this.lastStmt = buildQuerySql(stm, types, 0, data);
      if (config.isShowSql()) {
        logger.info(lastStmt);
      }
      rs = pstmt.executeQuery();
      if (null != rs) {
        if (rs.next()) {
          int value = rs.getInt(1);
          if (!rs.wasNull()) {
            rst = Integer.valueOf(value);
          }
        }
      }
    } catch (SQLException sqle) {
      logger.error("sqlSelectSingleInteger:"
          + buildErrorMessage(sqle, stm, types, 0, data));
      throw sqle;
    } finally {
      if (null != rs) {
        try {
          rs.close();
        } catch (SQLException sqle) {
          logger.error("Error closing result set and statement", sqle);
        }
      }
      if (null != pstmt) {
        try {
          pstmt.close();
        } catch (SQLException sqle) {
          logger.error("Error closing result set and statement", sqle);
        }
      }
    }
    return null == rst ? nullReplace : rst;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String sqlSelectSingleString(String stm, String nullReplace,
      Object[] data, Calendar cal) throws SQLException {
    String rst = nullReplace;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    HostVariableTypes types = null;
    try {
      pstmt = conn.prepareStatement(stm);
      if (ObjectUtil.isArrayNotEmpty(data)) {
        types = analysisTypes(data, null);
        setHostVariables(pstmt, types, 0, data, cal);
      }
      this.lastStmt = buildQuerySql(stm, types, 0, data);
      if (config.isShowSql()) {
        logger.info(lastStmt);
      }
      rs = pstmt.executeQuery();
      if (null != rs) {
        ResultSetMetaData metaData = rs.getMetaData();
        if (rs.next()) {
          switch (metaData.getColumnType(1)) {
          case Types.CLOB:
            Clob curClobValue = rs.getClob(1);
            if (curClobValue != null) {
              rst = clobToString(curClobValue);
            }
            break;
          case Types.LONGNVARCHAR:
          case Types.LONGVARCHAR:
            Reader curInputStream = rs.getCharacterStream(1);
            if (curInputStream != null) {
              rst = streamToString(curInputStream);
            }
            break;
          default:
            rst = rs.getString(1);
          }
        }
      }
    } catch (SQLException sqle) {
      logger.error("sqlSelectSingleString:"
          + buildErrorMessage(sqle, stm, types, 0, data));
      throw sqle;
    } catch (IOException ioe) {
      logger.error("sqlSelectSingleString:" + ioe.getMessage());
      throw new SQLException(ioe);
    } finally {
      if (null != rs) {
        try {
          rs.close();
        } catch (SQLException sqle) {
          logger.error("Error closing result set and statement", sqle);
        }
      }
      if (null != pstmt) {
        try {
          pstmt.close();
        } catch (SQLException sqle) {
          logger.error("Error closing result set and statement", sqle);
        }
      }
    }
    return null == rst ? nullReplace : rst;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Map<String, Object>> sqlSelectIntoMapList(String stm)
      throws SQLException {
    return sqlSelectIntoMapList(stm, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Map<String, Object>> sqlSelectIntoMapList(String stm, Calendar cal)
      throws SQLException {
    return sqlSelectIntoMapList(stm, null, cal);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Map<String, Object>> sqlSelectIntoMapList(String stm,
      Object[] data, Calendar cal) throws SQLException {
    return sqlSelectIntoMapList(stm, data, cal, 0, 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Map<String, Object>> sqlSelectIntoMapList(String stm,
      Object[] data, Calendar cal, int nStart, int nMaxdata)
      throws SQLException {
    return sqlSelectIntoMapList(stm, data, null, cal, nStart, nMaxdata);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Map<String, Object>> sqlSelectIntoMapList(String stm,
      Object[] data, Class<?>[] expectedColumnTypes, Calendar cal, int nStart,
      int nMaxdata) throws SQLException {

    final List<Map<String, Object>> rst = new ArrayList<Map<String, Object>>();

    IMapRowDataCallback callback = new IMapRowDataCallback() {

      @Override
      public boolean onRow(Object o, Map<String, Object> rowData)
          throws SQLException {
        rst.add(rowData);
        return true;
      }

    };

    this.sqlSelectAndCallback(callback, null, stm, expectedColumnTypes, nStart,
        nMaxdata, data, cal);

    return rst;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int sqlInsertByBatch(String stm, Object[] data) throws SQLException {
    return this.sqlInsertByBatch(stm, data, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int sqlInsertByBatch(String stm, Object[] data, Calendar cal)
      throws SQLException {
    int k = 0;
    HostVariableTypes types = null;
    PreparedStatement pst = null;
    try {
      types = analysisTypes(data, null);
      pst = conn.prepareStatement(stm);
      int num = 0;
      int batchCount = 0;
      for (k = 0; k < types.rows; k++) {
        setHostVariables(pst, types, k, data, cal);
        pst.addBatch();
        batchCount++;
        // If batch size or last row reached, execute the batch.
        if (batchCount >= BATCH_SIZE || k + 1 == types.rows) {
          int[] res = pst.executeBatch();
          for (int re : res) {
            if (Statement.SUCCESS_NO_INFO == re) {
              num = re;
            } else if (num > -1 && re > 0) {
              num += re;
            }
            if (Statement.EXECUTE_FAILED == re) {
              throw new BatchUpdateException(res);
            }
          }
          batchCount = 0;
        }
      }

      if (logger.isDebugEnabled()) {
        logger.debug("sqlInsertByCursorBatch rows=" + types.rows + " num="
            + num + " stm=" + stm);
      }
      return num;
    } catch (SQLException e) {
      logger.error("sqlInsertByBatch"
          + buildErrorMessage(e, stm, types, k, data));
      throw e;
    } finally {
      try {
        if (pst != null) {
          pst.close();
        }
      } catch (Exception ex) {
        logger.error("Error closing result set and statement", ex);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLastSqlStm() {
    return this.lastStmt;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Map<String, Object>> getResultSetMetaData(String stm)
      throws SQLException {
    List<Map<String, Object>> rtn = new LinkedList<Map<String, Object>>();
    PreparedStatement st1 = null;
    ResultSet result = null;
    try {
      st1 = conn.prepareStatement(stm);
      try {
        result = st1.executeQuery();
        ResultSetMetaData metaData = result.getMetaData();
        int ncols = metaData.getColumnCount();
        for (int i = 1; i <= ncols; i++) {
          Map<String, Object> col = new HashMap<String, Object>();
          rtn.add(col);
          col.put("name", metaData.getColumnLabel(i));
          col.put("jdbctype", Integer.valueOf(metaData.getColumnType(i)));
          col.put("precision", Integer.valueOf(metaData.getPrecision(i)));
          col.put("scale", Integer.valueOf(metaData.getScale(i)));
          col.put("nullable", Integer.valueOf(metaData.isNullable(i)));
          col.put("specifictype", metaData.getColumnTypeName(i));
        }

      } finally {
        if (result != null) {
          result.close();
        }
      }
    } catch (SQLException e) {
      logger.error("getResultSetMetaData:" + buildErrorMessage(e, stm));
      throw e;
    } catch (Exception e) {
      logger.error("getResultSetMetaData msg=" + e + " stm=" + stm, e);
      throw new SQLException(e);
    } finally {
      try {
        if (st1 != null) {
          st1.close();
        }
      } catch (Exception ex) {
        logger.error("Error closing result set and statement", ex);
      }
    }
    return rtn;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sqlSelectAndCallback(IListRowDataCallback rowCall,
      Object rowCallArg, String stm, Class<?>[] expectedColumnTypes,
      int nStart, int nMaxdata, Object[] data, Calendar cal)
      throws SQLException {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    HostVariableTypes types = null;
    try {

      // get paged SQL
      boolean useScrollable = false;

      if (nStart > 0) {
        if (dialect.supportPaged()) {
          if (nMaxdata > 0) {
            if (dialect.supportOffset()) {
              stm = dialect.getLimitSql(stm, true);
            } else {
              useScrollable = true;
            }
          } else {
            stm = dialect.getLimitSql(stm, false);
          }
        } else {
          useScrollable = true;
        }
      }

      if (useScrollable) {
        pstmt = conn.prepareStatement(stm, ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
      } else {
        pstmt = conn.prepareStatement(stm);
      }
      if (ObjectUtil.isArrayNotEmpty(data)) {
        types = analysisTypes(data, null);
        setHostVariables(pstmt, types, 0, data, cal);
      }
      this.lastStmt = buildQuerySql(stm, types, 0, data);
      if (config.isShowSql()) {
        logger.info(lastStmt);
      }
      rs = pstmt.executeQuery();
      if (null != rs) {
        ResultSetMetaData rsmd = rs.getMetaData();
        int ncols = rsmd.getColumnCount();
        rs.setFetchSize(100);
        if (useScrollable) {
          moveCursor(rs, nStart);
        }
        int rowFetched = 0;
        while (rs.next()) {
          List<Object> list = new ArrayList<Object>();
          for (int i = 1; i <= ncols; i++) {
            Object o = null;
            if (null == expectedColumnTypes) {
              o = getColumnValue(rsmd, rs, i, cal);
            } else {
              o = getColumnValue(rsmd, expectedColumnTypes, rs, i, cal);
            }
            list.add(o);
            if (!rowCall.onRow(rowCallArg, list)) {
              break;
            }
          }
          rowFetched++;
          if (nMaxdata > 0 && rowFetched >= nMaxdata) {
            break;
          }
        }
      }

    } catch (SQLException sqle) {
      logger.error("sqlSelectSingleInteger:"
          + buildErrorMessage(sqle, stm, types, 0, data));
      throw sqle;
    } catch (IOException ioe) {
      logger.error("sqlSelectSingleInteger:"
          + buildErrorMessage(new SQLException(ioe), stm, types, 0, data));
      throw new SQLException(ioe);
    } finally {
      if (null != rs) {
        try {
          rs.close();
        } catch (SQLException sqle) {
          logger.error("Error closing result set and statement", sqle);
        }
      }
      if (null != pstmt) {
        try {
          pstmt.close();
        } catch (SQLException sqle) {
          logger.error("Error closing result set and statement", sqle);
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sqlSelectAndCallback(IMapRowDataCallback rowCall,
      Object rowCallArg, String stm, Class<?>[] expectedColumnTypes,
      int nStart, int nMaxdata, Object[] data, Calendar cal)
      throws SQLException {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    HostVariableTypes types = null;
    try {

      // get paged SQL
      boolean useScrollable = false;

      if (nStart > 0) {
        if (dialect.supportPaged()) {
          if (nMaxdata > 0) {
            if (dialect.supportOffset()) {
              stm = dialect.getLimitSql(stm, true);
            } else {
              useScrollable = true;
            }
          } else {
            stm = dialect.getLimitSql(stm, false);
          }
        } else {
          useScrollable = true;
        }
      }

      if (useScrollable) {
        pstmt = conn.prepareStatement(stm, ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
      } else {
        pstmt = conn.prepareStatement(stm);
      }
      if (ObjectUtil.isArrayNotEmpty(data)) {
        types = analysisTypes(data, null);
        setHostVariables(pstmt, types, 0, data, cal);
      }
      this.lastStmt = buildQuerySql(stm, types, 0, data);
      if (config.isShowSql()) {
        logger.info(lastStmt);
      }
      rs = pstmt.executeQuery();
      if (null != rs) {
        ResultSetMetaData rsmd = rs.getMetaData();
        int ncols = rsmd.getColumnCount();
        rs.setFetchSize(100);
        if (useScrollable) {
          moveCursor(rs, nStart);
        }
        int rowFetched = 0;
        while (rs.next()) {
          Map<String, Object> map = new HashMap<String, Object>();
          for (int i = 1; i <= ncols; i++) {
            Object o = null;
            if (null == expectedColumnTypes) {
              o = getColumnValue(rsmd, rs, i, cal);
            } else {
              o = getColumnValue(rsmd, expectedColumnTypes, rs, i, cal);
            }
            map.put(rsmd.getColumnName(i), o);
            if (!rowCall.onRow(rowCallArg, map)) {
              break;
            }
          }
          rowFetched++;
          if (nMaxdata > 0 && rowFetched >= nMaxdata) {
            break;
          }
        }
      }

    } catch (SQLException sqle) {
      logger.error("sqlSelectSingleInteger:"
          + buildErrorMessage(sqle, stm, types, 0, data));
      throw sqle;
    } catch (IOException ioe) {
      logger.error("sqlSelectSingleInteger:"
          + buildErrorMessage(new SQLException(ioe), stm, types, 0, data));
      throw new SQLException(ioe);
    } finally {
      if (null != rs) {
        try {
          rs.close();
        } catch (SQLException sqle) {
          logger.error("Error closing result set and statement", sqle);
        }
      }
      if (null != pstmt) {
        try {
          pstmt.close();
        } catch (SQLException sqle) {
          logger.error("Error closing result set and statement", sqle);
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object sqlSelectAndCallback(IResultSetHandler resultSetHandler,
      String stm, int nStart, int nMaxdata, Object[] data, Calendar cal)
      throws SQLException {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    HostVariableTypes types = null;
    try {

      // get paged SQL
      boolean useScrollable = false;

      if (nStart > 0) {
        if (dialect.supportPaged()) {
          if (nMaxdata > 0) {
            if (dialect.supportOffset()) {
              stm = dialect.getLimitSql(stm, true);
            } else {
              useScrollable = true;
            }
          } else {
            stm = dialect.getLimitSql(stm, false);
          }
        } else {
          useScrollable = true;
        }
      }

      if (useScrollable) {
        pstmt = conn.prepareStatement(stm, ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
      } else {
        pstmt = conn.prepareStatement(stm);
      }
      if (ObjectUtil.isArrayNotEmpty(data)) {
        types = analysisTypes(data, null);
        setHostVariables(pstmt, types, 0, data, cal);
      }
      this.lastStmt = buildQuerySql(stm, types, 0, data);
      if (config.isShowSql()) {
        logger.info(lastStmt);
      }
      rs = pstmt.executeQuery();
      if (null != rs) {
        rs.setFetchSize(100);
        if (useScrollable) {
          moveCursor(rs, nStart);
        }
        try {
          return resultSetHandler.handleResultSet(rs);
        } catch (Exception e) {
          SQLException sqle = new SQLException(e);
          logger.error("sqlSelectAndCallback:"
              + buildErrorMessage(sqle, stm, types, 0, data));
          throw sqle;
        }
      }

    } catch (SQLException sqle) {
      logger.error("sqlSelectAndCallback:"
          + buildErrorMessage(sqle, stm, types, 0, data));
      throw sqle;
    } finally {
      if (null != rs) {
        try {
          rs.close();
        } catch (SQLException sqle) {
          logger.error("Error closing result set and statement", sqle);
        }
      }
      if (null != pstmt) {
        try {
          pstmt.close();
        } catch (SQLException sqle) {
          logger.error("Error closing result set and statement", sqle);
        }
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long sqlSelectSingleLong(String stm, Long nullReplace)
      throws SQLException {
    return this.sqlSelectSingleLong(stm, nullReplace, null, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer sqlSelectSingleInteger(String stm, Integer nullReplace)
      throws SQLException {
    return this.sqlSelectSingleInteger(stm, nullReplace, null, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String sqlSelectSingleString(String stm, String nullReplace)
      throws SQLException {
    return this.sqlSelectSingleString(stm, nullReplace, null, null);
  }

  /**
   * Fills InputStream from TEXT into String. The function is internaly used to
   * read database TEXT type values from stream.
   * 
   * @param o
   *          Reader
   * @return String with the content of the stream
   * @exception SQLException
   *              if an error occurs
   * @exception IOException
   *              if an error occurs
   */
  private String streamToString(Reader o) throws SQLException, IOException {
    if (null == o) {
      return null;
    }

    StringBuffer sBuf = new StringBuffer();
    int size = 512;
    char[] cArray = new char[size];
    int nchars = o.read(cArray, 0, size);
    while (nchars > 0) {
      sBuf.append(cArray, 0, nchars);
      nchars = o.read(cArray, 0, size);
    }
    return sBuf.toString();
  }

  /**
   * Fills oracle clob into String.
   * 
   * @param o
   *          Clob
   * @return String with the content of the oracle clob
   * @exception SQLException
   *              if an error occurs
   */
  private String clobToString(Clob o) throws SQLException {
    if (null == o) {
      return null;
    }
    return o.getSubString(1, (int) o.length());
  }

  /**
   * Fills oracle nclob into String.
   * 
   * @param o
   *          NClob
   * @return String with the content of the oracle nclob
   * @exception SQLException
   *              if an error occurs
   */
  private String nclobToString(NClob o) throws SQLException {
    if (null == o) {
      return null;
    }
    return o.getSubString(1, (int) o.length());
  }

  /**
   * method to get data debug string of data.
   * 
   * @param types
   *          a <code>HostVariableTypes</code> value
   * @param k
   *          an <code>int</code> value
   * @param data
   *          an <code>Object[]</code> value
   * @return a <code>String</code> value
   */
  protected String debugStringSetHostVariables(HostVariableTypes types, int k,
      Object[] data) {

    if (null == data) {
      return StringConstants.EMPTY_STRING;
    }
    StringBuilder sb = new StringBuilder();
    try {
      for (int i = 0; i < data.length; i++) {
        if (i > 0) {
          sb.append(StringConstants.COMMA);
        }

        if (null != types && types.isArray[i]) {
          if (types.isObject[i]) {
            Object[] val = (Object[]) data[i];
            Object obj = val[Math.min(k, val.length - 1)];
            if (obj instanceof java.util.Date) {
              sb.append(DateUtil.getDateString((java.util.Date) obj,
                  DATE_FORMAT, Locale.US, TimeZone.getTimeZone("UTC")));
              sb.append(" UTC");
            } else {
              sb.append(obj);
            }
          } else {
            switch (types.abstractType[i]) {
            case Types.BIGINT:
              long[] longVal = (long[]) data[i];
              sb.append(longVal[Math.min(k, longVal.length - 1)]);
              break;
            case Types.INTEGER:
              int[] intVal = (int[]) data[i];
              sb.append(intVal[Math.min(k, intVal.length - 1)]);
              break;
            default:
              sb.append("<array of unknown scalars>");
            }
          }
        } else {
          if (data[i] instanceof java.util.Date) {
            sb.append(DateUtil.getDateString((java.util.Date) data[i],
                DATE_FORMAT, Locale.US, TimeZone.getTimeZone("UTC")));
            sb.append(" UTC");
          } else {
            sb.append(data[i]);
          }
        }
      }
    } catch (Exception e) {
      logger.error(" data format error=" + e, e);
    }
    return sb.toString();
  }

  /**
   * INTERNAL format sql exception format message.
   * 
   * @param e
   *          a <code>SQLException</code> value
   * @return a <code>String</code> value
   */
  private String formatMsg(SQLException e) {
    StringBuilder sb = new StringBuilder();
    sb.append(" msg=").append(e).append(" code=").append(e.getErrorCode())
        .append(" state=").append(e.getSQLState()).append(getSubMsg(e));
    return sb.toString();
  }

  /**
   * Internal function to enrolls sql exception chain.
   * 
   * @param e
   *          a <code>SQLException</code> value
   * @return a <code>String</code> enrolled text
   */
  private String getSubMsg(SQLException e) {
    StringBuilder msg = new StringBuilder(StringConstants.EMPTY_STRING);
    SQLException nextE = e.getNextException();
    while (null != nextE) {
      msg.append(" Submsg=").append(nextE).append(" code=")
          .append(nextE.getErrorCode()).append(" state=")
          .append(nextE.getSQLState());
      nextE = nextE.getNextException();
    } // end of while ()
    return msg.toString();
  }

  /**
   * analysis Types of host variables. <br>
   * mapping:
   * <table border=1>
   * <tr>
   * <th>Class Name</th>
   * <th>Type</th>
   * </tr>
   * <tr>
   * <td>String[], String</td>
   * <td>Types.VARCHAR</td>
   * </tr>
   * <tr>
   * <td>Integer[], Integer, int[]</td>
   * <td>Types.INTEGER</td>
   * </tr>
   * <tr>
   * <td>Long[], Long, long[]</td>
   * <td>Types.BIGINT</td>
   * </tr>
   * <tr>
   * <td>Double[], Double, double[]</td>
   * <td>Types.DOUBLE</td>
   * </tr>
   * <tr>
   * <td>Timestamp[], Timestamp</td>
   * <td>Types.TIMESTAMP</td>
   * </tr>
   * <tr>
   * <td>BigDecimal[], BigDecimal</td>
   * <td>Types.DECIMAL</td>
   * </tr>
   * <tr>
   * <td>byte[]</td>
   * <td>Types.DOUBLE</td>
   * </tr>
   * </table>
   * Remark: Why are class names used here and not classes compared directly
   * like in <code>int[].class.equals(data.getClass())</code>?
   * 
   * @param data
   *          host variables
   * @param forcetype
   *          force type by overwriting with given java.sql.Types (0 == no
   *          overwrite)
   * @return a <code>HostVariableTypes</code> value
   * @exception SQLException
   *              if an error occurs
   */
  protected HostVariableTypes analysisTypes(Object[] data, int[] forcetype)
      throws SQLException {
    HostVariableTypes rtn = new HostVariableTypes(data.length);
    for (int i = 0; i < data.length; i++) {
      if (null != data[i]) {
        setHostVariableType(i, rtn, data[i]);
      } else {
        throw new SQLException(
            "data item may not be null. Use array with null object instead.");
      }
      if (null != forcetype // overwrite type with forcetype
          && forcetype.length > i && 0 != forcetype[i]) {
        rtn.abstractType[i] = forcetype[i];
      }
    }
    return rtn;
  }

  /**
   * Analyze and set single Types of host variables.
   * 
   * @see #analysisTypes(Object[] data, int[] forcetype)
   * @param index
   *          the index of the hostVarTypes to be set
   * @param hostVarTypes
   *          a prepared object with size > index to set the result of the
   *          analyses of the singleDataObject.
   * @param singleDataObject
   *          The object (not null) which type has to be analyzed.
   * @exception SQLException
   *              if an error occurs
   */
  protected void setHostVariableType(int index, HostVariableTypes hostVarTypes,
      Object singleDataObject) throws SQLException {
    this.setHostVariableType(index, hostVarTypes, singleDataObject, null);
  }

  /**
   * Analyze and set single Types of host variables.
   * 
   * @see #analysisTypes(Object[] data, int[] forcetype)
   * @param index
   *          the index of the hostVarTypes to be set
   * @param hostVarTypes
   *          a prepared object with size > index to set the result of the
   *          analyses of the singleDataObject.
   * @param singleDataObject
   *          The object which type has to be analyzed, arrays are allowed.
   *          Either this object is not null or aClass is not null.
   * @param aClass
   *          Ignored if singleDataObject not null. We can pass the classname of
   *          an object instead of an instance of the object, Arrays are then
   *          assumed to be of the size 1 !!!
   * @exception SQLException
   *              if an error occurs
   */
  protected void setHostVariableType(int index, HostVariableTypes hostVarTypes,
      Object singleDataObject, Class<?> aClass) throws SQLException {
    String dataClassName = null;
    if (singleDataObject != null) {
      dataClassName = singleDataObject.getClass().getName();
    } else {
      dataClassName = aClass.getName();
    }
    hostVarTypes.isArray[index] = true;
    hostVarTypes.isObject[index] = true;
    hostVarTypes.rows = Math.max(hostVarTypes.rows, 1);
    if ("[Ljava.lang.String;".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.VARCHAR;
      if (singleDataObject != null) {
        hostVarTypes.rows = Math.max(hostVarTypes.rows,
            ((Object[]) singleDataObject).length);
      }
    } else if ("java.lang.String".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.VARCHAR;
      hostVarTypes.isArray[index] = false;
    } else if ("[[B".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.BINARY;
    } else if ("[B".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.BINARY;
      hostVarTypes.isArray[index] = false;
    } else if ("[Ljava.lang.Integer;".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.INTEGER;
      if (singleDataObject != null) {
        hostVarTypes.rows = Math.max(hostVarTypes.rows,
            ((Object[]) singleDataObject).length);
      }
    } else if ("java.lang.Integer".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.INTEGER;
      hostVarTypes.isArray[index] = false;
    } else if ("[I".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.INTEGER;
      hostVarTypes.isObject[index] = false;
      if (singleDataObject != null) {
        hostVarTypes.rows = Math.max(hostVarTypes.rows,
            ((int[]) singleDataObject).length);
      }
    } else if ("[Ljava.lang.Long;".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.BIGINT;
      if (singleDataObject != null) {
        hostVarTypes.rows = Math.max(hostVarTypes.rows,
            ((Object[]) singleDataObject).length);
      }
    } else if ("java.lang.Long".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.BIGINT;
      hostVarTypes.isArray[index] = false;
    } else if ("[J".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.BIGINT;
      hostVarTypes.isObject[index] = false;
      if (singleDataObject != null) {
        hostVarTypes.rows = Math.max(hostVarTypes.rows,
            ((long[]) singleDataObject).length);
      }
    } else if ("[Ljava.lang.Double;".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.DOUBLE;
      if (singleDataObject != null) {
        hostVarTypes.rows = Math.max(hostVarTypes.rows,
            ((Object[]) singleDataObject).length);
      }
    } else if ("java.lang.Double".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.DOUBLE;
      hostVarTypes.isArray[index] = false;
    } else if ("[D".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.DOUBLE;
      hostVarTypes.isObject[index] = false;
      if (singleDataObject != null) {
        hostVarTypes.rows = Math.max(hostVarTypes.rows,
            ((double[]) singleDataObject).length);
      }
    } else if ("[Ljava.sql.Timestamp;".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.TIMESTAMP;
      if (singleDataObject != null) {
        hostVarTypes.rows = Math.max(hostVarTypes.rows,
            ((Object[]) singleDataObject).length);
      }
    } else if ("java.sql.Timestamp".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.TIMESTAMP;
      hostVarTypes.isArray[index] = false;
    } else if ("[Ljava.math.BigDecimal;".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.DECIMAL;
      if (singleDataObject != null) {
        hostVarTypes.rows = Math.max(hostVarTypes.rows,
            ((Object[]) singleDataObject).length);
      }
    } else if ("java.math.BigDecimal".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.DECIMAL;
      hostVarTypes.isArray[index] = false;
    } else if ("[Ljava.sql.Date;".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.DATE;
      if (singleDataObject != null) {
        hostVarTypes.rows = Math.max(hostVarTypes.rows,
            ((Object[]) singleDataObject).length);
      }
    } else if ("java.sql.Date".equals(dataClassName)) {
      hostVarTypes.abstractType[index] = Types.DATE;
      hostVarTypes.isArray[index] = false;
    } else {
      throw new SQLException("unsupported type=" + dataClassName);
    }
  }

  /**
   * set Host Variables values to cursor. <br>
   * tested of null values by {@link KiDbalExecTest#testCursorInsertB()
   * KiDbalExecTest.testCursorInsertB()} <br>
   * supported types
   * <table border=1>
   * <tr>
   * <th>type</th>
   * <th>setter methode</th>
   * </tr>
   * <tr>
   * <td>Types.VARCHAR</td>
   * <td>setString()</td>
   * </tr>
   * <tr>
   * <td>Types.INTEGER</td>
   * <td>setInt()</td>
   * </tr>
   * <tr>
   * <td>Types.BIGINT</td>
   * <td>setLong()</td>
   * </tr>
   * <tr>
   * <td>Types.DOUBLE</td>
   * <td>setDouble()</td>
   * </tr>
   * <tr>
   * <td>Types.TIMESTAMP</td>
   * <td>setTimestamp()</td>
   * </tr>
   * <tr>
   * <td>Types.DECIMAL</td>
   * <td>setBigDecimal()</td>
   * </tr>
   * <tr>
   * <td>KiDbal.VARCHAR4K</td>
   * <td>setString(), mysql-isam setCharacterStream()</td>
   * </tr>
   * <tr>
   * <td>KiDbal.UVARCHAR</td>
   * <td>setString(), oracle setFormOfUse(NCHAR)</td>
   * </tr>
   * <tr>
   * <td>KiDbal.BINARY</td>
   * <td>setByte()</td>
   * </tr>
   * </table>
   * 
   * @param pst
   *          Prepared Statement
   * @param types
   *          Host Variable Types analysis
   * @param k
   *          index row to set
   * @param data
   *          host variables for the sql host variables. The elements are arrays
   *          or Objects. The longest array determines the number of cursor
   *          executions (inserts). If an array is smaller as the other array
   *          its last value is repeated for the missing values. If a scalar
   *          Object (elementary types are not possible use array[1] instead) is
   *          given, its interpreted as an array[1].
   * @param cal
   *          Calendar for timestamps
   * @exception SQLException
   *              if an error occurs
   */
  protected void setHostVariables(PreparedStatement pst,
      HostVariableTypes types, int k, Object[] data, Calendar cal)
      throws SQLException {
    for (int i = 0; i < data.length; i++) {
      switch (types.abstractType[i]) {
      case Types.VARCHAR: // string
        if (types.isArray[i]) {
          String[] val = (String[]) data[i];
          pst.setString(1 + i, val[Math.min(k, val.length - 1)]);
        } else {
          pst.setString(1 + i, (String) data[i]);
        }
        break;
      case Types.BINARY: // byte array
        if (types.isArray[i]) {
          byte[][] val = (byte[][]) data[i];
          pst.setBytes(1 + i, val[Math.min(k, val.length - 1)]); // ????
        } else {
          pst.setBytes(1 + i, (byte[]) data[i]);
        }
        break;
      case Types.INTEGER: // int
        if (types.isObject[i]) {
          if (types.isArray[i]) {
            Integer[] val = (Integer[]) data[i];
            int ii = Math.min(k, val.length - 1);
            if (null == val[ii]) {
              pst.setNull(1 + i, Types.INTEGER);
            } else {
              pst.setInt(1 + i, val[ii].intValue());
            }
          } else {
            if (null == data[i]) {
              pst.setNull(1 + i, Types.INTEGER);
            } else {
              pst.setInt(1 + i, ((Integer) data[i]).intValue());
            }
          }
        } else {
          int[] val = (int[]) data[i];
          pst.setInt(1 + i, val[Math.min(k, val.length - 1)]);
        }
        break;
      case Types.BIGINT: // long
        if (types.isObject[i]) {
          if (types.isArray[i]) {
            Long[] val = (Long[]) data[i];
            int ii = Math.min(k, val.length - 1);
            if (null == val[ii]) {
              pst.setNull(1 + i, Types.BIGINT);
            } else {
              pst.setLong(1 + i, val[ii].longValue());
            }
          } else {
            if (null == data[i]) {
              pst.setNull(1 + i, Types.BIGINT);
            } else {
              pst.setLong(1 + i, ((Long) data[i]).longValue());
            }
          }
        } else {
          long[] val = (long[]) data[i];
          pst.setLong(1 + i, val[Math.min(k, val.length - 1)]);
        }
        break;
      case Types.DOUBLE: // double
        if (types.isObject[i]) {
          if (types.isArray[i]) {
            Double[] val = (Double[]) data[i];
            int ii = Math.min(k, val.length - 1);
            if (null == val[ii]) {
              pst.setNull(1 + i, Types.DOUBLE);
            } else {
              pst.setDouble(1 + i, val[ii].doubleValue());
            }
          } else {
            if (null == data[i]) {
              pst.setNull(1 + i, Types.DOUBLE);
            } else {
              pst.setDouble(1 + i, ((Double) data[i]).doubleValue());
            }
          }
        } else {
          double[] val = (double[]) data[i];
          pst.setDouble(1 + i, val[Math.min(k, val.length - 1)]);
        }
        break;
      case Types.TIMESTAMP: // timestamp
        if (types.isArray[i]) {
          Timestamp[] val = (Timestamp[]) data[i];
          if (null == cal) {
            pst.setTimestamp(1 + i, val[Math.min(k, val.length - 1)]);
          } else {
            pst.setTimestamp(1 + i, val[Math.min(k, val.length - 1)], cal);
          }
        } else {
          if (null == cal) {
            pst.setTimestamp(1 + i, (Timestamp) data[i]);
          } else {
            pst.setTimestamp(1 + i, (Timestamp) data[i], cal);
          }
        }
        break;
      case Types.DECIMAL: // decimal
        if (types.isArray[i]) {
          BigDecimal[] val = (BigDecimal[]) data[i];
          pst.setBigDecimal(1 + i, val[Math.min(k, val.length - 1)]);
        } else {
          pst.setBigDecimal(1 + i, (BigDecimal) data[i]);
        }
        break;
      case Types.DATE: // date
        if (types.isArray[i]) {
          Date[] val = (Date[]) data[i];
          if (null == cal) {
            pst.setDate(1 + i, val[Math.min(k, val.length - 1)]);
          } else {
            pst.setDate(1 + i, val[Math.min(k, val.length - 1)], cal);
          }
        } else {
          if (null == cal) {
            pst.setDate(1 + i, (Date) data[i]);
          } else {
            pst.setDate(1 + i, (Date) data[i], cal);
          }
        }
        break;
      default:
        break;
      }
    }
  }

  /**
   * Build the query SQL.
   * 
   * @param stm
   *          the sql statement
   * @return the query SQL
   */
  private String buildQuerySql(String stm) {
    return this.buildQuerySql(stm, null, 0, null);
  }

  /**
   * Build the query SQL.
   * 
   * @param stm
   *          the sql statement
   * @param types
   *          the types
   * @param k
   *          the k
   * @param data
   *          the data
   * @return the query SQL
   */
  private String buildQuerySql(String stm, HostVariableTypes types, int k,
      Object[] data) {
    StringBuilder sb = new StringBuilder();
    if (null != stm) {
      sb.append(" stm=").append(stm);
    }
    if (null != data) {
      sb.append(" data=(").append(debugStringSetHostVariables(types, k, data))
          .append(")");
    }
    return sb.toString();
  }

  /**
   * Build error message.
   * 
   * @param e
   *          the SQLException
   * @param stm
   *          the sql statement
   * @return the error message
   */
  private String buildErrorMessage(SQLException e, String stm) {
    return this.buildErrorMessage(e, stm, null, 0, null);
  }

  /**
   * Move the cursor to the first required row of the ResultSet
   * 
   * @param rs
   *          the ResultSet
   * @param startRow
   *          the start row
   * @throws SQLException
   */
  private void moveCursor(ResultSet rs, int startRow) throws SQLException {
    if (0 != startRow) {
      try {
        // go straight to the first required row
        rs.absolute(startRow);
      } catch (SQLException e) {
        // step through the rows one row at a time (slow)
        for (int i = 0; i < startRow; i++) {
          rs.next();
        }
      }
    }
  }

  /**
   * Helper function to get the value of a column as an object. We know which
   * types we want to get.
   * 
   * @param metaData
   *          The select meta data
   * @param expectedColumnTypes
   *          An array of classes for the column types expected.
   * @param result
   *          the result set
   * @param i
   *          the column index
   * @param cal
   *          The calendar used for timestamp culumns
   * @return the value
   * @throws SQLException
   *           if an SQl error occurs
   * @throws IOException
   *           if an error occurs when reading from a clob.
   */
  private Object getColumnValue(ResultSetMetaData metaData,
      Class<?>[] expectedColumnTypes, ResultSet result, int i, Calendar cal)
      throws SQLException, IOException {
    Object o = null;

    Class<?> expectedColumnType = expectedColumnTypes[i - 1];
    try {
      if (expectedColumnType.equals(String.class)) {
        /*
         * A String can originate from different column types, that have to be
         * handled differently.
         */
        switch (metaData.getColumnType(i)) {
        case Types.NCLOB:
          o = nclobToString(result.getNClob(i));
          break;
        case Types.CLOB:
          o = clobToString(result.getClob(i));
          break;
        case Types.LONGNVARCHAR:
        case Types.LONGVARCHAR:
          o = streamToString(result.getCharacterStream(i));
          break;
        case Types.NVARCHAR:
        case Types.NCHAR:
          o = result.getNString(i);
          if (null != o && ((String) o).length() < 1) {
            o = null;
          }
          break;
        case Types.CHAR:
        case Types.VARCHAR:
        default:
          // log.debug(" varchar-length="+metaData.getColumnDisplaySize(i));
          o = result.getString(i);
          if (null != o && ((String) o).length() < 1) {
            o = null;
          }
          break;
        }
      } else if (expectedColumnType.equals(Integer.class)) {
        int value = result.getInt(i);
        if (!result.wasNull()) {
          o = Integer.valueOf(value);
        }
      } else if (expectedColumnType.equals(Long.class)) {
        long value = result.getLong(i);
        if (!result.wasNull()) {
          o = Long.valueOf(value);
        }
      } else if (expectedColumnType.equals(Double.class)) {
        double value = result.getDouble(i);
        if (!result.wasNull()) {
          o = new Double(value);
        }
      } else if (expectedColumnType.equals(Timestamp.class)) {
        if (cal != null) {
          o = result.getTimestamp(i, cal);
        } else {
          o = result.getTimestamp(i);
        }
      } else if (expectedColumnType.equals(byte[].class)) {
        byte[] bArr = result.getBytes(i);
        if (!result.wasNull()) {
          o = bArr;
        }
      } else if (expectedColumnType.equals(BigDecimal.class)) {
        o = result.getBigDecimal(i);
      } else {
        o = result.getObject(i);
        logger.warn("#### unexpected Datatype sqltype="
            + metaData.getColumnType(i) + " class=" + o.getClass().getName());
      }
    } catch (SQLException e) {
      logger.error("getColumnValue error=" + e + " expectedColumnType="
          + expectedColumnType + " colIdx=" + i, e);
      throw e;
    }
    return o;
  }

  /**
   * Helper function to get the value of a column as an object.
   * 
   * @param metaData
   *          The select meta data
   * @param result
   *          the result set
   * @param i
   *          the column index
   * @param cal
   *          The calendar used for timestamp culumns
   * @return the value
   * @throws SQLException
   *           if an SQl error occurs
   * @throws IOException
   *           if an error occurs when reading from a clob.
   */
  private Object getColumnValue(ResultSetMetaData metaData, ResultSet result,
      int i, Calendar cal) throws SQLException, IOException {
    Object o = null;

    switch (metaData.getColumnType(i)) {
    case Types.NCLOB:
      o = nclobToString(result.getNClob(i));
      break;
    case Types.CLOB:
      o = clobToString(result.getClob(i));
      break;
    case Types.LONGVARCHAR:
    case Types.LONGNVARCHAR:
      o = streamToString(result.getCharacterStream(i));
      break;
    case Types.NVARCHAR:
    case Types.NCHAR:
      o = result.getNString(i);
      if (null != o && ((String) o).length() < 1) {
        o = null;
      }
      break;
    case Types.CHAR:
    case Types.VARCHAR:
      // log.debug(" varchar-length="+metaData.getColumnDisplaySize(i));
      o = result.getString(i);
      if (null != o && ((String) o).length() < 1) {
        o = null;
      }
      break;
    case Types.DATE:
    case Types.TIME:
    case Types.TIMESTAMP:
      if (null == cal) {
        o = result.getTimestamp(i);
      } else {
        o = result.getTimestamp(i, cal);
      }
      break;
    case Types.DECIMAL:
    case Types.NUMERIC:
      if (0 == metaData.getScale(i)) {
        if (metaData.getPrecision(i) <= 10) {
          int value = result.getInt(i);
          if (!result.wasNull()) {
            o = Integer.valueOf(value);
          }
        } else {
          long value = result.getLong(i);
          if (!result.wasNull()) {
            o = Long.valueOf(value);
          }
        }
      } else {
        if (dialect instanceof Oracle8iDialect && -127 == metaData.getScale(i)) {
          // oracle: number scales are in range -84..127, therefore -127 is a
          // indicator for float datatype
          double dvalue = result.getDouble(i);
          if (!result.wasNull()) {
            o = new Double(dvalue);
          }
        } else {
          o = result.getBigDecimal(i);
        }

      }
      break;
    case Types.DOUBLE:
    case Types.FLOAT:
    case Types.REAL:
      double dvalue = result.getDouble(i);
      if (!result.wasNull()) {
        o = new Double(dvalue);
      }
      break;
    case Types.BIGINT:
      long lvalue = result.getLong(i);
      if (!result.wasNull()) {
        o = Long.valueOf(lvalue);
      }
      break;
    case Types.INTEGER:
    case Types.SMALLINT:
    case Types.TINYINT:
      try {
        int ivalue = result.getInt(i);
        if (!result.wasNull()) {
          o = Integer.valueOf(ivalue);
        }
      } catch (ClassCastException e) {
        int sqltype = metaData.getColumnType(i);
        logger.info("Hit mssql driver bug column=" + i
            + " try getString as work around " + " sql type is=" + sqltype);
        o = result.getString(i);
        if (null != o && ((String) o).length() < 1) {
          o = null;
        }
      }
      break;
    case Types.BINARY:
      if (!result.wasNull()) {
        byte[] bb = result.getBytes(i);
        if (null != bb && bb.length > 0) {
          o = bb;
        }
      }
      break;
    case Types.VARBINARY: // oracle wasNull() thows exception
    case Types.LONGVARBINARY:
    case Types.BLOB:
      byte[] bb = result.getBytes(i);
      if (null != bb && bb.length > 0) {
        o = bb;
      }

      // o = result.getBytes(i);
      break;
    case Types.OTHER:
      if (!result.wasNull()) {
        o = result.getObject(i);
      }
      break;
    default:
      o = result.getObject(i);
      if (o == null) {
        logger.warn("#### unexpected Datatype sqltype="
            + metaData.getColumnType(i) + " value=null");
      } else {
        if (!(metaData.getColumnType(i) == Types.BIT)) {
          logger.warn("#### unexpected Datatype sqltype="
              + metaData.getColumnType(i) + " class=" + o.getClass().getName());
        }
      }
      break;
    } // end of switch ()
    return o;
  }

  /**
   * Build error message.
   * 
   * @param e
   *          the SQLException
   * @param stm
   *          the sql statement
   * @param types
   *          the types
   * @param k
   *          the k
   * @param data
   *          the data
   * @return the error message
   */
  private String buildErrorMessage(SQLException e, String stm,
      HostVariableTypes types, int k, Object[] data) {
    StringBuilder sb = new StringBuilder();
    sb.append(formatMsg(e)).append(buildQuerySql(stm, types, k, data));
    return sb.toString();
  }

  /**
   * represents the result of Host Variable Type analysis.
   * 
   */
  protected static class HostVariableTypes {
    /** sql types. */
    int[] abstractType = null;
    /** variable is an Object. */
    boolean[] isObject = null;
    /** variable is an array. */
    boolean[] isArray = null;
    /** maximum number of rows. */
    int rows = 0;

    /**
     * constructor.
     * 
     * @param size
     *          size of arrays.
     */
    public HostVariableTypes(int size) {
      abstractType = new int[size];
      isObject = new boolean[size];
      isArray = new boolean[size];
    }
  }
}
