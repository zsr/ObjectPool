package com.zsr.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.zsr.test.pool.ObjectFactory;

public class JDBCConnectionFactory implements ObjectFactory<Connection> {
  private String connectionUrl;
  private String userName;
  private String password;

  public JDBCConnectionFactory(String driver, String connectionUrl, String userName, String password) {
    super();

    try {
      Class.forName(driver);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Unable to find driver", e);
    }
    this.connectionUrl = connectionUrl;
    this.userName = userName;
    this.password = password;
  }

  @Override
  public Connection createObject() {
    try {
      return DriverManager.getConnection(connectionUrl, userName, password);
    } catch (SQLException e) {
      throw new IllegalArgumentException("Unable to create new connection.", e);
    }
  }

}
