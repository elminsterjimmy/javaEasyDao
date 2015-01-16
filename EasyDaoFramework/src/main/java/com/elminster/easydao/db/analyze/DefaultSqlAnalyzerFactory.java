package com.elminster.easydao.db.analyze;

import java.lang.reflect.Method;

import com.elminster.common.util.ObjectUtil;
import com.elminster.easydao.db.constants.SqlConstants;

public class DefaultSqlAnalyzerFactory implements ISqlAnalyzerFactory {

  private static DefaultSqlAnalyzerFactory instance = new DefaultSqlAnalyzerFactory();

  private DefaultSqlAnalyzerFactory() {
  }

  public static DefaultSqlAnalyzerFactory getInstance() {
    return instance;
  }

  public ISqlAnalyzer getSqlAnalyzer(Method method, Object[] args) {
    ISqlAnalyzer sqlAnalyzer = null;
    String methodName = method.getName();
    if (ObjectUtil.contains(SqlConstants.INSERT_KEYWORD, methodName)) {
      sqlAnalyzer = new ORMInsertAnalyzer();
    } else if (ObjectUtil.contains(SqlConstants.DELETE_KEYWORD, methodName)) {
      sqlAnalyzer = new ORMDeleteAnalyzer();
    } else if (ObjectUtil.contains(SqlConstants.MODIFY_KEYWORD, methodName)) {
      sqlAnalyzer = new ORMModifyAnalyzer();
    } else if (ObjectUtil.contains(SqlConstants.FETCH_KEYWORD, methodName)) {
      sqlAnalyzer = new ORMFetchAnalyzer();
    } else {
      sqlAnalyzer = new DefaultSqlAnalyzer();
    }
    return sqlAnalyzer;
  }
}
