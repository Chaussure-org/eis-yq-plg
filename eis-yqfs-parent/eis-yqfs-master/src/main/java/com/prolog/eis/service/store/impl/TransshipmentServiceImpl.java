package com.prolog.eis.service.store.impl;

import com.prolog.eis.dao.order.TransshipmentMapper;
import com.prolog.eis.model.order.Transshipment;
import com.prolog.eis.service.store.ITransshipmentService;
import com.prolog.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransshipmentServiceImpl implements ITransshipmentService {
    @Autowired
    private TransshipmentMapper transshipmentMapper;
    @Override
    public void saveTransshipment(String transshipmentName,String transshipmentNo) throws Exception {
        if (StringUtils.isBlank(transshipmentName))
            throw new Exception("参数不能为空");
        if (StringUtils.isBlank(transshipmentNo))
            throw new Exception("参数不能为空");
        Transshipment transshipment = new Transshipment();
        transshipment.setTransshipmentName(transshipmentName);
        transshipment.setTransshipmentNo(transshipmentNo);
        transshipmentMapper.save(transshipment);
    }

    @Override
    public void deleteTransshipment(int id) throws Exception {
        if (id == 0 )
            throw new Exception("中转仓id不能为空");
        transshipmentMapper.deleteById(id,Transshipment.class);
    }

    @Override
    public void updateTransshipment(Transshipment transshipment) throws Exception {
        if (transshipment == null)
            throw new Exception("参数不能为空");
        transshipmentMapper.update(transshipment);
    }

    @Override
    public List<Transshipment> findAllTransshipment() throws Exception {
      return   transshipmentMapper.findByMap(null,Transshipment.class);
    }
}
