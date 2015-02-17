package com.elminster.easydao.db.analyze;

import java.util.ArrayList;
import java.util.List;

public class AnalyzedSqlData {

  private String analyzedSql;
  private List<Object> analyzedSqlParameters = new ArrayList<Object>();
  
  public String getAnalyzedSql() {
    return analyzedSql;
  }
  
  public void setAnalyzedSql(String analyzedSql) {
    this.analyzedSql = analyzedSql;
  }
  
  public List<Object> getAnalyzedSqlParameters() {
    return analyzedSqlParameters;
  }
  
  public void setAnalyzedSqlParameters(List<Object> analyzedSqlParameters) {
    this.analyzedSqlParameters = analyzedSqlParameters;
  }
  
  public void addAnalyzedSqlParameter(Object analyzedSqlParameter) {
    this.analyzedSqlParameters.add(analyzedSqlParameter);
  }
}
