package com.elminster.easydao.db.analyze;

import java.lang.reflect.Method;

import com.elminster.easydao.db.manager.DAOSupportSession;

public interface ISqlAnalyzerFactory {

	public ISqlAnalyzer getSqlAnalyzer(Method method, Object[] args, DAOSupportSession session);
}
