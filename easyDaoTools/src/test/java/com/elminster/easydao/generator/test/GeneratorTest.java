package com.elminster.easydao.generator.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import com.elminster.common.constants.Constants.StringConstants;
import com.elminster.common.constants.RegexConstants;
import com.elminster.common.util.FileUtil;
import com.elminster.easydao.data.ClassData;
import com.elminster.easydao.db.ds.DataSourceFactory;
import com.elminster.easydao.db.schema.CollectSchemaException;
import com.elminster.easydao.db.schema.ITable;
import com.elminster.easydao.db.schema.SchemaReader;
import com.elminster.easydao.exception.UnknownTypeException;
import com.elminster.easydao.generator.DAOGenerator;
import com.elminster.easydao.generator.DTOGenerator;
import com.elminster.easydao.generator.EntityGenerator;
import com.elminster.easydao.generator.ServiceGenerator;

public class GeneratorTest {
  
  static DataSource ds;
  
  static final String TEST_GENERATE = "src/test/generated/";
  
  @BeforeClass
  public static void setup() throws IOException {
    DataSourceFactory dsFactory = DataSourceFactory.INSTANCE;
    Properties properties = new Properties();
    properties.load(GeneratorTest.class.getResourceAsStream("/DataSource.properties"));
    ds = dsFactory.getDataSource(properties);
  }
  
  @Test
  public void testGenerate() throws SQLException, CollectSchemaException, UnknownTypeException, IOException {
    String basePackage = "com.elminster.easydao.tools.test";
    String dbTableName = "testtable";
    List<ClassData> classDatas = new ArrayList<ClassData>();
    String entityPackage = basePackage + ".entity";
    String daoPackage = basePackage + ".dao";
    String dtoPackage = basePackage + ".dto";
    String serviceIfPackage = basePackage + ".serviceIf";
    String serviceImplPackage = basePackage + ".service";
    String exceptionPackage = basePackage + ".exception";
    EntityGenerator entityGenerator = new EntityGenerator();
    DAOGenerator daoGenerator = new DAOGenerator();
    ClassData baseDAOClassData = daoGenerator.geterateBaseDAO(daoPackage);
    classDatas.add(baseDAOClassData);
    ServiceGenerator serviceGenerator = new ServiceGenerator();
    ClassData serviceExceptionClassData = serviceGenerator.getnerateServiceException(exceptionPackage);
    classDatas.add(serviceExceptionClassData);
    ClassData baseServiceClassData = serviceGenerator.generateBaseService(serviceExceptionClassData, serviceIfPackage);
    classDatas.add(baseServiceClassData);
    DTOGenerator dtoGenerator = new DTOGenerator();
    ITable table = SchemaReader.getTableMetaData(ds.getConnection(), dbTableName);
    ClassData entityClassData = entityGenerator.generatEntity(table, entityPackage);
    classDatas.add(entityClassData);
    ClassData daoClassData = daoGenerator.generatDAO(entityClassData, daoPackage);
    classDatas.add(daoClassData);
    ClassData dtoClassData = dtoGenerator.generateDTO(entityClassData, dtoPackage);
    classDatas.add(dtoClassData);
    ClassData serviceIfClassData = serviceGenerator.generateServiceIf(dtoClassData, serviceIfPackage);
    classDatas.add(serviceIfClassData);
    ClassData serviceImplClassData = serviceGenerator.generateServiceImpl(entityClassData, dtoClassData,
        daoClassData, serviceExceptionClassData, serviceIfClassData, serviceImplPackage);
    classDatas.add(serviceImplClassData);
    for (ClassData classData: classDatas) {
      writeClass(classData);
    }
  }

  private void writeClass(ClassData classData) throws IOException {
    String path = classData.getFullName();
    path = TEST_GENERATE + path.replaceAll(RegexConstants.REGEX_DOT, StringConstants.SLASH) + ".java";
    FileUtil.createFolder(path);
    FileUtil.write2file(classData.getContent().getBytes(), path);
  }
}
