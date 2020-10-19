package com.prolog.eis.service.masterbase.impl;

import java.util.Date;
import java.util.List;

import com.prolog.eis.dao.masterbase.LotinfoMapper;
import com.prolog.eis.model.masterbase.Lotinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prolog.eis.dao.masterbase.OwnerMapper;
import com.prolog.eis.model.masterbase.Owner;
import com.prolog.eis.service.masterbase.OwnerService;
import com.prolog.eis.util.PrologDateUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Order;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OwnerServiceImpl implements OwnerService {
	
	@Autowired
	private OwnerMapper ownerMapper;
	@Autowired
	private LotinfoMapper lotinfoMapper;


	@Override
	public List<Owner> findAllOwner()throws Exception {
		Criteria owner = Criteria.forClass(Owner.class);
		owner.setOrder(Order.newInstance().asc("id"));
		List<Owner> owners = ownerMapper.findByCriteria(owner);
		return owners;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOwner(Owner owner)throws Exception {
		//初始化数据，删除以前数据
		lotinfoMapper.deleteByMap(null, Lotinfo.class);
		ownerMapper.deleteByMap(null,Owner.class);
		// supplier.setCreate_Time(PrologDateUtils.parseObject(new Date()));
		owner.setCreateTime(PrologDateUtils.parseObject(new Date()));
		ownerMapper.save(owner);
	}

	@Override
	public void updateOwner(Owner owner)throws Exception {
		if (owner.getId() == 0) {
			throw new Exception("业主id不能为空");
		}
		ownerMapper.update(owner);
	}
	
	

}
