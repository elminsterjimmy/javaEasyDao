package com.elminster.easydao.db.analyze.expression.evaluate;

/**
 * While - Endwhile.
 * 
 * @author jgu
 * @version 1.0
 */
public class WhileBlock extends Block {
  
  private String condition;
  
  private Block body;

  public WhileBlock(Block parent, int line, String condition) {
    super(parent, true, line);
    this.condition = condition;
  }

}
