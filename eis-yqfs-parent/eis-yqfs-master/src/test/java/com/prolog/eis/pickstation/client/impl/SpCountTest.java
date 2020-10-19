package com.prolog.eis.pickstation.client.impl;

import com.prolog.eis.Application;
import com.prolog.eis.boxbank.out.impl.BZEngineeImpl;
import com.prolog.eis.dao.yqfs.OutboundWarnMapper;
import com.prolog.eis.dto.yqfs.GoodsOutboundWarnDto;
import com.prolog.eis.service.monitor.ISpCountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author wangkang
 * @Description test
 * @CreateTime 2020/8/26 9:51
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class SpCountTest {

    @Autowired
    private BZEngineeImpl bzEnginee;

    @Autowired
    private OutboundWarnMapper outboundWarnMapper;

    @Autowired
    private ISpCountService spCountService;

    @Test
    public void spCount() throws Exception {
        Map<Integer, Integer> spStockMap = bzEnginee.getSpStockMap();
        List<Map.Entry<Integer, Integer>> list1 = new ArrayList<Map.Entry<Integer, Integer>>(spStockMap.entrySet());
        Collections.sort(list1,new Comparator<Map.Entry<Integer,Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1,
                               Map.Entry<Integer, Integer> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        System.out.println(list1.toString());
        Map<Integer, Integer> spCount1 = spCountService.getSpCount();
        List<Map.Entry<Integer, Integer>> list2 = new ArrayList<Map.Entry<Integer, Integer>>(spStockMap.entrySet());
        Collections.sort(list2,new Comparator<Map.Entry<Integer,Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1,
                               Map.Entry<Integer, Integer> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        System.out.println(list2.toString());

        List<GoodsOutboundWarnDto> spCount = outboundWarnMapper.getSpCount();

        Map<Integer, GoodsOutboundWarnDto> stroerCount = spCount.stream().collect(Collectors.toMap(GoodsOutboundWarnDto::getGoodsId, Function.identity(), (k1, k2) -> {
            k1.setKuCunCount(k1.getKuCunCount() + k2.getKuCunCount());
            return k1;
        }));
        Map<Integer, Integer> map = new HashMap<>();
        stroerCount.forEach((e,k)->{
            map.put(e,k.getKuCunCount());
        });
        List<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<Integer,Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1,
                               Map.Entry<Integer, Integer> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        System.out.println(list.toString());
        List<Map.Entry<Integer, Integer>> listbcd = new ArrayList<Map.Entry<Integer, Integer>>(spStockMap.entrySet());
        List<Map.Entry<Integer, Integer>> listabc = new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
        listbcd.removeAll(list);
        listabc.removeAll(list1);
        System.out.println(listbcd);
        System.out.println(listabc);
    }
}
