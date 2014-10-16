package com.elminster.easydao.db.handler;

import java.sql.ResultSet;
import java.util.List;

public class CollectionResultSetHandler extends ResultSetHandler implements IResultSetHandler {

  protected Class<?> collectionClass;

	public CollectionResultSetHandler(Class<?> mappingClass, Class<?> collectionClass) {
		super(mappingClass);
		this.collectionClass = collectionClass;
	}

	@SuppressWarnings("rawtypes")
  public Object handleResultSet(ResultSet rs) throws Exception {
	  List list = mapping(rs);
	  // TODO collection type?
	  return list;
	}
}
