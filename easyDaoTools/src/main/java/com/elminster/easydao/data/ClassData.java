package com.elminster.easydao.data;

import com.elminster.common.util.StringUtil;

public class ClassData {

  private String className;
  private String packageName;
  private String content;
  
  /**
   * @return the className
   */
  public String getClassName() {
    return className;
  }
  /**
   * @param className the className to set
   */
  public void setClassName(String className) {
    this.className = className;
  }
  /**
   * @return the packageName
   */
  public String getPackageName() {
    return packageName;
  }
  /**
   * @param packageName the packageName to set
   */
  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }
  /**
   * @return the content
   */
  public String getContent() {
    return content;
  }
  /**
   * @param content the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }
  
  public String getFullName() {
    return StringUtil.isEmpty(packageName) ? className : packageName + "." + className;
  }
}
