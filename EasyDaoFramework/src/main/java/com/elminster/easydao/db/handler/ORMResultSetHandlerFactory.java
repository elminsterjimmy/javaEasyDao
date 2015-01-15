package com.elminster.easydao.db.handler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class ORMResultSetHandlerFactory {

	private static ORMResultSetHandlerFactory instance = new ORMResultSetHandlerFactory();
	
	private ORMResultSetHandlerFactory() {}
	
	public static ORMResultSetHandlerFactory getInstance() {
		return instance;
	}
	
	public IResultSetHandler getResultSetHandler(Method invokeMethod, Class<?> originalClass) throws Exception {
		Class<?> returnClazz = invokeMethod.getReturnType();
		IResultSetHandler resultSetHandler = null;
		if (null == returnClazz) {
			return null;
		} else if (List.class.isAssignableFrom(returnClazz)) {
			// List
			ParameterizedType pt = (ParameterizedType) invokeMethod.getGenericReturnType();
			String typeArguments[] = pt.getActualTypeArguments()[0].toString().split(" ");
			String className = null;
			if (typeArguments.length > 1) {
			  className = typeArguments[1];
			} else {
			  Type[] types = originalClass.getGenericInterfaces();
			  if (types.length > 0) {
			    typeArguments = ((ParameterizedType)types[0]).getActualTypeArguments()[0].toString().split(" ");
			    if (typeArguments.length > 1) {
		        className = typeArguments[1];
			    }
			  }
			}
			if (null == className) {
			  throw new NullPointerException("cannot found generic type for class: " + originalClass.getName());
			}
			Class<?> clazz = Class.forName(className);
			resultSetHandler = new ORMListResultSetHandler(clazz);
		} else if (returnClazz.isArray()) {
			// Array
		} else {
			resultSetHandler = new ORMObjectResultSetHandler(returnClazz);
		}
		return resultSetHandler;
	}
}
