package com.elminster.easydao.db.analyze.expression;

public interface IVariable {

  public DataType getVariableType();

  public Object getVariableValue();
  
  public void setVariableName(String name);
  
  public String getVariableName();

  public void setVariableValue(Object value);

  public void setVariableType(DataType type);
}
