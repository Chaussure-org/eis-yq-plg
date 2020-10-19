package com.prolog.eis.pickstation.service;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/7/25 18:38
 */
public interface PrologDeleteTaskService {

    void deleteTask(String containerNo, int type) throws Exception;
}
