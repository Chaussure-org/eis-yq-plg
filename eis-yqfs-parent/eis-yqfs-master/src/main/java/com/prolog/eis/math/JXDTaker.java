package com.prolog.eis.math;

import com.prolog.eis.dto.dingdantaker.YunDanGroup;
import com.prolog.eis.dto.enginee.DingDanDto;
import com.prolog.eis.dto.enginee.JianXuanDanDto;

import java.util.*;

public class JXDTaker {
	public int maxSPCount = 20;// 拣选单最大商品数量，超过就开始进行收敛

	public List<DingDanDto> allYunDanList;

	private int ztId;

	// 其他站台的商品ID
	private Set<Integer> otherZhanTaiSpIdSet;

	/// <summary>
	/// 每次商品Id+批次的运单下的商品条目集合
	/// </summary>
	private HashMap<String, YunDanGroup> yunDanGroupDic;

	/// <summary>
	/// 一个拣选单的最大运单数
	/// </summary>
	private int jxdYunDanMaxCount;

	public JXDTaker(int ztId, List<DingDanDto> allYunDanList, Set<Integer> otherZhanTaiSpIdSet,
			int jxdYunDanMaxCount,int maxSPCount) {
		this.ztId = ztId;
		this.allYunDanList = allYunDanList;
		this.otherZhanTaiSpIdSet = otherZhanTaiSpIdSet;
		this.jxdYunDanMaxCount = jxdYunDanMaxCount;
		this.maxSPCount = maxSPCount;
	}

	public int getZtId() {
		return ztId;
	}

	public void setZtId(int ztId) {
		this.ztId = ztId;
	}

	public HashMap<String, YunDanGroup> getYunDanGroupDic() {
		return yunDanGroupDic;
	}

	public void setYunDanGroupDic(HashMap<String, YunDanGroup> yunDanGroupDic) {
		this.yunDanGroupDic = yunDanGroupDic;
	}

	public int getJxdYunDanMaxCount() {
		return jxdYunDanMaxCount;
	}

	public void setJxdYunDanMaxCount(int jxdYunDanMaxCount) {
		this.jxdYunDanMaxCount = jxdYunDanMaxCount;
	}

	/**
	 * 计算最小优先级(null为最大优先级)
	 * @param dingDanList
	 * @return
	 */
	private Integer getMinPriority(List<DingDanDto> dingDanList){
		Integer minPriority = null;
		for (DingDanDto dd : dingDanList) {
			if(minPriority == null) {
				minPriority = dd.getPriority();
			}
			else{
				if(dd.getPriority() != null){
					if(dd.getPriority() < minPriority){
						minPriority = dd.getPriority();
					}
				}
			}
		}
		return minPriority;
	}

	/**
	 * 计算指定优先级的最小时效
	 * @param dingDanList
	 * @param minPriority
	 * @return
	 * @throws Exception
	 */
	private long getMinShiXiao(List<DingDanDto> dingDanList,Integer minPriority) throws Exception {
		long minShiXiao =  Long.MAX_VALUE;
		for (DingDanDto dd : dingDanList) {
			if(dd.getPriority() == minPriority){
				if(dd.getShiXiaoTime().getTime() < minShiXiao){
					minShiXiao = dd.getShiXiaoTime().getTime();
				}
			}
		}

		if(minShiXiao == Long.MAX_VALUE)
			throw new Exception("getMinShiXiao异常，最小时效值为Long.MAX_VALUE");

		return minShiXiao;
	}

	private List<DingDanDto> getDingDanList(List<DingDanDto> dingDanList,Integer minPriority,long minShiXiao){
		List<DingDanDto> minDdList = new ArrayList<DingDanDto>();
		for(DingDanDto dd:dingDanList){
			if(dd.getPriority() == minPriority && dd.getShiXiaoTime().getTime() == minShiXiao)
				minDdList.add(dd);
		}
		return  minDdList;
	}

