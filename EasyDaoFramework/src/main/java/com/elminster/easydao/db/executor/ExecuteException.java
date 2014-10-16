package com.elminster.easydao.db.executor;

public class ExecuteException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -984521144280351384L;

  public ExecuteException() {
    super();
  }

  public ExecuteException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExecuteException(String message) {
    super(message);
  }

  public ExecuteException(Throwable cause) {
    super(cause);
  }

}
