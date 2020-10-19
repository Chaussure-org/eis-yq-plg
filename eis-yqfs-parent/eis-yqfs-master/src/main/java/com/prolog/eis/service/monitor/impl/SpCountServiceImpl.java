package com.prolog.eis.service.monitor.impl;

import com.prolog.eis.dao.base.SpCountMapper;
import com.prolog.eis.dto.enginee.KuCunTotalDto;
import com.prolog.eis.service.monitor.ISpCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author wangkang
 * @Description impl
 * @CreateTime 2020/8/26 16:49
 */
@Service
public class SpCountServiceImpl implements ISpCountService {

    @Autowired
    private SpCountMapper mapper;

    @Override
    public Map<Integer, Integer> getSpCount() throws Exception {
        // 查询已绑定拣选单的订单还需占用的商品库存
        List<KuCunTotalDto> findAllUsedKuCun = mapper.findAllUsedKuCun();
        // 查询已上架状态并且无锁定的箱库库存
        List<KuCunTotalDto> findAllXkKuCun = mapper.findAllXkKuCun();
        // 查询在途商品库存
        List<KuCunTotalDto> findAllztKuCun = mapper.findAllztKuCun();
        // 查询商品库存
        Map<Integer, Integer> spStockMap = getMap(findAllUsedKuCun, findAllXkKuCun, findAllztKuCun);

        return spStockMap;
    }

    private Map<Integer, Integer> getMap(List<KuCunTotalDto> findAllUsedKuCun, List<KuCunTotalDto> findAllXkKuCun,
                                         List<KuCunTotalDto> findAllztKuCun) throws Exception {

        Map<Integer, Integer> spNumMap = new HashMap<Integer, Integer>();
        // 箱库库存+在途库存-待使用的库存
        List<KuCunTotalDto> xkAndztKuCun = new ArrayList<KuCunTotalDto>();
        if (findAllXkKuCun.size() == 0) {
            xkAndztKuCun.addAll(findAllztKuCun);
        } else if (findAllztKuCun.size() == 0) {
            xkAndztKuCun.addAll(findAllXkKuCun);
        } else {
            xkAndztKuCun.addAll(findAllztKuCun);
            xkAndztKuCun.addAll(findAllXkKuCun);
            // TODO
            Map<KuCunTotalDto, KuCunTotalDto> map = new HashMap<KuCunTotalDto, KuCunTotalDto>();
            for (KuCunTotalDto kuCunTotalDto : xkAndztKuCun) {
                if (map.containsKey(kuCunTotalDto)) {
                    map.put(kuCunTotalDto, KuCunTotalDto.merge(kuCunTotalDto, map.get(kuCunTotalDto)));
                } else {
                    map.put(kuCunTotalDto, kuCunTotalDto);
                }
            }
            Collection<KuCunTotalDto> values = map.values();
            if (values instanceof List)
                xkAndztKuCun = (List) values;
            else
                xkAndztKuCun = new ArrayList<KuCunTotalDto>(values);
        }

        if (findAllUsedKuCun.size() == 0) {
            for (KuCunTotalDto kuCunTotalDto : xkAndztKuCun) {
                spNumMap.put(kuCunTotalDto.getSpId(), kuCunTotalDto.getNum());
            }
        } else if (xkAndztKuCun.size() == 0) {
            for (KuCunTotalDto findAllUsedKuCunByHangdao1 : findAllUsedKuCun) {
                spNumMap.put(findAllUsedKuCunByHangdao1.getSpId(), 0 - findAllUsedKuCunByHangdao1.getNum());
            }
        } else {
            CopyOnWriteArrayList<KuCunTotalDto> findAllUsedKuCun2 = new CopyOnWriteArrayList<KuCunTotalDto>(
                    findAllUsedKuCun);
            CopyOnWriteArrayList<KuCunTotalDto> xkAndztKuCun2 = new CopyOnWriteArrayList<KuCunTotalDto>(xkAndztKuCun);
            // 用总库存减去目前订单数量为可用数量
            for (KuCunTotalDto kuCunTotalDto2 : xkAndztKuCun2) {
                for (KuCunTotalDto kuCunTotalDto1 : findAllUsedKuCun2) {
                    if (kuCunTotalDto1.getSpId() == kuCunTotalDto2.getSpId()) {
                        int num = kuCunTotalDto2.getNum() - kuCunTotalDto1.getNum();
                        spNumMap.put(kuCunTotalDto1.getSpId(), num);
                    } else {
                        int num = 0 - kuCunTotalDto1.getNum();
                        if (!spNumMap.containsKey((kuCunTotalDto1.getSpId()))) {
                            spNumMap.put(kuCunTotalDto1.getSpId(), num);
                        }
                        if (!spNumMap.containsKey(kuCunTotalDto2.getSpId())) {
                            spNumMap.put(kuCunTotalDto2.getSpId(), kuCunTotalDto2.getNum());
                        }
                    }
                }
            }
        }
        return spNumMap;
    }
}
