package com.prolog.eis.dispatch;

import com.prolog.eis.boxbank.out.PDEnginee;
import com.prolog.eis.dto.pddispatch.PanDianLxResultDto;
import com.prolog.eis.dto.pddispatch.PanDianXiangKuDto;
import com.prolog.eis.util.FileLogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 盘点调度
 */
@Service
public class PDDispatch {
	@Autowired
	private PDEnginee pdEnginee;

	// 初始化数据，加载pdHangDaoList
	private PanDianXiangKuDto init() throws Exception {
		PanDianXiangKuDto pdXiangKu = pdEnginee.init();
		//计算料箱的盘点出库优先级
		pdXiangKu.ComputeLxPriority();
		return pdXiangKu;
	}

	// 检查正在作业的运单下所有未绑定满料箱的运单详细，并根据循环线、站台的作业情况判断出哪些料箱
	public void check() throws Exception {
		PanDianXiangKuDto pdXiangKu = this.init();

		if (pdXiangKu == null)
			return;

		//无盘点作业站台
		if(pdXiangKu.getStationList().size() == 0)
			return;

		//无可以出库的盘点料箱
		if(pdXiangKu.getCengList().size() == 0)
			return;
		
		while (true) {
			PanDianLxResultDto lxResult = pdXiangKu.FindPanDianLxResult();
			if (lxResult == null)
				break;

			// 发送料箱出库指令，修改料箱库存状态
			boolean isChukuSuccess = pdEnginee.chuku(lxResult.getLx(), lxResult.getStation().getStationId());

			if(!isChukuSuccess)
				FileLogHelper.WriteLog("PanDianChuKuError", "盘点出库异常，料箱：" + lxResult.getLx().getLxNo());
		}
	}
}
