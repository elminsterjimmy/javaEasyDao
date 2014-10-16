package com.elminster.easydao.db.analyze;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.elminster.easydao.db.analyze.data.MappingSqlStatementInfo;
import com.elminster.easydao.db.analyze.data.PagedData;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
import com.elminster.easydao.db.constants.SqlConstants;
import com.elminster.easydao.db.dialect.Dialect;
import com.elminster.easydao.db.dialect.IDialect;
import com.elminster.easydao.db.id.IdGenerator;
import com.elminster.easydao.db.manager.DAOSupportSession;
import com.elminster.easydao.db.mapping.MappingPolicy;

abstract public class BaseSqlAnalyzer implements ISqlAnalyzer {

  protected Class<?> originalClass;
  protected String analyzedSql;
  protected List<Object> analyzedSqlParameters = new ArrayList<Object>();
  protected boolean mapping;
  protected MappingPolicy mappingPolicy;
  protected List<MappingSqlStatementInfo> mappingStatementInfo;
  protected DAOSupportSession session;
  protected IdGenerator idGenerator;

  public BaseSqlAnalyzer(DAOSupportSession session) {
    this.session = session;
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
      Object... methodArguments) throws AnalyzeException {

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

    analyzeSql(invokedMethod, methodArguments);

    // paged result
    if (usePaged) {
      IDialect dialect = getDialect();
      if (null == dialect) {
        dialect = new Dialect();
      }
      if (dialect.supportPaged()) {
        if (pagedData.hasOffset()) {
          if (dialect.supportOffset()) {
            analyzedSql = dialect.getLimitSql(analyzedSql, true);
            analyzedSqlParameters.add(dialect.convertToFirstRowValue(pagedData.getStartRow()));
            analyzedSqlParameters.add(getMaxOrLimit(pagedData, dialect));
          } else {
            // TODO
          }
        } else {
          analyzedSql = dialect.getLimitSql(analyzedSql, false);
          analyzedSqlParameters.add(getMaxOrLimit(pagedData, dialect));
        }
      }
    }

    sqlStatementInfo.setAnalyzedSqlStatement(analyzedSql);
    sqlStatementInfo.setAnalyzedSqlParameters(analyzedSqlParameters);
    sqlStatementInfo.setAnalyzedSqlType(getSqlType());
    sqlStatementInfo.setUsePaged(usePaged);
    sqlStatementInfo.setPagedData(pagedData);
    sqlStatementInfo.setMapping(mapping);
    sqlStatementInfo.setMappingPolicy(mappingPolicy);
    sqlStatementInfo.setIdGenerator(idGenerator);
    sqlStatementInfo.setMappingSqlStatementInfo(mappingStatementInfo);
    return sqlStatementInfo;
  }
  
  protected void addMappingSqlStatementInfo(MappingSqlStatementInfo mappingSqlStatInfo) {
    if (null == this.mappingStatementInfo) {
      this.mappingStatementInfo = new ArrayList<MappingSqlStatementInfo>();
    }
    mappingStatementInfo.add(mappingSqlStatInfo);
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

  private IDialect getDialect() {
    return session.getDialect();
  }

  abstract protected void analyzeSql(Method invokedMethod,
      Object... methodArguments) throws AnalyzeException;
  

  /**
   * Get SQL statement type.
   * 
   * @return SQL statement type
   */
  protected SqlType getSqlType() {
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

  @Override
  public Class<?> getOriginalClass() {
    return originalClass;
  }

  @Override
  public void setOriginalClass(Class<?> orignalClass) {
    this.originalClass = orignalClass;
  }
}
