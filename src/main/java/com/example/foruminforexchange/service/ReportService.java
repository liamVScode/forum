package com.example.foruminforexchange.service;

import com.example.foruminforexchange.dto.ReportDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {

    Page<ReportDto> getReportByPost(Long postId, Pageable pageable);
}
