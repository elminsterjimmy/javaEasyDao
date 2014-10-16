package com.elminster.easydao.db.dialect;

public class Oracle9iDialect extends Oracle8iDialect {

  @Override
  public String getLimitSql(String sql, boolean hasOffset) {
    sql = sql.trim();
    boolean isForUpdate = false;
    if (sql.toLowerCase().endsWith(" for update")) {
      sql = sql.substring(0, sql.length() - 11);
      isForUpdate = true;
    }

    StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
    if (hasOffset) {
      pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
    } else {
      pagingSelect.append("select * from ( ");
    }
    pagingSelect.append(sql);
    if (hasOffset) {
      pagingSelect.append(" ) row_ where rownum <= ?) where rownum_ > ?");
    } else {
      pagingSelect.append(" ) where rownum <= ?");
    }

    if (isForUpdate) {
      pagingSelect.append(" for update");
    }

    return pagingSelect.toString();
  }

}
