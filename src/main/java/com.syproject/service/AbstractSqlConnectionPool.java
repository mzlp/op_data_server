package com.syproject.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.syproject.domian.Racetrack;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author:lee
 * @Date:2018/7/23
 * @description:
 */
@Component
@Slf4j
public abstract class AbstractSqlConnectionPool<T> implements SqlConnectionPool {

  private Cache<String, T> pools;
  @Value("${bonecp.poolLiveTime}")
  private long poolLiveTime;

  @PostConstruct
  public void registerComponent(){
    pools = CacheBuilder.newBuilder().expireAfterAccess(poolLiveTime,TimeUnit.MINUTES).removalListener(new RemovalListener<String, T>() {
      @Override
      public void onRemoval(RemovalNotification<String, T> removalNotification) {
        log.info("close connection pool");
        T pool = removalNotification.getValue();
        if (pool != null){
          closeConnectionPool(pool);
        }
      }

    }).concurrencyLevel(40).build();

  }

  @PreDestroy
  public void destoryComponent(){
     pools.cleanUp();
  }



  @Override
  public Connection getConnection(final Racetrack racetrack) throws SQLException {
    try {
      log.info("name: {}, clazz: {}, hostname: {}, port: {},endpoint: {}, username: {}",
              racetrack.getName(), racetrack.getClazz(), racetrack.getHostname(),
              racetrack.getPort(), racetrack.getEndpoint(), racetrack.getUsername());
      String key = key(racetrack);
      T pool = pools.get(key, new Callable<T>() {
        @Override
        public T call() throws Exception {
          log.info("create connection pool: {},hostName:{}",racetrack.getName(),racetrack.getHostname());
          return createConnectionPool(racetrack);
        }
      });
      return getConnection(pool);
    } catch (ExecutionException e) {
      throw new SQLException(e.getLocalizedMessage(), e);
    }
  }

  /**
   * 获取连接
   * @param pool
   * @return
   * @throws SQLException
   */
  protected abstract Connection getConnection(T pool) throws SQLException;

  /**
   * 关闭连接
   * @param pool
   */
  protected abstract void closeConnectionPool(T pool);

  /**
   * 创建连接池
   * @param racetrack
   * @return
   * @throws Exception
   */
  protected abstract T createConnectionPool(Racetrack racetrack) throws Exception;


  protected String buildConnector(Racetrack racetrack) {
    String props = "?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8&autoReconnect=true&socketTimeout=30000&connectTimeout=30000&zeroDateTimeBehavior=convertToNull";
    if (racetrack.getClazz().contains("mysql")) {
      return "jdbc:mysql://" + racetrack.getHostname() + "/" + racetrack.getEndpoint() + props;
    }
    throw new UnsupportedOperationException("Can't support clazz: " + racetrack.getClazz());
  }

  public long getPoolLiveTime() {
    return poolLiveTime;
  }

  public void setPoolLiveTime(long poolLiveTime) {
    this.poolLiveTime = poolLiveTime;
  }



  protected String key(Racetrack racetrack) {
    List<String> properties = new ArrayList<>(7);
    properties.add("name=" + racetrack.getName());
    properties.add("clazz=" + racetrack.getClazz());
    properties.add("hostname=" + racetrack.getHostname());
    properties.add("port=" + racetrack.getPort());
    properties.add("endpoint=" + racetrack.getEndpoint());
    properties.add("username=" + racetrack.getUsername());
    properties.add("password=" + racetrack.getPassword());
    return StringUtils.join(properties, "&");
  }
}
