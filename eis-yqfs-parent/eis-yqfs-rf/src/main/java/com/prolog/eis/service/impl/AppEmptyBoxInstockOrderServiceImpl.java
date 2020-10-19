package com.prolog.eis.service.impl;

import com.prolog.eis.dao.*;
import com.prolog.eis.model.apprf.AppContainerModel;
import com.prolog.eis.model.apprf.AppContainerSubModel;
import com.prolog.eis.model.apprf.AppEmptyBoxInstockOrderModel;
import com.prolog.eis.model.apprf.AppEmptyContainerInboundTaskModel;
import com.prolog.eis.service.AppEmptyBoxInstockOrderService;
import com.prolog.eis.util.PrologDateUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class AppEmptyBoxInstockOrderServiceImpl implements AppEmptyBoxInstockOrderService {
    @Autowired
    private AppEmptyBoxInstockOrderMapper appEmptyBoxInstockOrderMapper;

    @Autowired
    private AppEmptyContainerInboundTaskMapper appEmptyContainerInboundTaskMapper;

    @Autowired
    private AppInboundTaskMxMapper appInboundTaskMxMapper;

    @Autowired
    private AppCargoCaseValidationMapper appCargoCaseValidationMapper;

    @Autowired
    private AppContainerSubInfoMapper appContainerSubInfoMapper;

    @Autowired
    private AppInboundTaskHzMapper appInboundTaskHzMapper;

    /**
     * @title 空箱入库保存
     * @author jinxf
     * @Param: containerNo 箱号
     * @time 2020/4/28 10:37
     */
    @Override
    @Transactional
    public AppEmptyContainerInboundTaskModel add(String containerNo) throws Exception {
        if (StringUtils.isEmpty(containerNo)) {
            throw new Exception("料箱号不能为空！");
        }

        Criteria crt = Criteria.forClass(AppEmptyContainerInboundTaskModel.class);
        crt.setRestriction(Restrictions.eq("containerNo", containerNo));
        List<AppEmptyBoxInstockOrderModel> list = appEmptyBoxInstockOrderMapper.findByCriteria(crt);
        if (list != null && list.size() > 0) {
            throw new Exception("空箱已入库!");
        }


        if (appInboundTaskHzMapper.checkContainer(containerNo).size() > 0) {
            throw new Exception("库存中已存在该料箱号！");
        }

        AppContainerModel _model = appCargoCaseValidationMapper.findById(containerNo, AppContainerModel.class);
        if (_model == null) {
            throw new Exception("料箱未验证!");
        }

        Criteria criteria = Criteria.forClass(AppContainerSubModel.class);
        criteria.setRestriction(Restrictions.and(Restrictions.eq("containerNo", containerNo),
                Restrictions.isNotNull("commodityId")));
        List<AppContainerSubModel> sublist = appContainerSubInfoMapper.findByCriteria(criteria);
        if (sublist.size() > 0) {
            throw new Exception("该料箱已有货格入库!");
        }

        AppEmptyContainerInboundTaskModel model = new AppEmptyContainerInboundTaskModel();
        model.setContainerNo(containerNo);
        model.setCreateTime(PrologDateUtils.parseObject(new Date()));
        long save = appEmptyContainerInboundTaskMapper.save(model);
        if (save < 1) {
            throw new Exception("保存失败!");
        }
        return model;
    }

    @Override
    public void cancel(String containerNo) throws Exception {
        Criteria criteria = Criteria.forClass(AppEmptyContainerInboundTaskModel.class);
        criteria.setRestriction(Restrictions.eq("containerNo", containerNo));
        List<AppEmptyContainerInboundTaskModel> list = appEmptyContainerInboundTaskMapper.findByCriteria(criteria);
        if (list.size() == 0) {
            throw new Exception("未找到该空箱!");
        }
        appEmptyContainerInboundTaskMapper.deleteByCriteria(criteria);
    }
}
