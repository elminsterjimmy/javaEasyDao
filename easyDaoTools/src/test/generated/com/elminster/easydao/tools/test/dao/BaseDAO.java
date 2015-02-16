package com.elminster.easydao.tools.test.dao;

import java.util.List;

public interface BaseDAO<T> {

  public int insert(T entity);
  
  public void update(T entity);
  
  public List<T> fetch(T condition);
  
  public void delete(T entity);
  
}
