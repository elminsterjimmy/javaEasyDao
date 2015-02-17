package com.elminster.easydao.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.elminster.common.constants.Constants.StringConstants;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Key {
	public KeyPolicy policy() default KeyPolicy.NORMAL_POLICY;
	public String sequenceName() default StringConstants.EMPTY_STRING;
	public String customIdGeneratorClass() default StringConstants.EMPTY_STRING;
}
