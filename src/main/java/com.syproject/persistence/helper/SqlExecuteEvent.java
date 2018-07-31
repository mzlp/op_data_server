package com.syproject.persistence.helper;

/**
 * @author:lee
 * @Date:2018/7/26
 * @description:
 */
public interface SqlExecuteEvent {

    void executeBefore();

    void executeAfter();
}
