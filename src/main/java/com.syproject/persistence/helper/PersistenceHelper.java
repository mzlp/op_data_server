package com.syproject.persistence.helper;

import com.google.common.collect.Lists;
import com.syproject.persistence.dto.ColumnMeta;
import com.syproject.persistence.dto.DataSources;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:lee
 * @Date:2018/7/26
 * @description:JDBC Helper
 */
@Slf4j
public class PersistenceHelper {


  /**
   * GetConnection
   * @param sources
   * @return
   * @throws SQLException
   */
  public static Connection getConnection(DataSources sources) throws SQLException {
    try {
      Class.forName(sources.getDriverClass());
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(e.getLocalizedMessage(), e);
    }
    return DriverManager.getConnection(sources.getConnector(),sources.getUsername(),sources.getPassword());
  }


  /**
   * INSERT OR UPDATE
   * @param connection
   * @param event
   * @param sql
   * @param parameters
   * @return
   * @throws SQLException
   */
  public static int executeUpdate(Connection connection,SqlExecuteEvent event,String sql,Object...parameters) throws SQLException{
    if (null == event) {
      throw new NullPointerException("Sql execute event can't be empty.");
    }
    return  execute_update(connection,event,sql,parameters);
  }

  /**
   * INSERT OR UPDATE
   * @param connection
   * @param sql
   * @param parameters
   * @return
   * @throws SQLException
   */
  public static int executeUpdate(Connection connection,String sql,Object... parameters) throws SQLException{
    return execute_update(connection,null,sql,parameters);
  }


  /**
   * QUERY
   * @param connection
   * @param queryCallback
   * @param limit
   * @param executeEvent
   * @param sql
   * @param parameters
   * @param <T>
   * @return
   * @throws SQLException
   */
  public static  <T> List<T> executeQuery(Connection connection,QueryCallback queryCallback,Integer limit,SqlExecuteEvent executeEvent,String sql,Object... parameters) throws SQLException{
    if (null == executeEvent){
      throw new NullPointerException("Sql execute event can't be empty.");
    }
    return execute_query(connection,queryCallback,limit,executeEvent,sql,parameters);
  }


  /**
   * QUERY
   * @param connection
   * @param queryCallback
   * @param limit
   * @param sql
   * @param parameters
   * @param <T>
   * @return
   * @throws SQLException
   */
  public static <T> List<T> executeQuery(Connection connection,QueryCallback queryCallback,Integer limit,String sql,Object... parameters) throws SQLException{
    return execute_query(connection,queryCallback,limit,null,sql,parameters);
  }


  /**
   * QUERY
   * @param connection
   * @param queryCallback
   * @param sql
   * @param parameters
   * @param <T>
   * @return
   * @throws SQLException
   */
  public static <T> List<T> executeQuery(Connection connection,QueryCallback queryCallback,String sql,Object... parameters) throws SQLException{
    return execute_query(connection,queryCallback,null,null,sql,parameters);
  }


  private static int execute_update(Connection connection,SqlExecuteEvent executeEvent,String sql,Object... parameters) throws SQLException {
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = connection.prepareStatement(sql);
      setParameters(preparedStatement,parameters);
      if (null != executeEvent){
        executeEvent.executeBefore();
      }
      return preparedStatement.executeUpdate();
    }finally {
        if (null != executeEvent){
          executeEvent.executeAfter();
        }
        if (preparedStatement != null){
          try {
            preparedStatement.close();
          }catch (SQLException e){
            log.error("PreparedStatement Close Error");
          }

        }
    }
  }


  private static <T> List<T> execute_query(Connection connection,QueryCallback<T> callback,Integer limit, SqlExecuteEvent event,String sql,Object... parameters) throws SQLException{
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      preparedStatement = connection.prepareStatement(sql);
      setParameters(preparedStatement,parameters);
      if (null != limit){
          preparedStatement.setMaxRows(limit);
      }
      if (null != event){
         event.executeBefore();
      }
      resultSet = preparedStatement.executeQuery();
      int fetchSize = resultSet.getFetchSize();
      List<T> result = new ArrayList<T>(fetchSize > 5 ? fetchSize : 5);
      while (resultSet.next()) {
        result.add(callback.mappingRow(resultSet));
      }
      return result;
    } finally {
      if (null != event){
        event.executeAfter();
      }
      if (null != resultSet){
        try {
          resultSet.close();
        } catch (SQLException e) {
          log.error("ResultSet Close Error");
        }
        if (null != preparedStatement){
          try {
            preparedStatement.close();
          } catch (SQLException e) {
            log.error("PreparedStatement Close Error");
          }
        }
      }
    }
  }

  /**
   * GetColumnNames by tableName;
   * @param connection
   * @param entity
   * @return
   * @throws SQLException
   */
  public static List<String> getColumnNames(Connection connection,String entity) throws SQLException{
    ResultSet resultSet = null;
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      resultSet = databaseMetaData.getColumns(null,null,entity,null);
      List<String> columns = new ArrayList<>(resultSet.getFetchSize());
      while (resultSet.next()){
        columns.add(resultSet.getString(4));
      }
      return columns;
    } finally {
      if (null != resultSet){
        resultSet.close();
      }
    }
  }


  /**
   *
   * @param connection
   * @param entity
   * @return
   * @throws SQLException
   */
  public static List<ColumnMeta> getColumn(Connection connection, String entity) throws SQLException{
    ResultSet resultSet = null;
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      List<ColumnMeta> columnMetas = Lists.newArrayList();
      resultSet =databaseMetaData.getColumns(null,null,entity,null);
      while (resultSet.next()){
        ColumnMeta columnMeta = new ColumnMeta();
        columnMeta.setName(resultSet.getString(4));
        columnMeta.setType(resultSet.getInt(5));
        columnMetas.add(columnMeta);
      }
      return columnMetas;
    } finally {
      if (null != resultSet) {
        resultSet.close();
      }
    }
  }


  /**
   * ResultSet to Map
   * @param resultSet
   * @return
   * @throws SQLException
   */
  public static Map<String,Object> convert2Map(ResultSet resultSet) throws SQLException{
    ResultSetMetaData metaData = resultSet.getMetaData();
    int columnCount = metaData.getColumnCount();
    Map<String, Object> row = new HashMap<String, Object>(columnCount);
    for (int i = 1; i < columnCount; i++) {
      row.put(metaData.getColumnLabel(i), resultSet.getObject(i));
    }
    return row;
  }



  public static void setParameters(PreparedStatement statement,Object... parameters) throws SQLException {
    if (null == parameters || parameters.length < 1){
      return;
    }
    int i = 1;
    for (Object parameter : parameters){
      if (parameter == null){
        statement.setNull(i++, Types.VARCHAR);
      }else {
        statement.setObject(i++,parameter);
      }
    }
  }


  public static void closeConnection(Connection connection) {
    try {
      if (null != connection && !connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException e) {
      //ignore
    }
  }



}
