package com.elminster.easydao.db.exception;

/**
 * An Exception occur in analyzing a SQL statement
 * 
 * @author Gu
 * @version 1.0
 *
 */
public class SqlAnalyzeException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = -667828991189120372L;
	
	public SqlAnalyzeException(String message) {
		super(message);
	}
	
	public SqlAnalyzeException(Throwable t) {
		super(t);
	}

  public SqlAnalyzeException(String message, Throwable cause) {
    super(message, cause);
  }
}
