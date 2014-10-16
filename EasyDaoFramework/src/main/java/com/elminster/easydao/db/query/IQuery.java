package com.elminster.easydao.db.query;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.elminster.easydao.db.config.IConfiguration;
import com.elminster.easydao.db.handler.IResultSetHandler;

/**
 * The interface of query.
 * 
 * @author jgu
 * @version 1.0
 */
public interface IQuery {

  void setConfiguration(IConfiguration config);

  boolean testConnection() throws SQLException;

  void commit() throws SQLException;

  void rollback() throws SQLException;

  int sqlExecNoResult(String stm) throws SQLException;

  Long sqlSelectSingleLong(String stm, Long nullReplace) throws SQLException;

  Long sqlSelectSingleLong(String stm, Long nullReplace, Object[] data,
      Calendar cal) throws SQLException;

  Integer sqlSelectSingleInteger(String stm, Integer nullReplace)
      throws SQLException;

  Integer sqlSelectSingleInteger(String stm, Integer nullReplace,
      Object[] data, Calendar cal) throws SQLException;

  String sqlSelectSingleString(String stm, String nullReplace)
      throws SQLException;

  String sqlSelectSingleString(String stm, String nullReplace, Object[] data,
      Calendar cal) throws SQLException;

  List<Map<String, Object>> sqlSelectIntoMapList(String stm)
      throws SQLException;

  List<Map<String, Object>> sqlSelectIntoMapList(String stm, Calendar cal)
      throws SQLException;

  List<Map<String, Object>> sqlSelectIntoMapList(String stm, Object[] data,
      Calendar cal) throws SQLException;

  List<Map<String, Object>> sqlSelectIntoMapList(String stm, Object[] data,
      Calendar cal, int nStart, int nMaxdata) throws SQLException;

  List<Map<String, Object>> sqlSelectIntoMapList(String stm, Object[] data,
      Class<?>[] expectedColumnTypes, Calendar cal, int nStart, int nMaxdata)
      throws SQLException;

  int sqlInsertByBatch(String stm, Object[] data) throws SQLException;

  int sqlInsertByBatch(String stm, Object[] data, Calendar cal)
      throws SQLException;

  String getLastSqlStm();

  List<Map<String, Object>> getResultSetMetaData(String stm)
      throws SQLException;

  void sqlSelectAndCallback(IListRowDataCallback rowCall, Object rowCallArg,
      String stm, Class<?>[] expectedColumnTypes, int nStart, int nMaxdata,
      Object[] data, Calendar cal) throws SQLException;

  void sqlSelectAndCallback(IMapRowDataCallback rowCall, Object rowCallArg,
      String stm, Class<?>[] expectedColumnTypes, int nStart, int nMaxdata,
      Object[] data, Calendar cal) throws SQLException;

  Object sqlSelectAndCallback(IResultSetHandler resultSetHandler, String stm,
      int nStart, int nMaxdata, Object[] data, Calendar cal)
      throws SQLException;
}