	private void computePriorityShiXiaoDan() throws Exception {
		if(this.allYunDanList.size() == 0)
			return;

		Integer minPriority = this.getMinPriority(this.allYunDanList);
		long minShiXiao = this.getMinShiXiao(this.allYunDanList,minPriority);
		List<DingDanDto> bestDDList = this.getDingDanList(this.allYunDanList,minPriority,minShiXiao);

		// 按最大数量截取订单
		if (bestDDList.size() > 20000) {
			bestDDList = bestDDList.subList(0, 20000);
		}

		// 取最小时效单分组
		this.allYunDanList = bestDDList;
	}

//	private void ComputeShiXiaoDan() {
//		HashMap<Long, List<DingDanDto>> ddMap = new HashMap<Long, List<DingDanDto>>();
//
//		for (DingDanDto dd : this.allYunDanList) {
//			long time = 0;
//			if (dd.getShiXiaoTime() != null) {
//				time = dd.getShiXiaoTime().getTime();
//			}
//
//			if (ddMap.containsKey(time)) {
//				ddMap.get(time).add(dd);
//			} else {
//				List<DingDanDto> ddList = new ArrayList<DingDanDto>();
//				ddList.add(dd);
//				ddMap.put(time, ddList);
//			}
//		}
//
//		long minShiXiaoTime = -9999;
//		for (Entry<Long, List<DingDanDto>> entry : ddMap.entrySet()) {
//			if (minShiXiaoTime == -9999) {
//				minShiXiaoTime = entry.getKey();
//				continue;
//			}
//
//			if (entry.getKey() < minShiXiaoTime) {
//				minShiXiaoTime = entry.getKey();
//				continue;
//			}
//		}
//
//		List<DingDanDto> ddListTemp = new ArrayList<DingDanDto>();
//
//		if (ddMap.size() > 0) {
//			ddListTemp = ddMap.get(minShiXiaoTime);
//		}
//
//		List<DingDanDto> bestDDList = ddListTemp;
//
//		// 按最大数量截取订单
//		if (bestDDList.size() > 20000) {
//			bestDDList = bestDDList.subList(0, 20000);
//		}
//
//		// 取最小时效单分组
//		this.allYunDanList = bestDDList;
//	}

	private void CheckDingDan() throws Exception {
		HashSet<Integer> ddIdSet = new HashSet<Integer>();
		for (DingDanDto dd : this.allYunDanList) {
			if (ddIdSet.contains(dd.getId()))
				throw new Exception("JXDTaker的CheckDingDan异常，出现重复订单Id:" + dd.getId());
			else
				ddIdSet.add(dd.getId());
		}
	}

	private void init() throws Exception {
		if (this.yunDanGroupDic != null)
			throw new Exception("JXDTaker重复初始化!");	

		this.computePriorityShiXiaoDan();// 处理最小优先级、最早时效单

		this.CheckDingDan();// 检查订单

		this.yunDanGroupDic = new HashMap<String, YunDanGroup>();

		for (DingDanDto yd : this.allYunDanList) {
			yd.initYunDanSPKey();

			if (this.yunDanGroupDic.containsKey(yd.getYunDanSPKey())) {
				this.yunDanGroupDic.get(yd.getYunDanSPKey()).getYunDanList().add(yd);
			} else {
				YunDanGroup ydGroup = new YunDanGroup();
				ydGroup.initSPIdHS(yd);
				ydGroup.getYunDanList().add(yd);
				this.yunDanGroupDic.put(yd.getYunDanSPKey(), ydGroup);
			}
		}
	}

	private void TakeYunDan(JianXuanDanDto jxd, YunDanGroup ydGroup) {
		int takeCount = this.jxdYunDanMaxCount - jxd.getDdList().size();
		if (ydGroup.getYunDanList().size() <= takeCount) {
			jxd.AddYunDanGroup(ydGroup);
			this.getYunDanGroupDic().remove(ydGroup.getYunDanSPKey());
		} else {
			for (int i = 0; i < takeCount; i++) {
				jxd.AddYunDan(ydGroup.getYunDanList().get(0));
				ydGroup.getYunDanList().remove(0);
			}
		}
	}

	private void FindFirstYunDanGroup(JianXuanDanDto jxd) {
		YunDanGroup bestYdGroup = null;
		for (Map.Entry<String, YunDanGroup> entry : this.yunDanGroupDic.entrySet()) {
			YunDanGroup ydGroup = entry.getValue();
			if (ydGroup.getYunDanList().size() >= this.jxdYunDanMaxCount) {
				bestYdGroup = ydGroup;
				break;
			}
			if (bestYdGroup == null) {
				bestYdGroup = ydGroup;
				continue;
			}

			// 与其他站台差异化最大
			if (ydGroup.getOtherZtSameCount() < bestYdGroup.getOtherZtSameCount()) {
				bestYdGroup = ydGroup;
				continue;
			} else if (ydGroup.getOtherZtSameCount() == bestYdGroup.getOtherZtSameCount()) {
				if (ydGroup.getSpIdHS().size() > bestYdGroup.getSpIdHS().size()) {
					bestYdGroup = ydGroup;
					continue;
				} else if (ydGroup.getSpIdHS().size() == bestYdGroup.getSpIdHS().size()) {
					if (ydGroup.getYunDanList().size() > bestYdGroup.getYunDanList().size()) {
						bestYdGroup = ydGroup;
						continue;
					}
				}
			}
		}

		this.TakeYunDan(jxd, bestYdGroup);
	}

