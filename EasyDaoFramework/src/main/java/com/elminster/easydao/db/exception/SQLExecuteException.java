package com.elminster.easydao.db.exception;

public class SQLExecuteException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = -2647578157210876198L;

	public SQLExecuteException() {
		super();
	}

	public SQLExecuteException(String message, Throwable cause) {
		super(message, cause);
	}

	public SQLExecuteException(String message) {
		super(message);
	}

	public SQLExecuteException(Throwable cause) {
		super(cause);
	}
}
