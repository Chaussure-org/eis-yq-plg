package com.prolog.eis.service.inbound.impl;

import com.prolog.eis.dao.inbound.EmptyInboundTaskHisMapper;
import com.prolog.eis.dao.inbound.EmptyInboundTaskMapper;
import com.prolog.eis.model.inbound.EmptyInboundTask;
import com.prolog.eis.model.inbound.EmptyInboundTaskHis;
import com.prolog.eis.service.inbound.IEmptyInboundTaskService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class EmptyInboundTaskServiceImpl implements IEmptyInboundTaskService {
    @Autowired
    private EmptyInboundTaskMapper emptyInboundTaskMapper;
    @Autowired
    private EmptyInboundTaskHisMapper emptyInboundTaskHisMapper;
    @Override
    public List<EmptyInboundTask> getEmptyInboundTask(Map map) throws Exception {
        return emptyInboundTaskMapper.findByMap(map,EmptyInboundTask.class);
    }

    @Override
    public void toEmptyInboundHis(String containerNo) throws Exception {
        List<EmptyInboundTask> emptyInboundTasks = emptyInboundTaskMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), EmptyInboundTask.class);
        EmptyInboundTask emptyInboundTask = emptyInboundTasks.get(0);
        EmptyInboundTaskHis emptyInboundTaskHis = new EmptyInboundTaskHis();
        BeanUtils.copyProperties(emptyInboundTask,emptyInboundTaskHis);
        emptyInboundTaskHisMapper.save(emptyInboundTaskHis);
        emptyInboundTaskMapper.deleteByMap(MapUtils.put("containerNo", containerNo).getMap(), EmptyInboundTask.class);
    }

}
