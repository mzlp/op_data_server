package com.syproject.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.syproject.domian.Racetrack;
import com.syproject.service.EntityQuery;
import com.syproject.service.impl.BonecpConnectionPoolConfig;
import com.syproject.service.impl.ConnConfigProvider;
import com.syproject.service.impl.EntityMySqlQueryImpl;
import com.syproject.util.SimpleQueryEntityDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;


/**
 * @author:lee
 * @Date:2018/7/11
 * @description:
 */
@RestController
@Slf4j
public class HelloController {

  @Autowired
  private ConnConfigProvider connConfigProvider;
  @Autowired
  private BonecpConnectionPoolConfig connectionPoolConfig;

  @Autowired
  private EntityQuery entityMySqlQuery;


  @RequestMapping("/index")
  public String index(){
    log.info("111111111111");
    Racetrack racetrack = connConfigProvider.getRacetrack("11111");
    log.info(racetrack.toString());
    log.info("first project!!!");
    return "Spring Boot";
  }

  @RequestMapping("/test")
  public ResponseEntity<Object> test() throws SQLException {
    Racetrack racetrack = connConfigProvider.getRacetrack("conn_eyearcon");
    Connection connection = connectionPoolConfig.getConnection(racetrack);
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery(" select * from plt_taobao_customer limit 10 ");
    while(rs.next()){
      log.info(rs.getString(1));
      log.info(rs.getString(2));
      log.info(rs.getString(3));
    }
    return null;
  }


  @RequestMapping("/querytest")
  public ResponseEntity<Object> querytest() throws SQLException {
    Racetrack racetrack = connConfigProvider.getRacetrack("conn_eyearcon");
    SimpleQueryEntityDescriptor entityDescriptor = new SimpleQueryEntityDescriptor();
    entityDescriptor.setTableName("plt_taobao_customer");
    String [] fields = new String[]{"customerno","full_name","sex","zip","address","city","state","country","email","mobile","phone","last_sync_update"};
    entityDescriptor.setFields(fields);
    entityDescriptor.setSyncType(true);
    entityDescriptor.setIdentities(new String[]{"zip"});
    entityDescriptor.setTimestampName("last_sync_update");
    entityDescriptor.setPageNo(0);
    entityDescriptor.setPageSize(2000);
    Map<String ,Object> map = Maps.newHashMap();
    map.put("zip","000000");
    map.put("start_time","2017-09-01 11:40:47");
    map.put("end_time","2017-09-10 11:40:47");
    entityDescriptor.setValues(map);
    List<Map<String,Object>> list =  entityMySqlQuery.query(racetrack,entityDescriptor);
    return new ResponseEntity<Object>(list, HttpStatus.OK);
  }

}
