package com.elminster.easydao.db.analyze.expression.func;

import com.elminster.easydao.db.analyze.expression.DataType;
import com.elminster.easydao.db.analyze.expression.operator.IOperator;

public class FunctionDef {

  private String fullName;
  private String name;
  private DataType result;
  private DataType[] parameter;
  private IOperator operator;
  private String group;
  private FunctionModule module;
  private int priority;
  
  public FunctionDef(String name, DataType result, DataType[] parameter, IOperator operator) {
    this.name = name;
    this.result = result;
    if (null != parameter) {
      this.parameter = parameter.clone();
    } else {
      this.parameter = DataType.EMPTYARR;
    }
    this.operator = operator;
    this.group = null;
    StringBuilder sb = new StringBuilder(this.name);
    sb.append("@");
    for (int i = 0; i < this.parameter.length; i++) {
      sb.append(i).append(this.parameter[i].toString()).append("@");
    }
    this.fullName = sb.toString();
  }
  
  public boolean equals(Object o) {
    boolean rtn = false;
    if (o instanceof FunctionDef) {
      FunctionDef f = (FunctionDef) o;
      rtn = f.toString().equals(this.toString()); 
    }
    return rtn;
  }
  
  public String toString() {
    return fullName;
  }
  
  public int hashCode() {
    return fullName.hashCode();
  }

  /**
   * @return the fullName
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * @param fullName the fullName to set
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the result
   */
  public DataType getResult() {
    return result;
  }

  /**
   * @param result the result to set
   */
  public void setResult(DataType result) {
    this.result = result;
  }

  /**
   * @return the parameter
   */
  public DataType[] getParameter() {
    return parameter;
  }

  /**
   * @param parameter the parameter to set
   */
  public void setParameter(DataType[] parameter) {
    this.parameter = parameter;
  }

  /**
   * @return the operator
   */
  public IOperator getOperator() {
    return operator;
  }

  /**
   * @param operator the operator to set
   */
  public void setOperator(IOperator operator) {
    this.operator = operator;
  }

  /**
   * @return the group
   */
  public String getGroup() {
    return group;
  }

  /**
   * @param group the group to set
   */
  public void setGroup(String group) {
    this.group = group;
  }

  /**
   * @return the module
   */
  public FunctionModule getModule() {
    return module;
  }

  /**
   * @param module the module to set
   */
  public void setModule(FunctionModule module) {
    this.module = module;
  }

  /**
   * @return the priority
   */
  public int getPriority() {
    return priority;
  }

  /**
   * @param priority the priority to set
   */
  public void setPriority(int priority) {
    this.priority = priority;
  }
}
