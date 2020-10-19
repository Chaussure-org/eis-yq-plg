package com.prolog.eis.dispatch;

import com.prolog.eis.boxbank.out.TKEnginee;
import com.prolog.eis.dto.tkdispatch.TuiKuLxDto;
import com.prolog.eis.dto.tkdispatch.TuiKuXiangKuDto;
import com.prolog.eis.util.FileLogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 退库调度
 */
@Service
public class TKDispatch {
	@Autowired
	private TKEnginee tkEnginee;

	// 初始化数据，加载pdHangDaoList
	private TuiKuXiangKuDto init() throws Exception {
		TuiKuXiangKuDto tkXiangKu = tkEnginee.init();
		// 计算料箱的出库优先级
		tkXiangKu.ComputeLxPriority();
		return tkXiangKu;
	}

	// 退库出库检查
	public void check() throws Exception {
		TuiKuXiangKuDto tkXiangKu = this.init();

		if (tkXiangKu == null || tkXiangKu.getCengList().size() == 0)
			return;

		while (true) {
			TuiKuLxDto lx = tkXiangKu.FindTuiKuLxResult();
			if (lx == null)
				break;

			// 发送料箱出库指令，修改料箱库存状态
			boolean isChukuSuccess = tkEnginee.chuku(lx);

			if (!isChukuSuccess)
				FileLogHelper.WriteLog("PanDianChuKuError", "退库出库异常，料箱：" + lx.getLxNo());
		}
	}
}
