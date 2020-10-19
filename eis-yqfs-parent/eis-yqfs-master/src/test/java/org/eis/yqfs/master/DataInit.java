package org.eis.yqfs.master;

import com.prolog.eis.Application;
import com.prolog.eis.dao.order.ContainerSubBindingMxMapper;
import com.prolog.eis.model.order.ContainerSubBindingMx;
import com.prolog.eis.service.store.IStoreLocationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DataInit {

    @Autowired
    private ContainerSubBindingMxMapper containerSubBindingMxMapper;

    @Test
    public void testCSB(){
        ContainerSubBindingMx mx = containerSubBindingMxMapper.findById(622,ContainerSubBindingMx.class);
        mx.setActualNum(1);
        containerSubBindingMxMapper.update(mx);
    }
}
