package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PrefixService {
    Page<PrefixDto> getAllPrefix(Pageable pageable);

    List<PrefixDto> getAllListPrefix();
    PrefixDto createPrefix(CreatePrefixRequest createPrefixRequest);
    PrefixDto editPrefix(EditPrefixRequest editPrefixRequest);
    String deletePrefix(Long prefixId);
}
