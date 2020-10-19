package org.eis.yqfs.master;

import com.prolog.eis.util.StationLockUtils;

public class SynTest {
    int i =0;
    public void method1() throws Exception{
        Object obj = StationLockUtils.getInstance().getLock(1);
        synchronized (this){
            i = i+1;
            Thread.sleep(100);
            System.out.println(i);
            method2();
        }
    }

    public void method2() throws Exception{
        Object obj = StationLockUtils.getInstance().getLock(1);
        synchronized (this){
            i = i+1;
            Thread.sleep(100);
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        SynTest st = new SynTest();
        for(int i=0;i<10;i++){
            new Thread(()->{
                for(int j=0;j<100;j++) {
                    try {
                        st.method1();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        for(int i=0;i<10;i++){
            new Thread(()->{
                for(int j=0;j<100;j++) {
                    try {
                        st.method2();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
