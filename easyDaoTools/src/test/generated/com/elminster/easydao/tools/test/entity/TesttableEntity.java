package com.elminster.easydao.tools.test.entity;

import com.elminster.easydao.db.annotation.Column;
import com.elminster.easydao.db.annotation.Entity;
import com.elminster.easydao.db.annotation.Key;
import java.sql.Timestamp;

@Entity(tableName="testtable")
public class TesttableEntity {

  @Key
  @Column
  private Integer id;
  @Column
  private String name;
  @Column
  private String pass;
  @Column
  private Double account;
  @Column
  private Integer quary;
  @Column
  private String tel;
  @Column
  private Timestamp lastUpdate;

  public void setId(Integer id) {
    this.id = id;
  }
  public Integer getId() {
    return id;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getName() {
    return name;
  }
  public void setPass(String pass) {
    this.pass = pass;
  }
  public String getPass() {
    return pass;
  }
  public void setAccount(Double account) {
    this.account = account;
  }
  public Double getAccount() {
    return account;
  }
  public void setQuary(Integer quary) {
    this.quary = quary;
  }
  public Integer getQuary() {
    return quary;
  }
  public void setTel(String tel) {
    this.tel = tel;
  }
  public String getTel() {
    return tel;
  }
  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }
  public Timestamp getLastUpdate() {
    return lastUpdate;
  }

}
