package com.prolog.eis.dispatch;

import com.prolog.eis.boxbank.out.HXEnginee2;
import com.prolog.eis.dto.hxdispatch.HxAssemblBoxHz;
import com.prolog.eis.dto.hxdispatch.HxChuKuResultDto;
import com.prolog.eis.dto.hxdispatch.HxStationDto;
import com.prolog.eis.dto.hxdispatch.HxXiangKuDto;
import com.prolog.eis.boxbank.out.HXEnginee;
import com.prolog.eis.util.FileLogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 合箱调度
 */
@Service
public class HXDispatch {
	@Autowired
	private HXEnginee hxEnginee;

	@Autowired
	private HXEnginee2 hxEnginee2;

	public void check2() throws Exception{
		hxEnginee2.checkOut();
	}

	// 初始化数据，加载pdHangDaoList
	private HxXiangKuDto init() throws Exception {
		HxXiangKuDto xiangKu = hxEnginee.init();
		xiangKu.init();
		return xiangKu;
	}

	// 为所需料箱全部出库到线体的站台索取一个新的合箱任务
	private void takePlan(HxXiangKuDto xiangKu) throws Exception {
		while (true) {
			HxStationDto bestSt = xiangKu.FindBindingPlanBestStation();
			if (bestSt == null)
				break;

			HxAssemblBoxHz hxHz = xiangKu.TakeBestTask();
			if (hxHz == null)
				break;

			// 为站台索取一个合箱计划并写库
			hxEnginee.hzBindStation(hxHz.getHzId(), bestSt.getZhanTaiId());
			bestSt.getAssemblBoxHzList().add(hxHz);
			bestSt.setChuKuHz(hxHz);
		}
	}

	private void chuKu(HxXiangKuDto xiangKu) throws Exception {
		xiangKu.RecomputeStationChuKuHz();

		while (true) {
			HxChuKuResultDto hxLxResult = xiangKu.FindBestHxLxResult();
			if (hxLxResult == null)
				break;

			//发送料箱出库指令，修改料箱库存状态
			boolean isChukuSuccess = hxEnginee.chuku(hxLxResult.getMx().getLxNo(),hxLxResult.getStation().getZhanTaiId());
			
			if (!isChukuSuccess)
				FileLogHelper.WriteLog("PanDianChuKuError", "合箱出库失败，料箱：" + hxLxResult.getMx().getLxNo());
		}
	}

	// 合箱出库调度检查
	@Transactional
	public void check() throws Exception {
		HxXiangKuDto xiangKu = this.init();

		if (xiangKu == null || xiangKu.getStationList().size() == 0 || xiangKu.getCengList().size() == 0)
			return;
		// 为所需料箱全部出库到线体的站台索取一个新的合箱任务
		this.takePlan(xiangKu);
		// 为站台出库合箱任务的料箱
		this.chuKu(xiangKu);
	}
}
