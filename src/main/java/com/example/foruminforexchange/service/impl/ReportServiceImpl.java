package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.dto.ReportDto;
import com.example.foruminforexchange.mapper.ReportMapper;
import com.example.foruminforexchange.model.Report;
import com.example.foruminforexchange.repository.ReportRepo;
import com.example.foruminforexchange.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepo reportRepo;
    @Autowired
    private ReportMapper reportMapper;
    @Override
    public Page<ReportDto> getReportByPost(Long postId, Pageable pageable) {
        if (pageable == null || pageable.getPageSize() <= 0) {
            pageable = PageRequest.of(0, 10, Sort.by("createAt").descending());
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().and(Sort.by("createAt").descending()));
        }

        Page<Report> reports = reportRepo.findByPostPostId(postId, pageable);
        if(reports == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        Page<ReportDto> reportDtos = reports.map(report -> reportMapper.convertToReportDto(report));
        return reportDtos;
    }

}
