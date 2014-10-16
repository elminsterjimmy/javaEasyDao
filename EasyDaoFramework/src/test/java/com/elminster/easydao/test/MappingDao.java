package com.elminster.easydao.test;

import java.util.List;

import com.elminster.easydao.db.annotation.DAO;

@DAO
public interface MappingDao {

  public void insert(ORMMappingBean1 bean);
  
  public List<ORMMappingBean1> fetch(ORMMappingBean1 condition);
  
  public void delete(ORMMappingBean1 condition);
  
  public void insert(ORMMappingBean4 bean);
  
  public List<ORMMappingBean4> fetch(ORMMappingBean4 condition);
}
