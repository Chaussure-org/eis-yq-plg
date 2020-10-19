package com.prolog.eis.boxbank.out;

import com.prolog.eis.dto.pddispatch.PanDianLxDto;
import com.prolog.eis.dto.pddispatch.PanDianXiangKuDto;

public interface PDEnginee {
    PanDianXiangKuDto init()throws Exception;
    boolean chuku(PanDianLxDto lx, int stationId)throws Exception;
}
