package com.elminster.easydao.db.schema;

public class ForeignKey {

  private String foreignKeyTableName;
  private String foreignKeyColumnName;
  private String primaryKeyTableName;
  private String primaryKeyColumnName;
  private String fkName;
  private String pkName;
  
  /**
   * @return the foreignKeyTableName
   */
  public String getForeignKeyTableName() {
    return foreignKeyTableName;
  }
  /**
   * @param foreignKeyTableName the foreignKeyTableName to set
   */
  public void setForeignKeyTableName(String foreignKeyTableName) {
    this.foreignKeyTableName = foreignKeyTableName;
  }
  /**
   * @return the foreignKeyColumnName
   */
  public String getForeignKeyColumnName() {
    return foreignKeyColumnName;
  }
  /**
   * @param foreignKeyColumnName the foreignKeyColumnName to set
   */
  public void setForeignKeyColumnName(String foreignKeyColumnName) {
    this.foreignKeyColumnName = foreignKeyColumnName;
  }
  /**
   * @return the primaryKeyTableName
   */
  public String getPrimaryKeyTableName() {
    return primaryKeyTableName;
  }
  /**
   * @param primaryKeyTableName the primaryKeyTableName to set
   */
  public void setPrimaryKeyTableName(String primaryKeyTableName) {
    this.primaryKeyTableName = primaryKeyTableName;
  }
  /**
   * @return the primaryKeyColumnName
   */
  public String getPrimaryKeyColumnName() {
    return primaryKeyColumnName;
  }
  /**
   * @param primaryKeyColumnName the primaryKeyColumnName to set
   */
  public void setPrimaryKeyColumnName(String primaryKeyColumnName) {
    this.primaryKeyColumnName = primaryKeyColumnName;
  }
  /**
   * @return the fkName
   */
  public String getFkName() {
    return fkName;
  }
  /**
   * @param fkName the fkName to set
   */
  public void setFkName(String fkName) {
    this.fkName = fkName;
  }
  /**
   * @return the pkName
   */
  public String getPkName() {
    return pkName;
  }
  /**
   * @param pkName the pkName to set
   */
  public void setPkName(String pkName) {
    this.pkName = pkName;
  }
}
