package com.prolog.eis.pickstation.client.impl;

import com.prolog.eis.Application;
import com.prolog.eis.pickstation.model.SeedInfo;
import com.prolog.eis.pickstation.service.ISeedInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/8/14 16:35
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class SeedInfoServiceTest {

    @Autowired
    private ISeedInfoService seedInfoService;

    @Test
    public void testSeedInfoAdd(){
        SeedInfo seedInfo = new SeedInfo(null,"10000112",1,12,"60030",157,5,new Date());
        seedInfoService.add(seedInfo);
    }
}
