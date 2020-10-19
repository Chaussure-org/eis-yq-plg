package com.prolog.eis.util;

import java.util.HashMap;
import java.util.Map;

public class StationLockUtils {
    private static Map<String, Object> LockMap  = new HashMap<String, Object>();

    private static StationLockUtils instance;
    public static synchronized StationLockUtils getInstance() {
        if (instance == null) {
            instance = new StationLockUtils();
        }
        return instance;
    }

    public synchronized Object getLock(int stationId) {
        String key = "station"+stationId;
        if (this.LockMap.containsKey(key))
            return this.LockMap.get(key);
        else {
            Object lockObj = new Object();
            this.LockMap.put(key, lockObj);
            return lockObj;
        }
    }
}
