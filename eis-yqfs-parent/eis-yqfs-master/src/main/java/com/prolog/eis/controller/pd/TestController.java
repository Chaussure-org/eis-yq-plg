package com.prolog.eis.controller.pd;

import com.prolog.eis.dao.masterbase.OwnerMapper;
import com.prolog.eis.dto.base.ContainerDTO;
import com.prolog.eis.dto.pd.PdDwContainerDto;
import com.prolog.eis.model.masterbase.Owner;
import com.prolog.eis.pickstation.client.SendCsClientService;
import com.prolog.eis.pickstation.client.UpdateClientStationInfoService;
import com.prolog.eis.pickstation.dto.StationInfoDto;
import com.prolog.eis.pickstation.model.StationLxPosition;
import com.prolog.eis.schedule.ContainerArriveSchedule;
import com.prolog.eis.service.pd.PdExecuteService;
import com.prolog.eis.service.pd.impl.PdExecuteServiceImpl;
import com.prolog.eis.service.store.IStoreLocationService;
import com.prolog.eis.util.ContainerUtils;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.wcs.dto.HoisterInfoDto;
import com.prolog.eis.wcs.service.IWCSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/6/17 10:58
 */

@RestController
@RequestMapping("test")
public class TestController {
    public static void main(String[] args) {
        ContainerDTO parse = ContainerUtils.parse("80001A");
        System.out.println(parse.getNumber());
    }


    @Autowired
    private PdExecuteService pdExecuteService;

    @Autowired
    private IStoreLocationService iStoreLocationService;

    @Autowired
    private ContainerArriveSchedule containerArriveSchedule;

    @Autowired
    private SendCsClientService sendCsClientService;

    @Autowired
    private PdExecuteServiceImpl pdExecuteServiceImpl;

    @Autowired
    private OwnerMapper ownerMapper;

    @Autowired
    private IWCSService iwcsService;

    @GetMapping("e")
    public void getE() throws Exception {
        List<HoisterInfoDto> hoisterInfoDto = iwcsService.getHoisterInfoDto();
        System.out.println(hoisterInfoDto);
    }

    @GetMapping("requestContainer")
    public void requestContainer() throws Exception {
        containerArriveSchedule.requestContainer();
    }

    @RequestMapping("init/{stationId}") //ww.ccc.com/103
    public List<PdDwContainerDto> init(
            @PathVariable("stationId") int stationId) throws Exception {
        List<PdDwContainerDto> init = pdExecuteService.init(stationId);
        return init;
    }

    @Transactional
    @GetMapping("save")
    public void test1(){
        Owner owner = new Owner();
        owner.setId(2);
        owner.setBeactive(2);
        ownerMapper.save(owner);
        test2();
        int i =1/0;
    }

    @Transactional
    public void test2(){
        Owner owner = new Owner();
        owner.setId(2);
        owner.setBeactive(2);
        ownerMapper.save(owner);
    }

    @GetMapping("pd/{containerNo}/{stationId}/{locationNo}")
    public void pdDw(@PathVariable("containerNo") String containerNo, @PathVariable("stationId") int stationId, @PathVariable("locationNo") int locationNo) throws Exception {
        pdExecuteService.PdDw(containerNo, stationId, "J0"+locationNo );
    }

    @RequestMapping("/http")
    public String test() {
        return "000";
    }

    @GetMapping("light")
    public void light(){
        StationLxPosition stationLxPosition =new StationLxPosition();
        stationLxPosition.setStationId(1);
        pdExecuteServiceImpl.light(null,stationLxPosition);
    }

    /**
     * spp 测试
     */
    @Autowired
    private UpdateClientStationInfoService updateClientStationInfoService;


    @RequestMapping("/sun/{id}")
    public String testClient(@PathVariable int id) throws Exception {
        StationInfoDto stationSeedInfo = updateClientStationInfoService.findStationSeedInfo(id);
        return PrologApiJsonHelper.toJson(stationSeedInfo);
    }

    @RequestMapping("/send/{id}?type=kk")
    public void SendClient(@PathVariable int id)  {
        updateClientStationInfoService.sendStationInfoToClient(id);
    }
}
