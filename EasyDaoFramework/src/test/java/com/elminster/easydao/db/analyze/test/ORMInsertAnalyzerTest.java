package com.elminster.easydao.db.analyze.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.ORMInsertAnalyzer;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
import com.elminster.easydao.db.annotation.Column;
import com.elminster.easydao.db.annotation.DAO;
import com.elminster.easydao.db.annotation.Entity;
import com.elminster.easydao.db.annotation.Key;
import com.elminster.easydao.db.annotation.KeyPolicy;

public class ORMInsertAnalyzerTest extends AnalyzeTestBase {

	@Test
	public void testNormalPolicy() throws Exception {
		TestEntry entry = new TestEntry();
		entry.setId(2);
		entry.setName("test");
		entry.setPass("Pass");
		entry.setAccount(100.00d);
		
		Method invokedMethod = ReflectUtil.getDeclaredMethod(TestDAO.class, "insert", new Object[] {entry});
		
		ORMInsertAnalyzer analyzer = new ORMInsertAnalyzer();
		SqlStatementInfo sqlStatementInfo = analyzer.parser(invokedMethod, entry);
		Assert.assertEquals("INSERT INTO T_TEST (id, name, pass, account) VALUES (?, ?, ?, ?)", sqlStatementInfo.getAnalyzedSqlStatement());
		
		List<Object> expected = new ArrayList<Object>();
		expected.add(2);
		expected.add("test");
		expected.add("Pass");
		expected.add(100.00d);
		Assert.assertEquals(expected, sqlStatementInfo.getAnalyzedSqlParameters());
		
		Assert.assertEquals(SqlType.UPDATE, sqlStatementInfo.getAnalyzedSqlType());
	}
	
	@Test
	public void testAutoIncPolicy() throws Exception {
		TestEntryAutoInc entry = new TestEntryAutoInc();
		entry.setId(2);
		entry.setName("test");
		entry.setPass("Pass");
		entry.setAccount(100.00d);
		
		Method invokedMethod = ReflectUtil.getDeclaredMethod(TestDAO.class, "insert", new Object[] {entry});
		
		ORMInsertAnalyzer analyzer = new ORMInsertAnalyzer();
		SqlStatementInfo sqlStatementInfo = analyzer.parser(invokedMethod, entry);
		Assert.assertEquals("INSERT INTO T_TEST (name, pass, account) VALUES (?, ?, ?)", sqlStatementInfo.getAnalyzedSqlStatement());
		
		List<Object> expected = new ArrayList<Object>();
		expected.add("test");
		expected.add("Pass");
		expected.add(100.00d);
		Assert.assertEquals(expected, sqlStatementInfo.getAnalyzedSqlParameters());
		
		Assert.assertEquals(SqlType.UPDATE, sqlStatementInfo.getAnalyzedSqlType());
	}
	@DAO
	interface TestDAO {
		public void insert(TestEntry entry);
	}
	
	@Entity(tableName="T_TEST")
	class TestEntryAutoInc {
		
		@Key(policy=KeyPolicy.AUTO_INC_POLICY)
		@Column(name="id")
		private int id;
		@Column(name="name")
		private String name;
		@Column(name="pass")
		private String pass;
		@Column(name="account")
		private double account;
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPass() {
			return pass;
		}
		public void setPass(String pass) {
			this.pass = pass;
		}
		public double getAccount() {
			return account;
		}
		public void setAccount(double account) {
			this.account = account;
		}
	}
	
	@Entity(tableName="T_TEST")
	class TestEntry {
		
		@Key
		@Column(name="id")
		private Integer id;
		@Column(name="name")
		private String name;
		@Column(name="pass")
		private String pass;
		@Column(name="account")
		private Double account;
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPass() {
			return pass;
		}
		public void setPass(String pass) {
			this.pass = pass;
		}
		public Double getAccount() {
			return account;
		}
		public void setAccount(Double account) {
			this.account = account;
		}
	}
}