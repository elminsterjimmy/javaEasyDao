package com.elminster.easydao.db.handler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class ResultSetHandlerFactory {

	private static ResultSetHandlerFactory instance = new ResultSetHandlerFactory();
	
	private ResultSetHandlerFactory() {}
	
	public static ResultSetHandlerFactory getInstance() {
		return instance;
	}
	
	public IResultSetHandler getResultSetHandler(Method invokeMethod) throws Exception {
		Class<?> returnClazz = invokeMethod.getReturnType();
		IResultSetHandler resultSetHandler = null;
		if (null == returnClazz) {
			return null;
		} else if (List.class.isAssignableFrom(returnClazz)) {
			// List
			ParameterizedType pt = (ParameterizedType) invokeMethod.getGenericReturnType();
			String className = pt.getActualTypeArguments()[0].toString().split(" ")[1];
			Class<?> clazz = Class.forName(className);
			resultSetHandler = new ListResultSetHandler(clazz);
		} else if (returnClazz.isArray()) {
			// Array
		} else {
			resultSetHandler = new ObjectResultSetHandler(returnClazz);
		}
		return resultSetHandler;
	}
}
