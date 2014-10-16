package com.elminster.easydao.db.analyze;

import java.lang.reflect.Method;

import com.elminster.common.util.ObjectUtil;
import com.elminster.easydao.db.constants.SqlConstants;
import com.elminster.easydao.db.manager.DAOSupportSession;

public class DefaultSqlAnalyzerFactory implements ISqlAnalyzerFactory {

  private static DefaultSqlAnalyzerFactory instance = new DefaultSqlAnalyzerFactory();

  private DefaultSqlAnalyzerFactory() {
  }

  public static DefaultSqlAnalyzerFactory getInstance() {
    return instance;
  }

  public ISqlAnalyzer getSqlAnalyzer(Method method, Object[] args, DAOSupportSession session) {
    ISqlAnalyzer sqlAnalyzer = null;
    String methodName = method.getName();
    if (ObjectUtil.contains(SqlConstants.INSERT_KEYWORD, methodName)) {
      sqlAnalyzer = new ORMInsertAnalyzer(session);
    } else if (ObjectUtil.contains(SqlConstants.DELETE_KEYWORD, methodName)) {
      sqlAnalyzer = new ORMDeleteAnalyzer(session);
    } else if (ObjectUtil.contains(SqlConstants.MODIFY_KEYWORD, methodName)) {
      sqlAnalyzer = new ORMModifyAnalyzer(session);
    } else if (ObjectUtil.contains(SqlConstants.FETCH_KEYWORD, methodName)) {
      sqlAnalyzer = new ORMFetchAnalyzer(session);
    } else {
      sqlAnalyzer = new DefaultSqlAnalyzer(session);
    }
    return sqlAnalyzer;
  }
}
