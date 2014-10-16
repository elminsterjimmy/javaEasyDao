package com.elminster.easydao.db.analyze.expression.exception;

public class ParserException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -1490303787517528105L;
  
  private int colIdx;
  
  private int rowIdx;

  public ParserException(String message) {
    super(message);
  }
  
  public ParserException(String message, int rowIdx) {
    this(message, rowIdx, 0);
  }
  
  public ParserException(String message, int rowIdx, int colIdx) {
    super(message);
    this.colIdx = colIdx;
    this.rowIdx = rowIdx;
  }

  /** {@inheritDoc} */
  @Override
  public String getMessage() {
    String message = super.getMessage();
    StringBuilder sb = new StringBuilder();
    sb.append(message);
    if (rowIdx > 0) {
      sb.append(" at line:");
      sb.append(rowIdx);
    }
    if (colIdx > 0) {
      sb.append(" column:");
      sb.append(colIdx);
    }
    return sb.toString();
  }
  
}
