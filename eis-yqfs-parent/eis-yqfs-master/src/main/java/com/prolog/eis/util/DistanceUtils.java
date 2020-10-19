package com.prolog.eis.util;

public class DistanceUtils {

    /**
     * 计算距离
     * @param pointX
     * @param pointY
     * @param targetPointX
     * @param targetPointY
     * @return
     */
   public static double toDistance(int pointX,int pointY,int targetPointX,int targetPointY){
       double v = (pointX - targetPointX)^2 + (pointY - targetPointY)^2;
       int distance =(int) Math.abs(Math.sqrt(v));
       return distance;
    }
}
