package com.prolog.eis.service.assembl;

import com.prolog.eis.dto.assembl.AssemblBoxMxDto;
import com.prolog.eis.dto.pd.AssemblBoxDto;
import com.prolog.eis.model.assembl.AssemblBoxHz;
import com.prolog.eis.model.assembl.AssemblBoxMx;

import java.util.List;

public interface AssemblBoxService {
	/**
	 * 添加合箱计划
	 * @param boxMxs
	 * @throws Exception
	 */
	void saveAssemblBox(List<AssemblBoxDto> boxMxs)throws Exception;
	/**
	 * 合箱计划下发
	 */
	void updateAssemblBoxTaskState(int id) throws Exception;

	/**
	 * 合箱计划删除
	 */
	void deleteAssemblBoxTask(int id) throws Exception;

	/**
	 * 根据合箱计划，料箱修改状态
	 * @param containerNo
	 * @param id
	 * @param taskState
	 * @throws Exception
	 */
	void updateAssemblBoxMxTaskState(String containerNo ,int id,int taskState) throws Exception;

	/**
	 * 查看合箱明细
	 * @param id 合箱汇总id
	 * @return
	 * @throws Exception
	 */
	List<AssemblBoxMxDto> findAssembBoxMxInfo(int id) throws Exception;

	void updateMx(AssemblBoxMx mx) throws Exception;

	void update(AssemblBoxHz hz) throws Exception;

}
