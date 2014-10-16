package com.elminster.easydao.db.analyze;

/**
 * An Exception occur in analyzing a SQL statement
 * 
 * @author Gu
 * @version 1.0
 *
 */
public class AnalyzeException extends Exception {

	/** serialVersionUID */
	private static final long serialVersionUID = -667828991189120372L;
	
	public AnalyzeException(String message) {
		super(message);
	}
	
	public AnalyzeException(Throwable t) {
		super(t);
	}

  public AnalyzeException() {
    super();
  }

  public AnalyzeException(String message, Throwable cause) {
    super(message, cause);
  }
}
