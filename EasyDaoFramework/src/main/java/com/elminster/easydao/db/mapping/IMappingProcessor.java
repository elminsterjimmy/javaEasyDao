package com.elminster.easydao.db.mapping;

import java.lang.reflect.Field;

import com.elminster.easydao.db.analyze.data.MappingSqlStatementInfo;
import com.elminster.easydao.db.manager.DAOSupportSession;

public interface IMappingProcessor {

  MappingSqlStatementInfo processMapping(DAOSupportSession session, Field mappingField, Object obj) throws MappingException;
}
