package com.prolog.eis.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GolableLockUtils {
    private static Map<String, ReentrantLock> LockMap  = new HashMap<String, ReentrantLock>();
    private static GolableLockUtils instance;

    public static synchronized GolableLockUtils getInstance() {
        if (instance == null) {
            instance = new GolableLockUtils();
        }
        return instance;
    }

    public synchronized ReentrantLock getLock(String key) {
        if (this.LockMap.containsKey(key))
            return this.LockMap.get(key);
        else {
            ReentrantLock lockObj = new ReentrantLock(true);
            this.LockMap.put(key, lockObj);
            return lockObj;
        }
    }

}
