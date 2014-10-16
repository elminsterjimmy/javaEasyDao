package com.elminster.easydao.test;

import java.util.List;

import com.elminster.common.util.ObjectUtil;
import com.elminster.easydao.db.annotation.Column;
import com.elminster.easydao.db.annotation.Entity;
import com.elminster.easydao.db.annotation.Key;
import com.elminster.easydao.db.annotation.Mapping;
import com.elminster.easydao.db.id.KeyPolicy;
import com.elminster.easydao.db.mapping.UpdatePolicy;
import com.elminster.easydao.db.mapping.InsertPolicy;
import com.elminster.easydao.db.mapping.MappingPolicy;

/***
 * The Mapping1 bean
 * @author jgu
 *
 */
@Entity(tableName="testMappingTable1")
public class ORMMappingBean1 {

  @Key(policy = KeyPolicy.AUTO_INC_POLICY)
  @Column
  private Integer id;
  
  @Column
  private String name;
  
  @Mapping(mappingPolicy=MappingPolicy.INTERMEDIARY_POLICY,
      entity=ORMMappingBean2.class,
      intermediaryTable="testMappingIntermediate12",
      intermediaryControlColumn="m1Id",
      intermediaryUncontrolColumn="m2Id",
      updatePolicy=UpdatePolicy.CASCADE)
  private List<ORMMappingBean2> nameList;
  
  @Mapping(mappingPolicy=MappingPolicy.INTERMEDIARY_POLICY,
      entity=ORMMappingBean2.class,
      intermediaryTable="testMappingIntermediate13",
      intermediaryControlColumn="m1Id",
      intermediaryUncontrolColumn="m2Id",
      insertPolicy=InsertPolicy.SELECT_INSERT)
  private List<ORMMappingBean3> name2List;
  
  @Mapping(mappingPolicy=MappingPolicy.DIRECT_POLICY,
      entity=ORMMappingBean2.class,
      directRefColumn="id")
  @Column(name="t4_ref_id")
  private ORMMappingBean4 bean4;

  /**
   * @return the id
   */
  public Integer getId() {
    return id;
  }

  /**
   * @return the nameList
   */
  public List<ORMMappingBean2> getNameList() {
    return nameList;
  }

  /**
   * @param nameList the nameList to set
   */
  public void setNameList(List<ORMMappingBean2> nameList) {
    this.nameList = nameList;
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
  
  /**
   * @return the name2List
   */
  public List<ORMMappingBean3> getName2List() {
    return name2List;
  }

  /**
   * @param name2List the name2List to set
   */
  public void setName2List(List<ORMMappingBean3> name2List) {
    this.name2List = name2List;
  }

  /**
   * @return the bean4
   */
  public ORMMappingBean4 getBean4() {
    return bean4;
  }

  /**
   * @param bean4 the bean4 to set
   */
  public void setBean4(ORMMappingBean4 bean4) {
    this.bean4 = bean4;
  }

  public String toString() {
    return ObjectUtil.buildToStringByReflect(this);
  }
}
