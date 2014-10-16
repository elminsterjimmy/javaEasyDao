package com.elminster.easydao.db.id;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.elminster.common.util.CollectionUtil;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.manager.DAOSupportSession;
import com.elminster.easydao.db.query.IQuery;

public class SelectInsertIdGenerator implements ISelectInsertIdGenerator {
  
  private IdGenerator idGenerator;
  
  private SqlStatementInfo sqlSelectionInfo;
  
  public SelectInsertIdGenerator(IdGenerator idGenerator, SqlStatementInfo sqlSelectionInfo) {
    this.idGenerator = idGenerator;
    this.sqlSelectionInfo = sqlSelectionInfo;
  }
  
  @Override
  public Serializable[] selectId(DAOSupportSession session)
      throws IdGenerateException {
    Serializable[] ids = null;
    IQuery query = session.getQuery();
    List<Map<String, Object>> mapList;
    try {
      mapList = query.sqlSelectIntoMapList(
          sqlSelectionInfo.getAnalyzedSqlStatement(),
          sqlSelectionInfo.getAnalyzedSqlParameters().toArray(new Object[0]),
          Calendar.getInstance());
    
      if (CollectionUtil.isNotEmpty(mapList)) {
        Map<String, Object> map = mapList.get(0);
        Collection<Object> values = map.values();
        ids = new Serializable[values.size()];
        values.toArray(ids);
      }
    } catch (SQLException e) {
      throw new IdGenerateException(e);
    }
    return ids;
  }



  @Override
  public Serializable[] generate(DAOSupportSession session, Object obj)
      throws IdGenerateException {
    return idGenerator.generate(session, obj);
  }



  @Override
  public Serializable[] lastId() throws IdGenerateException {
    return idGenerator.lastId();
  }

}
