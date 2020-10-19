package com.prolog.eis.pickstation.service;

import com.prolog.eis.dto.pickorder.SeedTaskDto;
import com.prolog.eis.dto.station.StationsDto;
import com.prolog.eis.dto.yqfs.PickBillDto;
import com.prolog.eis.dto.yqfs.PickOrderDetail;
import com.prolog.eis.dto.yqfs.StationDto;
import com.prolog.eis.pickstation.model.Station;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface IStationService {

    void add(Station station) throws Exception;

    List<StationDto> queryStation();

    Station getByNo(String stationNo);

    void insert(StationDto stationDto) throws Exception;

    void update(StationDto stationDto) throws Exception;

    void deleteStation(int stationId) throws Exception;

    /**
     * 站台缺货查询
     * @param stationId
     * @return
     */
    ConcurrentHashMap<String, List> lack(int stationId)throws Exception;

    StationDto getstationwithportbyips( String ipaddress) throws Exception;

    void deleteAll();
    Station getStation(int stationId);

    void addBath(List<Station> stations) throws Exception;

    long countData() throws Exception;

    List<Station> findByMap(Map map)throws Exception;
    void update(Station station);

    /**
     * 获取拣选站台ip
     * @param stationId
     * @return
     */
    String getIp(int stationId);

    Map init(int stationId) throws Exception;

    /**
     * 查找可拣选的站台
     * @return
     */
    List<Station> getPickable();

    /**
     *
     * @param stationId
     * @return
     */
    List<PickOrderDetail> getPickOrderDetail(String stationId) throws Exception;


    /**
     * 清单查询
     * @param
     * @return
     */
    List<PickBillDto> getPickBillDto(String wave_no, String dealer_id,String billNo) throws Exception;


    List<StationsDto> getAllStation() throws Exception;

    void seedLight(int stationId) throws Exception;

    List<SeedTaskDto> getSeedTask(String containerNo, String goodsNo, String goodsName);

   //截单
    void cutOrder(String stationNo) throws Exception;

    void orderInit(int positionNo, int stationId);
}
