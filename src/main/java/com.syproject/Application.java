package com.syproject;

import com.syproject.service.impl.BonecpConnectionPoolConfig;
import com.syproject.service.impl.ConnConfigProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author:lee
 * @Date:2018/6/25
 * @description:
 */
@SpringBootApplication
@Slf4j
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class,args);
  }

  @Bean
  public ConnConfigProvider connConfigProvider(){
    return new ConnConfigProvider();
  }


  @Bean
  public BonecpConnectionPoolConfig connectionPoolConfig(){
    return new BonecpConnectionPoolConfig();
  }

}
