package com.elminster.easydao.db.schema;

import com.elminster.common.constants.Constants.StringConstants;

public class Column implements IColumn {

  private String name;
  private String alias;
  private ITable table;
  private long maxLength;
  private int precision;
  private boolean nullable;
  private int type;
  private ForeignKey foreignKey;
  
  public Column(String name, int type) {
    this.name = name;
    this.type = type;
  }
  
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  
  /**
   * @return the alias
   */
  public String getAlias() {
    return alias;
  }
  
  /**
   * @param alias the alias to set
   */
  public void setAlias(String alias) {
    this.alias = alias;
  }
  
  /**
   * @return the table
   */
  public ITable getTable() {
    return table;
  }
  
  /**
   * @param table the table to set
   */
  public void setTable(ITable table) {
    this.table = table;
  }

  /**
   * @return the maxLength
   */
  public long getMaxLength() {
    return maxLength;
  }

  /**
   * @param maxLength the maxLength to set
   */
  public void setMaxLength(long maxLength) {
    this.maxLength = maxLength;
  }

  /**
   * @return the precision
   */
  public int getPrecision() {
    return precision;
  }

  /**
   * @param precision the precision to set
   */
  public void setPrecision(int precision) {
    this.precision = precision;
  }

  /**
   * @return the notNull
   */
  public boolean isNullable() {
    return nullable;
  }

  /**
   * @param notNull the notNull to set
   */
  public void setNullable(boolean nullable) {
    this.nullable = nullable;
  }

  /**
   * @return the type
   */
  public int getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(int type) {
    this.type = type;
  }

  /**
   * @return the foreignKey
   */
  public ForeignKey getForeignKey() {
    return foreignKey;
  }

  /**
   * @param foreignKey the foreignKey to set
   */
  public void setForeignKey(ForeignKey foreignKey) {
    this.foreignKey = foreignKey;
  }

  @Override
  public String getFullName() {
    if (null == table) {
      return name;
    }
    if (null != table.getAlias()) {
      return table.getAlias() + StringConstants.DOT + name;
    }
    return table.getName() + StringConstants.DOT + name;
  }
}
