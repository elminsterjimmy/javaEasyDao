package com.elminster.easydao.db.query;

import java.sql.SQLException;
import java.util.Map;

public interface IMapRowDataCallback {

  boolean onRow(Object o, Map<String, Object> rowData) throws SQLException;
}
