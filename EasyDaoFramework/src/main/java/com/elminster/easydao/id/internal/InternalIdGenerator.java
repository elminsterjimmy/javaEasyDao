package com.elminster.easydao.id.internal;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import com.elminster.easydao.id.IdGenerator;

public class InternalIdGenerator implements IdGenerator {
  
  private final AtomicLong atomicLong = new AtomicLong();

  @Override
  public Serializable nextId() {
    return atomicLong.getAndIncrement();
  }

}
