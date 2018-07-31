package com.syproject.persistence.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author:lee
 * @Date:2018/7/25
 * @description:
 */
@Setter
@Getter
public class EntityMapper {

  private String tableName;
  private String identityName;
  private String[] uniqueNames;
  private PropertyMapper propertyMapper;
}
