package com.elminster.easydao.exception;

public class GenerateException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public GenerateException() {
    super();
  }

  public GenerateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public GenerateException(String message, Throwable cause) {
    super(message, cause);
  }

  public GenerateException(String message) {
    super(message);
  }

  public GenerateException(Throwable cause) {
    super(cause);
  }
}
