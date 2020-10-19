package com.prolog.eis.pickstation.client.impl;

import com.prolog.eis.pickstation.client.SendCsClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/6/15 19:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SendCsClientServiceTest {

    @Autowired
    private SendCsClientService sendCsClientService;

    @Test
    public void sendLxDetails() throws Exception {

       sendCsClientService.sendLxDetails("600123","J0102",101);

       sendCsClientService.sendLxDetails("90001","J0101",101);
//       sendCsClientService.sendLxDetails("90002","J0102","101");

    }

    @Test
    public void sendDdxDetails() throws Exception {
        sendCsClientService.sendDdxDetails("88088","1",101);
    }

    @Test
    public void sendCurrentStoreHz() throws Exception {
        sendCsClientService.sendCurrentStoreHz(88,"600123","201",101);
    }

    @Test
    public void sendCurrentSeed() throws Exception {
        sendCsClientService.sendCurrentSeed("600123","201",101,"DW0101");
    }
}
