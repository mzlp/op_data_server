package com.syproject.service;

import com.syproject.domian.Racetrack;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author:lee
 * @Date:2018/7/23
 * @description:
 */
public interface SqlConnectionPool {

  Connection getConnection(Racetrack racetrack) throws SQLException;
}
