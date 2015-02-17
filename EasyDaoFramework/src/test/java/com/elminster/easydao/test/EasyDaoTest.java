package com.elminster.easydao.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.elminster.common.constants.Constants.StringConstants;
import com.elminster.common.util.FileUtil;
import com.elminster.common.util.StringUtil;
import com.elminster.easydao.db.analyze.data.PagedData;
import com.elminster.easydao.db.dao.DAOSupportManager;
import com.elminster.easydao.db.ds.DataSourceFactory;
import com.elminster.easydao.db.query.IQuery;
import com.elminster.easydao.db.session.DAOSupportSession;
import com.elminster.easydao.db.session.DAOSupportSessionFactory;
import com.elminster.easydao.db.session.DAOSupportSessionFactoryManager;
import com.elminster.easydao.db.transaction.TransactionTemplate;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EasyDaoTest {

  private static String factoryId;

  @BeforeClass
  public static void init() throws Exception {
    DOMConfigurator.configure("log4j.xml");
    DataSourceFactory dsFactory = DataSourceFactory.INSTANCE;
    Properties properties = new Properties();
    properties.load(EasyDaoTest.class.getResourceAsStream("DataSource.properties"));
    DataSource ds = dsFactory.getDataSource(properties);
    DAOSupportSessionFactory sessionFactory = new DAOSupportSessionFactory(ds);
    DAOSupportSessionFactoryManager.getSessionManager().putSessionFactory(
        sessionFactory);
    factoryId = sessionFactory.getFactoryId();
    DAOSupportSession session = sessionFactory.popDAOSupportSession();
    try {
      IQuery query = session.getQuery();
      String stmt = FileUtil.readFile2String(EasyDaoTest.class
          .getResourceAsStream("CreateTestDB.sql"));
      stmt = StringUtil.chomp(stmt);
      String[] sqls = stmt.split(StringConstants.SEMICOLON);
      for (String sql : sqls) {
        if (StringUtil.isNotEmpty(sql)) {
          query.sqlExecNoResult(sql);
        }
      }
    } finally {
      sessionFactory.pushDAOSupportSession(session);
    }
  }

  @AfterClass
  public static void finalized() throws SQLException {
    DAOSupportSessionFactoryManager.getSessionManager()
        .getSessionFactory(factoryId).shutdown();
  }

  @Test
  public void test1ORMInsert() throws Exception {
    DAOSupportSessionFactory factory = DAOSupportSessionFactoryManager
        .getSessionManager().getSessionFactory(factoryId);
    DAOSupportSession session = factory.popDAOSupportSession();
    try {
      new TransactionTemplate() {

        @Override
        public void doTransaction(DAOSupportSession session) throws Exception {
          DAOSupportManager manager = DAOSupportManager.INSTANCE;
          TestDao dao = (TestDao) manager.getDAO(TestDao.class);
          for (int i = 1; i <= 5; i++) {
            ORMBean condition = new ORMBean();
            condition.setAccount(Math.pow(-1, i) * 1021.5d);
            Date now = new Date();
            condition.setLastUpdate(new java.sql.Date(now.getTime()));
            condition.setName("user" + i);
            condition.setPass("ps" + i);
            condition.setQuary(i);
            List<Integer> list = new ArrayList<Integer>();
            list.add(12345678);
            list.add(23456789);
            condition.setTel(list);
            dao.insert(condition);
          }
        }
      }.workWithTransaction(session);
    } finally {
      factory.pushDAOSupportSession(session);
    }

  }

  @Test
  public void test2ORMFetch() throws Exception {
    DAOSupportSessionFactory factory = DAOSupportSessionFactoryManager
        .getSessionManager().getSessionFactory(factoryId);
    DAOSupportSession session = factory.popDAOSupportSession();
    try {
      DAOSupportManager manager = DAOSupportManager.INSTANCE;
      TestDao dao = (TestDao) manager.getDAO(TestDao.class);
      ORMBean condition = new ORMBean();
      PagedData pagedData = new PagedData(0, 3);
      List<ORMBean> list = dao.fetch(condition, pagedData);
      System.out.println(list);
    } finally {
      factory.pushDAOSupportSession(session);
    }

  }

  @Test
  public void test3ORMUpdate() throws Exception {
    DAOSupportSessionFactory factory = DAOSupportSessionFactoryManager
        .getSessionManager().getSessionFactory(factoryId);
    DAOSupportSession session = factory.popDAOSupportSession();
    try {
      DAOSupportManager manager = DAOSupportManager.INSTANCE;
      TestDao dao = (TestDao) manager.getDAO(TestDao.class);
      ORMBean condition = new ORMBean();
      condition.setId(1);
      condition.setQuary(10);
      dao.update(condition);
      condition = new ORMBean();
      condition.setId(1);
      List<ORMBean> list = dao.fetch(condition, null);
      Assert.assertEquals(1, list.size());
      Assert.assertEquals(10, list.get(0).getQuary().intValue());
    } finally {
      factory.pushDAOSupportSession(session);
    }

  }

  @Test
  public void test4ORMDelete() throws Exception {
    DAOSupportSessionFactory factory = DAOSupportSessionFactoryManager
        .getSessionManager().getSessionFactory(factoryId);
    DAOSupportSession session = factory.popDAOSupportSession();
    try {
      DAOSupportManager manager = DAOSupportManager.INSTANCE;
      TestDao dao = (TestDao) manager.getDAO(TestDao.class);
      ORMBean condition = new ORMBean();
      condition.setId(6);
      dao.delete(condition);
    } finally {
      factory.pushDAOSupportSession(session);
    }

  }

  @Test
  public void test5SqlStatement() throws Exception {
    DAOSupportSessionFactory factory = DAOSupportSessionFactoryManager
        .getSessionManager().getSessionFactory(factoryId);
    DAOSupportSession session = factory.popDAOSupportSession();
    try {
      DAOSupportManager manager = DAOSupportManager.INSTANCE;
      TestDao dao = (TestDao) manager.getDAO(TestDao.class);
      String pass = dao.getPasswordByUserName("user1");
      System.out.println(pass);
      ORMBean bean = new ORMBean();
      bean.setName("user2");
      String pass2 = dao.getPasswordByBean(bean);
      Assert.assertEquals("ps2", pass2);
    } finally {
      factory.pushDAOSupportSession(session);
    }

  }

  @Test
  public void test6SqlFile() throws Exception {
    DAOSupportSessionFactory factory = DAOSupportSessionFactoryManager
        .getSessionManager().getSessionFactory(factoryId);
    DAOSupportSession session = factory.popDAOSupportSession();
    try {
      DAOSupportManager manager = DAOSupportManager.INSTANCE;
      TestDao dao = (TestDao) manager.getDAO(TestDao.class);
      Double acc = dao.getAccountByUserName("user1");
      Assert.assertEquals(Double.valueOf(-1021.5), acc);
      ORMBean bean = new ORMBean();
      bean.setName("user2");
      List<Integer> list = new ArrayList<Integer>();
      list.add(12345678);
      list.add(23456789);
      bean.setTel(list);
      Double acc2 = dao.getAccountByBean(bean);
      Assert.assertEquals(Double.valueOf(1021.5), acc2);
    } finally {
      factory.pushDAOSupportSession(session);
    }

  }

  @Test
  public void test7SqlExpression1() throws Exception {
    DAOSupportSessionFactory factory = DAOSupportSessionFactoryManager
        .getSessionManager().getSessionFactory(factoryId);
    DAOSupportSession session = factory.popDAOSupportSession();
    try {
      DAOSupportManager manager = DAOSupportManager.INSTANCE;
      TestDao dao = (TestDao) manager.getDAO(TestDao.class);
      Double acc = dao.getAccountByCondition(1);
      Assert.assertEquals(Double.valueOf(-1021.5), acc);
      acc = dao.getAccountByCondition(-1);
      Assert.assertEquals(Double.valueOf(1021.5), acc);
    } finally {
      factory.pushDAOSupportSession(session);
    }

  }

  @Test
  public void test8SqlExpression2() throws Exception {
    DAOSupportSessionFactory factory = DAOSupportSessionFactoryManager
        .getSessionManager().getSessionFactory(factoryId);
    DAOSupportSession session = factory.popDAOSupportSession();
    try {
      DAOSupportManager manager = DAOSupportManager.INSTANCE;
      TestDao dao = (TestDao) manager.getDAO(TestDao.class);
      ORMBean bean = new ORMBean();
      bean.setName("user1");
      Double acc = dao.getAccountByBeanCondition(bean);
      Assert.assertEquals(Double.valueOf(-1021.5), acc);
      bean.setName("user2");
      acc = dao.getAccountByBeanCondition(bean);
      Assert.assertEquals(Double.valueOf(1021.5), acc);
    } finally {
      factory.pushDAOSupportSession(session);
    }
  }
}