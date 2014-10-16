package com.elminster.easydao.db.handler;

import java.sql.ResultSet;

public class ListResultSetHandler extends ResultSetHandler implements IResultSetHandler {


	public ListResultSetHandler(Class<?> mappingClass) {
		super(mappingClass);
	}

	public Object handleResultSet(ResultSet rs) throws Exception {
		return mapping(rs);
	}
}
