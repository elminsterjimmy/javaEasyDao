package com.elminster.easydao.db.analyze.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MappingSqlStatementInfo extends SqlStatementInfo {

  /**
   * 
   */
  private static final long serialVersionUID = -3854111207577792874L;
  
  /** the mapping statementInfo. */
  private List<MappingSqlStatementInfo> mappingStatementInfo;
  /** the mapping class, only used in mapping statement info. */
  private Class<?> mappingClass;
  /** the generic types if the mapping class is a collection or map */
  private Class<?>[] genericTypes;
  /** the mapping field. */
  private Field mappingField;
  /** the SQL for updating intermediary. */
  private List<MappingSqlStatementInfo> updateIntermediarySql;
  /** the bean. */
  private Object bean;
  
  /**
   * Default constructor.
   */
  public MappingSqlStatementInfo() {
  }
  
  /**
   * Copy constructor.
   * @param sqlStatmentInfo the sql statement info
   */
  public MappingSqlStatementInfo(SqlStatementInfo sqlStatmentInfo) {
    this.setAnalyzedSqlParameters(sqlStatmentInfo.getAnalyzedSqlParameters());
    this.setAnalyzedSqlStatement(sqlStatmentInfo.getAnalyzedSqlStatement());
    this.setCallable(sqlStatmentInfo.isCallable());
    this.setMapping(sqlStatmentInfo.isMapping());
    this.setPagedData(sqlStatmentInfo.getPagedData());
    this.setScrollMode(sqlStatmentInfo.getScrollMode());
    this.setUsePaged(sqlStatmentInfo.isUsePaged());
    this.setIdGenerator(sqlStatmentInfo.getIdGenerator());
  }
  
  public void addMappingStatementInfo(MappingSqlStatementInfo mappingSqlStatement) {
    if (null == mappingStatementInfo) {
      mappingStatementInfo = new ArrayList<MappingSqlStatementInfo>();
    }
    mappingStatementInfo.add(mappingSqlStatement);
  }

  /**
   * @return the updateIntermediarySql
   */
  public List<MappingSqlStatementInfo> getUpdateIntermediaryStatInfo() {
    return updateIntermediarySql;
  }

  /**
   * @param updateIntermediarySql the updateIntermediarySql to set
   */
  public void setUpdateIntermediarySql(List<MappingSqlStatementInfo> updateIntermediarySql) {
    this.updateIntermediarySql = updateIntermediarySql;
  }
  
  /**
   */
  public void addUpdateIntermediarySql(MappingSqlStatementInfo info) {
    if (null == updateIntermediarySql) {
      updateIntermediarySql = new ArrayList<MappingSqlStatementInfo>();
    }
    updateIntermediarySql.add(info);
  }

  /**
   * @return the mappingClass
   */
  public Class<?> getMappingClass() {
    return mappingClass;
  }

  /**
   * @param mappingClass the mappingClass to set
   */
  public void setMappingClass(Class<?> mappingClass) {
    this.mappingClass = mappingClass;
  }
  
  /**
   * @return the mappingStatementInfo
   */
  public List<MappingSqlStatementInfo> getMappingStatementInfo() {
    return mappingStatementInfo;
  }

  /**
   * @param mappingStatementInfo the mappingStatementInfo to set
   */
  public void setMappingStatementInfo(List<MappingSqlStatementInfo> mappingStatementInfo) {
    this.mappingStatementInfo = mappingStatementInfo;
  }

  /**
   * @return the bean
   */
  public Object getBean() {
    return bean;
  }

  /**
   * @param bean the bean to set
   */
  public void setBean(Object bean) {
    this.bean = bean;
  }

  /**
   * @return the genericTypes
   */
  public Class<?>[] getGenericTypes() {
    return genericTypes;
  }

  /**
   * @param genericTypes the genericTypes to set
   */
  public void setGenericTypes(Class<?>[] genericTypes) {
    this.genericTypes = genericTypes;
  }

  /**
   * @return the mappingField
   */
  public Field getMappingField() {
    return mappingField;
  }

  /**
   * @param mappingField the mappingField to set
   */
  public void setMappingField(Field mappingField) {
    this.mappingField = mappingField;
  }
}
