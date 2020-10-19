package com.prolog.eis.init;

import com.prolog.eis.configuration.EisProperties;
import com.prolog.eis.model.point.PointLocation;
import com.prolog.eis.pickstation.model.Station;
import com.prolog.eis.pickstation.model.StationLxPosition;
import com.prolog.eis.pickstation.model.StationOrderPosition;
import com.prolog.eis.pickstation.service.IStationDdxService;
import com.prolog.eis.pickstation.service.IStationLxService;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.service.pointlocation.IPointLocationService;
import com.prolog.eis.service.store.IStoreLocationService;
import com.prolog.framework.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataInitService {

    @Autowired
    private EisProperties properties;
    @Autowired
    private IPointLocationService pointLocationService;
    @Autowired
    private IStationLxService stationLxService;
    @Autowired
    private IStationDdxService stationDdxService;
    @Autowired
    private IStationService stationService;

    @Autowired
    private IStoreLocationService locationService;

    /**
     * 初始化站台数据
     * @throws Exception
     */
    @Transactional
    public void initStationData() throws Exception{
        try{

            long countStation = stationService.countData();
            if(countStation>0)
                return;

            String path = PickStationDataInit.class.getResource("/file/station.xlsx").getPath();
            path = java.net.URLDecoder.decode(path, "utf-8");
            File file = new File(path);
            InputStream inputStream =new FileInputStream(file);
            List<Object[]> xls = ExcelUtils.parse(inputStream, ExcelUtils.Format_XLSX, 0);
            List<Station> list = new ArrayList<>();
            for (int i = 1; i < xls.size(); i++) {
                Object[] objects = xls.get(i);
                Station station = new Station();
                station.setStationNo(objects[0].toString());
                station.setCurrentStationPickId(Integer.parseInt(objects[1].toString()));
                station.setLxMaxCount(Integer.parseInt(objects[2].toString()));
                station.setStationTaskType(Integer.parseInt(objects[3].toString()));
                station.setCreateTime(new Date());
                station.setIpAddress(objects[5].toString());
                list.add(station);
            }
            stationService.addBath(list);
            this.importStationLxData(list);
            this.importStationOrderData(list);

        }catch (Exception e){
            throw new RuntimeException("初始化拣选站失败,"+e.getMessage(),e);
        }
    }

    /**
     * 初始化点位数据
     */
    @Transactional
    public void importPointLocationData() throws Exception{
        try {
            long totalCount = pointLocationService.getTotalCount();
            if(totalCount>0)
                return;
            String path = PickStationDataInit.class.getResource("/file/point.xlsx").getPath();
            path = java.net.URLDecoder.decode(path, "utf-8");
            File file = new File(path);
            InputStream inputStream =new FileInputStream(file);
            List<Object[]> xls = ExcelUtils.parse(inputStream, ExcelUtils.Format_XLSX, 0);
            List<PointLocation> list = new ArrayList<>();
            for (int i = 1; i < xls.size(); i++) {
                Object[] objects = xls.get(i);
                PointLocation pointLocation = new PointLocation();

                pointLocation.setPointId(objects[0].toString());
                pointLocation.setPointName(objects[1].toString());

                pointLocation.setPointType(Integer.parseInt(objects[2].toString()));

                String stationNo = objects[3].toString();
                int stationId =0;
                if (!stationNo.trim().equals("0")) {
                    Station station = stationService.getByNo(stationNo);
                    if(station==null){
                        throw new RuntimeException("站台号错误");
                    }
                    stationId = station.getId();
                }
                pointLocation.setStationId(stationId);

                list.add(pointLocation);
            }
            pointLocationService.addBath(list);
        }catch (Exception e){
            throw new RuntimeException("初始化点位表失败,"+e.getMessage(),e);
        }
    }

    private void importStationLxData(List<Station> stations){
        try {
            String path = PickStationDataInit.class.getResource("/file/stationLx.xlsx").getPath();
            path = java.net.URLDecoder.decode(path, "utf-8");
            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            List<Object[]> xls = ExcelUtils.parse(inputStream, ExcelUtils.Format_XLSX, 0);
            List<StationLxPosition> list = new ArrayList<>();
            for (int i = 1; i < xls.size(); i++) {
                Object[] objects = xls.get(i);
                StationLxPosition stationLxPosition = new StationLxPosition();
                stationLxPosition.setId(Integer.parseInt(objects[0].toString()));
                String stationNo = objects[1].toString();
                int stationId = stations.stream().filter(x -> x.getStationNo().equals(stationNo)).collect(Collectors.toList()).get(0).getId();
                stationLxPosition.setStationId(stationId);
                stationLxPosition.setPositionNo(objects[2].toString());

                stationLxPosition.setPositionDeviceNo(objects[3].toString());
                stationLxPosition.setStatus(Integer.parseInt(objects[4].toString()));
                stationLxPosition.setDistributePriority(Integer.parseInt(objects[5].toString()));
                list.add(stationLxPosition);
            }
            stationLxService.addBath(list);
        } catch (Exception e){
            throw new RuntimeException("初始化料箱位置失败,"+e.getMessage(),e);
        }
    }

    private void importStationOrderData(List<Station> stations) {
        try {
            String path = PickStationDataInit.class.getResource("/file/stationOrder.xlsx").getPath();
            path = java.net.URLDecoder.decode(path, "utf-8");
            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            List<Object[]> xls = ExcelUtils.parse(inputStream, ExcelUtils.Format_XLSX, 0);
            List<StationOrderPosition> list = new ArrayList<>();
            for (int i = 1; i < xls.size(); i++) {
                Object[] objects = xls.get(i);
                StationOrderPosition stationOrderPosition = new StationOrderPosition();
                stationOrderPosition.setId(Integer.parseInt(objects[0].toString()));
                String stationNo = objects[1].toString();
                int stationId = stations.stream().filter(x -> x.getStationNo().equals(stationNo)).collect(Collectors.toList()).get(0).getId();
                stationOrderPosition.setStationId(stationId);
                stationOrderPosition.setPositionNo(objects[2].toString());
                stationOrderPosition.setLightNo(objects[3].toString());
                stationOrderPosition.setDeviceNo(objects[4].toString());
                stationOrderPosition.setDistributePriority(Integer.parseInt(objects[5].toString()));
                list.add(stationOrderPosition);
            }
            stationDdxService.addBath(list);
        } catch (Exception e){
            throw new RuntimeException("初始化订单箱位置失败",e);
        }
    }

    /**
     *
     * 初始化货位数据
     * @throws Exception
     */
    public void initStoreLocation() throws Exception {
        if(locationService.getTotalCount()>0)
            return;
        locationService.initData(13,12,64);
        locationService.saveSxStoreRelation();
    }
}
