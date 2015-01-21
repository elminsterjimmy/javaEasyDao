package com.elminster.easydao.db.util;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class JavaSqlTypeMap {

  public static final Map<Integer, Class<?>> JAVA_SQL_MAPPING = new HashMap<Integer, Class<?>>();
  
  static {
    JAVA_SQL_MAPPING.put(Types.CHAR, String.class);
    JAVA_SQL_MAPPING.put(Types.VARCHAR, String.class);
    JAVA_SQL_MAPPING.put(Types.LONGNVARCHAR, String.class);
    JAVA_SQL_MAPPING.put(Types.NUMERIC, BigDecimal.class);
    JAVA_SQL_MAPPING.put(Types.DECIMAL, BigDecimal.class);
    JAVA_SQL_MAPPING.put(Types.BIT, Boolean.class);
    JAVA_SQL_MAPPING.put(Types.TINYINT, Byte.class);
    JAVA_SQL_MAPPING.put(Types.SMALLINT, Short.class);
    JAVA_SQL_MAPPING.put(Types.INTEGER, Integer.class);
    JAVA_SQL_MAPPING.put(Types.BIGINT, Long.class);
    JAVA_SQL_MAPPING.put(Types.REAL, Float.class);
    JAVA_SQL_MAPPING.put(Types.FLOAT, Double.class);
    JAVA_SQL_MAPPING.put(Types.DOUBLE, Double.class);
    JAVA_SQL_MAPPING.put(Types.BINARY, Byte[].class);
    JAVA_SQL_MAPPING.put(Types.VARBINARY, Byte[].class);
    JAVA_SQL_MAPPING.put(Types.LONGVARBINARY, Byte[].class);
    JAVA_SQL_MAPPING.put(Types.DATE, Date.class);
    JAVA_SQL_MAPPING.put(Types.TIME, Time.class);
    JAVA_SQL_MAPPING.put(Types.TIMESTAMP, Timestamp.class);
    JAVA_SQL_MAPPING.put(Types.BLOB, Blob.class);
    JAVA_SQL_MAPPING.put(Types.CLOB, Clob.class);
    JAVA_SQL_MAPPING.put(Types.STRUCT, Struct.class);
    JAVA_SQL_MAPPING.put(Types.REF, Ref.class);
    JAVA_SQL_MAPPING.put(Types.ARRAY, Array.class);
    JAVA_SQL_MAPPING.put(Types.BOOLEAN, Boolean.class);
    JAVA_SQL_MAPPING.put(Types.NCHAR, String.class);
    JAVA_SQL_MAPPING.put(Types.NVARCHAR, String.class);
    JAVA_SQL_MAPPING.put(Types.NCLOB, Clob.class);
    JAVA_SQL_MAPPING.put(Types.LONGVARCHAR, String.class);
  }
}
