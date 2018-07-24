package com.syproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


/**
 * @author:lee
 * @Date:2018/7/20
 * @description:
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ErrorDetails {

  private Date timestamp;
  private String message;
  private String details;
}

