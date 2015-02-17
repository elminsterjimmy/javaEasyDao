package com.elminster.easydao.db.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.elminster.common.util.ReflectUtil;

public abstract class InterfaceInvocationHandler implements InvocationHandler {
	
	private static final String[] object_class_method = {
		"clone",
		"toString",
		"equals",
		"finalize",
		"hashCode"
	};
	private List<String> extractOverrideMethods = new ArrayList<String>();
	private Class<?> originalClass;
	
	public InterfaceInvocationHandler() {
		for (String methodName : object_class_method) {
			extractOverrideMethods.add(methodName);
		}
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// Do NOT override
		if (extractOverrideMethods.contains(method.getName())) {
			Object p;
			if (proxy instanceof Proxy) {
				p = ReflectUtil.getFieldValue(proxy, "h");
			} else {
				p = proxy;
			}
			return ReflectUtil.invoke(p, method, args);
		} else {
			return override(proxy, method, args);
		}
	}

	abstract protected Object override(Object proxy, Method method, Object[] args) throws Throwable;

	public List<String> getExtractOverrideMethods() {
		return extractOverrideMethods;
	}

	public void setExtractOverrideMethods(List<String> extractOverrideMethods) {
		this.extractOverrideMethods = extractOverrideMethods;
	}
	
	public void addExtractOverrideMethod(String extractOverrideMethod) {
		extractOverrideMethods.add(extractOverrideMethod);
	}

  public Class<?> getOriginalClass() {
    return originalClass;
  }

  public void setOriginalClass(Class<?> originalClass) {
    this.originalClass = originalClass;
  }
}
