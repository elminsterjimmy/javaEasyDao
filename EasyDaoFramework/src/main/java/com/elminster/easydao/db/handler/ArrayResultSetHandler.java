package com.elminster.easydao.db.handler;

import java.sql.ResultSet;
import java.util.List;

import com.elminster.common.util.CollectionUtil;
import com.elminster.common.util.ObjectUtil;

public class ArrayResultSetHandler extends ResultSetHandler implements IResultSetHandler {

	public ArrayResultSetHandler(Class<?> mappingClass) {
		super(mappingClass);
	}

	@SuppressWarnings({"rawtypes" })
	@Override
	public Object handleResultSet(ResultSet rs) throws Exception {
		List list = mapping(rs);
		if (CollectionUtil.isEmpty(list)) {
			return null;
		}
		return ObjectUtil.toObjectArray(CollectionUtil.collection2Array(list));
	}
	
}
