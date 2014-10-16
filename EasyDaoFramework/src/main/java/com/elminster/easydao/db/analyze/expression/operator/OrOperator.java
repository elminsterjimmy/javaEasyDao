package com.elminster.easydao.db.analyze.expression.operator;

import java.util.Stack;

import com.elminster.easydao.db.analyze.expression.IVariable;
import com.elminster.easydao.db.analyze.expression.Variable;
import com.elminster.easydao.db.analyze.expression.evaluate.IResult;
import com.elminster.easydao.db.analyze.expression.evaluate.OperationResult;

public class OrOperator implements IOperator {
  
  public static final IOperator INSTANCE = new OrOperator();

  @Override
  public IResult operate(Stack<Object> stack) {
    boolean rst;
    IVariable rightVariable = (IVariable)stack.pop();
    IVariable leftVariable = (IVariable)stack.pop();
    Boolean right = (Boolean) rightVariable.getVariableValue();
    Boolean left = (Boolean) leftVariable.getVariableValue();
    if (null == left || null == right) {
      rst = false;
    } else if (Boolean.TRUE.equals(left)) {
      rst = true;
    } else {
      rst = left || right;
    }
    IVariable varRst = new Variable(rst);
    stack.push(varRst);
    return new OperationResult(leftVariable, rightVariable, getOperatorName(), varRst);
  }

  @Override
  public String getOperatorName() {
    return " or ";
  }
}
