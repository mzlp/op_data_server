package com.syproject.persistence.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:lee
 * @Date:2018/7/25
 * @description:
 */
public class PropertyMapper {
  private Map<String, String> entries = Maps.newHashMap();
  private List<String> insertOrders = Lists.newArrayList();

  public void put(String columnName, String propertyName) {
    if (!entries.containsKey(columnName)) {
      insertOrders.add(columnName);
    }
    entries.put(columnName, propertyName);
  }

  public void add(String columnName, String propertyName) {
    insertOrders.add(columnName);
    if (!entries.containsKey(columnName)) {
      entries.put(columnName, propertyName);
    }
  }


  public List<String> keyList() {
    return insertOrders;
  }

  public String[] keyArray() {
    return insertOrders.toArray(new String[insertOrders.size()]);
  }

  public List<String> valueList() {
    List<String> values = new ArrayList<String>(insertOrders.size());
    for (String key : insertOrders) {
      values.add(entries.get(key));
    }
    return values;
  }

  public String[] valueArray() {
    String[] values = new String[insertOrders.size()];
    for (int i = 0; i < values.length; i++) {
      values[i] = entries.get(insertOrders.get(i));
    }
    return values;
  }

  public String get(String key) {
    return entries.get(key);
  }

  public int size() {
    return insertOrders.size();
  }

}
