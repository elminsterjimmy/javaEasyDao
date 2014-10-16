package com.elminster.easydao.db.mapping;


public class MappingProcessorFactory {
  
  private static MappingProcessorFactory instance = new MappingProcessorFactory();
  
  private MappingProcessorFactory() {}
  
  public static MappingProcessorFactory getInstance() {
    return instance;
  }

  public IMappingProcessor getMappingProcessor(MappingPolicy mappingPolicy, ORMType ormType) {
    IMappingProcessor mappingProcessor = null;
    switch (mappingPolicy) {
      case INTERMEDIARY_POLICY:
        switch (ormType) {
          case ORM_FETCH:
            mappingProcessor = new IntermediaryMappingFetchProcessor();
            break;
          case ORM_INSERT:
            mappingProcessor = new IntermediaryMappingInsertProcessor();
            break;
          case ORM_DELETE:
            mappingProcessor = new IntermediaryMappingDeleteProcessor();
            break;
        }
      case DIRECT_POLICY:
        switch (ormType) {
        case ORM_FETCH:
          mappingProcessor = new DirectMappingFetchProcessor();
          break;
        case ORM_INSERT:
          mappingProcessor = new DirectMappingInsertProcessor();
          break;
        case ORM_DELETE:
          mappingProcessor = new DirectMappingDeleteProcessor();
          break;
      }
      break;
    }
    return mappingProcessor;
  }
}
