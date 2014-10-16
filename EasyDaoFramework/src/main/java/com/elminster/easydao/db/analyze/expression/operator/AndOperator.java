package com.elminster.easydao.db.analyze.expression.operator;

import java.util.Stack;

import com.elminster.easydao.db.analyze.expression.IVariable;
import com.elminster.easydao.db.analyze.expression.Variable;
import com.elminster.easydao.db.analyze.expression.evaluate.IResult;
import com.elminster.easydao.db.analyze.expression.evaluate.OperationResult;

public class AndOperator implements IOperator {
  
  public static final IOperator INSTANCE = new AndOperator();

  @Override
  public IResult operate(Stack<Object> stack) {
    boolean rst;
    IVariable rightVariable = (IVariable)stack.pop();
    IVariable leftVariable = (IVariable)stack.pop();
    Boolean right = (Boolean) rightVariable.getVariableValue();
    Boolean left = (Boolean) leftVariable.getVariableValue();
    if (null == left || null == right || Boolean.FALSE.equals(left)) {
      rst = false;
    } else {
      rst = left && right;
    }
    IVariable rstVar = new Variable(rst);
    stack.push(rstVar);
    return new OperationResult(leftVariable, rightVariable, getOperatorName(), rstVar);
  }

  @Override
  public String getOperatorName() {
    return " and ";
  }
}
