package com.prolog.eis.service.masterbase;

import java.util.List;

import com.prolog.eis.model.masterbase.Goods;

public interface GoodsService {

	// 保存商品信息
	public void saveGoods(Goods goods) throws Exception;

	// 更新商品信息
	public void updateGoods(Goods goods) throws Exception;
	
	//查询所有商品
	public List<Goods> queryGoods() throws Exception;
	
	//删除所有商品
	public void deleteAllGoods() throws Exception;

	public Goods getGoodsById(int goodsId);

	public List<String> getGoodsABCByIds(int[] goodsIds);
}
