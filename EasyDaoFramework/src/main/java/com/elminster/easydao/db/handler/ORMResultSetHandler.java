package com.elminster.easydao.db.handler;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

abstract public class ORMResultSetHandler implements IResultSetHandler {
	
	protected Class<?> mappingClass;
	
	public ORMResultSetHandler(Class<?> mappingClass) {
		this.mappingClass = mappingClass;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	protected List mapping(ResultSet rs) throws Exception {
		List list = new ArrayList();
		while (rs.next()) {
			Object obj = ResultSetHelper.mapRow(rs, mappingClass);
			list.add(obj);
		}
		return list;
	}
}
