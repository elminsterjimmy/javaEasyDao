package com.elminster.easydao.db.analyze.expression.evaluate;

import com.elminster.easydao.db.analyze.expression.func.FunctionDef;

public interface IEvaluater {

  public void addVariable(String variableName, Object value);

  public void setVariableValue(String variableName, Object value);
  
  public void addFunction(String funcName, FunctionDef func);

  public Object evaluate(String expression);
}
