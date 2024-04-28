package com.example.foruminforexchange.controller.admin;

import com.example.foruminforexchange.dto.ApiResponse;
import com.example.foruminforexchange.dto.ReportDto;
import com.example.foruminforexchange.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/reports")
@CrossOrigin(origins = "http://localhost:4200")
public class ReportAdController {

    private final ReportService reportService;

    @GetMapping("/all-report")
    public ApiResponse<Page<ReportDto>> getReportByPost(@RequestParam("postId") Long postId, Pageable pageable){
        ApiResponse<Page<ReportDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(reportService.getReportByPost(postId, pageable));
        return apiResponse;
    }
}