	/// <summary>
	/// 计算每个运单与当前拣选单的匹配数量和不匹配数量
	/// </summary>
	private void ComputeMatchCount(JianXuanDanDto jxd) {
		for (Map.Entry<String, YunDanGroup> entry : this.yunDanGroupDic.entrySet()) {
			YunDanGroup ydGroup = entry.getValue();

			int matchCount = 0;
			int notMatchCount = 0;

			for (Integer spId : ydGroup.getSpIdHS()) {
				if (jxd.getSpKeyHs().contains(spId))
					matchCount++;
				else
					notMatchCount++;
			}

			ydGroup.setMatchCount(matchCount);// 匹配数量
			ydGroup.setNotMatchCount(notMatchCount);// 不匹配数量
		}

		for (Map.Entry<String, YunDanGroup> entry : this.yunDanGroupDic.entrySet()) {
			YunDanGroup ydGroup = entry.getValue();

			int otherZtSameCount = 0;
			for (Integer spId : ydGroup.getSpIdHS()) {
				if (this.otherZhanTaiSpIdSet.contains(spId)) {
					otherZtSameCount++;
				}
			}
			ydGroup.setOtherZtSameCount(otherZtSameCount);
		}
	}

	public interface IFindBestYDGroupCompare {
		boolean compare(YunDanGroup group1, YunDanGroup group2);
	}

	/// <summary>
	/// 根据比较器查找集合中的最大元素
	/// </summary>
	public YunDanGroup FindBestYDGroup(Collection<YunDanGroup> ydGroupList, IFindBestYDGroupCompare icompare) {
		YunDanGroup bestGroup = null;

		for (YunDanGroup group : ydGroupList) {
			if (bestGroup == null) {
				bestGroup = group;
			} else {
				if (icompare.compare(group, bestGroup))// 判断当前元素是否比最大元素要大
				{
					bestGroup = group;
				}
			}
		}

		return bestGroup;
	}

	private void FindYunDan(JianXuanDanDto jxd) {
		this.ComputeMatchCount(jxd);

		YunDanGroup bestGroup = this.FindBestYDGroup(this.getYunDanGroupDic().values(), (ydGroup1, ydGroup2) -> {
			// 如果拣选单的商品数量低于最大商品数，则依次比较匹配数量，站台差异数、不匹配数量
			if (jxd.getSpKeyHs().size() < maxSPCount) {
				if (ydGroup1.getMatchCount() == ydGroup2.getMatchCount()) {
					if (ydGroup1.getOtherZtSameCount() == ydGroup2.getOtherZtSameCount()) {
						return ydGroup1.getNotMatchCount() < ydGroup2.getNotMatchCount();
					}

					return ydGroup1.getOtherZtSameCount() < ydGroup2.getOtherZtSameCount();
				}

				return ydGroup1.getMatchCount() > ydGroup2.getMatchCount();
			} else {
				// 如果拣选单的商品数量大于等于最大商品数，则依次比较不匹配数量，站台差异数、匹配数量
				if (ydGroup1.getNotMatchCount() == ydGroup2.getNotMatchCount()) {
					if (ydGroup1.getOtherZtSameCount() == ydGroup2.getOtherZtSameCount()) {
						return ydGroup1.getMatchCount() > ydGroup2.getMatchCount();
					}

					return ydGroup1.getOtherZtSameCount() < ydGroup2.getOtherZtSameCount();
				}

				return ydGroup1.getNotMatchCount() < ydGroup2.getNotMatchCount();
			}
		});

		this.TakeYunDan(jxd, bestGroup);
	}

	private JianXuanDanDto GetJXD() {
		JianXuanDanDto jxd = new JianXuanDanDto();

		this.FindFirstYunDanGroup(jxd);

		while (this.getYunDanGroupDic().size() > 0 && jxd.getDdList().size() < this.jxdYunDanMaxCount) {
			this.FindYunDan(jxd);
		}

		return jxd;
	}

	public JianXuanDanDto Compute() throws Exception {
		this.init();

		if (this.yunDanGroupDic.size() == 0)
			return null;

		JianXuanDanDto jxd = this.GetJXD();
		return jxd;
	}
}
