package com.syproject.persistence;

import com.syproject.persistence.helper.PersistenceHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author:lee
 * @Date:2018/7/26
 * @description:
 */
public class QueryCallback implements com.syproject.persistence.helper.QueryCallback<Map<String,Object>> {
  @Override
  public Map<String,Object> mappingRow(ResultSet resultSet) throws SQLException {

    return PersistenceHelper.convert2Map(resultSet);
  }
}
