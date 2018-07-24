package com.syproject.service.impl;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.syproject.domian.ConnConfig;
import com.syproject.domian.Racetrack;
import com.syproject.repository.ConnConfigRepository;
import com.syproject.service.RacetrackProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author:lee
 * @Date:2018/7/20
 * @description:
 */
@Component
@Slf4j
public class ConnConfigProvider implements RacetrackProvider {

  private final AtomicReference<Map<String, Racetrack>> racetracksRef = new AtomicReference<>();
  private String driverClass = "com.mysql.jdbc.Driver";

  @Autowired
  private ConnConfigRepository configRepository;

  /**
   * 加载到缓存
   */
  @PostConstruct
  public void registerComponent() {
    log.info("加载数据");
    Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("connConfig-pool-%d").build())
    .scheduleWithFixedDelay(new Runnable() {
      @Override
      public void run() {
        try {
          loadMapping();
        } catch (Exception e) {
          log.error("loader connConfig failed" + e.getLocalizedMessage(), e);
        }
      }
    },0,20,TimeUnit.MINUTES)
    ;

  }

  private boolean loadMapping(){
    List<ConnConfig> connConfigs = configRepository.findAll();
    log.info("loader connConfig for db");
    if (!CollectionUtils.isEmpty(connConfigs)){
      log.info("loader connConfgi size ---> {}",connConfigs.size());
      final Map<String,Racetrack> racetrackMap = transMap(connConfigs);
      racetracksRef.set(racetrackMap);
      return true;
    }
    return false;
  }

  private Map<String,Racetrack> transMap(List<ConnConfig> connConfigs){
    Map<String,Racetrack> map = Maps.newHashMap();
    for (ConnConfig connConfig:connConfigs){
        Racetrack racetrack = new Racetrack();
        racetrack.setClazz(driverClass);
        racetrack.setName(connConfig.getConnName());
        racetrack.setHostname(connConfig.getDbHost());
        racetrack.setPassword(connConfig.getDbPass());
        racetrack.setUsername(connConfig.getDbUser());
        racetrack.setEndpoint(connConfig.getDbName());
        racetrack.setPort(3306);
        map.put(connConfig.getConnName(),racetrack);
    }
    return map;
  }

  @PreDestroy
  public void destroyComponent() {
  }

  @Override
  public Racetrack getRacetrack(String connName) {
    if (racetracksRef.get() == null){
      synchronized (this) {
        if (racetracksRef.get() == null) {
          loadMapping();
        }
      }
    }

    return racetracksRef.get().get(connName);
  }
}
