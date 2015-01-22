package com.elminster.easydao.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.elminster.common.constants.Constants.StringConstants;
import com.elminster.common.constants.RegexConstants;
import com.elminster.common.util.CollectionUtil;
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

/**
 * The source generate mojo for EasyDAO framework.
 * 
 * @author jgu
 * @version 1.0
 */
@Mojo(name="easyDAOSourceGenerator")
public class SourceGeneratorMojo extends AbstractMojo {

  private static final DataSourceFactory dsFactory = DataSourceFactory.INSTANCE;

  @Parameter
  private String srcDirectory;

  @Parameter
  private String basePackage;

  @Parameter
  private List<String> dbTableNames;

  @Parameter
  private boolean overrideExist;

  @Parameter
  private String propertiesName;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    DataSource ds = getDataSource();
    if (CollectionUtil.isNotEmpty(dbTableNames)) {
      String entityPackage = basePackage + ".entity";
      String daoPackage = basePackage + ".dao";
      String dtoPackage = basePackage + ".dto";
      String serviceIfPackage = basePackage + ".serviceIf";
      String serviceImplPackage = basePackage + ".service";
      String exceptionPackage = basePackage + ".exception";
      List<ClassData> classDatas = new ArrayList<ClassData>();
      EntityGenerator entityGenerator = new EntityGenerator();
      DAOGenerator daoGenerator = new DAOGenerator();
      ClassData baseDAOClassData = daoGenerator.geterateBaseDAO(daoPackage);
      classDatas.add(baseDAOClassData);
      ServiceGenerator serviceGenerator = new ServiceGenerator();
      ClassData serviceExceptionClassData = serviceGenerator.getnerateServiceException(exceptionPackage);
      classDatas.add(serviceExceptionClassData);
      ClassData baseServiceClassData = serviceGenerator
          .generateBaseService(serviceExceptionClassData, serviceIfPackage);
      classDatas.add(baseServiceClassData);
      DTOGenerator dtoGenerator = new DTOGenerator();
      for (String dbTableName : dbTableNames) {
        try {
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
        } catch (SQLException e) {
          throw new MojoExecutionException("SQL Exception happens in table [" + dbTableName + "]", e);
        } catch (CollectSchemaException e) {
          throw new MojoExecutionException("Collecting schema for table: [" + dbTableName + "] failed.", e);
        } catch (UnknownTypeException e) {
          throw new MojoExecutionException("Unkonwn datatype in table: [" + dbTableName + "].", e);
        }
      }

      if (CollectionUtil.isNotEmpty(classDatas)) {
        for (ClassData cd : classDatas) {
          try {
            writeClass(cd);
          } catch (IOException e) {
            throw new MojoExecutionException("Generate class: [" + cd.getClassName() + "] failed.", e);
          }
        }
      }
    }
  }

  private DataSource getDataSource() {
    DataSource ds = null;
    Properties properties = new Properties();
    File f = new File("./" + propertiesName);
    InputStream is = null;
    try {
      is = new FileInputStream(f);
      properties.load(is);
      ds = dsFactory.getDataSource(properties);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
        }
      }
    }
    return ds;
  }

  private void writeClass(ClassData classData) throws IOException {
    String path = classData.getFullName();
    path = srcDirectory + path.replaceAll(RegexConstants.REGEX_DOT, StringConstants.SLASH) + ".java";
    FileUtil.createFolder(path);
    FileUtil.write2file(classData.getContent().getBytes(), path, overrideExist);
  }
}
