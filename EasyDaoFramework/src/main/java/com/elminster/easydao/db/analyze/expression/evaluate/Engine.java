package com.elminster.easydao.db.analyze.expression.evaluate;

import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.elminster.common.util.CollectionUtil;
import com.elminster.common.util.ObjectUtil;
import com.elminster.easydao.db.analyze.expression.exception.ParserException;

public class Engine {

  private static final String KEY_IF = "--if";

  private static final String KEY_THEN = "then";

  private static final String KEY_ELSE = "--else";

  private static final String KEY_ELSEIF = "--elseif";

  private static final String KEY_ENDIF = "--endif";

  private static final String[] KEYS = { KEY_ELSE, KEY_ELSEIF, KEY_ENDIF };

  private IEvaluater evaluater;

  private int line;

  private int offset;

  private int size;

  private String expression;

  private String lastKey;

  public Engine() {
    this.evaluater = new Evaluater();
    line = 1;
    offset = 0;
  }
  
  public void addVariables(Map<String, Object> variables) {
    if (CollectionUtil.isNotEmpty(variables)) {
      Set<String> keySet = variables.keySet();
      for (String key : keySet) {
        this.addVariable(key, variables.get(key));
      }
    }
  }

  public void addVariable(String variableName, Object value) {
    evaluater.addVariable(variableName, value);
  }
  
  public void setVariableValue(String variableName, Object value) {
    evaluater.setVariableValue(variableName, value);
  }

  public Object execute(String expression) throws ParserException {
    this.expression = expression;
    this.line = 1;
    this.offset = 0;
    this.size = expression.length();
    Block block = compile(new RootBlock(evaluater));
    Stack<Object> stack = new Stack<Object>();
    block.operate(stack);
    return stack.pop();
  }

  public void compile(String expression) throws ParserException {
    this.expression = expression;
    this.size = expression.length();
    compile(new RootBlock(evaluater));
  }

  public Block compile(Block parent) throws ParserException {
    Block block = createBlock(parent);
    complieBlock(block);
    return block;
  }

  private Block compileStatement(Block block) throws ParserException {
    Block b = null;
    skipSpace();
    int index = offset;
    if (index < size) {
      String key = readNextWord();
      lastKey = key;
      b = compileToken(key, block, index);
    }
    return b;
  }

  private void complieBlock(Block parent) throws ParserException {
    while (null != compileStatement(parent))
      ;
  }

  private Block compileToken(String key, Block block, int index) throws ParserException {
    skipSpace();
    Block rst = null;
    if (KEY_IF.equals(key)) {
      rst = createIfElseBlock(block);
    } else if (isKey(key)) {
      return null;
    } else {
      // TODO variable define
      // TODO function call
      return createCommonBlock(block, index);
    }
    return rst;
  }

  private Block createCommonBlock(Block parent, int index) {
    Block commonBlock = new CommonBlock(parent);
    offset = index;
    String nextLine = readNexLine();
    commonBlock.setExpression(nextLine);
    parent.addChild(commonBlock);
    return commonBlock;
  }

  private Block createIfElseBlock(Block parent) throws ParserException {
    int startLine = line;
    int startIdx = offset;
    // find then
    if (!findKey(KEY_THEN)) {
      throw new ParserException("Unexpect end of if.");
    }
    // get condition
    String condition = expression.substring(startIdx, offset
        - KEY_THEN.length());

    // create then
    Block ifBlock = createBlock(parent);
    ifBlock.setStartLine(line);
    // compile then
    complieBlock(ifBlock);

    // create else
    Block elseBlock = createBlock(parent);
    elseBlock.setStartLine(line);

    if (KEY_ELSEIF.equals(lastKey)) {
      compileToken(KEY_IF, elseBlock, 0);
    }

    if (KEY_ELSE.equals(lastKey)) {
      // compile else
      complieBlock(elseBlock);
    }

    if (!KEY_ENDIF.equals(lastKey)) {
      // TODO error
    }

    IfElseBlock block = new IfElseBlock(condition, parent, ifBlock, elseBlock, startLine);
    return block;
  }

  private boolean findKey(String key) {
    boolean found = false;
    while (!found && offset < size) {
      String nextWord = readNextWord();
      if (key.equals(nextWord)) {
        found = true;
      }
    }
    return found;
  }

  private String readNexLine() {
    StringBuilder sb = new StringBuilder();
    for (; offset < size; offset++) {
      char c = expression.charAt(offset);
      if (10 == c) {
        break;
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  private String readNextWord() {
    skipSpace();
    StringBuilder sb = new StringBuilder();
    for (; offset < size; offset++) {
      char c = expression.charAt(offset);
      if (isSkipSpace(c)) {
        break;
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  private boolean isSkipSpace(char c) {
    return ' ' == c || '\t' == c || '\n' == c || '\r' == c;
  }

  private void skipSpace() {
    for (; offset < size; offset++) {
      char c = expression.charAt(offset);
      if (!isSkipSpace(c)) {
        break;
      }
      // new line
      if (10 == c) {
        line++;
      }
    }
  }

  private boolean isKey(String key) {
    return ObjectUtil.contains(KEYS, key);
  }

  private Block createBlock(Block parent) {
    Block block = new Block(parent);
    return block;
  }
}
