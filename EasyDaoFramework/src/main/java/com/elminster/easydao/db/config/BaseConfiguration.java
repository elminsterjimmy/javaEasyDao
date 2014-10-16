package com.elminster.easydao.db.config;

import java.util.HashMap;
import java.util.Map;

public class BaseConfiguration implements IConfiguration {

  public static final String SHOW_SQL = "SHOW_SQL";

  private Map<String, Object> configuration = new HashMap<String, Object>();

  public BaseConfiguration() {
    configuration.put(SHOW_SQL, true);
  }

  public Map<String, Object> getConfiguration() {
    return this.configuration;
  }

  public void setShowSql(boolean showSql) {
    this.configuration.put(SHOW_SQL, showSql);
  }

  public boolean isShowSql() {
    return (Boolean) this.configuration.get(SHOW_SQL);
  }
}
