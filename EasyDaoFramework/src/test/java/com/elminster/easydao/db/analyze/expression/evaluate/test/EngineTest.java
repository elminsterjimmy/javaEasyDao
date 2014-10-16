package com.elminster.easydao.db.analyze.expression.evaluate.test;

import java.io.InputStream;
import java.util.List;

import javax.mail.internet.ParseException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.elminster.common.util.CloseUtil;
import com.elminster.common.util.FileUtil;
import com.elminster.easydao.db.analyze.expression.evaluate.Engine;

public class EngineTest {

  private String[] expression;

  @Before
  public void init() throws Exception {
    expression = new String[2];
    for (int i = 0; i < expression.length; i++) {
      InputStream is = null;
      try {
        is = this.getClass().getResourceAsStream("evaluaterTest" + (i + 1) + ".sql");
        List<String> lines = FileUtil.readFileByLine(is, false, "ASCII");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
          sb.append(line);
          sb.append("\n");
        }
        expression[i] = sb.toString();
      } finally {
        CloseUtil.closeInputStreamQuiet(is);
      }
    }
  }

  @Test
  public void test1() throws ParseException {
    Engine engine = new Engine();
    engine.addVariable("a", 2);
    engine.addVariable("b", 2);
    Assert.assertEquals("select * from A where test = 1", engine.execute(expression[0]));
    engine.setVariableValue("a", 0);
    engine.setVariableValue("b", -1);
    Assert.assertEquals("select * from D where test = 4", engine.execute(expression[0]));
    engine.setVariableValue("a", -1);
    engine.setVariableValue("b", 2);
    Assert.assertEquals("select * from C where test = 3", engine.execute(expression[0]));
    engine.setVariableValue("a", -1);
    engine.setVariableValue("b", -1);
    Assert.assertEquals("select * from C where test = 3", engine.execute(expression[0]));
    engine.setVariableValue("a", -2);
    engine.setVariableValue("b", -2);
    Assert.assertEquals("select * from B where test = 2", engine.execute(expression[0]));
    engine.setVariableValue("a", -2);
    engine.setVariableValue("b", 2);
    Assert.assertEquals("select * from B where test = 2", engine.execute(expression[0]));
    engine.setVariableValue("a", 5);
    engine.setVariableValue("b", -2);
    Assert.assertEquals("select * from B where test = 2", engine.execute(expression[0]));
    engine.setVariableValue("a", -2);
    engine.setVariableValue("b", 5);
    Assert.assertEquals("select * from B where test = 2", engine.execute(expression[0]));
    engine.setVariableValue("a", -1);
    Assert.assertEquals("select * from C where test = 3", engine.execute(expression[0]));
    engine.setVariableValue("a", 0);
    Assert.assertEquals("select * from D where test = 4", engine.execute(expression[0]));
  }
  
  @Test
  public void test2() throws ParseException {
    Engine engine = new Engine();
    Assert.assertEquals("true", String.valueOf(engine.execute(expression[1])).trim());
  }
}
