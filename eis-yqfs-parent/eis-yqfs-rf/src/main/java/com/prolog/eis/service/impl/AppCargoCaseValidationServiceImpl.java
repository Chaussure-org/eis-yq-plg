package com.prolog.eis.service.impl;

import com.prolog.eis.dao.AppCargoCaseValidationMapper;
import com.prolog.eis.dao.AppContainerInfoMapper;
import com.prolog.eis.dao.AppContainerSubInfoMapper;
import com.prolog.eis.model.apprf.AppContainerModel;
import com.prolog.eis.model.apprf.AppContainerSubModel;
import com.prolog.eis.service.AppCargoCaseValidationService;
import com.prolog.framework.common.message.RestMessage;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
  * @author jinxf
  * @time 2020/4/22 20:01
  */
@Service
public class AppCargoCaseValidationServiceImpl implements AppCargoCaseValidationService {

	@Autowired
	private AppCargoCaseValidationMapper appCargoCaseValidationMapper;

	@Autowired
	private AppContainerInfoMapper appContainerInfoMapper;

	@Autowired
	private AppContainerSubInfoMapper appContainerSubInfoMapper;

	/**
	 * @title 查询料箱信息
	 * @author jinxf
	 * @Param: containerNo 料箱号
	 * @time 2020/4/22 20:02
	 */
	@Override
	public AppContainerModel query(String containerNo) throws Exception {
		if (StringUtils.isEmpty(containerNo)) {
			throw new Exception("料箱号不能为空！");
		}

		AppContainerModel model = appCargoCaseValidationMapper.findById(containerNo, AppContainerModel.class);
		if (model != null) {
			throw new Exception("料箱已存在！");
		}
		return model;
	}

	/**
	 * @title 料箱子容器新增
	 * @author jinxf
	 * @Param: containerNo 子容器号
	 * @Param: containerSubNo 条码箱
	 * @Param: type 箱型 1 整箱 2 日字 3 田字
	 * @time 2020/4/22 20:18
	 */
	@Override
	@Transactional
	public RestMessage<AppContainerSubModel> save(String containerNo, String containerSubNos, String type) throws Exception {
		if (StringUtils.isEmpty(containerNo)) {
			throw new Exception("料箱号不能为空！");
		}
		if (StringUtils.isEmpty(containerSubNos)) {
			throw new Exception("子容器号不能为空！");
		}
		if (StringUtils.isEmpty(type)) {
			throw new Exception("箱型不能为空！");
		}

		int gekouSum =0;
		if("1".equals(type)){
			gekouSum=1;
		}else if("2".equals(type)){
			gekouSum=2;
		}else if("3".equals(type)){
			gekouSum=4;
		}else{
			throw new Exception("箱料类型不正确!");
		}

		String[] nos = containerSubNos.split("~");

		if(gekouSum!=nos.length){
			throw new Exception("扫描的子容器数量和该类型数量不匹配!");
		}

		//添加主料箱
		AppContainerModel info = new AppContainerModel();
		info.setContainerNo(containerNo);
		info.setLayoutType(Integer.parseInt(type));
		appCargoCaseValidationMapper.save(info);

		for (String no : nos) {
			Criteria crt = Criteria.forClass(AppContainerSubModel.class);
			crt.setRestriction(Restrictions.and(Restrictions.eq("containerNo", containerNo), Restrictions.eq("containerSubNo", no)));
			List<AppContainerSubModel> subInfoModelList = appContainerSubInfoMapper.findByCriteria(crt);
			if(subInfoModelList!=null && subInfoModelList.size()>0) {
				throw new Exception("子容器号["+no+"]信息已存在,请不要重复添加");
			}
			AppContainerSubModel container =new AppContainerSubModel();
			container.setContainerSubNo(no);
			container.setContainerNo(containerNo);
			appContainerSubInfoMapper.save(container);
		}
		return RestMessage.newInstance(true, "操作全部完成", null);
	}
}
