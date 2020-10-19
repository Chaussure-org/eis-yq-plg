package com.prolog.eis.service.impl;

import com.prolog.eis.dao.*;
import com.prolog.eis.dto.apprf.AppInstockOrderCcceptanceDto;
import com.prolog.eis.model.apprf.AppInboundTaskModel;
import com.prolog.eis.service.AppInstockOrderCancelService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author jinxf
 * @title
 * @time 2020/5/8 9:47
 */
@Service
public class AppInstockOrderCancelServiceImpl implements AppInstockOrderCancelService {
    @Autowired
    private AppInboundTaskHzMapper appInboundTaskHzMapper;

    @Autowired
    private AppInboundTaskHzHisMapper appInboundTaskHzHisMapper;

    @Autowired
    private AppInboundTaskMxMapper appInboundTaskMxMapper;

    @Autowired
    private AppInboundTaskMxHisMapper appInboundTaskMxHisMapper;

    @Autowired
    private AppContainerSubInfoMapper appContainerSubInfoMapper;


    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * @title 查询货格详情
     * @author jinxf
     * @Param: containerNo 料箱号
     * @time 2020/4/26 17:30
     */
    @Override
    public List<AppInstockOrderCcceptanceDto> query(String containerNo) throws Exception {
        if (StringUtils.isEmpty(containerNo)) {
            throw new Exception("料箱号不能为空！");
        }
        return appContainerSubInfoMapper.query(containerNo);
        //return  appInboundTaskMxMapper.query(containerNo);
    }

    /**
     * @title 入库取消(整单取消)
     * @author jinxf
     * @Param: containerNo 料箱号
     * @time 2020/4/26 19:24
     */
    @Override
    @Transactional
    public void cancel(String containerNo) throws Exception {
        if (StringUtils.isEmpty(containerNo)) {
            throw new Exception("容器号不能为空！");
        }

        Criteria hzcrt = Criteria.forClass(AppInboundTaskModel.class);
        hzcrt.setRestriction(Restrictions.eq("containerNo", containerNo));
        List<AppInboundTaskModel> hzlist = appInboundTaskHzMapper.findByCriteria(hzcrt);
        if (hzlist != null && hzlist.size() < 1) {
            throw new Exception("入库汇总信息不存在！");
        }
        appContainerSubInfoMapper.cancel(containerNo);
        appInboundTaskHzMapper.deleteByCriteria(hzcrt);
    }
}
