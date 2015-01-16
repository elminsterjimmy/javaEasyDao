package com.elminster.easydao.db.schema;

public class Index {

  private final IColumn[] columns;
  
  private boolean unique;
  
  private final String name;

  public Index(String name, boolean unique, IColumn[] columns) {
    super();
    this.columns = columns;
    this.unique = unique;
    this.name = name;
  }

  /**
   * @return the unique
   */
  public boolean isUnique() {
    return unique;
  }

  /**
   * @param unique the unique to set
   */
  public void setUnique(boolean unique) {
    this.unique = unique;
  }

  /**
   * @return the columns
   */
  public IColumn[] getColumns() {
    return columns;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
}
