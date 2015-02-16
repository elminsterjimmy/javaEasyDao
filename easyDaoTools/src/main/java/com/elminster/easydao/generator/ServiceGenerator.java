package com.elminster.easydao.generator;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.elminster.common.constants.Constants.StringConstants;
import com.elminster.common.util.FileUtil;
import com.elminster.easydao.data.ClassData;

/**
 * The Service generator.
 * 
 * @author jgu
 * @version 1.0
 */
public class ServiceGenerator {

  private static final String IMPORT = "import ";
  private static final String SERVICE_EXCEPTION_TEMPLATE = "ServiceException.template";
  private static final String BASE_SERVICE_INTERFACE_TEMPLATE = "BaseService.template";
  private static final String SERVICE_INTERFACE_TEMPLATE = "ServiceIf.template";
  private static final String SERVICE_IMPL_TEMPLATE = "ServiceImpl.template";
  private static String SERVICE_EXCEPTION_TEMPLATE_STRING;
  private static String BASE_SERVICE_INTERFACE_TEMPLATE_STRING;
  private static String SERVICE_INTERFACE_TEMPLATE_STRING;
  private static String SERVICE_IMPL_TEMPLATE_STRING;
  private static final String BASE_SERVICE_NAME = "BaseService";
  private static final String SERVICE_EXCEPTION_NAME = "ServiceException";

  private static final Log logger = LogFactory.getLog(ServiceGenerator.class);

  static {
    try {
      BASE_SERVICE_INTERFACE_TEMPLATE_STRING = FileUtil.readFile2String(DAOGenerator.class.getClassLoader()
          .getResourceAsStream(BASE_SERVICE_INTERFACE_TEMPLATE), false);
      SERVICE_INTERFACE_TEMPLATE_STRING = FileUtil.readFile2String(DAOGenerator.class.getClassLoader()
          .getResourceAsStream(SERVICE_INTERFACE_TEMPLATE), false);
      SERVICE_EXCEPTION_TEMPLATE_STRING = FileUtil.readFile2String(DAOGenerator.class.getClassLoader()
          .getResourceAsStream(SERVICE_EXCEPTION_TEMPLATE), false);
      SERVICE_IMPL_TEMPLATE_STRING = FileUtil.readFile2String(
          DAOGenerator.class.getClassLoader().getResourceAsStream(SERVICE_IMPL_TEMPLATE), false);
    } catch (IOException e) {
      // should not happen
      e = null;
    }
  }

  public ServiceGenerator() {
  }

  public ClassData generateBaseService(ClassData serviceExceptionClassData, String packageName) {
    String generated = MessageFormat.format(BASE_SERVICE_INTERFACE_TEMPLATE_STRING, packageName,
        serviceExceptionClassData.getFullName());
    logger.debug("Base Service generated source:\n" + generated);
    ClassData cd = new ClassData();
    cd.setClassName(BASE_SERVICE_NAME);
    cd.setPackageName(packageName);
    cd.setContent(generated);
    return cd;
  }

  public ClassData generateServiceIf(ClassData dtoClassData, String packageName) {
    String dtoName = dtoClassData.getClassName();
    String dtoFullName = dtoClassData.getFullName();
    String dtoClassName = dtoClassData.getClassName();
    String serviceName = dtoName + "Service";
    String generated = MessageFormat.format(SERVICE_INTERFACE_TEMPLATE_STRING, packageName, dtoFullName, serviceName,
        dtoClassName);
    logger.debug("Service interface: [" + serviceName + "]. Generated source:\n" + generated);
    ClassData cd = new ClassData();
    cd.setClassName(serviceName);
    cd.setPackageName(packageName);
    cd.setContent(generated);
    return cd;
  }

  public ClassData generateServiceImpl(ClassData entityClassData, ClassData dtoClassData, ClassData daoClassData,
      ClassData serviceExceptionClassData, ClassData serviceIfClassData, String packageName) {
    StringBuilder imports = new StringBuilder();
    imports.append(getImportDefine(entityClassData));
    imports.append(getImportDefine(dtoClassData));
    imports.append(getImportDefine(daoClassData));
    imports.append(getImportDefine(serviceIfClassData));
    imports.append(getImportDefine(serviceExceptionClassData));

    String className = serviceIfClassData.getClassName() + "Impl";

    String generated = MessageFormat.format(SERVICE_IMPL_TEMPLATE_STRING, packageName, imports.toString(), className,
        serviceIfClassData.getClassName(), daoClassData.getClassName(), dtoClassData.getClassName(),
        entityClassData.getClassName());
    logger.debug("Service Impl: [" + className + "]. Generated source:\n" + generated);

    ClassData cd = new ClassData();
    cd.setClassName(className);
    cd.setPackageName(packageName);
    cd.setContent(generated);
    return cd;
  }

  private Object getImportDefine(ClassData cd) {
    StringBuilder sb = new StringBuilder();
    sb.append(IMPORT);
    sb.append(cd.getFullName());
    sb.append(StringConstants.SEMICOLON);
    sb.append(StringConstants.LF);
    return sb.toString();
  }

  public ClassData getnerateServiceException(String exceptionPackage) {
    String generated = MessageFormat.format(SERVICE_EXCEPTION_TEMPLATE_STRING, exceptionPackage);
    logger.debug("Service Exception generated source:\n" + generated);
    ClassData cd = new ClassData();
    cd.setClassName(SERVICE_EXCEPTION_NAME);
    cd.setPackageName(exceptionPackage);
    cd.setContent(generated);
    return cd;
  }
}
