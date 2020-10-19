package org.eis.yqfs.master;

import com.prolog.eis.Application;
import com.prolog.eis.wcs.dto.TaskCallbackDTO;
import com.prolog.eis.wcs.impl.WCSCallbackServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/8/8 19:30
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class SafeDoorTest {

    @Autowired
    private WCSCallbackServiceImpl wcsCallbackService;

    @Test
    public void doInTest() throws Exception{
        TaskCallbackDTO taskCallbackDTO = new TaskCallbackDTO();
        taskCallbackDTO.setTaskId("482902944870703104");
        taskCallbackDTO.setType((short) 5);
        taskCallbackDTO.setAddress("R01");
        taskCallbackDTO.setContainerNo("10000052");
        taskCallbackDTO.setStatus((short) 2);
        wcsCallbackService.doXZTask(taskCallbackDTO);
    }
}
