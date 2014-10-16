package com.elminster.easydao.db.analyze.expression.evaluate;

import java.util.Stack;

public class CommonBlock extends Block {

  public CommonBlock(Block parent) {
    super(parent);
  }

  /** {@inheritDoc} */
  @Override
  public IResult operate(Stack<Object> stack) {
    StringBuilder sb;
    if (stack.isEmpty()) {
      sb = new StringBuilder();
    } else {
      sb = new StringBuilder((String)stack.pop());
    }
    sb.append(expression);
    stack.push(sb.toString());
    return super.operate(stack);
  }
}
