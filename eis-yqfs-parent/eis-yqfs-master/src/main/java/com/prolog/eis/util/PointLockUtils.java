package com.prolog.eis.util;

import java.util.HashMap;
import java.util.Map;

public class PointLockUtils {
    private static Map<String, Object> LockMap  = new HashMap<String, Object>();

    private static PointLockUtils instance;
    public static synchronized PointLockUtils getInstance() {
        if (instance == null) {
            instance = new PointLockUtils();
        }
        return instance;
    }

    public synchronized Object getLock(String pointId) {
        if (this.LockMap.containsKey(pointId))
            return this.LockMap.get(pointId);
        else {
            Object lockObj = new Object();
            this.LockMap.put(pointId, lockObj);
            return lockObj;
        }
    }
}
