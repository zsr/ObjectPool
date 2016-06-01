package com.zsr.test.pool;

/**
 * 引入了一个新的接口，Validator，它定义了验证对象的方法
 * @author david.zhang
 */
public interface Validator<T> {
  
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
