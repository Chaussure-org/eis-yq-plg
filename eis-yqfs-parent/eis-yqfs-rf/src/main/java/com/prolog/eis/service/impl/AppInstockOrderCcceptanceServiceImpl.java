package com.prolog.eis.service.impl;

import com.prolog.eis.dao.*;
import com.prolog.eis.dto.apprf.AppInstockOrderCcceptanceDto;
import com.prolog.eis.model.apprf.*;
import com.prolog.eis.service.AppInstockOrderCcceptanceService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AppInstockOrderCcceptanceServiceImpl implements AppInstockOrderCcceptanceService {

    @Autowired
    private AppInboundTaskHzMapper appInboundTaskHzMapper;

    @Autowired
    private AppGoodsMapper appGoodsMapper;

    @Autowired
    private AppInboundTaskMxMapper appInboundTaskMxMapper;

    @Autowired
    private AppInboundTaskHzHisMapper appInboundTaskHzHisMapper;

    @Autowired
    private AppInboundTaskMxHisMapper appInboundTaskMxHisMapper;

    @Autowired
    private AppContainerSubInfoMapper appContainerSubInfoMapper;

    @Autowired
    private AppContainerInfoMapper appContainerInfoMapper;

    @Autowired
    private AppEmptyContainerInboundTaskMapper appEmptyContainerInboundTaskMapper;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * @title 查询货格详情, 通过结果总条数确认货格的类型
     * @author jinxf
     * @Param: containerNo 料箱号
     * @time 2020/4/26 17:30
     * @retrun
     */
    @Override
    public List<AppInstockOrderCcceptanceDto> query(String containerNo) throws Exception {
        if (StringUtils.isEmpty(containerNo)) {
            throw new Exception("料箱号不能为空！");
        }
        return appContainerSubInfoMapper.query(containerNo);
        //return appInboundTaskMxMapper.query(containerNo);
    }

    /**
     * @title 入库收货
     * @author jinxf
     * @Param: containerNo 料箱号
     * @Param: gridNumber 格口号
     * @Param: 商品Id goodsId
     * @Param: 有效期 expiryDate
     * @Param: 数量 goodsNum
     * @time 2020/5/6 16:16
     * @retrun
     */
    @Override
    @Transactional
    public void add(String containerNo, String containerSubNo, String goodsId, String expiryDate, String goodsNum, String inboundTime, String referenceNum) throws Exception {
        if (StringUtils.isEmpty(containerNo)) {
            throw new Exception("箱号不能为空！");
        }

        if (StringUtils.isEmpty(goodsId)) {
            throw new Exception("商品Id不能为空！");
        }

        if (StringUtils.isEmpty(goodsNum)) {
            throw new Exception("商品数量不能为空！");
        }

        if (Integer.parseInt(goodsNum) < 0) {
            throw new Exception("商品数量不能为负数！");
        }


        Criteria criteria = Criteria.forClass(AppInboundTaskModel.class);
        criteria.setRestriction(Restrictions.eq("containerNo", containerNo));
        List<AppInboundTaskModel> hzList = appInboundTaskHzMapper.findByCriteria(criteria);
        if (hzList != null && hzList.size() < 1) {
            throw new Exception("请先扫描料箱！");
        }

        criteria = Criteria.forClass(AppContainerSubModel.class);
        criteria.setRestriction(Restrictions.eq("containerNo", containerNo));
        criteria.setRestriction(Restrictions.eq("containerSubNo", containerSubNo));
        List<AppContainerSubModel> sublist = appContainerSubInfoMapper.findByCriteria(criteria);
        if (sublist.size() == 1) {
            AppContainerSubModel subModel = sublist.get(0);
            subModel.setCommodityId(Integer.parseInt(goodsId));
            subModel.setCommodityNum(Integer.parseInt(goodsNum));
            if (!StringUtils.isEmpty(expiryDate)) {
                subModel.setExpiryDate(sdf.parse(expiryDate));
            }
            subModel.setGmtCreateTime(new Date());
            subModel.setInboundTime(sdf.parse(inboundTime));
            subModel.setReferenceNum(Integer.parseInt(referenceNum));
            appContainerSubInfoMapper.update(subModel);
        } else {
            throw new Exception("货格信息不存在");
        }
    }

    /**
     * @title 入库(料箱)取消
     * @author jinxf
     * @Param: inboundTaskHzId 入库汇总Id
     * @Param: containerSubNo 子容器编号
     * @time 2020/4/26 19:24
     */
    @Override
    @Transactional
    public void cancel(String inboundTaskHzId, String containerSubNo) throws Exception {
        if (StringUtils.isEmpty(inboundTaskHzId)) {
            throw new Exception("入库汇总Id不能为空！");
        }


        if (StringUtils.isEmpty(containerSubNo)) {
            throw new Exception("子容器号不能为空！");
        }

        Criteria crt = Criteria.forClass(AppInboundTaskMxModel.class);
        crt.setRestriction(Restrictions.and(Restrictions.eq("inboundTaskHzId", inboundTaskHzId), Restrictions.eq("containerSubNo", containerSubNo)));
        appInboundTaskMxMapper.deleteByCriteria(crt);

        //明细没数据删除主表
        Criteria crit = Criteria.forClass(AppInboundTaskMxModel.class);
        crit.setRestriction(Restrictions.eq("containerSubNo", containerSubNo));
        List<AppInboundTaskMxModel> mxList = appInboundTaskMxMapper.findByCriteria(crit);
        if (mxList == null || mxList.size() == 0) {
            appInboundTaskHzMapper.deleteById(inboundTaskHzId, AppInboundTaskModel.class);
        }

    }

    @Override
    public AppGoodsModel goodsQuery(String goodsNo) throws Exception {
        if (StringUtils.isEmpty(goodsNo)) {
            throw new Exception("商品编号不能为空！");
        }
        Criteria crt = Criteria.forClass(AppGoodsModel.class);
        crt.setRestriction(Restrictions.eq("goodsNo", goodsNo));
        List<AppGoodsModel> list = appGoodsMapper.findByCriteria(crt);
        if (list != null && list.size() < 1) {
            throw new Exception("商品信息不存在！");
        }

        return list.get(0);
    }

    @Override
    public String boxQuery(String containerNo, String userId) throws Exception {
        if (StringUtils.isEmpty(containerNo)) {
            throw new Exception("料箱号不能为空！");
        }

        if (StringUtils.isEmpty(userId)) {
            throw new Exception("登录用户不能为空！");
        }


        Criteria crt = Criteria.forClass(AppContainerSubModel.class);
        crt.setRestriction(Restrictions.eq("containerNo", containerNo));
        List<AppContainerSubModel> list = appContainerSubInfoMapper.findByCriteria(crt);
        if (list == null || list.size() == 0) {
            throw new Exception("料箱不存在！");
        }

        Criteria empty = Criteria.forClass(AppEmptyContainerInboundTaskModel.class);
        empty.setRestriction(Restrictions.eq("containerNo", containerNo));
        List<AppEmptyContainerInboundTaskModel> emptyList = appEmptyContainerInboundTaskMapper.findByCriteria(empty);
        if (emptyList.size() > 0) {
            throw new Exception("该料箱已空箱入库！");
        }

        if (appInboundTaskHzMapper.checkContainer(containerNo).size() > 0) {
            throw new Exception("库存中已存在该料箱号！");
        }


        //保存汇总
        Criteria criteria = Criteria.forClass(AppInboundTaskModel.class);
        criteria.setRestriction(Restrictions.eq("containerNo", containerNo));
        List<AppInboundTaskModel> hzList = appInboundTaskHzMapper.findByCriteria(criteria);

        if (hzList != null && hzList.size() < 1) {
            AppInboundTaskModel hz = new AppInboundTaskModel();
            hz.setInboundStatus(1);
            hz.setContainerNo(containerNo);
            hz.setCreateTime(new Date());
            hz.setOperator(userId);
            long save = appInboundTaskHzMapper.save(hz);
            if (save < 1) {
                throw new Exception("入库汇总保存失败!");
            }
        }

        if (list.size() == 1) {
            return "1";
        } else if (list.size() == 2) {
            return "2";
        } else if (list.size() == 4) {
            return "3";
        } else {
            throw new Exception("料箱类型无法判断,确认此料箱验收是否完成！");
        }
    }

}
