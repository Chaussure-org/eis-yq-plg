package com.prolog.eis.service.pickorder;

import com.prolog.eis.dao.pickorder.PickOrderHistoryMapper;
import com.prolog.eis.dao.pickorder.PickOrderMapper;
import com.prolog.eis.pickstation.model.PickOrder;
import com.prolog.eis.pickstation.model.PickOrderHistory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author wangkang
 * @Description 拣选单历史服务实现类
 * @CreateTime 2020/6/20 15:42
 */
@Service
public class PickOrderHistoryServiceImpl implements IPickOrderHistoryService {

    @Autowired
    private PickOrderMapper pickOrderMapper;
    @Autowired
    private PickOrderHistoryMapper pickOrderHistoryMapper;
    @Override
    public void toHistory(PickOrder pickOrder) throws Exception {
        PickOrderHistory pickOrderHistory = new PickOrderHistory();
        BeanUtils.copyProperties(pickOrder,pickOrderHistory);

        try{
            pickOrderHistoryMapper.save(pickOrderHistory);
            pickOrderMapper.deleteById(pickOrder.getId(),PickOrder.class);
        }catch (Exception e){
            throw new Exception("拣选单转历史失败");
        }
    }
}
