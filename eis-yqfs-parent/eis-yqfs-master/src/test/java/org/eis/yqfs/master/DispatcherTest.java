package org.eis.yqfs.master;

import com.prolog.eis.Application;
import com.prolog.eis.dao.order.ContainerSubBindingMxMapper;
import com.prolog.eis.dispatch.*;
import com.prolog.eis.model.order.ContainerSubBindingMx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Soundbank;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class DispatcherTest {
    @Autowired
    private PDDispatch pdDispatch;
    @Autowired
    private TKDispatch tkDispatch;
    @Autowired
    private BHDispatch bhDispatch;
    @Autowired
    private HXDispatch hxDispatch;
    @Autowired
    private BZDispatch bzDispatch;

    @Autowired
    private ContainerSubBindingMxMapper cmapper;

    @Test
    public void testSelect(){
    }

    @Test
    public void testPanDianDispatch() throws Exception {
        while (true) {
            try {
                pdDispatch.check();
                System.out.println("abc");
            }catch(Exception ex){
                ex.printStackTrace();
            }

            Thread.sleep(2000);
        }
    }

    @Test
    public void testTuiKuDispatch() throws Exception {
        while (true) {
            try {
                tkDispatch.check();
                System.out.println("abc");
            }catch(Exception ex){
                ex.printStackTrace();
            }

            Thread.sleep(2000);
        }
    }

    @Test
    public void testBuHuoDispatch() throws Exception {
        while (true) {
            try {
                bhDispatch.check();
                System.out.println("abc");
            }catch(Exception ex){
                ex.printStackTrace();
            }

            Thread.sleep(2000);
        }
    }

    @Test
    public void testHeXiangDispatch() throws Exception {
        while (true) {
            try {
                hxDispatch.check();
                System.out.println("abc");
            }catch(Exception ex){
                ex.printStackTrace();
            }

            Thread.sleep(2000);
        }
    }

    @Test
    public void testBoZhongDispatch() throws Exception {
        while (true) {
            try {
                bzDispatch.check();
                System.out.println("abc");
            }catch(Exception ex){
                ex.printStackTrace();
            }

            Thread.sleep(2000);
        }
    }
}
