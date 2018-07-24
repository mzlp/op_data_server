package com.syproject.service.impl;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.syproject.domian.Racetrack;
import com.syproject.service.AbstractSqlConnectionPool;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author:lee
 * @Date:2018/7/23
 * @description:
 */
@Slf4j
public class BonecpConnectionPoolConfig extends AbstractSqlConnectionPool<BoneCP> {
  @Override
  protected Connection getConnection(BoneCP pool) throws SQLException {
    return pool.getConnection();
  }

  @Override
  protected void closeConnectionPool(BoneCP pool) {
    pool.shutdown();
  }

  @Override
  protected BoneCP createConnectionPool(Racetrack racetrack) throws Exception {
    log.info("create BoneCP ConnectionPool ");
    BoneCPConfig config = new BoneCPConfig();
    config.setJdbcUrl(buildConnector(racetrack));
    config.setUsername(racetrack.getUsername());
    config.setPassword(racetrack.getPassword());
    config.setPoolName(racetrack.getName());
    Class.forName(racetrack.getClazz());
    //config.setMinConnectionsPerPartition(5);
    //config.setMaxConnectionsPerPartition(10);
    //config.setPartitionCount(1);
    return new BoneCP(config);
  }
}
