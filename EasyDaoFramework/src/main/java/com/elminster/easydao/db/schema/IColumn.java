package com.elminster.easydao.db.schema;

public interface IColumn {

  public String getName();

  /**
   * Get the column name (include table/table alias).
   * 
   * @return the full column name
   */
  public String getFullName();

  public void setAlias(String alias);

  public String getAlias();

  public void setTable(ITable table);

  public long getMaxLength();

  public int getPrecision();

  public boolean isNullable();

  public void setNullable(boolean nullable);

  public ForeignKey getForeignKey();

  /**
   * @return column type (uses java.sql.Types)
   */
  public int getType();
}
