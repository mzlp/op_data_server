package com.syproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author:lee
 * @Date:2018/7/20
 * @description:
 */
public class BaseException extends RuntimeException {

  protected HttpStatus httpStatus;
  protected String message;

  public BaseException(){
    super();
  }

  public BaseException(HttpStatus httpStatus) {
    this(httpStatus, null);
  }

  public BaseException(HttpStatus httpStatus,String message){
    super(message);
    this.httpStatus = httpStatus;
    this.message = buildMsg(message);
  }

  private String buildMsg(String msg){
    String value = null;
    if (value == null) {
      value = getDefaultErrorMsg();
    }
    if (msg != null) {
      value = value.replace("{msg}", msg);
    }
    return value;
  }

  protected String getDefaultErrorMsg() {
    return "未知异常";
  }
}
