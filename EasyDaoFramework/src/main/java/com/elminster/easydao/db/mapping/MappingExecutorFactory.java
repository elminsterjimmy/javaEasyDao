package com.elminster.easydao.db.mapping;

import com.elminster.easydao.db.executor.ISqlExecutor;
import com.elminster.easydao.db.manager.DAOSupportSession;

public class MappingExecutorFactory {

  private static MappingExecutorFactory instance = new MappingExecutorFactory();

  private MappingExecutorFactory() {
  }

  public static MappingExecutorFactory getInstance() {
    return instance;
  }

  public IMappingExecutor getMappingExecutor(MappingPolicy mappingPolicy,
      ISqlExecutor executor, DAOSupportSession session) {
    IMappingExecutor mappingExecutor = null;
    switch (mappingPolicy) {
    case INTERMEDIARY_POLICY:
      mappingExecutor = new IntermediaryMappingExecutor(executor, session);
      break;
    case DIRECT_POLICY:
      
    }
    return mappingExecutor;
  }
}
