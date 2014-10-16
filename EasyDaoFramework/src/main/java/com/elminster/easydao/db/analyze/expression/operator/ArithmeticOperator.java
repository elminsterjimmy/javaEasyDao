package com.elminster.easydao.db.analyze.expression.operator;

import java.util.Stack;

import com.elminster.easydao.db.analyze.expression.DataType;
import com.elminster.easydao.db.analyze.expression.IVariable;
import com.elminster.easydao.db.analyze.expression.evaluate.IResult;
import com.elminster.easydao.db.analyze.expression.evaluate.OperationResult;
import com.elminster.easydao.db.analyze.expression.exception.OperateException;

abstract public class ArithmeticOperator implements IOperator {

  protected DataType returnType;

  @Override
  public IResult operate(Stack<Object> stack) throws OperateException {
    IVariable rightVariable = (IVariable) stack.pop();
    IVariable leftVariable = (IVariable) stack.pop();

    DataType ldt = leftVariable.getVariableType();
    DataType rdt = rightVariable.getVariableType();

    IVariable rst = null;
    if (processable(ldt, rdt)) {
      rst = process(leftVariable, rightVariable);
    } else {
      String currentOperation = String.valueOf(leftVariable.getVariableValue())
          + getOperatorName()
          + String.valueOf(rightVariable.getVariableValue());
      throw new OperateException("cannot process current operation: "
          + currentOperation);
    }
    stack.push(rst);
    return new OperationResult(leftVariable, rightVariable, getOperatorName(),
        rst);
  }

  abstract protected boolean processable(DataType ldt, DataType rdt);

  abstract protected IVariable process(IVariable leftVariable,
      IVariable rightVariable);
}
