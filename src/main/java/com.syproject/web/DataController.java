package com.syproject.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:lee
 * @Date:2018/7/30
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/data/sync")
public class DataController {


  @GetMapping("/queryData")
  public ResponseEntity<Object> queryData(){


    return new ResponseEntity<Object>("", HttpStatus.OK);
  }
}
