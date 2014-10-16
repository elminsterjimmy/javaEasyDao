package com.elminster.easydao.db.analyze;

import java.lang.reflect.Field;

import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.data.MappingSqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
import com.elminster.easydao.db.annotation.Mapping;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.constants.SqlConstants;
import com.elminster.easydao.db.manager.DAOSupportSession;
import com.elminster.easydao.db.mapping.IMappingProcessor;
import com.elminster.easydao.db.mapping.MappingException;
import com.elminster.easydao.db.mapping.MappingPolicy;
import com.elminster.easydao.db.mapping.ORMType;

public class ORMFetchAnalyzer extends ORMSqlAnalyzer {

  public ORMFetchAnalyzer(DAOSupportSession session) {
    super(session);
  }

  @Override
  public SqlType getSqlType() {
    return SqlType.QUERY;
  }

  @Override
  protected void mapping(Object obj) throws AnalyzeException {
    StringBuilder builder = new StringBuilder();
    StringBuilder whereClause = new StringBuilder();

    builder.append("SELECT ");
    Field[] fields = ReflectUtil.getAllField(obj.getClass());
    boolean firstValue = true;
    boolean firstKey = true;
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      if (AnnotationUtil.isColumn(field)) {
        if (firstKey) {
          firstKey = false;
        } else {
          builder.append(", ");
        }
        String columnName = AnnotationUtil.getColumnName(field, AnnotationUtil.getColumnConverter(obj));
        builder.append(columnName);

        Object value = null;
        try {
          value = ReflectUtil.getFieldValue(obj, field);
          value = AnnotationUtil.getCustomerDBValue(field, value);
        } catch (IllegalArgumentException e) {
          throw new AnalyzeException(e);
        } catch (IllegalAccessException e) {
          throw new AnalyzeException(e);
        }
        if (null != value) {
          if (firstValue) {
            whereClause.append(" WHERE ");
            firstValue = false;
          } else {
            whereClause.append(" AND ");
          }
          if (null != value) {
            whereClause.append(columnName);
            if (value instanceof String) {
              String s = (String) value;
              if (s.contains(SqlConstants.LIKE)) {
                whereClause.append(" like ?");
                value = s.replaceAll(SqlConstants.LIKE, "%");
              } else if (s.contains(SqlConstants.NOT)) {
                whereClause.append(" <> ?");
                value = s.replace(SqlConstants.NOT, "");
              } else {
                whereClause.append(" = ?");
              }
            } else {
              whereClause.append(" = ?");
            }
          }
          analyzedSqlParameters.add(value);
        }
      } else if (AnnotationUtil.isMapping(field)) {
        this.mapping = true;
        Mapping mapping = field.getAnnotation(Mapping.class);
        MappingPolicy mappingPolicy = mapping.mappingPolicy();
        this.mappingPolicy = mappingPolicy;
        IMappingProcessor mappingProcessor = this.mappingProcessorFactory.getMappingProcessor(
            mapping.mappingPolicy(), this.getORMType());
        try {
          MappingSqlStatementInfo mappingSqlStatementInfo = mappingProcessor.processMapping(session, field, obj);
          this.addMappingSqlStatementInfo(mappingSqlStatementInfo);
        } catch (MappingException e) {
          throw new AnalyzeException("cannot mapping bacause:" +  e);
        }
      }
    }
    builder.append(" FROM ");
    builder.append(AnnotationUtil.getTableName(obj));
    builder.append(whereClause.toString());
    analyzedSql = builder.toString();
  }

  @Override
  protected ORMType getORMType() {
    return ORMType.ORM_FETCH;
  }
}
