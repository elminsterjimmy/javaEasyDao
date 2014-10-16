package com.elminster.easydao.test;

import com.elminster.easydao.db.annotation.Column;
import com.elminster.easydao.db.annotation.Entity;
import com.elminster.easydao.db.annotation.Key;
import com.elminster.easydao.db.id.KeyPolicy;

@Entity(tableName="testMappingTable4")
public class ORMMappingBean4 {

  @Key(policy = KeyPolicy.AUTO_INC_POLICY)
  @Column
  private Integer id;
  
  @Column
  private String name;
  
  public ORMMappingBean4(String name) {
    this.name = name;
  }

  /**
   * @return the id
   */
  public Integer getId() {
    return id;
  }

  /**
   * @param id the id to set
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
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
}
