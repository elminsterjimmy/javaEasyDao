package com.elminster.easydao.db.query;

import java.sql.SQLException;
import java.util.List;

public interface IListRowDataCallback {

  boolean onRow(Object o, List<Object> rowData) throws SQLException;
}
