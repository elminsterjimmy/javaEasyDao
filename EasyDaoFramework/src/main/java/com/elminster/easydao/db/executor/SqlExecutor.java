package com.elminster.easydao.db.executor;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.elminster.common.util.JDBCUtil;
import com.elminster.easydao.db.analyze.data.PagedData;
import com.elminster.easydao.db.analyze.data.ScrollMode;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
import com.elminster.easydao.db.dialect.IDialect;
import com.elminster.easydao.db.exception.SQLExecuteException;
import com.elminster.easydao.db.exception.SqlAnalyzeException;
import com.elminster.easydao.db.handler.IResultSetHandler;
import com.elminster.easydao.db.handler.ResultSetHandlerFactory;
import com.elminster.easydao.db.manager.DAOSupportSession;

public class SqlExecutor implements ISqlExecutor {

  private static final Log logger = LogFactory.getLog(SqlExecutor.class);

  private Connection conn;
  private ResultSetHandlerFactory resultSetHandlerFactory = ResultSetHandlerFactory.getInstance();
  private DAOSupportSession session;
  
  public SqlExecutor(DAOSupportSession session) {
    this.session = session;
  }
  
  public Object execute(SqlStatementInfo sqlStatementInfo, Method invokeMethod,
      Object[] args) throws SQLExecuteException {
    this.conn = getConnection(invokeMethod, args);
    Object rst = null;
    SqlType sqlType = sqlStatementInfo.getAnalyzedSqlType();
    if (SqlType.UPDATE == sqlType) {
      rst = executeUpdate(sqlStatementInfo, invokeMethod);
    } else if (SqlType.QUERY == sqlType) {
      rst = executeQuery(sqlStatementInfo, invokeMethod);
    } else if (SqlType.STORED_PROCEDURE == sqlType) {

    }

    return rst;
  }

  private Connection getConnection(Method invokeMethod, Object[] args) {
    for (Object arg : args) {
      if (arg instanceof Connection) {
        return (Connection) arg;
      }
    }
    return session.getConnection();
  }

  private Object executeQuery(SqlStatementInfo sqlStatementInfo,
      Method invokeMethod) {

    PreparedStatement pstmt = null;
    ResultSet rs = null;
    IResultSetHandler resultSetHandler = null;
    try {
      pstmt = createPreparedStatement(sqlStatementInfo);
      rs = getResultSet(pstmt, sqlStatementInfo);

      resultSetHandler = resultSetHandlerFactory
          .getResultSetHandler(invokeMethod);
      return resultSetHandler.handleResultSet(rs);
    } catch (Exception e) {
      throw new SQLExecuteException(e);
    } finally {
      JDBCUtil.closeResultSet(rs);
      JDBCUtil.closeStatement(pstmt);
    }
  }

  private Object executeUpdate(SqlStatementInfo sqlStatementInfo,
      Method invokeMethod) {
    PreparedStatement pstmt = null;
    try {
      pstmt = createPreparedStatement(sqlStatementInfo);
      int updateCount = pstmt.executeUpdate();

      Class<?> returnClazz = invokeMethod.getReturnType();

      if (null == returnClazz) {
        return null;
      } else if (Integer.class == returnClazz || Integer.TYPE == returnClazz) {
        return updateCount;
      } else {
        return null;
      }
    } catch (Exception e) {
      throw new SQLExecuteException(e);
    } finally {
      JDBCUtil.closeStatement(pstmt);
    }
  }

  private PreparedStatement createPreparedStatement(
      SqlStatementInfo sqlStatementInfo) throws SQLException {
    String sql = sqlStatementInfo.getAnalyzedSqlStatement();
    List<Object> params = sqlStatementInfo.getAnalyzedSqlParameters();

    // IS Show SQL
    if (session.getConfiguraton().isShowSql()) {
      logger.debug("SQL: " + sql);
      logger.debug("Parameter(s): " + params.toString());
    }

    PreparedStatement pstmt = null;

    PagedData pagedData = sqlStatementInfo.getPagedData();
    IDialect dialect = session.getDialect();
    boolean usePaged = sqlStatementInfo.isUsePaged();
    boolean callable = sqlStatementInfo.isCallable();
    boolean hasOffset = null == pagedData ? false : pagedData.hasOffset();
    boolean useScrollable = hasOffset && !(usePaged && dialect.supportOffset());
    ScrollMode scrollMode = null == sqlStatementInfo.getScrollMode() ? ScrollMode.SCROLL_INSENSITIVE
        : sqlStatementInfo.getScrollMode();

    if (useScrollable) {
      if (callable) {
        pstmt = conn.prepareCall(sql, scrollMode.getResultSetType(),
            ResultSet.CONCUR_READ_ONLY);
      } else {
        pstmt = conn.prepareStatement(sql, scrollMode.getResultSetType(),
            ResultSet.CONCUR_READ_ONLY);
      }
    } else {
      if (callable) {
        pstmt = conn.prepareCall(sql);
      } else {
        pstmt = conn.prepareStatement(sql);
      }
    }

    ParameterMetaData pmd = pstmt.getParameterMetaData();
    if (null != params) {
      if (pmd.getParameterCount() != params.size()) {
        throw new SqlAnalyzeException("Parameters count is NOT matched.");
      }
      for (int i = 0; i < params.size(); i++) {
        Object obj = params.get(i);
        if (null == obj) {
          int type = Types.VARCHAR;
          try {
            type = pmd.getParameterType(i + 1);
          } catch (SQLException e) {
          }
          pstmt.setNull(i + 1, type);
        } else {
          pstmt.setObject(i + 1, obj);
        }
      }
    }
    return pstmt;
  }

  private ResultSet getResultSet(PreparedStatement pstmt,
      SqlStatementInfo sqlStatementInfo) throws SQLException {
    ResultSet rs = null;
    IDialect dialect = session.getDialect();
    PagedData pagedData = sqlStatementInfo.getPagedData();
    boolean usePaged = sqlStatementInfo.isUsePaged();
    boolean callable = sqlStatementInfo.isCallable();
    boolean hasOffset = null == pagedData ? false : pagedData.hasOffset();
    boolean useScrollable = hasOffset && !(usePaged && dialect.supportOffset());
    if (callable) {
      rs = dialect.getResultSet((CallableStatement) pstmt);
    } else {
      rs = pstmt.executeQuery();
    }

    // only if scrollable
    if (useScrollable) {
      moveCursor(rs, pagedData);
    }
    return rs;
  }

  /**
   * Move the cursor to the first required row of the ResultSet
   * 
   * @param rs
   *          the ResultSet
   * @param pagedData
   *          paged data
   * @throws SQLException
   */
  private void moveCursor(ResultSet rs, PagedData pagedData)
      throws SQLException {
    int startRow = pagedData.getStartRow();
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
}
