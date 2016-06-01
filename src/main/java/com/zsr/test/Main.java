package com.zsr.test;

import java.sql.Connection;

import com.zsr.test.pool.Pool;
import com.zsr.test.pool.PoolFactory;

public class Main {

  public static void main(String[] args) {
    String driver = null;
    String connectionUrl = null;
    String userName = null;
    String password = null;
    Pool<Connection> pool = PoolFactory.newBoundedBlockingPool(10, new JDBCConnectionValidator(),
        new JDBCConnectionFactory(driver, connectionUrl, userName, password));
  }
}
