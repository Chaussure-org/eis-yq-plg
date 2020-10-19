package com.prolog.eis.service.log.impl;

import com.prolog.eis.dao.log.LogMapper;
import com.prolog.eis.model.log.Log;
import com.prolog.eis.service.log.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/7/20 21:44
 */

@Service
public class LogServiceImpl implements ILogService {

    @Autowired
    private LogMapper mapper;
    @Override
    public void save(Log log) {
        mapper.save(log);
    }

    @Override
    public void update(Log log) {
        mapper.update(log);
    }
}
