package com.elminster.easydao.db.analyze.expression;

import java.util.Stack;

import com.elminster.common.constants.Constants.StringConstants;
import com.elminster.easydao.db.analyze.expression.evaluate.IResult;
import com.elminster.easydao.db.analyze.expression.evaluate.OperationResult;
import com.elminster.easydao.db.analyze.expression.operator.IOperator;

public class Variable implements IVariable, IOperator {

  private String name;
  
  private DataType type;

  private Object value;

  public Variable() {
    this(StringConstants.EMPTY_STRING);
  }
  
  public Variable(String name) {
    this(name, null);
  }

  public Variable(String name, Object value) {
    this.type = DataType.UNDEFINE;
    this.name = name;
    setVariableValue(value);
  }
  
  public Variable(Object value) {
    this(generateConstantName(value), value);
  }

  private static String generateConstantName(Object value) {
    String constName = "Object constant";
    if (value instanceof Boolean) {
      constName = "Boolean constant";
    } else if (value instanceof Integer) {
      constName = "Integer constant";
    } else if (value instanceof Long) {
      constName = "Long constant";
    } else if (value instanceof Float) {
      constName = "Float constant";
    } else if (value instanceof Double) {
      constName = "Double constant";
    } else if (value instanceof String) {
      constName = "String constant";
    } else if (value instanceof Short) {
      constName = "Short constant";
    } else if (value instanceof Character) {
      constName = "Character constant";
    }
    return constName;
  }

  public Variable(DataType type, Object value) {
    this.type = type;
    this.value = value;
    this.setVariableName(type.getName());
  }

  @Override
  public DataType getVariableType() {
    return type;
  }

  @Override
  public Object getVariableValue() {
    return value;
  }

  @Override
  public void setVariableValue(Object value) {
    this.value = value;
    if (DataType.UNDEFINE.equals(this.type)
        || DataType.NULL.equals(this.type)) {
      autoAdaptDataType();
    }
  }

  @Override
  public void setVariableType(DataType type) {
    this.type = type;
  }

  protected void autoAdaptDataType() {
    if (null == this.value) {
      this.type = DataType.NULL;
    } else {
      Class< ? > clazz = this.value.getClass();
      if (Integer.class.isAssignableFrom(clazz)
          || int.class.isAssignableFrom(clazz)) {
        this.type = DataType.INTEGER;
      } else if (Short.class.isAssignableFrom(clazz)
          || short.class.isAssignableFrom(clazz)) {
        this.type = DataType.INTEGER;
      } else if (Long.class.isAssignableFrom(clazz)
          || long.class.isAssignableFrom(clazz)) {
        this.type = DataType.LONG;
      } else if (Float.class.isAssignableFrom(clazz)
          || float.class.isAssignableFrom(clazz)) {
        this.type = DataType.DOUBLE;
      } else if (Double.class.isAssignableFrom(clazz)
          || double.class.isAssignableFrom(clazz)) {
        this.type = DataType.DOUBLE;
      } else if (Boolean.class.isAssignableFrom(clazz)
          || boolean.class.isAssignableFrom(clazz)) {
        this.type = DataType.BOOLEAN;
      } else if (String.class.isAssignableFrom(clazz)) {
        this.type = DataType.STRING;
      } else {
        this.type = DataType.OBJECT;
      }
    }
  }

  @Override
  public String getOperatorName() {
    return "variable";
  }

  @Override
  public IResult operate(Stack<Object> stack) {
    stack.push(this);
    return new OperationResult("variable: " + this.name, String.valueOf(this.value));
  }

  @Override
  public void setVariableName(String name) {
    this.name = name;
  }

  @Override
  public String getVariableName() {
    return this.name;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return this.name + ":" + this.value;
  }
}
