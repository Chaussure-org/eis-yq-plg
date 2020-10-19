package com.prolog.eis.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化拣选站台数据
 */
@Component
public class PickStationDataInit implements CommandLineRunner {

    @Autowired
    private DataInitService dataInitService;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
         try {
             dataInitService.importPointLocationData();
         }catch (Exception e){
             e.printStackTrace();
         }

        try {
            dataInitService.initStationData();
        }catch (Exception e){
             e.printStackTrace();
        }

        try {
            dataInitService.initStoreLocation();

        }catch (Exception e){
             e.printStackTrace();
        }

    }


}
