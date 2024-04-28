package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PrefixService {
    Page<PrefixDto> getAllPrefix(Pageable pageable);
    PrefixDto createPrefix(CreatePrefixRequest createPrefixRequest);
    PrefixDto editPrefix(EditPrefixRequest editPrefixRequest);
    String deletePrefix(Long prefixId);
}
