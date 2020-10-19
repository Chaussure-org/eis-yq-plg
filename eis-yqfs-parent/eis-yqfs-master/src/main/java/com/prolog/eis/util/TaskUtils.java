package com.prolog.eis.util;

import com.prolog.framework.utils.SnowFlakeUtils;

public class TaskUtils {

    public static String gerenateTaskId(){
        long id = SnowFlakeUtils.getInstance(1,2).nextId();
        return String.valueOf(id);
    }
}
