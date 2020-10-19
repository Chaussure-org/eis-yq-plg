package com.prolog.eis.service.pointlocation.impl;

import com.prolog.eis.dao.pointlocation.PointLocationMapper;
import com.prolog.eis.model.point.PointLocation;
import com.prolog.eis.service.pointlocation.IPointLocationService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PointLocationServiceImpl implements IPointLocationService {
    @Autowired
    private PointLocationMapper mapper;
    @Override
    public PointLocation getPoint(String pointId) {
        return mapper.findById(pointId,PointLocation.class);
    }

    @Override
    public List<PointLocation> getPointByStation(int stationId) {
        return mapper.findByMap(MapUtils.put("stationId",stationId).getMap(),PointLocation.class);
    }

    @Override
    public List<PointLocation> getPointByStation(int stationId, int pointType) {
        return mapper.findByMap(MapUtils.put("stationId",stationId).put("pointType",pointType).getMap(),PointLocation.class);
    }

    @Override
    public List<PointLocation> getPointByType(int pointType) {
        return mapper.findByMap(MapUtils.put("pointType",pointType).getMap(),PointLocation.class);
    }

    @Override
    public List<PointLocation> findByMap(Map map) {
        return mapper.findByMap(map,PointLocation.class);
    }

    @Override
    public void add(PointLocation pointLocation) throws Exception {
        mapper.save(pointLocation);
    }

    @Override
    public void addBath(List<PointLocation> pointLocations) throws Exception {
        mapper.saveBatch(pointLocations);
    }

    @Override
    public void update(PointLocation pointLocation) throws Exception {
        mapper.update(pointLocation);
    }

    @Override
    public void delete(String pointId) {
        mapper.deleteById(pointId,PointLocation.class);
    }

    @Override
    public void deleteAll() {
        mapper.deleteByMap(null,PointLocation.class);
    }

    @Override
    public long getTotalCount() {
        return mapper.findCountByMap(null,PointLocation.class);
    }

}
