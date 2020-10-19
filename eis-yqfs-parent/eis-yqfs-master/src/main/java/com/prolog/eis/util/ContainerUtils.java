package com.prolog.eis.util;

import com.prolog.eis.dto.base.ContainerDTO;

import java.util.Arrays;
import java.util.List;

public class ContainerUtils {

    public static ContainerDTO parse(String containerNo){
        if(containerNo==null){
            return null;
        }

        if(containerNo.trim().length()==0)
            return new ContainerDTO("","");

        List<String> letters = Arrays.asList(new String[]{"A","B","C","D"});
        String lastLetter = containerNo.substring(containerNo.length()-1,containerNo.length());
        if(letters.contains(lastLetter)){
            String word = containerNo.substring(0,containerNo.length()-1);
            return new ContainerDTO(word,lastLetter);
        }

        return new ContainerDTO(containerNo,"");
    }
}
