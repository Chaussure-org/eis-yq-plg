package com.prolog.eis.service.store;

import com.prolog.eis.dto.store.ZtLxDetailsDto;
import com.prolog.eis.model.store.ZtStore;

import java.util.List;
import java.util.Map;

/**
 * @Author wangkang
 * @Description 在途
 * @CreateTime 2020/6/20 11:06
 */
public interface IZtSoreService {
    List<ZtStore> findByMap(Map map);

    void update(ZtStore ztStore);

    List<ZtLxDetailsDto> findZtLxDeails(String containerNo);

    /**
     * 新增在途
     *
     */
    void addZtStore(ZtStore ztStore) throws Exception;

    /**
     * 根据料箱寻找在途
     * @param containerNo
     * @return
     */
    ZtStore getByContainerNo(String containerNo);


}
