package com.prolog.eis.pickstation.eis.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.prolog.eis.pickstation.dto.LxDetailsDto;
import com.prolog.eis.pickstation.dto.LxSubDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prolog.eis.dao.masterbase.GoodsBarCodeMapper;
import com.prolog.eis.dao.store.ZtStoreMapper;
import com.prolog.eis.dto.store.ZtLxDetailsDto;
import com.prolog.eis.model.masterbase.GoodsBarCode;
import com.prolog.eis.pickstation.eis.EisStationInterfaceService;
import com.prolog.framework.utils.MapUtils;

@Service
public class EisStationInterfaceServiceImpl implements EisStationInterfaceService{

	@Autowired
	private ZtStoreMapper ztStoreMapper;
	@Autowired
	private GoodsBarCodeMapper goodsBarCodeMapper;

	@Override
	public LxDetailsDto queryLxDetails(String lxNo) throws Exception {
		List<ZtLxDetailsDto> dtos = ztStoreMapper.findZtLxDeails(lxNo);
		LxDetailsDto lxDetailsDto = new LxDetailsDto();
		
		if(dtos.size() > 0) {
			lxDetailsDto.setTaskType(dtos.get(0).getTaskType());
			lxDetailsDto.setLxNo(lxNo);
			lxDetailsDto.setLxType(dtos.get(0).getLayoutType());
			List<LxSubDetailsDto> list  = new ArrayList<LxSubDetailsDto>();
			for(ZtLxDetailsDto dto : dtos) {
				LxSubDetailsDto lxSubDetailsDto = new LxSubDetailsDto();
				lxSubDetailsDto.setGoodsName(dto.getGoodsName());
				lxSubDetailsDto.setLxSubNo(dto.getContainerSubNo());
				lxSubDetailsDto.setGoodsNum(dto.getGoodsNum());
				List<GoodsBarCode> goodsBarCodes = goodsBarCodeMapper.findByMap(MapUtils.put("goodsId", dto.getGoodsId()).getMap(), GoodsBarCode.class);
				List<String> barCodes = goodsBarCodes.stream().map(GoodsBarCode :: getBarCode).collect(Collectors.toList());
				lxSubDetailsDto.setGoodsBarCodes(barCodes);
				list.add(lxSubDetailsDto);
			}
			lxDetailsDto.setLxSubDeatilsDtos(list);
		}
		return lxDetailsDto;
	}

}
