package com.elminster.easydao.db.analyze.data;

import java.io.Serializable;
import java.util.List;

import com.elminster.common.util.ObjectUtil;

/**
 * The information class about the sql statement.
 * 
 * @author jgu
 * @version 1.0
 */
public class SqlStatementInfo implements Serializable {

  /** serialVersionUID */
  private static final long serialVersionUID = -2736826200502206069L;

  /**
   * SQL statement type
   * 
   * @author Gu
   * @version 1.0
   * 
   */
  public enum SqlType {
    QUERY, UPDATE, STORED_PROCEDURE
  }

  /** Analyzed SQL Statement */
  private String analyzedSqlStatement;
  /** Analyzed SQL Statement's Parameters */
  private List<Object> analyzedSqlParameters;
  /** Analyzed SQL Statement's Type */
  private SqlType analyzedSqlType;
  /** Analyzed SQL Statement's use paged feature */
  private boolean usePaged;
  /** Paged data for analyzed SQL */
  private PagedData pagedData;
  /** Analyzed SQL Statement is callable */
  private boolean callable;
  /** Analyzed SQL Statement's scroll mode  */
  private ScrollMode scrollMode;
  
  public SqlStatementInfo() {
  }

  /**
   * @return the analyzedSqlStatement
   */
  public String getAnalyzedSqlStatement() {
    return analyzedSqlStatement;
  }

  /**
   * @param analyzedSqlStatement
   *          the analyzedSqlStatement to set
   */
  public void setAnalyzedSqlStatement(String analyzedSqlStatement) {
    this.analyzedSqlStatement = analyzedSqlStatement;
  }

  /**
   * @return the analyzedSqlParameters
   */
  public List<Object> getAnalyzedSqlParameters() {
    return analyzedSqlParameters;
  }

  /**
   * @param analyzedSqlParameters
   *          the analyzedSqlParameters to set
   */
  public void setAnalyzedSqlParameters(List<Object> analyzedSqlParameters) {
    this.analyzedSqlParameters = analyzedSqlParameters;
  }

  /**
   * @return the analyzedSqlType
   */
  public SqlType getAnalyzedSqlType() {
    return analyzedSqlType;
  }

  /**
   * @param analyzedSqlType
   *          the analyzedSqlType to set
   */
  public void setAnalyzedSqlType(SqlType analyzedSqlType) {
    this.analyzedSqlType = analyzedSqlType;
  }

  /**
   * @return the usePaged
   */
  public boolean isUsePaged() {
    return usePaged;
  }

  /**
   * @param usePaged
   *          the usePaged to set
   */
  public void setUsePaged(boolean usePaged) {
    this.usePaged = usePaged;
  }

  /**
   * @return the pagedData
   */
  public PagedData getPagedData() {
    return pagedData;
  }

  /**
   * @param pagedData the pagedData to set
   */
  public void setPagedData(PagedData pagedData) {
    this.pagedData = pagedData;
  }

  /**
   * @return the callable
   */
  public boolean isCallable() {
    return callable;
  }

  /**
   * @param callable the callable to set
   */
  public void setCallable(boolean callable) {
    this.callable = callable;
  }

  /**
   * @return the scrollMode
   */
  public ScrollMode getScrollMode() {
    return scrollMode;
  }

  /**
   * @param scrollMode the scrollMode to set
   */
  public void setScrollMode(ScrollMode scrollMode) {
    this.scrollMode = scrollMode;
  }

  public String toString() {
    return ObjectUtil.buildToStringByReflect(this);
  }
}
