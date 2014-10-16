package com.elminster.easydao.test;

import java.sql.Date;
import java.util.List;

import com.elminster.common.util.ObjectUtil;
import com.elminster.easydao.db.annotation.Column;
import com.elminster.easydao.db.annotation.CustomerType;
import com.elminster.easydao.db.annotation.Entity;
import com.elminster.easydao.db.annotation.Key;
import com.elminster.easydao.db.id.KeyPolicy;

@Entity(tableName = "testtable")
public class ORMBean {

  @Key(policy = KeyPolicy.AUTO_INC_POLICY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "name")
  private String name;
  @Column(name = "pass")
  private String pass;
  @Column(name = "account")
  private Double account;
  @Column(name = "quary")
  private Integer quary;
  @CustomerType(className = com.elminster.easydao.test.SplitListConverter.class)
  @Column(name = "tel")
  private List<Integer> tel;

  /**
   * @return the tel
   */
  public List<Integer> getTel() {
    return tel;
  }

  /**
   * @param tel
   *          the tel to set
   */
  public void setTel(List<Integer> tel) {
    this.tel = tel;
  }

  @Column(name = "last_update")
  private Date lastUpdate;

  /**
   * @return the id
   */
  public Integer getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the pass
   */
  public String getPass() {
    return pass;
  }

  /**
   * @param pass
   *          the pass to set
   */
  public void setPass(String pass) {
    this.pass = pass;
  }

  /**
   * @return the account
   */
  public Double getAccount() {
    return account;
  }

  /**
   * @param account
   *          the account to set
   */
  public void setAccount(Double account) {
    this.account = account;
  }

  /**
   * @return the quary
   */
  public Integer getQuary() {
    return quary;
  }

  /**
   * @param quary
   *          the quary to set
   */
  public void setQuary(Integer quary) {
    this.quary = quary;
  }

  /**
   * @return the lastUpdate
   */
  public Date getLastUpdate() {
    return lastUpdate;
  }

  /**
   * @param lastUpdate
   *          the lastUpdate to set
   */
  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public String toString() {
    return ObjectUtil.buildToStringByReflect(this);
  }
}
