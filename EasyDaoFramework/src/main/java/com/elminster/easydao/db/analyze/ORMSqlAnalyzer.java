package com.elminster.easydao.db.analyze;

import java.lang.reflect.Method;

import com.elminster.easydao.db.annotation.Entity;
import com.elminster.easydao.db.manager.DAOSupportSession;
import com.elminster.easydao.db.mapping.MappingProcessorFactory;
import com.elminster.easydao.db.mapping.ORMType;

abstract public class ORMSqlAnalyzer extends BaseSqlAnalyzer implements
    ISqlAnalyzer {
  
  protected MappingProcessorFactory mappingProcessorFactory = MappingProcessorFactory.getInstance();

  public ORMSqlAnalyzer(DAOSupportSession session) {
    super(session);
  }

  @Override
  public void analyzeSql(Method invokedMethod, Object... methodArguments) throws AnalyzeException {
    boolean hasEntry = false;
    for (Object obj : methodArguments) {
      if (null == obj.getClass().getAnnotation(Entity.class)) {
        continue;
      }
      hasEntry = true;
      mapping(obj);
      break;
    }
    if (!hasEntry) {
      throw new AnalyzeException("No Entiry defined.");
    }
  }

  abstract protected void mapping(Object obj) throws AnalyzeException;
  abstract protected ORMType getORMType();
}
