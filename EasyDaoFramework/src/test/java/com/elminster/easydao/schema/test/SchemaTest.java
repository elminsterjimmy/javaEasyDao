package com.elminster.easydao.schema.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.elminster.easydao.db.schema.CollectSchemaException;
import com.elminster.easydao.db.schema.ITable;
import com.elminster.easydao.db.schema.SchemaReader;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class SchemaTest {

  @Test
  public void testGetTableMetaData() throws SQLException, CollectSchemaException {
    MysqlDataSource ds = new MysqlDataSource();
    ds.setUrl("jdbc:mysql://localhost:3306/test");
    ds.setUser("root");
    ds.setPassword("root");
    Connection conn = ds.getConnection();
    ITable table = SchemaReader.getTableMetaData(conn, "t_game_info");
    System.out.println(table);
  }
}
