package com.elminster.easydao.db.id;

import com.elminster.easydao.db.analyze.data.SqlStatementInfo;

public class IdGeneratorHelper {

  public static IdGenerator toSelectInsertGenerator(IdGenerator idGenerator,
      SqlStatementInfo sqlSelectionInfo) {
    IdGenerator selectInsertGenerator = new SelectInsertIdGenerator(
        idGenerator, sqlSelectionInfo);
    return selectInsertGenerator;
  }
}
