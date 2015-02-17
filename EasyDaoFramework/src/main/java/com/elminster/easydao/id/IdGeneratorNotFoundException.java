package com.elminster.easydao.id;

public class IdGeneratorNotFoundException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public IdGeneratorNotFoundException() {
    super();
  }

  public IdGeneratorNotFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public IdGeneratorNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public IdGeneratorNotFoundException(String message) {
    super(message);
  }

  public IdGeneratorNotFoundException(Throwable cause) {
    super(cause);
  }
}
