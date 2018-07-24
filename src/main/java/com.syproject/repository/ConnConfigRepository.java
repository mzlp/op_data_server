package com.syproject.repository;

import com.syproject.domian.ConnConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author:lee
 * @Date:2018/7/18
 * @description:
 */
public interface ConnConfigRepository extends JpaRepository<ConnConfig,String> {

  @Override
  List<ConnConfig> findAll();
  @Override
  public ConnConfig save(ConnConfig connConfig);
}
