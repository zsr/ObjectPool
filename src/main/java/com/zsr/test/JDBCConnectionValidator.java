package com.zsr.test;

import java.sql.Connection;
import java.sql.SQLException;

import com.zsr.test.pool.Pool.Validator;

public final class JDBCConnectionValidator implements Validator<Connection> {

  @Override
  public boolean isVaild(Connection t) {
    if (t == null) {
      return false;
    }
    try {
      return !t.isClosed();
    } catch (SQLException e) {
      return false;
    }
  }

  @Override
  public void inValidate(Connection t) {
    try {
      t.close();
    } catch (SQLException e) {
    }
  }

}
