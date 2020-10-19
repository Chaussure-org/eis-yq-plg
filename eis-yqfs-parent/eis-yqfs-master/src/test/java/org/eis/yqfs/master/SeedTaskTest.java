package org.eis.yqfs.master;

import com.prolog.eis.Application;
import com.prolog.eis.dto.pickorder.SeedTaskDto;
import com.prolog.eis.pickstation.service.IStationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/8/31 16:50
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class SeedTaskTest {
    @Autowired
    private IStationService stationService;

    @Test
    public void test(){
        List<SeedTaskDto> seedTask = stationService.getSeedTask("", null, "支");
        System.out.println(seedTask.toString());
    }
}
