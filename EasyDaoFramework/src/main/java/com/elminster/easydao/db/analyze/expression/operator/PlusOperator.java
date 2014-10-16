package com.elminster.easydao.db.analyze.expression.operator;

import com.elminster.easydao.db.analyze.expression.DataType;
import com.elminster.easydao.db.analyze.expression.IVariable;
import com.elminster.easydao.db.analyze.expression.Variable;

public class PlusOperator extends ArithmeticOperator {

  public static final IOperator INSTANCE = new PlusOperator();
  
  @Override
  public String getOperatorName() {
    return " + ";
  }

  @Override
  protected IVariable process(IVariable left, IVariable right) {
    Object rst = null;
    if (DataType.STRING.equals(returnType)) {
      rst = String.valueOf(left.getVariableValue()) + String.valueOf(right.getVariableValue());
    } else if (DataType.DOUBLE.equals(returnType)) {
      rst = (Double) left.getVariableValue() + (Double) right.getVariableValue();
    } else if (DataType.LONG.equals(returnType)) {
      rst = (Long) left.getVariableValue() + (Long) right.getVariableValue();
    } else if (DataType.INTEGER.equals(returnType)) {
      rst = (Integer) left.getVariableValue() + (Integer) right.getVariableValue();
    }
    
    IVariable v = new Variable(returnType, rst);
    return v;
  }

  @Override
  protected boolean processable(DataType ldt, DataType rdt) {
    if (DataType.STRING.equals(ldt) || DataType.STRING.equals(rdt)) {
      this.returnType = DataType.STRING;
    } else if (DataType.DOUBLE.equals(ldt)) {
      if (DataType.DOUBLE.equals(rdt)
          || DataType.LONG.equals(rdt)
          || DataType.INTEGER.equals(rdt)) {
        this.returnType = DataType.DOUBLE;
      }
    } else if (DataType.LONG.equals(ldt)) {
      if (DataType.DOUBLE.equals(rdt)) {
        this.returnType = DataType.DOUBLE;
      } else if (DataType.LONG.equals(rdt)
          || DataType.INTEGER.equals(rdt)) {
        this.returnType = DataType.LONG;
      }
    } else if (DataType.INTEGER.equals(ldt)) {
      if (DataType.DOUBLE.equals(rdt)) {
        this.returnType = DataType.DOUBLE;
      } else if (DataType.LONG.equals(rdt)) {
        this.returnType = DataType.LONG;
      } else if (DataType.INTEGER.equals(rdt)) {
        this.returnType = DataType.INTEGER;
      }
    }
    return null != this.returnType;
  }

}
