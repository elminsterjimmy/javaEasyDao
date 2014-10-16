package com.elminster.easydao.db.analyze.expression.evaluate;

/**
 * The interface of operation result.
 * 
 * @author jgu
 * @version 1.0
 */
public interface IResult {

  enum Result {
    OK,
    NG
  }
  
  /**
   * Get the operate command.
   * @return the operate command
   */
  public String getCommand();
  
  public Result getSuccess();
  
  /**
   * Get the result of the command.
   * @return the result
   */
  public Object getResult();
  
  /**
   * Get last message.
   * @return the last message
   */
  public String getLastMessage();
  
  /**
   * Get the timestamp.
   * @return the timestamp
   */
  public long getTimestamp();
}
