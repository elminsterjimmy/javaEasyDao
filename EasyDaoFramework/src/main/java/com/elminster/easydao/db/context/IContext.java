package com.elminster.easydao.db.context;

import java.util.List;

public interface IContext {

  public List<DataSourceInfo> getDataSourceInforamtion();
  
  public DataSourceInfo getDataSourceInforamtion(String dsName);
}
