package com.elminster.easydao.db.analyze;

import java.lang.reflect.Method;

import com.elminster.easydao.db.analyze.data.SqlStatementInfo;

/**
 * An analyzer for parser SQL statement.
 * 
 * @author Gu
 * @version 1.0
 * 
 */
public interface ISqlAnalyzer {
	
	/**
	 * Parser a SQL statement to get the SQL statement and the SQL statement's parameter(s).
	 * @param method the method which original SQL statement is invoked
	 * @param methodArguments the method's argument(s)
	 * @return SqlStatementInfo SQL statement information
	 */
	public SqlStatementInfo parser(Method invokedMethod, Object... methodArguments) throws Exception;
	
	/**
	 * Get original DAO interface
	 * @return original DAO interface
	 */
	public Class<?> getOriginalClass();
	
	/**
   * Set original DAO interface
   */
	public void setOriginalClass(Class<?> originalClass);
}
