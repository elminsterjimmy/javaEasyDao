package com.elminster.easydao.test;

import java.util.List;

import com.elminster.easydao.db.analyze.data.PagedData;
import com.elminster.easydao.db.annotation.DAO;
import com.elminster.easydao.db.annotation.Sql;
import com.elminster.easydao.db.annotation.SqlFile;
import com.elminster.easydao.db.annotation.SqlParam;

@DAO
// <-- DAO Class Mark
public interface TestDao {

  public List<ORMBean> fetch(ORMBean bean, PagedData pagedData);

  public int insert(ORMBean bean);

  public int update(ORMBean bean);

  public int delete(ORMBean bean);

  @Sql("select pass from testtable where name = $user")
  public String getPasswordByUserName(@SqlParam("user") String user);

  @Sql("select pass from testtable where name = $bean.name")
  public String getPasswordByBean(@SqlParam("bean") ORMBean bean);

  @SqlFile("getAccountByUserName.sql")
  public Double getAccountByUserName(@SqlParam("user") String user);

  @SqlFile("getAccountByBean.sql")
  public Double getAccountByBean(@SqlParam("bean") ORMBean bean);

  @SqlFile("getAccountByCondition.sql")
  public Double getAccountByCondition(@SqlParam("condition") int codition);
  
  @SqlFile("getAccountByBeanCondition.sql")
  public Double getAccountByBeanCondition(@SqlParam("bean") ORMBean bean);
}
