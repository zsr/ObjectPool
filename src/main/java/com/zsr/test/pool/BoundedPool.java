package com.zsr.test.pool;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * 非阻塞的对象池的实现，这个实现和BoundedBlockingPool的唯一不同就是即使对象不可用， 它也不会让客户端阻塞，而是直接返回null
 * 
 * @author david.zhang
 * @param <T>
 */
public class BoundedPool<T> extends AbstractPool<T> {
  private int size;
  private Queue<T> objects;
  private Validator<T> validate;
  private ObjectFactory<T> objectFactory;
  private Semaphore permits;
  private volatile boolean shutdownCalled;

  public BoundedPool(int size, Validator<T> validate, ObjectFactory<T> objectFactory) {
    super();
    this.size = size;
    this.validate = validate;
    this.objectFactory = objectFactory;
    this.objects = new LinkedList<T>();
    initializeObjects();
    shutdownCalled = false;
  }

  private void initializeObjects() {
    for (int i = 0; i < size; i++) {
      objectFactory.createObject();
    }
  }

  @Override
  public T get() {
    T t = null;
    if (!shutdownCalled) {
      if (permits.tryAcquire()) {
        t = objects.poll();
      }
    } else {
      throw new IllegalArgumentException("Object pool already shutdown.");
    }
    return t;
  }

  @Override
  public void shutdown() {
    shutdownCalled = true;
    clearResources();
  }
  
  private void clearResources(){
    for(T t:objects){
      validate.inValidate(t);
    }
  }

  @Override
  public boolean isVaild(T t) {
    return validate.isVaild(t);
  }

  @Override
  public void returnToPool(T t) {
    boolean added = objects.add(t);
    if(added){
      permits.release();
    }
  }

  @Override
  public void handleInvalidReturn(T t) {

  }
}
