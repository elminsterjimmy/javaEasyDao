package com.elminster.easydao.db.analyze.expression.func;

import com.elminster.easydao.db.analyze.expression.DataType;
import com.elminster.easydao.db.analyze.expression.operator.IOperator;

/**
 * The unary operator.
 * 
 * @author jgu
 * @version 1.0
 */
public class Operator1Def extends FunctionDef {

  public Operator1Def(String name, int priority, DataType result,
      DataType left, IOperator operator) {
    super(name, result, new DataType[] {left}, operator);
    this.setPriority(priority);
    this.setModule(FunctionModule.MODULE_STANDARD);
  }
}
