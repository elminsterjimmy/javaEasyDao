package com.elminster.easydao.db.executor;

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
import com.elminster.easydao.db.dialect.IDialect;
import com.elminster.easydao.db.handler.IResultSetHandler;
import com.elminster.easydao.db.manager.DAOSupportSession;
import com.elminster.easydao.db.mapping.IMappingExecutor;
import com.elminster.easydao.db.mapping.MappingExecutorFactory;

public class SqlExecutor implements ISqlExecutor {

  private static final Log logger = LogFactory.getLog(SqlExecutor.class);

  private MappingExecutorFactory mappingExecutorFactory = MappingExecutorFactory
      .getInstance();
  private DAOSupportSession session;

  public SqlExecutor(DAOSupportSession session) {
    this.session = session;
  }

  @Override
  public Object executeQuery(SqlStatementInfo sqlStatementInfo,
      IResultSetHandler resultHandler) throws ExecuteException {

    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      pstmt = createPreparedStatement(sqlStatementInfo);
      rs = getResultSet(pstmt, sqlStatementInfo);

      Object obj = resultHandler.handleResultSet(rs);

      if (sqlStatementInfo.isMapping()) {
        IMappingExecutor mappingExecutor = mappingExecutorFactory
            .getMappingExecutor(sqlStatementInfo.getMappingPolicy(), this, this.session);
        obj = mappingExecutor.executeQuery(sqlStatementInfo, obj);
      }

      return obj;
    } catch (Exception e) {
      throw new ExecuteException(e);
    } finally {
      JDBCUtil.closeResultSet(rs);
      JDBCUtil.closeStatement(pstmt);
    }
  }

  public int executeUpdate(SqlStatementInfo sqlStatementInfo) throws ExecuteException {
    PreparedStatement pstmt = null;
    try {
      pstmt = createPreparedStatement(sqlStatementInfo);
      int updateCount = pstmt.executeUpdate();
      if (sqlStatementInfo.isMapping()) {
        IMappingExecutor mappingExecutor = mappingExecutorFactory
            .getMappingExecutor(sqlStatementInfo.getMappingPolicy(), this, this.session);
        updateCount += mappingExecutor.executeExecute(sqlStatementInfo, updateCount);
      }
      return updateCount;
    } catch (Exception e) {
      throw new ExecuteException(e);
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
    Connection conn = session.getConnection();
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
        throw new SQLException("Parameters count is NOT matched.");
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
