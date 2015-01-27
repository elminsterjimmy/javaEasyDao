package com.elminster.easydao.db.context;

public class DataSourceInfo {

  private String name;
  private String driverClassName;
  private String dsUrl;
  private String dsUsername;
  private String dsPassword;
  
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  /**
   * @return the driverClassName
   */
  public String getDriverClassName() {
    return driverClassName;
  }
  /**
   * @param driverClassName the driverClassName to set
   */
  public void setDriverClassName(String driverClassName) {
    this.driverClassName = driverClassName;
  }
  /**
   * @return the dsUrl
   */
  public String getUrl() {
    return dsUrl;
  }
  /**
   * @param dsUrl the dsUrl to set
   */
  public void setUrl(String dsUrl) {
    this.dsUrl = dsUrl;
  }
  /**
   * @return the dsUsername
   */
  public String getUsername() {
    return dsUsername;
  }
  /**
   * @param dsUsername the dsUsername to set
   */
  public void setUsername(String dsUsername) {
    this.dsUsername = dsUsername;
  }
  /**
   * @return the dsPassword
   */
  public String getPassword() {
    return dsPassword;
  }
  /**
   * @param dsPassword the dsPassword to set
   */
  public void setPassword(String dsPassword) {
    this.dsPassword = dsPassword;
  }
}
