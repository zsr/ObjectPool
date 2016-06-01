package com.zsr.test.pool;

/**
 * 
 * @author david.zhang
 * @param <T>
 * 对象池
 * 1）如果有可用的对象，对象池应当能返回给客户端。
 * 2）客户端把对象放回池里后，可以对这些对象进行重用。
 * 3）对象池能够创建新的对象来满足客户端不断增长的需求。
 * 4）需要有一个正确关闭池的机制来确保关闭后不会发生内存泄露。
 */
public interface Pool<T> {
  
  public T get();
  
  public void release(T t);
  
  public void shutdown();
  
  /**
   * 引入了一个新的接口Validator，它定义了验证对象的方法
   * @author david.zhang
   */
  public static interface Validator<T> {
    
    /**
     * 通用的方法来完成对象的校验
     * @param t
     * @return
     */
    public boolean isVaild(T t);
    
    /**
     * 当准备废弃一个对象并清理内存的时候
     * @param t
     */
    public void inValidate(T t);
  }

}
