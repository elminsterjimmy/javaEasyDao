package com.elminster.easydao.db.analyze;

import java.lang.reflect.Method;

import com.elminster.easydao.db.analyze.data.PagedData;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
import com.elminster.easydao.db.constants.SqlConstants;
import com.elminster.easydao.db.dialect.Dialect;
import com.elminster.easydao.db.dialect.IDialect;
import com.elminster.easydao.db.session.DAOSupportSession;
import com.elminster.easydao.db.session.ThreadSessionMap;

abstract public class BaseSqlAnalyzer implements ISqlAnalyzer {

  public BaseSqlAnalyzer() {
  }
  
  /**
   * Parser a SQL statement to get the SQL statement and the SQL statement's
   * parameter(s).
   * 
   * @param method
   *          the method which original SQL statement is invoked
   * @param methodArguments
   *          the method's argument(s)
   * @return SqlStatementInfo SQL statement information
   * @throws Exception
   */
  public SqlStatementInfo parser(Method invokedMethod,
      Object... methodArguments) throws Exception {

    boolean usePaged = false;
    PagedData pagedData = null;

    if (null != methodArguments) {
      for (Object arg : methodArguments) {
        if (arg instanceof PagedData) {
          usePaged = true;
          pagedData = (PagedData) arg;
          break;
        }
      }
    }

    SqlStatementInfo sqlStatementInfo = new SqlStatementInfo();

    AnalyzedSqlData analyzedSqlData = analyzeSql(invokedMethod, methodArguments);

    // paged result
    if (usePaged) {
      IDialect dialect = getDialect();
      if (null == dialect) {
        dialect = new Dialect();
      }
      if (dialect.supportPaged()) {
        if (pagedData.hasOffset()) {
          if (dialect.supportOffset()) {
            analyzedSqlData.setAnalyzedSql(dialect.getLimitSql(analyzedSqlData.getAnalyzedSql(), true));
            analyzedSqlData.addAnalyzedSqlParameter(dialect.convertToFirstRowValue(pagedData.getStartRow()));
            analyzedSqlData.addAnalyzedSqlParameter(getMaxOrLimit(pagedData, dialect));
          } else {
            // TODO
          }
        } else {
          analyzedSqlData.setAnalyzedSql(dialect.getLimitSql(analyzedSqlData.getAnalyzedSql(), false));
          analyzedSqlData.addAnalyzedSqlParameter(getMaxOrLimit(pagedData, dialect));
        }
      }
    }

    sqlStatementInfo.setAnalyzedSqlStatement(analyzedSqlData.getAnalyzedSql());
    sqlStatementInfo.setAnalyzedSqlParameters(analyzedSqlData.getAnalyzedSqlParameters());
    sqlStatementInfo.setAnalyzedSqlType(getSqlType(analyzedSqlData.getAnalyzedSql()));
    sqlStatementInfo.setUsePaged(usePaged);
    sqlStatementInfo.setPagedData(pagedData);
    return sqlStatementInfo;
  }

  /**
   * Some dialect-specific LIMIT clauses require the maximium last row number
   * (aka, first_row_number + total_row_count), while others require the maximum
   * returned row count (the total maximum number of rows to return).
   * 
   * @param selection
   *          The selection criteria
   * @param dialect
   *          The dialect
   * @return The appropriate value to bind into the limit clause.
   */
  private static int getMaxOrLimit(PagedData pagedData, IDialect dialect) {
    int firstRow = pagedData.getStartRow();
    int lastRow = pagedData.getEndRow();
    if (dialect.useMaxForLimit()) {
      return dialect.convertToFirstRowValue(lastRow);
    } else {
      return lastRow - firstRow;
    }
  }

  protected IDialect getDialect() {
    DAOSupportSession session = ThreadSessionMap.INSTANCE.getSessionPerThread(Thread.currentThread());
    if (null == session) {
      throw new RuntimeException("Session is null.");
    }
    return session.getDialect();
  }

  abstract protected AnalyzedSqlData analyzeSql(Method invokedMethod,
      Object... methodArguments) throws Exception;

  /**
   * Get SQL statement type.
   * 
   * @return SQL statement type
   */
  protected SqlType getSqlType(String analyzedSql) {
    SqlType type = null;
    if (null != analyzedSql) {
      boolean first = false;
      StringBuilder builder = new StringBuilder(15);
      for (int i = 0; i < analyzedSql.length(); i++) {
        char ch = analyzedSql.charAt(i);
        if (Character.isLetter(ch)) {
          if (!first) {
            first = true;
          }
          builder.append(ch);
        } else {
          if (!first) {
            continue;
          } else {
            break;
          }
        }
      }
      if (SqlConstants.SELECT.equalsIgnoreCase(builder.toString())) {
        type = SqlType.QUERY;
      } else if (SqlConstants.CALL.equals(builder.toString())) {
        type = SqlType.STORED_PROCEDURE;
      } else {
        type = SqlType.UPDATE;
      }
    }
    return type;
  }
}
