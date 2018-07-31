package com.syproject.util;

import com.syproject.service.QueryEntityDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author:lee
 * @Date:2018/7/30
 * @description:
 */
@Slf4j
@Getter
@Setter
public class SimpleQueryEntityDescriptor implements QueryEntityDescriptor {
  private String tableName;
  private String [] fields;
  private boolean syncType;
  private String[] identities;
  private String timestampName;
  private Integer pageNo;
  private Integer pageSize;
  private Map<String, Object> values;


  @Override
  public String queryEntityName() {
    return tableName;
  }

  @Override
  public String[] fields() {
    return fields;
  }

  @Override
  public boolean syncType() {
    return syncType;
  }

  @Override
  public String[] identities() {
    return identities;
  }

  @Override
  public String timestampName() {
    return timestampName;
  }

  @Override
  public Object value(String key) {
    return values.get(key);
  }

  @Override
  public String startDateKey() {
    return "start_time";
  }

  @Override
  public String endDateKey() {
    return "end_time";
  }

  @Override
  public Integer pageNo() {
    Integer _pageNo = 0;
    if (syncType){
      return _pageNo;
    }else {
       return pageNo*pageSize;
    }
  }

  @Override
  public Integer pageSize() {
    Integer _pageSize = 999999999;
    if (syncType){
      return _pageSize;
    }else {
      return pageSize;
    }
  }

}
