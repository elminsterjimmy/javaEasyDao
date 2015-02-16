package com.elminster.easydao.db.handler;

import java.lang.reflect.Method;
import java.util.List;

import com.elminster.common.util.CollectionUtil;
import com.elminster.common.util.TypeUtil;

public class ORMResultSetHandlerFactory {

	private static ORMResultSetHandlerFactory instance = new ORMResultSetHandlerFactory();
	
	private ORMResultSetHandlerFactory() {}
	
	public static ORMResultSetHandlerFactory getInstance() {
		return instance;
	}
	
	public IResultSetHandler getResultSetHandler(Method invokeMethod, Class<?> originalClass) throws Exception {
	  List<Class<?>> returnClasses = TypeUtil.INSTANCE.getMethodReturnTypeClass(originalClass, invokeMethod);
		IResultSetHandler resultSetHandler = null;
		if (CollectionUtil.isEmpty(returnClasses)) {
			return null;
		} 
		Class<?> returnClazz = returnClasses.get(0);
		if (List.class.isAssignableFrom(returnClazz)) {
			// List
			Class<?> genericClass = returnClasses.get(1);
			Class<?> clazz = Class.forName(genericClass.getName());
			resultSetHandler = new ORMListResultSetHandler(clazz);
		} else if (returnClazz.isArray()) {
			// Array
		  resultSetHandler = new ORMArrayResultSetHandler(returnClazz);
		} else {
			resultSetHandler = new ORMObjectResultSetHandler(returnClazz);
		}
		return resultSetHandler;
	}
}
