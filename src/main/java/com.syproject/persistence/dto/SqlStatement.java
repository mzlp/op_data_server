package com.syproject.persistence.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author:lee
 * @Date:2018/7/26
 * @description:
 */
@Setter
@Getter
public class SqlStatement {

  private String sql;
  private PropertyMapper propertyMapper;

}
