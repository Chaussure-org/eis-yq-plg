package com.prolog.eis.util;

import java.util.Arrays;

/**
 * @Author wangkang
 * @Description 灯位工具类
 * @CreateTime 2020/6/18 20:37
 */
public class LightUtils {

    private static String[] four1 = {"1","2","3","4"};

    private static String[] four2 = {"5","6","7","8"};

    private static String[] two1 = {"1","3"};

    private static String[] two2 = {"5","7"};

    private static String[] two3 = {"2","4"};

    private static String[] two4 = {"6","8"};

    public static String[] getLightPosition(String containerNo, String containerSubNo, int type, String face,int positionNo){
        String[] result = null;
        if (type == 3) {
            //田字格
            Integer num = Integer.parseInt(containerSubNo.substring(containerSubNo.length() - 1, containerSubNo.length()));
            if (positionNo == 1){
                switch (face){
                    case "A":
                        if (num == 1) {
                            result = new String[]{four1[3]};
                        }else if (num == 2) {
                            result = new String[]{four1[2]};
                        }else if (num == 3) {
                            result = new String[]{four1[1]};;
                        }else if (num == 4) {
                            result = new String[]{four1[0]};
                        }
                        break;
                    case "C":
                        if (num == 1) {
                            result = new String[]{four1[0]};
                        }else if (num == 2) {
                            result = new String[]{four1[1]};
                        }else if (num == 3) {
                            result = new String[]{four1[2]};;
                        }else if (num == 4) {
                            result = new String[]{four1[3]};
                        }
                        break;
                }
            }else if (positionNo == 2){
                switch (face){
                    case "A":
                        if (num == 1) {
                            result = new String[]{four2[3]};
                        }else if (num == 2) {
                            result = new String[]{four2[2]};
                        }else if (num == 3) {
                            result = new String[]{four2[1]};;
                        }else if (num == 4) {
                            result = new String[]{four2[0]};
                        }
                        break;
                    case "C":
                        if (num == 1) {
                            result = new String[]{four2[0]};
                        }else if (num == 2) {
                            result = new String[]{four2[1]};
                        }else if (num == 3) {
                            result = new String[]{four2[2]};;
                        }else if (num == 4) {
                            result = new String[]{four2[3]};
                        }
                        break;
                }
            }
        }else if(type ==2) {
            //日字格
            Integer num = Integer.parseInt(containerSubNo.substring(containerSubNo.length() - 1, containerSubNo.length()));
            if (positionNo == 1){
                switch (face){
                    case "A":
                        if (num == 1) {
                            result = two3;
                        }else if (num == 2) {
                            result = two1;
                        }
                        break;
                    case "C":
                        if (num == 1) {
                            result = two1;
                        }else if(num == 2) {
                            result = two3;
                        }
                }
            }else if (positionNo == 2){
                switch (face){
                    case "A":
                        if (num == 1) {
                            result = two4;
                        }else if (num == 2) {
                            result = two2;
                        }
                        break;
                    case "C":
                        if (num == 1) {
                            result = two2;
                        }else if (num == 2) {
                            result = two4;
                        }
                }
            }
        }else if (type ==1){
            //整格 只用传type,containerNo,positionNo
            if (containerNo != null) {
                if (positionNo == 1){
                    result = four1;
                }else if (positionNo == 2){
                    result = four2;
                }
            }
        }else{
            throw new RuntimeException("无法识别的type");
        }

        return result;
    }

    public static void main(String[] args) {
        String[] as = LightUtils.getLightPosition("600123", "6001234", 3, "C", 1);
        System.out.println(Arrays.toString(as));
    }
}
