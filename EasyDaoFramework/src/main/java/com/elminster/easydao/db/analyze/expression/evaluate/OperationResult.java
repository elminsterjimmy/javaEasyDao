package com.elminster.easydao.db.analyze.expression.evaluate;

import com.elminster.easydao.db.analyze.expression.IVariable;

public class OperationResult implements IResult {

  private String command;

  private Result success;
  
  private Object result;

  private String lastMessage;

  private long timestamp;

  public OperationResult(String command) {
    this(command, "");
  }
  
  public OperationResult(String command, Object result) {
    this(command, result, Result.OK, "");
  }
  
  public OperationResult(IVariable left, IVariable right, String op, Object result) {
    this(String.valueOf(left.getVariableValue()) + op + String.valueOf(right.getVariableValue()), result);
  }

  public OperationResult(String command, Object result, Result success, String lastMessage) {
    this.success = success;
    this.command = command;
    this.lastMessage = lastMessage;
    this.result = result;
    this.timestamp = System.currentTimeMillis();
  }

  @Override
  public Result getSuccess() {
    return success;
  }

  @Override
  public String getLastMessage() {
    return lastMessage;
  }

  @Override
  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public String getCommand() {
    return command;
  }

  /** {@inheritDoc} */
  @Override
  public Object getResult() {
    return result;
  }
}
