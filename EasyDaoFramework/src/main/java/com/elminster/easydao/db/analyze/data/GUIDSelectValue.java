package com.elminster.easydao.db.analyze.data;

import com.elminster.easydao.db.dialect.IDialect;

public class GUIDSelectValue implements DBSelectValue {
  
  /** the database dialect. */
  private final IDialect dialect;
  
  /**
   * Constructor.
   * @param dialect the database dialect
   */
  public GUIDSelectValue(IDialect dialect) {
    super();
    this.dialect = dialect;
  }

  @Override
  public String getSelectValueSql() {
    return dialect.getGUIdSql();
  }
}
