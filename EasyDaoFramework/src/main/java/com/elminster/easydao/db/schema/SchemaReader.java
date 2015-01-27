package com.elminster.easydao.db.schema;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elminster.common.util.CollectionUtil;
import com.elminster.easydao.db.handler.IResultSetHandler;
import com.elminster.easydao.db.handler.ListMapResultSetHandler;

public class SchemaReader {

  @SuppressWarnings("unchecked")
  public static ITable getTableMetaData(Connection conn, String catalog, String schemaName, String tableName)
      throws SQLException, CollectSchemaException {
    Table table = new Table(tableName);
    DatabaseMetaData dbmd = conn.getMetaData();
    ResultSet rs = null;
    // get table columns
    try {
      rs = dbmd.getColumns(catalog, schemaName, tableName, null);
      try {
        IResultSetHandler handler = new ListMapResultSetHandler();
        List<Map<String, Object>> listMap = (List<Map<String, Object>>) handler.handleResultSet(rs);
        for (Map<String, Object> map : listMap) {
          String columnName = (String) map.get("COLUMN_NAME");
          int columnType = (Integer) map.get("DATA_TYPE");
          int nullable = (Integer) map.get("NULLABLE");
          int columnSize = (Integer) map.get("COLUMN_SIZE");
          Integer precision = (Integer) map.get("DECIMAL_DIGITS");
          Column column = new Column(columnName, columnType);
          column.setNullable(1 == nullable);
          column.setMaxLength(columnSize);
          if (null != precision) {
            column.setPrecision(precision);
          }
          table.addColumn(column);
        }
      } catch (Exception e) {
        throw new CollectSchemaException(e);
      }
    } finally {
      if (null != rs) {
        rs.close();
      }
    }
    // get table primary keys
    try {
      rs = dbmd.getPrimaryKeys(catalog, schemaName, tableName);
      try {
        IResultSetHandler handler = new ListMapResultSetHandler();
        List<Map<String, Object>> listMap = (List<Map<String, Object>>) handler.handleResultSet(rs);
        IColumn[] pkColumns = new Column[listMap.size()];
        int i = 0;
        for (Map<String, Object> map : listMap) {
          String columnName = (String) map.get("COLUMN_NAME");
          pkColumns[i++] = table.getColumn(columnName);
        }
        table.setPrimaryKeys(pkColumns);
      } catch (Exception e) {
        throw new CollectSchemaException(e);
      }
    } finally {
      if (null != rs) {
        rs.close();
      }
    }
    // get foreign keys
    try {
      rs = dbmd.getImportedKeys(catalog, schemaName, tableName);
      try {
        IResultSetHandler handler = new ListMapResultSetHandler();
        List<Map<String, Object>> listMap = (List<Map<String, Object>>) handler.handleResultSet(rs);
        for (Map<String, Object> map : listMap) {
          String fkTableName = (String) map.get("FKTABLE_NAME");
          String fkColumnName = (String) map.get("FKCOLUMN_NAME");
          String pkTableName = (String) map.get("PKTABLE_NAME");
          String pkColumnName = (String) map.get("PKCOLUMN_NAME");
          String fkName = (String) map.get("FK_NAME");
          String pkName = (String) map.get("FK_NAME");
          ForeignKey fk = new ForeignKey();
          fk.setFkName(fkName);
          fk.setForeignKeyColumnName(fkColumnName);
          fk.setForeignKeyTableName(fkTableName);
          fk.setPkName(pkName);
          fk.setPrimaryKeyColumnName(pkColumnName);
          fk.setPrimaryKeyTableName(pkTableName);
          Column column = (Column) table.getColumn(fkColumnName);
          column.setForeignKey(fk);
        }
      } catch (Exception e) {
        throw new CollectSchemaException(e);
      }
    } finally {
      if (null != rs) {
        rs.close();
      }
    }

    // get indexs
    try {
      rs = dbmd.getIndexInfo(catalog, schemaName, tableName, false, false);
      try {
        IResultSetHandler handler = new ListMapResultSetHandler();
        List<Map<String, Object>> listMap = (List<Map<String, Object>>) handler.handleResultSet(rs);
        Map<String, List<IColumn>> indexMap = new HashMap<String, List<IColumn>>();
        Map<String, Boolean> indexUniqueMap = new HashMap<String, Boolean>();
        for (Map<String, Object> map : listMap) {
          String indexName = (String) map.get("INDEX_NAME");
          String columnName = (String) map.get("COLUMN_NAME");
          boolean nonUnique = (Boolean) map.get("NON_UNIQUE");
          if (null != indexName) {
            List<IColumn> columnList = indexMap.get(indexName);
            if (null == columnList) {
              columnList = new ArrayList<IColumn>();
            }
            IColumn column = table.getColumn(columnName);
            columnList.add(column);
            indexMap.put(indexName, columnList);
            indexUniqueMap.put(indexName, !nonUnique);
          }
        }
        for (String key : indexMap.keySet()) {
          List<IColumn> columns = indexMap.get(key);
          Boolean unique = indexUniqueMap.get(key);
          Index index = new Index(key, unique.booleanValue(), (IColumn[]) CollectionUtil.collection2Array(columns));
          table.addIndex(index);
        }
      } catch (Exception e) {
        throw new CollectSchemaException(e);
      }
    } finally {
      if (null != rs) {
        rs.close();
      }
    }
    return table;
  }

  public static ITable getTableMetaData(Connection conn, String schemaName, String tableName) throws SQLException,
      CollectSchemaException {
    return getTableMetaData(conn, null, schemaName, tableName);
  }

  public static ITable getTableMetaData(Connection conn, String tableName) throws SQLException, CollectSchemaException {
    return getTableMetaData(conn, null, null, tableName);
  }
}
