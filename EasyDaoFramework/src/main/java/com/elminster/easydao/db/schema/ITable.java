package com.elminster.easydao.db.schema;

import java.util.List;

public interface ITable {

  public void setAlias(String alias);
  
  public String getAlias();
  
  public String getName();
  
  public List<IColumn> getColumns();
  
  public IColumn[] getPrimaryKeys();
  
  public List<Index> getIndexs();
  
  public int getCoumnCount();
}
