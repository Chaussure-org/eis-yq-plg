package com.prolog.eis.boxbank.out;

import com.prolog.eis.dto.tkdispatch.TuiKuLxDto;
import com.prolog.eis.dto.tkdispatch.TuiKuXiangKuDto;
import com.prolog.eis.wcs.dto.TaskCallbackDTO;

public interface TKEnginee {
    TuiKuXiangKuDto init()throws Exception;
    boolean chuku(TuiKuLxDto tuiKuLxDto)throws Exception;


}
