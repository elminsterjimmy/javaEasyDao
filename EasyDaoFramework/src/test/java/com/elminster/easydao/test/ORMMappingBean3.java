package com.elminster.easydao.test;

import com.elminster.common.util.ObjectUtil;
import com.elminster.easydao.db.annotation.Column;
import com.elminster.easydao.db.annotation.Entity;
import com.elminster.easydao.db.annotation.Key;
import com.elminster.easydao.db.id.KeyPolicy;

/***
 * The Mapping1 bean
 * @author jgu
 *
 */
@Entity(tableName="testMappingTable3")
public class ORMMappingBean3 {

  @Key(policy = KeyPolicy.AUTO_INC_POLICY)
  @Column
  private Integer id;
  
  @Column
  private String name;
  
  public ORMMappingBean3() {
    
  }

  public ORMMappingBean3(String name) {
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

  @Override
  public String toString() {
    return ObjectUtil.buildToStringByReflect(this);
  }
}
