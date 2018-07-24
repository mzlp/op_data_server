package com.syproject.web;

import com.syproject.domian.ConnConfig;
import com.syproject.repository.ConnConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author:lee
 * @Date:2018/7/18
 * @description:
 */
@RestController
@Slf4j
@RequestMapping("/connconfig")
public class ConnConfigController {

  @Autowired
  private ConnConfigRepository configRepository;

  @PostMapping("/put")
  @ResponseBody
  public ResponseEntity<Object> sava(@RequestBody ConnConfig connConfig) throws Exception{
    log.info("connConfig--->{}",connConfig.toString());
    configRepository.save(connConfig);
    return new ResponseEntity<Object>(connConfig,HttpStatus.CREATED);
  }
}
