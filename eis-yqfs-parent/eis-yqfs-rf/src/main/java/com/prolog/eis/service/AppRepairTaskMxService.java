package com.prolog.eis.service;


import java.util.HashMap;
import java.util.List;

/**
 * @author zhousx
 */
public interface AppRepairTaskMxService {
    public List<HashMap<String,Object>> query(String containerNo) throws Exception;
}
