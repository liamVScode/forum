package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.ReportDto;
import com.example.foruminforexchange.model.Report;

public class ReportMapper {
    public static ReportDto convertToReportDto(Report report){
        ReportDto reportDto = new ReportDto();
        reportDto.setReportId(report.getReportId());
        reportDto.setPostId(report.getPost().getPostId());
        reportDto.setUserDto(UserMapper.convertToUserDto(report.getUser()));
        reportDto.setReportContent(report.getReportContent());
        reportDto.setCreateAt(report.getCreateAt());
        return reportDto;
    }
}
