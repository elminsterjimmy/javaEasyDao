package com.elminster.easydao.db.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class DoubleListResultSetHandler implements IResultSetHandler {
  
  private List<List<Object>> result;
  
  public DoubleListResultSetHandler() {
    this.result = new ArrayList<List<Object>>();
  }

  @Override
  public Object handleResultSet(ResultSet rs) throws Exception {
    ResultSetMetaData rsmd = rs.getMetaData();
    int columnCount = rsmd.getColumnCount();
    while (rs.next()) {
      List<Object> list = new ArrayList<Object>();
      for (int i = 1; i <= columnCount; i++) {
        list.add(rs.getObject(i));
      }
      result.add(list);
    }
    return result;
  }
}
