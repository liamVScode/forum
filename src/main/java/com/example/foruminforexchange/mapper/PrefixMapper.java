package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.PrefixDto;
import com.example.foruminforexchange.model.Prefix;
import org.springframework.security.core.parameters.P;

public class PrefixMapper {
    public static PrefixDto convertToPrefixDto(Prefix prefix){
        PrefixDto prefixDto = new PrefixDto();
        prefixDto.setPrefixId(prefix.getPrefixId());
        prefixDto.setPrefixName(prefix.getPrefixName());
        return prefixDto;
    }
}
