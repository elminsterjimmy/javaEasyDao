package com.elminster.easydao.db.handler;

import java.sql.ResultSet;

public class ORMListResultSetHandler extends ORMResultSetHandler implements IResultSetHandler {


	public ORMListResultSetHandler(Class<?> mappingClass) {
		super(mappingClass);
	}

	public Object handleResultSet(ResultSet rs) throws Exception {
		return mapping(rs);
	}
}
