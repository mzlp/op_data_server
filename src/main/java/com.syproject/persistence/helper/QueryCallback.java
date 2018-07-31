package com.syproject.persistence.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author:lee
 * @Date:2018/7/26
 * @description:回调
 */
public interface QueryCallback<T> {
  T mappingRow(ResultSet resultSet) throws SQLException;
}
