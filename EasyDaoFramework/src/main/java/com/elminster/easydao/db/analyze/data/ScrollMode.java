package com.elminster.easydao.db.analyze.data;

import java.sql.ResultSet;

public class ScrollMode {

  private int resultSetType;
  private String name;
  
  private ScrollMode(int resultSetType, String name) {
    this.resultSetType = resultSetType;
    this.name = name;
  }
  
  public int getResultSetType() {
    return resultSetType;
  }
  
  public String toString() {
    return name;
  }
  
  public static final ScrollMode FORWARD_ONLY = new ScrollMode(ResultSet.TYPE_FORWARD_ONLY, "FORWARD_ONLY");
  public static final ScrollMode SCROLL_SENSITIVE = new ScrollMode(ResultSet.TYPE_SCROLL_SENSITIVE, "SCROLL_SENSITIVE");
  public static final ScrollMode SCROLL_INSENSITIVE = new ScrollMode(ResultSet.TYPE_SCROLL_INSENSITIVE, "SCROLL_INSENSITIVE");
}
