package com.elminster.easydao.db.executor;

import java.lang.reflect.Method;

import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.exception.SQLExecuteException;

public interface ISqlExecutor {

	public Object execute(SqlStatementInfo sqlStatementInfo, Method invokeMethod, Object[] args) throws SQLExecuteException;

  public void setOriginalClass(Class<?> originalClass);
}
