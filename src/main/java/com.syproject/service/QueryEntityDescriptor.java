package com.syproject.service;

/**
 * @author:lee
 * @Date:2018/7/27
 * @description:
 */
public interface QueryEntityDescriptor {

  /**表名**/
  String queryEntityName();

  /**要查询的列**/
  String[] fields();

  /**是否全量**/
  boolean syncType();

  /**查询条件**/
  String[] identities();

  /**时间增量字段**/
  String timestampName();

  /**参数值**/
  Object value(String key);

  /**开始时间**/
  String startDateKey();

  /**结束时间**/
  String endDateKey();

  /**页码**/
  Integer pageNo();

  /**页码**/
  Integer pageSize();
}
