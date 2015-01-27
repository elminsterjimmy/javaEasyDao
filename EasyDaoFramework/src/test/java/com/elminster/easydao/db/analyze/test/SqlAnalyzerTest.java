package com.elminster.easydao.db.analyze.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.DefaultSqlAnalyzer;
import com.elminster.easydao.db.analyze.ISqlAnalyzer;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.annotation.DAO;
import com.elminster.easydao.db.annotation.Sql;
import com.elminster.easydao.db.annotation.SqlParam;

public class SqlAnalyzerTest extends AnalyzeTestBase {

	@Test
	public void testAnalyzedSql1() throws Exception {

		Object[] methodArguments = new Object[] {"testId", "testS"};
		Method method = ReflectUtil.getDeclaredMethod(TestDAO.class, "getName", methodArguments);
		ISqlAnalyzer analyzer = new DefaultSqlAnalyzer();
		SqlStatementInfo sqlStatementInfo = analyzer.parser(method, methodArguments);
		String analyzedSql = sqlStatementInfo.getAnalyzedSqlStatement();
		List<Object> analyzedSqlParameters = sqlStatementInfo.getAnalyzedSqlParameters();
		Assert.assertEquals("select * from test_table where test1 = ? and test2=?", analyzedSql);
		List<Object> expectedSqlParameters = new ArrayList<Object>();
		expectedSqlParameters.add("testId");
		expectedSqlParameters.add("testS");
		Assert.assertEquals(expectedSqlParameters, analyzedSqlParameters);
	}
	
	@Test
	public void testAnalyzedSql2() throws Exception {
		TestEntry entry = new TestEntry();
		entry.setTest1("entry test1");
		entry.setTest2(2);
		
		Object[] methodArguments = new Object[] {entry};
		Method method = ReflectUtil.getDeclaredMethod(TestDAO.class, "getId", methodArguments);

		ISqlAnalyzer analyzer = new DefaultSqlAnalyzer();
		SqlStatementInfo sqlStatementInfo = analyzer.parser(method, methodArguments);
		String analyzedSql = sqlStatementInfo.getAnalyzedSqlStatement();
		List<Object> analyzedSqlParameters = sqlStatementInfo.getAnalyzedSqlParameters();
		Assert.assertEquals("select * from test_table where test1 = ? and test2=?", analyzedSql);
		List<Object> expectedSqlParameters = new ArrayList<Object>();
		expectedSqlParameters.add("entry test1");
		expectedSqlParameters.add(2);
		Assert.assertEquals(expectedSqlParameters, analyzedSqlParameters);
	}
	
	@DAO
	public interface TestDAO {

		@Sql(value = "select * from test_table where test1 = $test1 and test2=$test2")
		public String getName(@SqlParam("test1")String id, @SqlParam("test2")String s);
		
		@Sql(value = "select * from test_table where test1 = $info.test1 and test2=$info.test2")
		public String getId(@SqlParam("info")TestEntry entry);
	}

	public class TestEntry {
		private String test1;
		private int test2;
		
		public String getTest1() {
			return test1;
		}
		public void setTest1(String test1) {
			this.test1 = test1;
		}
		public int getTest2() {
			return test2;
		}
		public void setTest2(int test2) {
			this.test2 = test2;
		}
	}
}
