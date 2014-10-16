package com.elminster.easydao.db.analyze.expression.evaluate;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.elminster.easydao.db.analyze.expression.evaluate.IResult.Result;
import com.elminster.easydao.db.analyze.expression.operator.IOperator;

public class Block implements IOperator {

  protected Block parent;

  protected IEvaluater evaluater;

  protected int startLine;

  protected String expression;

  protected boolean loop;

  protected List<Block> children = new ArrayList<Block>();

  public Block(Block parent) {
    this.parent = parent;
    if (null != parent) {
      this.evaluater = parent.evaluater;
    } else {
      this.evaluater = new Evaluater();
    }
  }
  
  public Block(Block parent, boolean addToParent, int line) {
    this.parent = parent;
    if (null != parent) {
      this.evaluater = parent.evaluater;
    } else {
      this.evaluater = new Evaluater();
    }
    if (null != parent && addToParent) {
      parent.addChild(this);
    }
    this.startLine = line;
  }

  public Block(Block parent, boolean addToParent, boolean loop) {
    this.parent = parent;
    if (null != parent) {
      this.evaluater = parent.evaluater;
    } else {
      this.evaluater = new Evaluater();
    }
    if (null != parent && addToParent) {
      parent.addChild(this);
    }
    this.loop = loop;
  }

  /**
   * @return the parent
   */
  public Block getParent() {
    return parent;
  }

  /**
   * @param parent the parent to set
   */
  public void setParent(Block parent) {
    this.parent = parent;
  }

  @Override
  public IResult operate(Stack<Object> stack) {
    for (Block child : children) {
      IResult result = child.operate(stack);
      if (Result.NG == result.getSuccess()) {
        return result;
      }
    }
    return new OperationResult(expression);
  }

  /**
   * @return the startLine
   */
  public int getStartLine() {
    return startLine;
  }

  /**
   * @param startLine the startLine to set
   */
  public void setStartLine(int startLine) {
    this.startLine = startLine;
  }

  /**
   * @return the expression
   */
  public String getExpression() {
    return expression;
  }

  /**
   * @param expression the expression to set
   */
  public void setExpression(String expression) {
    this.expression = expression;
  }

  public void addChild(Block child) {
    this.children.add(child);
  }

  @Override
  public String getOperatorName() {
    return "Block";
  }
}
