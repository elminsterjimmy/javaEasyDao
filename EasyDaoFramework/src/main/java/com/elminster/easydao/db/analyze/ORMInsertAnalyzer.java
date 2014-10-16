package com.elminster.easydao.db.analyze;

import java.lang.reflect.Field;

import com.elminster.common.util.ReflectUtil;
import com.elminster.easydao.db.analyze.data.MappingSqlStatementInfo;
import com.elminster.easydao.db.analyze.data.SqlStatementInfo.SqlType;
import com.elminster.easydao.db.annotation.Mapping;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.id.AutoIncreaseIdGenerator;
import com.elminster.easydao.db.id.IdGenerateException;
import com.elminster.easydao.db.id.KeyPolicy;
import com.elminster.easydao.db.id.NoneIdGenerator;
import com.elminster.easydao.db.manager.DAOSupportSession;
import com.elminster.easydao.db.mapping.IMappingProcessor;
import com.elminster.easydao.db.mapping.MappingException;
import com.elminster.easydao.db.mapping.MappingPolicy;
import com.elminster.easydao.db.mapping.ORMType;

public class ORMInsertAnalyzer extends ORMSqlAnalyzer {

  public ORMInsertAnalyzer(DAOSupportSession session) {
    super(session);
  }

  @Override
  public SqlType getSqlType() {
    return SqlType.UPDATE;
  }

  @Override
  protected void mapping(Object obj) throws AnalyzeException {
    StringBuilder builder = new StringBuilder();

    builder.append("INSERT INTO ");
    builder.append(AnnotationUtil.getTableName(obj));
    builder.append(" (");
    Field[] fields = ReflectUtil.getAllField(obj.getClass());
    boolean first = true;
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      if (AnnotationUtil.isKey(field)) {
        try {
          KeyPolicy keyPolicy = AnnotationUtil.getKeyPolicy(field);
          // auto inc policy is not necessary to set the key
          if (KeyPolicy.AUTO_INC_POLICY == keyPolicy) {
            this.idGenerator = new AutoIncreaseIdGenerator();
            idGenerator.generate(session, obj);
            continue;
          } else {
            if (null == idGenerator) {
              this.idGenerator = new NoneIdGenerator();
              idGenerator.generate(session, obj);
            }
          }
        } catch (IdGenerateException e) {
          throw new AnalyzeException("cannot generate id bacause:" +  e);
        }
      }
      if (AnnotationUtil.isColumn(field)) {
        try {
          Object value = ReflectUtil.getFieldValue(obj, field);
          value = AnnotationUtil.getCustomerDBValue(field, value);
          if (null != value) {
            if (!first) {
              builder.append(", ");
            } else {
              first = false;
            }
            analyzedSqlParameters.add(value);
            builder.append(AnnotationUtil.getColumnName(field, AnnotationUtil.getColumnConverter(obj)));
          }
        } catch (IllegalArgumentException e) {
          throw new AnalyzeException(e);
        } catch (IllegalAccessException e) {
          throw new AnalyzeException(e);
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
    builder.append(") VALUES (");
    for (int i = 0; i < analyzedSqlParameters.size(); i++) {
      if (0 != i) {
        builder.append(", ");
      }
      builder.append("?");
    }
    builder.append(")");
    analyzedSql = builder.toString();
  }

  @Override
  protected ORMType getORMType() {
    return ORMType.ORM_INSERT;
  }
}
