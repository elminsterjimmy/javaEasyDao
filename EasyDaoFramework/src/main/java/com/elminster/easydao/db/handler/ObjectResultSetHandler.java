package com.elminster.easydao.db.handler;

import java.sql.ResultSet;
import java.util.List;

import com.elminster.common.util.CollectionUtil;

public class ObjectResultSetHandler extends ResultSetHandler implements IResultSetHandler {


	public ObjectResultSetHandler(Class<?> mappingClass) {
		super(mappingClass);
	}

	@SuppressWarnings({"rawtypes"})
	public Object handleResultSet(ResultSet rs) throws Exception {
		List list = mapping(rs);
		if (CollectionUtil.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}
}
