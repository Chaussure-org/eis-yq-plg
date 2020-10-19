package com.prolog.eis.schedule;

import com.prolog.eis.dispatch.*;
import com.prolog.eis.init.SysIsRunningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CoreSChedule {
    @Autowired
    private BHDispatch bhDispatch;
    @Autowired
    private BZDispatch bzDispatch;
    @Autowired
    private HXDispatch hxDispatch;
    @Autowired
    private PDDispatch pdDispatch;
    @Autowired
    private TKDispatch tkDispatch;

    @Autowired
    private SysIsRunningService sysIsRunningService;

    @Scheduled(initialDelay = 3000, fixedDelay = 3000)
    public void schedule() {


        if ("0".equals(sysIsRunningService.findSysIsrunning())) {
            return;
        }
            try {
                bzDispatch.check();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                bhDispatch.check();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                //hxDispatch.check();
                hxDispatch.check2();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                pdDispatch.check();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                tkDispatch.check();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private void releaseTKTask () {

        }
    }
