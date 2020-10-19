package com.prolog.eis.util;


import com.prolog.eis.dto.store.AreaSortDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrologTypeSortHelper {

	/**
	 * 根据类型获取入库规则
	 * @param type(1.空托盘入库、2.理货出库入库、3.融合/HUB入库、4.MIT入库)
	 * @return
	 * @throws Exception
	 */
	public static List<AreaSortDto> getAreaSort(int type)throws Exception{
		List<AreaSortDto> areaSortDtos = new ArrayList<AreaSortDto>();
		
		if(type == 1) {
			List<Integer> types1 = Arrays.asList(1);
			List<Integer> types2 = Arrays.asList(3,4);
			List<Integer> types3 = Arrays.asList(2);
			AreaSortDto areaSortDto1 = new AreaSortDto(1,types1)  ;
			AreaSortDto areaSortDto2 = new AreaSortDto(2,types2);
			AreaSortDto areaSortDto3 = new AreaSortDto(3,types3);
			areaSortDtos.add(areaSortDto1);
			areaSortDtos.add(areaSortDto2);
			areaSortDtos.add(areaSortDto3);
		}else if(type == 2) {
			List<Integer> types1 = Arrays.asList(2);
			List<Integer> types2 = Arrays.asList(3,4);
			List<Integer> types3 = Arrays.asList(1);
			AreaSortDto areaSortDto1 = new AreaSortDto(1,types1)  ;
			AreaSortDto areaSortDto2 = new AreaSortDto(2,types2);
			AreaSortDto areaSortDto3 = new AreaSortDto(3,types3);
			areaSortDtos.add(areaSortDto1);
			areaSortDtos.add(areaSortDto2);
			areaSortDtos.add(areaSortDto3);
		}else if(type == 3) {
			List<Integer> types1 = Arrays.asList(3);
			List<Integer> types2 = Arrays.asList(4);
			List<Integer> types3 = Arrays.asList(2);
			List<Integer> types4 = Arrays.asList(1);
			AreaSortDto areaSortDto1 = new AreaSortDto(1,types1)  ;
			AreaSortDto areaSortDto2 = new AreaSortDto(2,types2);
			AreaSortDto areaSortDto3 = new AreaSortDto(3,types3);
			AreaSortDto areaSortDto4 = new AreaSortDto(4,types4);
			areaSortDtos.add(areaSortDto1);
			areaSortDtos.add(areaSortDto2);
			areaSortDtos.add(areaSortDto3);
			areaSortDtos.add(areaSortDto4);
		}else if(type == 4) {
			List<Integer> types1 = Arrays.asList(4);
			List<Integer> types2 = Arrays.asList(3);
			List<Integer> types3 = Arrays.asList(2);
			List<Integer> types4 = Arrays.asList(1);
			AreaSortDto areaSortDto1 = new AreaSortDto(1,types1)  ;
			AreaSortDto areaSortDto2 = new AreaSortDto(2,types2);
			AreaSortDto areaSortDto3 = new AreaSortDto(3,types3);
			AreaSortDto areaSortDto4 = new AreaSortDto(4,types4);
			areaSortDtos.add(areaSortDto1);
			areaSortDtos.add(areaSortDto2);
			areaSortDtos.add(areaSortDto3);
			areaSortDtos.add(areaSortDto4);
		}
	
		return areaSortDtos;
	}
}
