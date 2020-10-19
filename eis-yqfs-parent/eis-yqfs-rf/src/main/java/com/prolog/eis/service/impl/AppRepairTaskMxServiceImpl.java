package com.prolog.eis.service.impl;
import com.prolog.eis.dao.AppRepairTaskMxMapper;
import com.prolog.eis.service.AppRepairTaskMxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;

@Service
public class AppRepairTaskMxServiceImpl implements AppRepairTaskMxService {

    @Autowired
    private AppRepairTaskMxMapper appRepairTaskMxMapper;

    @Override
    public List<HashMap<String, Object>> query(String containerNo) throws Exception {
         List<HashMap<String, Object>> list = appRepairTaskMxMapper.query(containerNo);
        if (list.size() == 0) {
            throw new Exception("料箱号不存在！");
        }
        appRepairTaskMxMapper.updateStation(containerNo);
        return list;
    }
}
