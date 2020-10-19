package com.prolog.eis.service.tk;

import java.util.List;

import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.tk.BackStorePlan;
import com.prolog.eis.model.tk.TKTask;
import com.prolog.eis.wcs.dto.TaskCallbackDTO;

/**
 * @author 孙四月
 */
public interface TkTaskService {

	/**
	 * 批量添加退库任务
	 * @param TkList
	 * @throws Exception
	 */
	void AddTkTask(List<String> TkList)throws Exception;

	/**
	 * 删除退库任务
	 * @param id
	 * @throws Exception
	 */
	void deleteTkPlan(int id)throws Exception;


	
	/**
	 * 批量下发退库任务
	 * @param idList
	 * @throws Exception
	 */
	void releaseTask(List<Integer> idList)throws Exception;

	/**
	 * 执行退库
	 * @param ids
	 * @throws Exception
	 */
	void processTask(int ...ids) throws Exception;

	/**
	 * 根据料箱号，执行退库任务
	 * @param containerNo
	 * @throws Exception
	 */
	void processTask(String ...containerNo) throws Exception;

	/**
	 * 完成退库任务
	 * @param ids
	 * @throws Exception
	 */
	void finishTask(int ...ids) throws Exception;
	/**
	 * 根据料箱编号完成退库任务
	 * @param containerNo
	 * @throws Exception
	 */
	void finishTask(String ...containerNo) throws Exception;

	/**
	 * 转历史
	 * @param id
	 * @throws Exception
	 */
	void toHistory(int id) throws Exception;

	/**
	 * 根据容器转历史
	 * @param containerNo
	 * @throws Exception
	 */
	void toHistoryByContainerNo(String containerNo) throws Exception;

	/**
	 * 根据容器查找退库任务
	 * @param containerNo
	 * @return
	 */
	TKTask getByContainerNo(String containerNo);


}
