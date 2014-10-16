package com.elminster.easydao.db.analyze.expression.operator;

import java.util.Stack;

import com.elminster.easydao.db.analyze.expression.DataType;
import com.elminster.easydao.db.analyze.expression.IVariable;
import com.elminster.easydao.db.analyze.expression.Variable;
import com.elminster.easydao.db.analyze.expression.evaluate.IResult;
import com.elminster.easydao.db.analyze.expression.evaluate.OperationResult;

public abstract class CompareOperator implements IOperator {

  @Override
  public IResult operate(Stack<Object> stack) {
    IVariable rightVariable = (IVariable)stack.pop();
    IVariable leftVariable = (IVariable)stack.pop();
    IVariable rst = new Variable(false);
    if (DataType.NULL.equals(leftVariable)) {
      if (DataType.NULL.equals(rightVariable)) {
        rst = new Variable(true);
      }
    } else {
      if (!DataType.NULL.equals(rightVariable)) {
        DataType leftDt = leftVariable.getVariableType();
        DataType rightDt = rightVariable.getVariableType();
        if (comparable(leftDt, rightDt)) {
          rst = new Variable(compare(leftVariable, rightVariable));
        } else {
          // TODO exception
        }
      }
    }
    stack.push(rst);
    return new OperationResult(leftVariable, rightVariable, getOperatorName(), rst);
  }

  abstract protected boolean compare(IVariable leftVariable,
      IVariable rightVariable);

  private boolean comparable(DataType leftDt, DataType rightDt) {
    boolean rst = false;
    if (leftDt.equals(rightDt)) {
      rst = true;
    }
    // TODO Auto Convert
    return rst;
  }
}
