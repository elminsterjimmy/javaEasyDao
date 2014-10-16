package com.elminster.easydao.db.analyze.expression.func;

public enum FunctionModule {

  MODULE_STANDARD {
    public String getModuleName() {
      return "Standard";
    }
  };
  
  public abstract String getModuleName();
  
}
