package com.zsr.test.pool;

import com.zsr.test.pool.Pool.Validator;

/**
 * 通过工厂用具体的名称来创建不同的对象池
 * 
 * @author david.zhang
 */
public final class PoolFactory {

  private PoolFactory() {

  }

  public static <T> Pool<T> newBoundedBlockingPool(int size, Validator<T> validate, ObjectFactory<T> objectFactory) {
    return new BoundedBlockingPool<T>(size, validate, objectFactory);
  }

  public static <T> Pool<T> newBoundPool(int size, Validator<T> validate, ObjectFactory<T> objectFactory) {
    return new BoundedPool<T>(size, validate, objectFactory);
  }

}
