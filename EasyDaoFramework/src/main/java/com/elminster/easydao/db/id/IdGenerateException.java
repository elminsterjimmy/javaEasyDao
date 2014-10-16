package com.elminster.easydao.db.id;

public class IdGenerateException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 6279146566553742739L;

  public IdGenerateException() {
    super();
  }

  public IdGenerateException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public IdGenerateException(String message, Throwable cause) {
    super(message, cause);
  }

  public IdGenerateException(String message) {
    super(message);
  }

  public IdGenerateException(Throwable cause) {
    super(cause);
  }

}
