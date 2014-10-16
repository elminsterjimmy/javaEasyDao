package com.elminster.easydao.db.analyze;

import java.lang.reflect.Field;

import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.data.MappingSqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
import com.elminster.easydao.db.annotation.Mapping;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.manager.DAOSupportSession;
import com.elminster.easydao.db.mapping.IMappingProcessor;
import com.elminster.easydao.db.mapping.MappingException;
import com.elminster.easydao.db.mapping.MappingPolicy;
import com.elminster.easydao.db.mapping.ORMType;

public class ORMModifyAnalyzer extends ORMSqlAnalyzer {

  public ORMModifyAnalyzer(DAOSupportSession session) {
    super(session);
  }

  @Override
  public SqlType getSqlType() {
    return SqlType.UPDATE;
  }

  @Override
  protected void mapping(Object obj) throws AnalyzeException {
    StringBuilder builder = new StringBuilder();

    builder.append("UPDATE ");
    builder.append(AnnotationUtil.getTableName(obj));
    builder.append(" SET ");
    Field[] fields = ReflectUtil.getAllField(obj.getClass());
    boolean first = true;
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      if (!AnnotationUtil.isKey(field)) {
        if (AnnotationUtil.isColumn(field)) {
          Object value = null;
          try {
            value = ReflectUtil.getFieldValue(obj, field);
            value = AnnotationUtil.getCustomerDBValue(field, value);
          } catch (IllegalArgumentException e) {
            throw new AnalyzeException(e);
          } catch (IllegalAccessException e) {
            throw new AnalyzeException(e);
          }
          if (null == value) {
            continue;
          }
          if (!first) {
            builder.append(" , ");
          } else {
            first = false;
          }
          builder.append(AnnotationUtil.getColumnName(field, AnnotationUtil.getColumnConverter(obj)));
          builder.append(" = ?");
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
    first = true;
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      if (AnnotationUtil.isKey(field)) {
        if (AnnotationUtil.isColumn(field)) {
          Object value = null;
          try {
            value = ReflectUtil.getFieldValue(obj, field);
          } catch (IllegalArgumentException e) {
            throw new AnalyzeException(e);
          } catch (IllegalAccessException e) {
            throw new AnalyzeException(e);
          }
          if (null == value) {
            continue;
          }
          if (!first) {
            builder.append(" AND ");
          } else {
            builder.append(" WHERE ");
            first = false;
          }
          builder.append(AnnotationUtil.getColumnName(field, AnnotationUtil.getColumnConverter(obj)));
          builder.append(" = ?");
          analyzedSqlParameters.add(value);
        }
      }
    }
    analyzedSql = builder.toString();
  }

  @Override
  protected ORMType getORMType() {
    return ORMType.ORM_UPDATE;
  }
}