package com.syproject.exception;

import org.springframework.http.HttpStatus;

/**
 * @author:lee
 * @Date:2018/7/20
 * @description:业务操作异常基类 用于接口返回
 */
public class BusinessException extends BaseException {

  public BusinessException(HttpStatus httpStatus) {
    super(httpStatus);
  }

  public BusinessException(HttpStatus httpStatus, String message) {
    super(httpStatus, message);
  }
}
