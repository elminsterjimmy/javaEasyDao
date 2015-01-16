package com.elminster.easydao.db.schema;

import java.util.ArrayList;
import java.util.List;

public class Table implements ITable {
  
  private String name;
  private List<IColumn> columns;
  private String alias;
  private IColumn[] primaryKeys;
  private List<Index> indexs;
  
  public Table(String name) {
    this.name = name;
  }
  
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  
  public void addColumn(IColumn column) {
    if (null == columns) {
      columns = new ArrayList<IColumn>();
    }
    this.columns.add(column);
    column.setTable(this);
  }
  
  /**
   * @return the columns
   */
  public List<IColumn> getColumns() {
    return columns;
  }

  /**
   * @return the alias
   */
  public String getAlias() {
    return alias;
  }
  
  /**
   * @param alias the alias to set
   */
  public void setAlias(String alias) {
    this.alias = alias;
  }
  
  /**
   * @return the primaryKeys
   */
  public IColumn[] getPrimaryKeys() {
    return primaryKeys;
  }
  
  /**
   * @param primaryKeys the primaryKeys to set
   */
  public void setPrimaryKeys(IColumn[] primaryKeys) {
    this.primaryKeys = primaryKeys;
  }
  
  /**
   * @return the indexs
   */
  public List<Index> getIndexs() {
    return indexs;
  }
  
  /**
   * @param indexs the indexs to set
   */
  public void setIndexs(List<Index> indexs) {
    this.indexs = indexs;
  }
  
  public void addIndex(Index index) {
    if (null == indexs) {
      indexs = new ArrayList<Index>();
    }
    this.indexs.add(index);
  }
  
  @Override
  public int getCoumnCount() {
    return this.columns.size();
  }
  
  public IColumn getColumn(String columnName) {
    for (IColumn column : columns) {
      if (column.getName().equals(columnName)) {
        return column;
      }
    }
    return null;
  }
}
