package com.elminster.easydao.id;

import java.io.Serializable;
import java.util.UUID;

public class UUIDGenerator implements IdGenerator {

  @Override
  public Serializable nextId() {
    return UUID.randomUUID().toString();
  }
}
