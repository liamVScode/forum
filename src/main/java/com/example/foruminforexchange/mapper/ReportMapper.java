package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.ReportDto;
import com.example.foruminforexchange.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    @Autowired
    private UserMapper userMapper;

    public  ReportDto convertToReportDto(Report report){
        ReportDto reportDto = new ReportDto();
        reportDto.setReportId(report.getReportId());
        reportDto.setPostId(report.getPost().getPostId());
        reportDto.setUserDto(userMapper.convertToUserDto(report.getUser()));
        reportDto.setReportContent(report.getReportContent());
        reportDto.setCreateAt(report.getCreateAt());
        return reportDto;
    }
}
