package com.zsr.test.pool;

import java.util.concurrent.TimeUnit;

/**
 * 阻塞的对象池，当没有对象可用的时候，让客户端先阻塞住。
 * 阻塞机制是让客户端一直阻塞直到有对象可用为止
 * @author david.zhang
 * @param <T>
 */
public interface BlockingPool<T> extends Pool<T>{
  
  T get();
  
  T get(long time, TimeUnit timeUnit) throws InterruptedException;
}
