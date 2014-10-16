package com.elminster.easydao.db.analyze.expression.func;

import com.elminster.easydao.db.analyze.expression.DataType;
import com.elminster.easydao.db.analyze.expression.operator.IOperator;

/**
 * The binary operator.
 * 
 * @author jgu
 * @version 1.0
 */
public class Operator2Def extends FunctionDef {

  public Operator2Def(String name, int priority, DataType result,
      DataType left, DataType right, IOperator operator) {
    super(name, result, new DataType[] {left, right}, operator);
    this.setPriority(priority);
    this.setModule(FunctionModule.MODULE_STANDARD);
  }
}
