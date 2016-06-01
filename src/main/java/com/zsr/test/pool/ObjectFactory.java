package com.zsr.test.pool;

/**
 * 通用的方式来创建新的对象
 * @author david.zhang
 */
public interface ObjectFactory<T> {

  public T createObject();
}
