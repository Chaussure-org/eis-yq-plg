package com.prolog.eis.boxbank.out;

import com.prolog.eis.dto.bhdispatch.BuHuoLxDto;
import com.prolog.eis.dto.bhdispatch.BuHuoXiangKuDto;

public interface BHEnginee {
    BuHuoXiangKuDto init()throws Exception;
    boolean chuku(BuHuoLxDto buHuoLxDto)throws Exception;
}
