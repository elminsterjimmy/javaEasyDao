package com.elminster.easydao.db.analyze.expression.evaluate;

import java.util.Stack;

import com.elminster.easydao.db.analyze.expression.DataType;
import com.elminster.easydao.db.analyze.expression.Variable;
import com.elminster.easydao.db.analyze.expression.exception.OperateException;

/**
 * If - Then - Else block.
 * 
 * @author jgu
 * @version 1.0
 */
public class IfElseBlock extends Block {

  private Block ifBlock;

  private Block elseBlock;

  private String condition;

  public IfElseBlock(String condition, Block parent, Block ifBlock, Block elseBlock, int line) {
    super(parent, true, line);
    this.condition = condition;
    this.ifBlock = ifBlock;
    this.elseBlock = elseBlock;
  }

  private boolean hasElse() {
    return null != elseBlock;
  }

  @Override
  public IResult operate(Stack<Object> stack) {
    Object rst = evaluater.evaluate(condition);
    if (rst instanceof Variable) {
      Variable var = (Variable)rst;
      if (DataType.BOOLEAN.equals(var.getVariableType())) {
        if ((Boolean)var.getVariableValue()) {
          return ifBlock.operate(stack);
        } else {
          if (hasElse()) {
            return elseBlock.operate(stack);
          } else {
            return new OperationResult("");
          }
        }
      } else {
        throw new OperateException();
      }
    } else if (rst instanceof Boolean) {
      if ((Boolean)rst) {
        return ifBlock.operate(stack);
      } else {
        if (hasElse()) {
          return elseBlock.operate(stack);
        } else {
          return new OperationResult("");
        }
      }
    } else {
      throw new OperateException();
    }
  }
}
