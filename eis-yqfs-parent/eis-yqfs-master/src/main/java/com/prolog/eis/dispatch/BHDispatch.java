package com.prolog.eis.dispatch;

import com.prolog.eis.boxbank.out.BHEnginee;
import com.prolog.eis.dto.bhdispatch.BuHuoLxDto;
import com.prolog.eis.dto.bhdispatch.BuHuoXiangKuDto;
import com.prolog.eis.util.FileLogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 补货调度
 */
@Service
public class BHDispatch {

	@Autowired
	private BHEnginee bhEnginee;

	/**
	 * 初始化数据
	 * @return
	 * @throws Exception
	 */
	private BuHuoXiangKuDto init() throws Exception {
		BuHuoXiangKuDto xiangKu = bhEnginee.init();
		// 计算料箱的出库优先级
		xiangKu.ComputeLxPriority();
		return xiangKu;
	}

	/**
	 * 出库检查
	 * @throws Exception
	 */
	public void check() throws Exception {
		BuHuoXiangKuDto xiangKu = this.init();

		if (xiangKu == null || xiangKu.getCengList().size() == 0)
			return;

		while (true) {
			BuHuoLxDto lx = xiangKu.FindBuHuoLxResult();
			if (lx == null)
				break;
			// 发送料箱出库指令，修改料箱库存状态
			boolean isChukuSuccess = bhEnginee.chuku(lx);
			if (!isChukuSuccess)
				FileLogHelper.WriteLog("BuHuoChuKuError", "补货出库异常，料箱：" + lx.getLxNo());
		}
	}
}
