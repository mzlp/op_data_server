package com.syproject.persistence.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author:lee
 * @Date:2018/7/25
 * @description:
 */
@Getter
@Setter
public class DataSources {
  private String driverClass;
  private String connector;
  private String username;
  private String password;
}
