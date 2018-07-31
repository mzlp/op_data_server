package com.syproject.service.impl;

import com.syproject.persistence.helper.SqlExecuteEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:lee
 * @Date:2018/7/30
 * @description:Sql执行时间
 */
@Getter
@Setter
@Slf4j
public class SqlExecuteStatEvent implements SqlExecuteEvent {
  private String pin;
  private String sql;
  private long started;
  private long timeout;

  public SqlExecuteStatEvent(String pin, String sql, long timeout) {
    this.pin = pin;
    this.sql = sql;
    this.timeout = timeout;

  }

  @Override
  public void executeBefore() {
    started = System.nanoTime();
  }

  @Override
  public void executeAfter() {
    long cost = (System.nanoTime() - started) / 1000000;
    if (cost > timeout) {
      log.warn("Sql timeout: {}; pin: {}, sql: {}", cost, pin, sql );
    }
  }
}
