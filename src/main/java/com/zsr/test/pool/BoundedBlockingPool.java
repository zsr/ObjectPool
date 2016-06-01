package com.zsr.test.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 内部的存储是一个LinkedBlockingQueue实现的，如果我们直接把返回的对象扔进去的话，
 * 如果队列已满可能会阻塞住客户端。不过我们不希望客户端因为把对象放回池里这么个普通的方法就阻塞住了。
 * 所以我们把最终将对象插入到队列里的任务作为一个异步的的任务提交给一个Executor来执行，
 * 以便让客户端线程能立即返回
 * @author david.zhang
 * @param <T>
 */
public final class BoundedBlockingPool<T> extends AbstractPool<T> implements BlockingPool<T> {

  private int size;
  private BlockingQueue<T> objects;
  private Validator<T> validate;
  private ObjectFactory<T> objectFactory;
  private ExecutorService executor = Executors.newCachedThreadPool();
  private volatile boolean shutdownCalled;

  public BoundedBlockingPool(int size, Validator<T> validate, ObjectFactory<T> objectFactory) {
    super();
    this.size = size;
    this.validate = validate;
    this.objectFactory = objectFactory;
    objects = new LinkedBlockingQueue<T>();
    initializeObjects();
    shutdownCalled = false;
  }

  public void initializeObjects() {
    for (int i = 0; i < size; i++) {
      objectFactory.createObject();
    }
  }

  @Override
  public T get(long time, TimeUnit timeUnit) {
    if (!shutdownCalled) {
      T t = null;
      try {
        t = objects.poll(time, timeUnit);
        return t;
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      return t;
    }
    throw new IllegalStateException("Object pool is already shutdowm.");
  }

  @Override
  public T get() {
    if (!shutdownCalled) {
      T t = null;
      try {
        t = objects.take();
        return t;
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      return t;
    }
    throw new IllegalStateException("Object pool is already shutdowm.");
  }

  @Override
  public void shutdown() {
    shutdownCalled = true;
    executor.shutdownNow();
    clearResources();
  }

  private void clearResources() {
    for (T t : objects) {
      validate.inValidate(t);
    }
  }

  @Override
  public boolean isVaild(T t) {
    return validate.isVaild(t);
  }

  @Override
  public void returnToPool(T t) {
    if(validate.isVaild(t)){
      executor.submit(new ObjectReturner(objects, t));
    }
  }
  
  private class ObjectReturner<E> implements Callable<Void>{
    private BlockingQueue<E> queue;
    private E e;
    
    public ObjectReturner(BlockingQueue<E> queue, E e){
      this.queue = queue;
      this.e = e;
    }
    
    public Void call(){
      while(true){
        try {
          queue.put(e);
          break;
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
      return null;
    }
  }

  @Override
  public void handleInvalidReturn(T t) {
  }
}
