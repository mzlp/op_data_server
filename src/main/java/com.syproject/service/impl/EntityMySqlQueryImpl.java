package com.syproject.service.impl;

import com.syproject.domian.Racetrack;
import com.syproject.exception.BaseException;
import com.syproject.persistence.QueryCallback;
import com.syproject.persistence.dto.PropertyMapper;
import com.syproject.persistence.dto.SqlStatement;
import com.syproject.persistence.helper.PersistenceHelper;
import com.syproject.service.EntityQuery;
import com.syproject.service.QueryEntityDescriptor;
import com.syproject.service.SqlConnectionPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:lee
 * @Date:2018/7/27
 * @description:
 */
@Component
@Slf4j
public class EntityMySqlQueryImpl implements EntityQuery {

  @Value("${sqlTimeout}")
  private long sqlTimeout;

  @Autowired
  private SqlConnectionPool sqlConnectionPool;

  @Override
  public List<Map<String, Object>> query(Racetrack racetrack, QueryEntityDescriptor queryEntityDescriptor) throws BaseException {
    SqlStatement sqlStatement = generateQueryStatement(queryEntityDescriptor);
    return _query(racetrack,sqlStatement,queryEntityDescriptor);
  }


  private List<Map<String,Object>> _query(Racetrack racetrack,SqlStatement sqlStatement,QueryEntityDescriptor queryEntityDescriptor) throws BaseException {
    Connection connection = null;
    Object[] parameters = null;
    try {
      parameters = getParameters(queryEntityDescriptor,sqlStatement.getPropertyMapper());
      log.info(sqlStatement.getSql() + " [" + StringUtils.join(parameters,",") + "]");
      if (log.isDebugEnabled()){
          log.debug(sqlStatement.getSql() + " [" + StringUtils.join(parameters,",") + "]");
      }
      SqlExecuteStatEvent sqlExecuteStatEvent = new SqlExecuteStatEvent(racetrack.getEndpoint()+queryEntityDescriptor.queryEntityName(),sqlStatement.getSql(),sqlTimeout);
      connection = sqlConnectionPool.getConnection(racetrack);
      return PersistenceHelper.executeQuery(connection,new QueryCallback(),null,sqlExecuteStatEvent,sqlStatement.getSql(),parameters);
    } catch (Exception e) {
      log.error("table: " + queryEntityDescriptor.queryEntityName()
              + "cause: " + e.getLocalizedMessage(), e);
      throw new BaseException(HttpStatus.BAD_REQUEST,"entity: " + queryEntityDescriptor.queryEntityName() + e.getLocalizedMessage());
    }finally {
      PersistenceHelper.closeConnection(connection);
    }
  }


  private SqlStatement generateQueryStatement(QueryEntityDescriptor entityDescriptor){
    String sql = "Select ${columns}  From ${entity} Where ${condition} Order by ${sort} asc Limit ${pageNo},${pageSize}";
    sql = sql.replace("${entity}",entityDescriptor.queryEntityName());
    sql = sql.replace("${columns}", StringUtils.join(entityDescriptor.fields(),","));
    PropertyMapper propertyMapper = new PropertyMapper();
    String [] identities = entityDescriptor.identities();
    List<String> conditions = new ArrayList<String>(identities.length);

    for (String property : identities) {
      conditions.add(property + " = ?");
      propertyMapper.put(property, property);
    }
    String timestampName = entityDescriptor.timestampName();

    //时间增量同步
    if (!entityDescriptor.syncType()){
      //Fixme
      conditions.add(timestampName + " between " + "?" + " and " + "?");
      propertyMapper.add(entityDescriptor.startDateKey(), timestampName);
      propertyMapper.add(entityDescriptor.endDateKey(), timestampName);
    }
    sql = sql.replace("${sort}",timestampName);
    sql = sql.replace("${condition}", StringUtils.join(conditions, " AND "));

    sql = sql.replace("${pageNo}",Integer.toString(entityDescriptor.pageNo()));
    sql = sql.replace("${pageSize}",Integer.toString(entityDescriptor.pageSize()));

    SqlStatement sqlStatement = new SqlStatement();
    sqlStatement.setSql(sql);
    sqlStatement.setPropertyMapper(propertyMapper);
    return  sqlStatement;
  }



    private  Object[] getParameters(QueryEntityDescriptor entityDescriptor, PropertyMapper propertyMapper) {
    Object[] parameters = new Object[propertyMapper.size()];
    int i = 0;
    for (String property : propertyMapper.keyArray()) {
      parameters[i++] = entityDescriptor.value(property);
    }
    return parameters;
  }





}
