package com.elminster.easydao.db.analyze.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.elminster.common.util.CollectionUtil;
import com.elminster.common.util.ObjectUtil;
import com.elminster.easydao.db.id.IdGenerator;
import com.elminster.easydao.db.mapping.MappingPolicy;

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
  /** the id generator. */
  private IdGenerator idGenerator;
  /** has mapping to other table? */
  private boolean mapping;
  /** the mapping policy. */
  private MappingPolicy mappingPolicy;
  /** the mapping sql statement info. */
  private List<MappingSqlStatementInfo> mappingSqlStatementInfo;
  
  public SqlStatementInfo() {
  }

  /**
   * @return the mapping
   */
  public boolean isMapping() {
    return mapping;
  }

  /**
   * @param mapping the mapping to set
   */
  public void setMapping(boolean mapping) {
    this.mapping = mapping;
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
   * @param analyzedSqlParameters
   *          the analyzedSqlParameters to set
   */
  public void setAnalyzedSqlParameters(Serializable[]... analyzedSqlParameters) {
    List<Object> list = new ArrayList<Object>();
    for (int i = 0; i < analyzedSqlParameters.length; i++) {
      list.addAll(CollectionUtil.array2List(analyzedSqlParameters[i]));
    }
    this.analyzedSqlParameters = list;
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
  
  /**
   * @return the idGenerator
   */
  public IdGenerator getIdGenerator() {
    return idGenerator;
  }

  /**
   * @param idGenerator the idGenerator to set
   */
  public void setIdGenerator(IdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  /**
   * @return the mappingPolicy
   */
  public MappingPolicy getMappingPolicy() {
    return mappingPolicy;
  }

  /**
   * @param mappingPolicy the mappingPolicy to set
   */
  public void setMappingPolicy(MappingPolicy mappingPolicy) {
    this.mappingPolicy = mappingPolicy;
  }

  /**
   * @return the mappingSqlStatementInfo
   */
  public List<MappingSqlStatementInfo> getMappingSqlStatementInfo() {
    return mappingSqlStatementInfo;
  }

  /**
   * @param mappingSqlStatementInfo the mappingSqlStatementInfo to set
   */
  public void setMappingSqlStatementInfo(
      List<MappingSqlStatementInfo> mappingSqlStatementInfo) {
    this.mappingSqlStatementInfo = mappingSqlStatementInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return ObjectUtil.buildToStringByReflect(this);
  }
}
