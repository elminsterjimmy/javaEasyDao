package com.elminster.easydao.db.analyze.data;

import com.elminster.easydao.db.dialect.IDialect;

public class SeqenceSelectValue implements DBSelectValue {

  /** the sequence name. */
  private final String sequenceName;
  
  /** the database dialect. */
  private final IDialect dialect;
  
  /**
   * Constructor.
   * @param dialect the database dialect
   * @param sequenceName the sequence name
   */
  public SeqenceSelectValue(IDialect dialect, String sequenceName) {
    this.dialect = dialect;
    this.sequenceName = sequenceName;
  }

  /**
   * @return the sequenceName
   */
  public String getSequenceName() {
    return sequenceName;
  }

  @Override
  public String getSelectValueSql() {
    return dialect.getSequenceNextValueSql(sequenceName);
  }
}
