package com.elminster.easydao.db.handler;

import java.sql.ResultSet;

public interface IResultSetHandler {

	public Object handleResultSet(ResultSet rs) throws Exception;
}
