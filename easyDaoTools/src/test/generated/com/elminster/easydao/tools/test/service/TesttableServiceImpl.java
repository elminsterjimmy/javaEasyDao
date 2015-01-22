package com.elminster.easydao.tools.test.service;

import java.util.ArrayList;
import java.util.List;

import com.elminster.common.util.CollectionUtil;
import com.elminster.common.util.ObjectUtil;
import com.elminster.easydao.tools.test.entity.TesttableEntity;
import com.elminster.easydao.tools.test.dto.Testtable;
import com.elminster.easydao.tools.test.dao.TesttableEntityDAO;
import com.elminster.easydao.tools.test.serviceIf.TesttableService;
import com.elminster.easydao.tools.test.exception.ServiceException;

public class TesttableServiceImpl implements TesttableService {

  private TesttableEntityDAO dao;
  
  public void setDao(TesttableEntityDAO dao) {
    this.dao = dao;
  }

  public int insert(Testtable condition) throws ServiceException {
    TesttableEntity entity = new TesttableEntity();
    try {
      entity = (TesttableEntity) ObjectUtil.copyProperties(condition, entity);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
    return dao.insert(entity);
  }
  
  public void update(Testtable condition) throws ServiceException {
    TesttableEntity entity = new TesttableEntity();
    try {
      entity = (TesttableEntity) ObjectUtil.copyProperties(condition, entity);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
    dao.update(entity);
  }
  
  public void delete(Testtable condition) throws ServiceException {
    TesttableEntity entity = new TesttableEntity();
    try {
      entity = (TesttableEntity) ObjectUtil.copyProperties(condition, entity);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
    dao.delete(entity);
  }
  
  public List<Testtable> fetch(Testtable condition) throws ServiceException {
    List<Testtable> result = new ArrayList<Testtable>();
    TesttableEntity entity = new TesttableEntity();
    try {
      entity = (TesttableEntity) ObjectUtil.copyProperties(condition, entity);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
    List<TesttableEntity> entities = dao.fetch(entity);
    try {
      if (CollectionUtil.isNotEmpty(entities)) {
        Testtable data = new Testtable();
        condition = (Testtable) ObjectUtil.copyProperties(entity, data);
        result.add(data);
      }
    } catch (Exception e) {
      throw new ServiceException(e);
    }
    return result;
  }
}
