package com.elminster.easydao.db.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadSessionMap {
  
  private static final Log logger = LogFactory.getLog(ThreadSessionMap.class);

  public static final ThreadSessionMap INSTANCE = new ThreadSessionMap();
  
  private static final Map<Thread, DAOSupportSession> threadSessionMap = new HashMap<Thread, DAOSupportSession>();
  
  public void dump() {
    
  }
  
  public void putSessionPerThread(Thread thread, DAOSupportSession session) {
    logger.debug("Thread: " + thread + " | Session: " + session);
    threadSessionMap.put(thread, session);
  }
  
  public void removeSessionPerThread(Thread thread, DAOSupportSession session) {
    logger.debug("Thread: " + thread + " | Session: " + session);
    DAOSupportSession removedSession = threadSessionMap.remove(thread);
    if (!removedSession.equals(session)) {
      logger.error("Session doesn't match. Removed Session: " + removedSession + " | Expect Session: " + session);
    }
  }
  
  public DAOSupportSession getSessionPerThread(Thread thread) {
    return threadSessionMap.get(thread);
  }
}
