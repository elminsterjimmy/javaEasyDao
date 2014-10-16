package com.elminster.easydao.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.elminster.easydao.db.mapping.UpdatePolicy;
import com.elminster.easydao.db.mapping.InsertPolicy;
import com.elminster.easydao.db.mapping.MappingPolicy;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {

  MappingPolicy mappingPolicy();
  UpdatePolicy updatePolicy() default UpdatePolicy.NONE;
  InsertPolicy insertPolicy() default InsertPolicy.DIRECT_INSERT;
  String intermediaryTable() default "";
  String[] intermediaryControlColumn() default "";
  String[] intermediaryUncontrolColumn() default "";
  String directRefColumn() default "";
  Class<?> entity();
}
