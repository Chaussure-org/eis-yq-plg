package com.prolog.eis.init.imp;

import com.prolog.eis.dao.base.SysParameMapper;
import com.prolog.eis.init.SysIsRunningService;
import com.prolog.eis.model.base.SysParame;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SunPP and Rod Johnson
 * @date 2020/07/03 19:42
 */
@Service
public class SysIsRunningImp implements SysIsRunningService {

    private final Logger logger = LoggerFactory.getLogger(SysIsRunningImp.class);

    @Autowired
    private SysParameMapper sysParameMapper;

    @Override
    public String findSysIsrunning() {
        try {
            List<SysParame> parames = sysParameMapper.findByMap(MapUtils.put("parameNo", "sys_running").getMap(), SysParame.class);
            if (parames.size() == 0 || StringUtils.isBlank(parames.get(0).getParameValue())) {
                throw new Exception("配置文件异常");
            }
            if (parames.get(0).getParameValue().equals("0")) {
                logger.info("全局开关是关闭状态");
            }
            String isRunning = parames.get(0).getParameValue();
            return isRunning;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
