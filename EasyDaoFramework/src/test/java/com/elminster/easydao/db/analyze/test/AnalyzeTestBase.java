package com.elminster.easydao.db.analyze.test;

import org.junit.After;
import org.junit.Before;

import com.elminster.easydao.db.session.ThreadSessionMap;

public class AnalyzeTestBase {
  
  private TestDummySession dummySession = new TestDummySession();

  @Before
  public void setup() {
    ThreadSessionMap.INSTANCE.putSessionPerThread(Thread.currentThread(), dummySession);
  }
  
  @After
  public void cleanup() {
    ThreadSessionMap.INSTANCE.removeSessionPerThread(Thread.currentThread(), dummySession);
  }
}
