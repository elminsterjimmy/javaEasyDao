package com.elminster.easydao.db.handler;

public class HandleException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 4858129914985341534L;

  public HandleException() {
    super();
  }

  public HandleException(String message, Throwable cause) {
    super(message, cause);
  }

  public HandleException(String message) {
    super(message);
  }

  public HandleException(Throwable cause) {
    super(cause);
  }

}
