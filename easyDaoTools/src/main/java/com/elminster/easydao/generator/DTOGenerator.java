package com.elminster.easydao.generator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.elminster.easydao.data.ClassData;

public class DTOGenerator {

  private static final Log logger = LogFactory.getLog(DTOGenerator.class);

  public DTOGenerator() {
  }

  public ClassData generateDTO(ClassData entityClassData, String packageName) {
    String entityClassName = entityClassData.getClassName();
    String entityContent = entityClassData.getContent();
    String entityPackage = entityClassData.getPackageName();
    String className = entityClassName.substring(0, entityClassName.length() - EntityGenerator.ENTITY.length());
    String content = removeContents(replaceContents(entityContent, entityPackage, packageName, entityClassName, className));
    logger.debug("DTO [" + entityClassName + "]. Generated Source:\n" + content);
    ClassData cd = new ClassData();
    cd.setClassName(className);
    cd.setPackageName(packageName);
    cd.setContent(content);
    return cd;
  }

  private String removeContents(String replacePackage) {
    String removeImports = replacePackage.replaceAll("import com\\.elminster\\.easydao\\.db\\.annotation\\..*?\n", "");
    String removeAnnotations = removeImports.replaceAll("( )*?@.*?\n", "");
    return removeAnnotations;
  }

  private String replaceContents(String entityContent, String entityPackage, String replacePackageName, String entityClassName, String replaceClassName) {
    String replacePackage = entityContent.replace(entityPackage, replacePackageName);
    String replaceClazzName = replacePackage.replace(entityClassName, replaceClassName);
    return replaceClazzName;
  }
}
