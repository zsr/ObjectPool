package com.zsr.test.pool;

/**
 * 
 * @author david.zhang
 * @param <T>
 * 
 */
public abstract class AbstractPool<T> implements Pool<T> {

  /**
   * 一个理想的release方法应该先尝试检查下这个客户端返回的对象是否还能重复使用。
   * 如果是的话再把它扔回池里，如果不是，就舍弃掉这个对象
   */
  public final void release(T t) {
    if (isVaild(t)) {
      returnToPool(t);
    }
    handleInvalidReturn(t);
  }

  public abstract boolean isVaild(T t);

  public abstract void returnToPool(T t);

  public abstract void handleInvalidReturn(T t);
}
