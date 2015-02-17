package com.elminster.easydao.db.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadSessionMap {
  
  private static final Log logger = LogFactory.getLog(ThreadSessionMap.class);

  public static final ThreadSessionMap INSTANCE = new ThreadSessionMap();
  
  private static final Map<Thread, Stack<DAOSupportSession>> threadSessionMap = new HashMap<Thread, Stack<DAOSupportSession>>();
  
  public void dump() {
  }
  
  public void putSessionPerThread(Thread thread, DAOSupportSession session) {
    logger.debug("Thread: " + thread + " | Session: " + session);
    Stack<DAOSupportSession> sessionStack = threadSessionMap.get(thread);
    if (null == sessionStack) {
      sessionStack = new Stack<DAOSupportSession>();
    }
    sessionStack.push(session);
    threadSessionMap.put(thread, sessionStack);
  }
  
  public void removeSessionPerThread(Thread thread, DAOSupportSession session) {
    logger.debug("Thread: " + thread + " | Session: " + session);
    Stack<DAOSupportSession> sessionStack = threadSessionMap.get(thread);
    if (null == sessionStack) {
      String message = "Unexpected State [UnManagered Session] : " + session;
      logger.error(message);
      throw new IllegalStateException(message);
    }
    DAOSupportSession removedSession = sessionStack.pop();
    if (!removedSession.equals(session)) {
      String message = "Session doesn't match. Removed Session: " + removedSession + " | Expect Session: " + session;
      logger.error(message);
      throw new IllegalStateException(message);
    }
  }
  
  public DAOSupportSession getSessionPerThread(Thread thread) {
    DAOSupportSession session = null;
    Stack<DAOSupportSession> sessionStack = threadSessionMap.get(thread);
    if (null != sessionStack) {
      session = sessionStack.peek();
    }
    return session;
  }
}
