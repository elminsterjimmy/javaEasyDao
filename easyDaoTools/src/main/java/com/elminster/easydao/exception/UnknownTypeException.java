package com.elminster.easydao.exception;

public class UnknownTypeException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public UnknownTypeException() {
    super();
  }

  public UnknownTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public UnknownTypeException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnknownTypeException(String message) {
    super(message);
  }

  public UnknownTypeException(Throwable cause) {
    super(cause);
  }

}
