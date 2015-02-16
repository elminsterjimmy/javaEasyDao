# javaEasyDao
Summary
=
Easy DAO Framework is a lightweight DAO(Data Access Object) framework what coded in pure JAVA for idea of easy is good, simple is nice.
It takes the role of implementing a DAO interface automatically for you and supports a SQL with variable oriented function and simple ORM function, that makes it lightweight and easy to use. You just define a DAO layer interface without implementing it, let easy DAO framework implement it for you.
Feature
=
 - **One stop DAO implementation.** 
 - **SQL separation.**
 - **Easy but sufficient ORM.**
 - **Thread level transaction support.**
 - **Component hot plug support.**
 - **Source auto generate support.**
  - by using easydao generate tool (maven plugin)

Getting Started
=
**Firstly**, create your own DAO interface like below:

    package com.elminster.easydao.test;
    
    import java.util.List;
    
    import com.elminster.easydao.db.analyze.data.PagedData;
    import com.elminster.easydao.db.annotation.DAO;
    import com.elminster.easydao.db.annotation.Sql;
    import com.elminster.easydao.db.annotation.SqlFile;
    import com.elminster.easydao.db.annotation.SqlParam;

    @DAO
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
    }

You can see many annotations in this declared interface. Let's have a look at these annotations.

 - @DAO : This annotation indicates that the marked interface is a DAO interface and it will be automatically implement by Easy DAO Framework.
 - @Sql : This annotation indicates that the marked method using specified SQL statement to execute.
 - @SqlFile : This annotation indicates that the marked method using the SQL statement in specified location in the classpath to execute.
 - @SqlParam : This annotation indicates that the marked argument is the entity of specified variable which started with a $ in the external SQL( whether declared in @Sql or @SqlFile).
  - You can also using this annotation at a Bean type class, and use a . to specified its member.

Then, you can see 4 methods which named fetch, insert, update, delete. These methods are using a simple ORM implementation in Easy DAO Framework. If you wanna use a ORM fuction in Easy DAO Framework, using the method name below in the DAO interface.

<table>
<th>
<td>Fuction	</td>
<td>Method Name</td>
</th>
<tr>
<td/>
<td>Insert</td>
<td>insert, add</td>
</tr>
<tr>
<td/>
<td>Update</td>
<td>update, modify, change</td>
</tr>
<tr>
<td/>
<td>Delete</td>
<td>delete, remove</td>
</tr>
<tr>
<td/>
<td>Select</td>
<td>select, fetch</td>
</tr>
</table>

Let's have a glance at some SQL files which is mentioned above:

**getAccountByUserName.sql**

    SELECT 
      account
    FROM 
      testtable
    WHERE 
      name = $user
      
**getAccountByBean.sql**

    SELECT 
      account
    FROM 
      testtable
    WHERE 
      name = $bean.name and
      tel = $bean.tel
        
**getAccountByCondition.sql**

     SELECT 
       account
     FROM 
       testtable
     WHERE
       --if condition > 0 then
         id = 1
       --else
         id = 2
       --endif
                  
 **getAccountByBeanCondition.sql**

    SELECT 
      account
    FROM 
      testtable
    WHERE
      --if bean.name = "user1" then
        id = 1
      --else
        id = 2
      --endif
      
**Secondly**, create bean class like below:

    package com.elminster.easydao.test;

    import java.sql.Date;
    import java.util.List;

    import com.elminster.common.util.ObjectUtil;
    import com.elminster.easydao.db.annotation.Column;
    import com.elminster.easydao.db.annotation.CustomerType;
    import com.elminster.easydao.db.annotation.Entity;
    import com.elminster.easydao.db.annotation.Key;
    import com.elminster.easydao.db.annotation.KeyPolicy;

    @Entity(tableName = "testtable")
    public class ORMBean {

      @Key(policy = KeyPolicy.AUTO_INC_POLICY)
      @Column(name = "id")
      private Integer id;
      @Column(name = "name")
      private String name;
      @Column(name = "pass")
      private String pass;
      @Column(name = "account")
      private Double account;
      @Column(name = "quary")
      private Integer quary;
      @CustomerType(className = com.elminster.easydao.test.SplitListConverter.class)
      @Column(name = "tel")
      private List<Integer> tel;
    
       /**
       * @return the tel
       */
      public List<Integer> getTel() {
        return tel;
      }

      /**
       * @param tel
       *          the tel to set
       */
      public void setTel(List<Integer> tel) {
        this.tel = tel;
      }

      @Column(name = "last_update")
      private Date lastUpdate;

      /**
       * @return the id
       */
      public Integer getId() {
        return id;
      }
    
      /**
       * @param id
       *          the id to set
       */
      public void setId(Integer id) {
        this.id = id;
      }

      /**
       * @return the name
       */
      public String getName() {
        return name;
      }
    
      /**
       * @param name
       *          the name to set
       */
      public void setName(String name) {
        this.name = name;
      }
    
      /**
       * @return the pass
       */
      public String getPass() {
        return pass;
      }

      /**
       * @param pass
       *          the pass to set
       */
      public void setPass(String pass) {
        this.pass = pass;
      }
    
      /**
       * @return the account
       */
      public Double getAccount() {
        return account;
      }
    
      /**
       * @param account
       *          the account to set
       */
      public void setAccount(Double account) {
        this.account = account;
      }
        
      /**
       * @return the quary
       */
      public Integer getQuary() {
        return quary;
      }
    
      /**
       * @param quary
       *          the quary to set
       */
      public void setQuary(Integer quary) {
        this.quary = quary;
      }

          /**
       * @return the lastUpdate
       */
      public Date getLastUpdate() {
        return lastUpdate;
      }

      /**
       * @param lastUpdate
       *          the lastUpdate to set
       */
      public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
      }
    
      public String toString() {
        return ObjectUtil.buildToStringByReflect(this);
      }
    }

