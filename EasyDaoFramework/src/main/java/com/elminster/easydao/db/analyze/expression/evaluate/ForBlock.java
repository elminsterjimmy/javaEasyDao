package com.elminster.easydao.db.analyze.expression.evaluate;

/**
 * For - Next block.
 * 
 * @author jgu
 * @version 1.0
 */
public class ForBlock extends Block {

  private String toExpr;

  private String stepExpr;

  private Block nextBlock;

  public ForBlock(Block parent, int line, String toExpr, String stepExpr,
      Block nextBlock) {
    super(parent);
  }

}
