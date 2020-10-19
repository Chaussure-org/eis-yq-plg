package com.prolog.eis.service.monitor;

import java.util.Map;
/**
 * @Author wangkang
 * @Description service
 * @CreateTime 2020/8/26 16:47
 */
public interface ISpCountService {

    Map<Integer,Integer> getSpCount() throws Exception;
}
