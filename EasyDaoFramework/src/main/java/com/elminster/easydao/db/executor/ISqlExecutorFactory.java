package com.elminster.easydao.db.executor;

import com.elminster.easydao.db.analyze.data.SqlStatementInfo;
import com.elminster.easydao.db.manager.DAOSupportSession;

public interface ISqlExecutorFactory {

	public ISqlExecutor getSQLExecutor(SqlStatementInfo sqlStatementInfo, DAOSupportSession session);
}
