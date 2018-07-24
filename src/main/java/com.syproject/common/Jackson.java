package com.syproject.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author:lee
 * @Date:2018/7/18
 * @description:
 */
public class Jackson {

  static ObjectMapper mapper = new ObjectMapper();

  static {
    // SerializationFeature for changing how JSON is written	序列化参数
    // to enable standard indentation ("pretty-printing"):	整理为漂亮的格式
    mapper.disable(SerializationFeature.INDENT_OUTPUT);

    // to allow serialization of "empty" POJOs (no properties to serialize)
    // (without this setting, an exception is thrown in those cases)	允许序列化没有属性的类
    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    // to write java.util.Date, Calendar as number (timestamp):	把时间格式化为时间戳
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public static String asString(Object object){
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }
}
