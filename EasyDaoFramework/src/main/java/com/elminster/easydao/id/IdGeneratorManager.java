package com.elminster.easydao.id;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.elminster.common.util.ReflectUtil;

public class IdGeneratorManager {

  public static final IdGeneratorManager INSTANCE = new IdGeneratorManager();

  private final Map<String, IdGenerator> idGeneratorCache = new HashMap<String, IdGenerator>();

  private IdGeneratorManager() {
    // default
    idGeneratorCache.put(UUIDGenerator.class.getName(), new UUIDGenerator());
  }

  public synchronized IdGenerator getIdGenerator(String className) throws IdGeneratorNotFoundException {
    IdGenerator idGenerator = idGeneratorCache.get(className);
    if (null == idGenerator) {
      try {
        Object obj = ReflectUtil.newInstanceViaReflect(className);
        if (obj instanceof IdGenerator) {
          idGenerator = (IdGenerator) obj;
          idGeneratorCache.put(className, idGenerator);
        } else {
          throw new IdGeneratorNotFoundException(className + " doesn't implement interface ["
              + IdGenerator.class.getName() + "].");
        }
      } catch (NoSuchMethodException e) {
        throw new IdGeneratorNotFoundException(e);
      } catch (SecurityException e) {
        throw new IdGeneratorNotFoundException(e);
      } catch (InstantiationException e) {
        throw new IdGeneratorNotFoundException(e);
      } catch (IllegalAccessException e) {
        throw new IdGeneratorNotFoundException(e);
      } catch (IllegalArgumentException e) {
        throw new IdGeneratorNotFoundException(e);
      } catch (InvocationTargetException e) {
        throw new IdGeneratorNotFoundException(e);
      } catch (ClassNotFoundException e) {
        throw new IdGeneratorNotFoundException(e);
      }
    }
    return idGenerator;
  }
}
