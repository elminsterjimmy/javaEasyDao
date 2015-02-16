package com.elminster.easydao.tools.test.serviceIf;

import com.elminster.easydao.tools.test.exception.ServiceException;

import java.util.List;

public interface BaseService<T> {

  public int insert(T entity) throws ServiceException;
  
  public void update(T entity) throws ServiceException;
  
  public List<T> fetch(T condition) throws ServiceException;
  
  public void delete(T entity) throws ServiceException;
}