Then, let's look into the declared annotation:

- @Entity : This annotation indicates the actual table name with it's attribute tableName.
- @Column : This annotation indicates the actual column name with it's attribute name.
- @Key : This annotation indicates this column is a key.
- @Key also has an attribute named policy. As default, it is set to KeyPolicy.NORMAL_POLICY as it omit, if it set to KeyPolicy.AUTO_INC_POLICY means when executing an Insert-SQL statement the key column is set by native database itself. Ex:
  - When policy is set to KeyPolicy.NORMAL_POLICY or omit the SQL statement will be INSERT INTO testtable (id, name, pass, account, quary, last_update) VALUES (?, ?, ?, ?, ?, ?)
  - When policy is set to KeyPolicy.AUTO_INC_POLICY the SQL statement will be INSERT INTO testtable (name, pass, account, quary, last_update) VALUES (?, ?, ?, ?, ?) *1

*1: Notice that there is NO id column in the SQL statement.
*PS：If you need only one return column in a quary, you can ignore the Step2 and just use the return column's type instead of it directly.

**Thirdly** and finally, use Easy DAO Framework to automatically implement your DAO interface like below:

	package com.elminster.easydao.test;

	import java.util.ArrayList;	
	import java.util.Date;
	import java.util.List;
	import java.util.Properties;

	import javax.sql.DataSource;

	import org.apache.log4j.xml.DOMConfigurator;
	import org.junit.AfterClass;
	import org.junit.Assert;
	import org.junit.BeforeClass;
	import org.junit.Test;

	import com.elminster.common.constants.Constants.StringConstants;
	import com.elminster.common.util.FileUtil;
	import com.elminster.common.util.StringUtil;
	import com.elminster.easydao.db.analyze.data.PagedData;
	import com.elminster.easydao.db.ds.DataSourceFactory;
	import com.elminster.easydao.db.manager.DAOSupportManager;
	import com.elminster.easydao.db.manager.DAOSupportSession;
	import com.elminster.easydao.db.manager.DAOSupportSessionFactory;
	import com.elminster.easydao.db.manager.DAOSupportSessionFactoryManager;
	import com.elminster.easydao.db.query.IQuery;

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
	  public static void finalized() {
		DAOSupportSessionFactoryManager.getSessionManager()
			.getSessionFactory(factoryId).shutdown();
	  }

	  @Test
	  public void testORMInsert() throws Exception {
		DAOSupportSessionFactory factory = DAOSupportSessionFactoryManager
			.getSessionManager().getSessionFactory(factoryId);
		DAOSupportSession session = factory.popDAOSupportSession();
		try {
		  DAOSupportManager manager = DAOSupportManager.INSTANCE;
		  TestDao dao = (TestDao) manager.getDAO(TestDao.class);
		  session.beginTransaction();
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
		  session.endTransaction();
		} finally {
		  factory.pushDAOSupportSession(session);
		}
	  }

	  @Test
	  public void testORMFetch() throws Exception {
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
	  public void testORMUpdate() throws Exception {
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
	  public void testORMDelete() throws Exception {
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
	  public void testSqlStatement() throws Exception {
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
	  public void testSqlFile() throws Exception {
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
	  public void testExpression1() throws Exception {
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
	  public void testExpression2() throws Exception {
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
**Enjoy IT!**

