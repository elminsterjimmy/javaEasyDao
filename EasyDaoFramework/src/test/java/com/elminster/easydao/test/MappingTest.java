package com.elminster.easydao.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.elminster.common.constants.Constants.StringConstants;
import com.elminster.common.util.FileUtil;
import com.elminster.common.util.StringUtil;
import com.elminster.easydao.db.manager.DAOSupportManager;
import com.elminster.easydao.db.manager.DAOSupportSession;
import com.elminster.easydao.db.manager.DAOSupportSessionFactory;
import com.elminster.easydao.db.manager.DAOSupportSessionFactoryManager;
import com.elminster.easydao.db.query.IQuery;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class MappingTest {

  private static String factoryId;

  @BeforeClass
  public static void init() throws Exception {
    DOMConfigurator.configure("log4j.xml");
    MysqlDataSource ds = new MysqlDataSource();
    ds.setUrl("jdbc:mysql://localhost:3306/test");
    ds.setUser("root");
    ds.setPassword("root");
    DAOSupportSessionFactory sessionFactory = new DAOSupportSessionFactory(ds);
    DAOSupportSessionFactoryManager.getSessionManager().putSessionFactory(
        sessionFactory);
    factoryId = sessionFactory.getFactoryId();
    DAOSupportSession session = sessionFactory.popDAOSupportSession();
    try {
      IQuery query = session.getQuery();
      String stmt = FileUtil.readFile2String(EasyDaoTest.class
          .getResourceAsStream("CreateMappingDB.sql"));
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
  public static void finalized() {
    DAOSupportSessionFactoryManager.getSessionManager()
        .getSessionFactory(factoryId).shutdown();
  }
  
  @Test
  public void testInsert() throws Exception {
    DAOSupportSessionFactory factory = DAOSupportSessionFactoryManager
        .getSessionManager().getSessionFactory(factoryId);
    DAOSupportSession session = factory.popDAOSupportSession();
    try {
      DAOSupportManager manager = DAOSupportManager.getInstance();
      manager.setSession(session);
      MappingDao dao = (MappingDao) manager.getDAO(MappingDao.class);
      session.beginTransaction();
      
      ORMMappingBean1 bean1 = new ORMMappingBean1();
      bean1.setName("bean1 name1");
      List<ORMMappingBean2> nameList = new ArrayList<ORMMappingBean2>();
      nameList.add(new ORMMappingBean2("bean2 name1"));
      nameList.add(new ORMMappingBean2("bean2 name2"));
      bean1.setNameList(nameList);
      
      List<ORMMappingBean3> name2List = new ArrayList<ORMMappingBean3>();
      name2List.add(new ORMMappingBean3("bean3 name1"));
      name2List.add(new ORMMappingBean3("bean3 name2"));
      bean1.setName2List(name2List);
      
      dao.insert(bean1);
      
      ORMMappingBean4 b4 = new ORMMappingBean4("bean4 name1");
      dao.insert(b4);
      
      
      ORMMappingBean1 bean2 = new ORMMappingBean1();
      bean2.setName("bean1 name2");
      nameList = new ArrayList<ORMMappingBean2>();
      nameList.add(new ORMMappingBean2("bean2 name1"));
      nameList.add(new ORMMappingBean2("bean2 name2"));
      bean2.setNameList(nameList);
      
      name2List = new ArrayList<ORMMappingBean3>();
      name2List.add(new ORMMappingBean3("bean3 name1"));
      name2List.add(new ORMMappingBean3("bean3 name2"));
      bean2.setName2List(name2List);
      
      dao.insert(bean2);
      
      session.endTransaction();
    } finally {
      factory.pushDAOSupportSession(session);
    }
  }
  
  @Test
  public void testFetch() throws Exception {
    DAOSupportSessionFactory factory = DAOSupportSessionFactoryManager
        .getSessionManager().getSessionFactory(factoryId);
    DAOSupportSession session = factory.popDAOSupportSession();
    try {
      DAOSupportManager manager = DAOSupportManager.getInstance();
      manager.setSession(session);
      MappingDao dao = (MappingDao) manager.getDAO(MappingDao.class);
      session.beginTransaction();
      
      ORMMappingBean1 condition = new ORMMappingBean1();
      condition.setId(1);
      
      List<ORMMappingBean1> fetched = dao.fetch(condition);
      
      ORMMappingBean1 bean1 = new ORMMappingBean1();
      bean1.setId(1);
      bean1.setName("bean1 name1");
      List<ORMMappingBean2> nameList = new ArrayList<ORMMappingBean2>();
      ORMMappingBean2 bean2 = new ORMMappingBean2();
      bean2.setId(1);
      bean2.setName("bean2 name1");
      nameList.add(bean2);
      bean2 = new ORMMappingBean2();
      bean2.setId(2);
      bean2.setName("bean2 name2");
      nameList.add(bean2);
      bean1.setNameList(nameList);
      
      List<ORMMappingBean3> name2List = new ArrayList<ORMMappingBean3>();
      ORMMappingBean3 bean3 = new ORMMappingBean3();
      bean3.setId(1);
      bean3.setName("bean2 name1");
      name2List.add(bean3);
      bean3 = new ORMMappingBean3();
      bean3.setId(2);
      bean3.setName("bean2 name2");
      name2List.add(bean3);
      bean1.setName2List(name2List);
      
      List<ORMMappingBean1> expect = new ArrayList<ORMMappingBean1>();
      expect.add(bean1);
      
      Assert.assertEquals(expect.toString(), fetched.toString());
      
      condition = new ORMMappingBean1();
      condition.setId(2);
      
      fetched = dao.fetch(condition);
      
      bean1 = new ORMMappingBean1();
      bean1.setId(2);
      bean1.setName("bean1 name2");
      nameList = new ArrayList<ORMMappingBean2>();
      bean2 = new ORMMappingBean2();
      bean2.setId(3);
      bean2.setName("bean2 name1");
      nameList.add(bean2);
      bean2 = new ORMMappingBean2();
      bean2.setId(4);
      bean2.setName("bean2 name2");
      nameList.add(bean2);
      bean1.setNameList(nameList);
      
      name2List = new ArrayList<ORMMappingBean3>();
      bean3 = new ORMMappingBean3();
      bean3.setId(1);
      bean3.setName("bean2 name1");
      name2List.add(bean3);
      bean3 = new ORMMappingBean3();
      bean3.setId(2);
      bean3.setName("bean2 name2");
      name2List.add(bean3);
      bean1.setName2List(name2List);
      
      expect = new ArrayList<ORMMappingBean1>();
      expect.add(bean1);
      
      Assert.assertEquals(expect.toString(), fetched.toString());
      
      session.endTransaction();
    } finally {
      factory.pushDAOSupportSession(session);
    }
  }
  
  @Test
  public void testDelete() throws Exception {
    DAOSupportSessionFactory factory = DAOSupportSessionFactoryManager
        .getSessionManager().getSessionFactory(factoryId);
    DAOSupportSession session = factory.popDAOSupportSession();
    try {
      DAOSupportManager manager = DAOSupportManager.getInstance();
      manager.setSession(session);
      MappingDao dao = (MappingDao) manager.getDAO(MappingDao.class);
      session.beginTransaction();
      
      ORMMappingBean1 bean1 = new ORMMappingBean1();
      bean1.setId(1);
      dao.delete(bean1);
      
      session.endTransaction();
    } finally {
      factory.pushDAOSupportSession(session);
    }
  }
}
