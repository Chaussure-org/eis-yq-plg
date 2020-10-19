package com.prolog.eis.service.store;

import com.prolog.eis.model.order.Transshipment;

import java.util.List;

public interface ITransshipmentService {

    void saveTransshipment(String transshipmentName,String transshipmentNo) throws Exception;


    void deleteTransshipment(int id) throws Exception;

    void updateTransshipment(Transshipment transshipment) throws Exception;

    List<Transshipment> findAllTransshipment() throws Exception;
}
