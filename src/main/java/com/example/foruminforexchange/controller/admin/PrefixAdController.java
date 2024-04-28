package com.example.foruminforexchange.controller.admin;

import com.example.foruminforexchange.dto.*;
import com.example.foruminforexchange.service.PrefixService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/prefixes")
@CrossOrigin(origins = "http://localhost:4200")
public class PrefixAdController {

    private final PrefixService prefixService;

    @GetMapping("/all-prefix")
    public ApiResponse<Page<PrefixDto>> getAllPrefix(Pageable pageable){
        ApiResponse<Page<PrefixDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(prefixService.getAllPrefix(pageable));
        return apiResponse;
    }

    @PostMapping("/create-prefix")
    public ApiResponse<PrefixDto> createPrefix(@RequestBody CreatePrefixRequest createPrefixRequest){
        ApiResponse<PrefixDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(prefixService.createPrefix(createPrefixRequest));
        return apiResponse;
    }

    @PutMapping("/edit-prefix")
    public ApiResponse<PrefixDto> editPrefix(@RequestBody EditPrefixRequest editPrefixRequest){
        ApiResponse<PrefixDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(prefixService.editPrefix(editPrefixRequest));
        return apiResponse;
    }

    @DeleteMapping("/delete-prefix")
    public ApiResponse<String> deletePrefix(@RequestParam("prefixId") Long prefixId){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(prefixService.deletePrefix(prefixId));
        return apiResponse;
    }
}
