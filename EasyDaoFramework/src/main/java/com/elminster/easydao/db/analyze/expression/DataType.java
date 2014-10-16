package com.elminster.easydao.db.analyze.expression;

public class DataType {

  public static final DataType UNDEFINE = new DataType("Undefine");

  public static final DataType NULL = new DataType("Null");

  public static final DataType OBJECT = new DataType("Object");

  public static final DataType STRING = new DataType("String");

  public static final DataType INTEGER = new DataType("Integer");

  public static final DataType LONG = new DataType("Long");

  public static final DataType DOUBLE = new DataType("Double");

  public static final DataType BOOLEAN = new DataType("Boolean");
  
  public static final DataType[] EMPTYARR = new DataType[0];

  private String name;
  
  private String fullName;
  
  private int dimension;
  
  /** the basic type, if it is an arrray. */
  private DataType basicType;
  
  /**  */
  private boolean isArray = false;

  /**  */
  private boolean isStruct = false;

  public DataType(String name) {
    this.name = name;
    this.fullName = name;
    this.dimension = 0;
    this.basicType = null;
    this.isArray = false;
    this.isStruct = false;
  }
  
  public DataType(DataType basicType, int dimenstion) {
    if (null == basicType) {
      throw new IllegalArgumentException("Basic Type cannot be null.");
    }
    if (null != basicType.getBasicType()) {
      throw new IllegalArgumentException("Basic Type cannot be an array.");
    }
    this.name = basicType.getName();
    this.dimension = dimenstion;
    this.basicType = basicType;
    this.isArray = true;
    this.isStruct = false;
    String bracket = "[]";
    StringBuilder sb = new StringBuilder(this.dimension * 2 + name.length());
    sb.append(name);
    for (int i = 0; i < this.dimension; i++) {
      sb.append(bracket);
    }
    this.fullName = sb.toString();
  }
  
  public boolean isUndefined() {
    return DataType.UNDEFINE.equals(this);
  }
  
  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    return fullName.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DataType) {
      DataType dt = (DataType)obj;
      return dt.getFullName().equals(this.getFullName());
    }
    return false;
  }

  /**
   * @return the fullName
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * @param fullName the fullName to set
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  /**
   * @return the dimension
   */
  public int getDimension() {
    return dimension;
  }

  /**
   * @param dimension the dimension to set
   */
  public void setDimension(int dimension) {
    this.dimension = dimension;
  }

  /**
   * @return the basicType
   */
  public DataType getBasicType() {
    return basicType;
  }

  /**
   * @param basicType the basicType to set
   */
  public void setBasicType(DataType basicType) {
    this.basicType = basicType;
  }

  /**
   * @return the isArray
   */
  public boolean isArray() {
    return isArray;
  }

  /**
   * @param isArray the isArray to set
   */
  public void setArray(boolean isArray) {
    this.isArray = isArray;
  }

  /**
   * @return the isStruct
   */
  public boolean isStruct() {
    return isStruct;
  }

  /**
   * @param isStruct the isStruct to set
   */
  public void setStruct(boolean isStruct) {
    this.isStruct = isStruct;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  
  @Override
  public String toString() {
    return fullName;
  }
}
