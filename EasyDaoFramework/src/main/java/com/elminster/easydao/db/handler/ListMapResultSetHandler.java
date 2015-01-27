package com.elminster.easydao.db.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListMapResultSetHandler implements IResultSetHandler {
  
  private List<Map<String, Object>> result;
  
  public ListMapResultSetHandler() {
    this.result = new ArrayList<Map<String, Object>>();
  }

  @Override
  public Object handleResultSet(ResultSet rs) throws Exception {
    ResultSetMetaData rsmd = rs.getMetaData();
    int columnCount = rsmd.getColumnCount();
    while (rs.next()) {
      Map<String, Object> map = new HashMap<String, Object>();
      for (int i = 1; i <= columnCount; i++) {
        map.put(rsmd.getColumnLabel(i), rs.getObject(i));
      }
      result.add(map);
    }
    return result;
  }
}
