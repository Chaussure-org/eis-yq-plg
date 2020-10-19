package com.prolog.eis.service.pointlocation;

import com.prolog.eis.model.point.PointLocation;
import com.sun.javafx.collections.MappingChange;

import java.util.List;
import java.util.Map;

public interface IPointLocationService {
    PointLocation getPoint(String pointId);
    List<PointLocation> getPointByStation(int stationId);
    List<PointLocation> getPointByStation(int stationId,int pointType);
    List<PointLocation> getPointByType(int pointType);
    List<PointLocation> findByMap(Map map);
    void add(PointLocation pointLocation) throws Exception;
    void addBath(List<PointLocation> pointLocations) throws Exception;
    void update(PointLocation pointLocation) throws Exception;
    void delete(String pointId);
    void deleteAll();
    long getTotalCount();


}
