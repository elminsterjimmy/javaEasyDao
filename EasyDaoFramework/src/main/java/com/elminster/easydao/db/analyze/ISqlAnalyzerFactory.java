package com.elminster.easydao.db.analyze;

import java.lang.reflect.Method;

public interface ISqlAnalyzerFactory {

	public ISqlAnalyzer getSqlAnalyzer(Method method, Object[] args);
}
