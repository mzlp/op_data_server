package com.syproject.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;


/**
 * @author:lee
 * @Date:2018/7/20
 * @description:
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseBody
  private ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request){
    ErrorDetails errorDetails = new ErrorDetails(new Date(),ex.getMessage(),request.getDescription(false));

    return new ResponseEntity<Object>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
  }


  @ExceptionHandler(BaseException.class)
  @ResponseBody
  private ResponseEntity<Object> handleBaseExceptions(BaseException baseException,WebRequest request){
    ErrorDetails errorDetails = new ErrorDetails(new Date(),baseException.getMessage(),request.getDescription(false));
    return new ResponseEntity<Object>(errorDetails,baseException.httpStatus);
  }




}
