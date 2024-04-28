package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.dto.CreatePrefixRequest;
import com.example.foruminforexchange.dto.EditPrefixRequest;
import com.example.foruminforexchange.dto.PrefixDto;
import com.example.foruminforexchange.dto.TopicDto;
import com.example.foruminforexchange.mapper.PrefixMapper;
import com.example.foruminforexchange.mapper.TopicMapper;
import com.example.foruminforexchange.model.Prefix;
import com.example.foruminforexchange.model.Topic;
import com.example.foruminforexchange.model.TopicPrefix;
import com.example.foruminforexchange.repository.PrefixRepo;
import com.example.foruminforexchange.repository.TopicPrefixRepo;
import com.example.foruminforexchange.service.PrefixService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrefixServiceImpl implements PrefixService {

    private final PrefixRepo prefixRepo;
    private final TopicPrefixRepo topicPrefixRepo;
    @Override
    public Page<PrefixDto> getAllPrefix(Pageable pageable) {
        if (pageable == null || pageable.getPageSize() <= 0) {
            pageable = PageRequest.of(0, 10, Sort.by("prefixId").ascending());
        } else {
            int pageSize = Math.min(pageable.getPageSize(), 10);
            pageable = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort().and(Sort.by("prefixId").ascending()));
        }
        Page<Prefix> prefixes = prefixRepo.findAll(pageable);
        if(prefixes == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        Page<PrefixDto> prefixDtos = prefixes.map(prefix -> PrefixMapper.convertToPrefixDto(prefix));
        return prefixDtos;
    }

    @Override
    public PrefixDto createPrefix(CreatePrefixRequest createPrefixRequest) {
        if(createPrefixRequest.getPrefixName() == null){
            throw new AppException(ErrorCode.NOT_BLANK);
        }
        Prefix prefix = new Prefix(createPrefixRequest.getPrefixName());
        prefixRepo.save(prefix);
        PrefixDto prefixDto = PrefixMapper.convertToPrefixDto(prefix);
        return prefixDto;
    }

    @Override
    public PrefixDto editPrefix(EditPrefixRequest editPrefixRequest) {
        if(editPrefixRequest.getPrefixName() == null || editPrefixRequest.getPrefixId() == null){
            throw new AppException(ErrorCode.NOT_BLANK);
        }
        Prefix prefix = prefixRepo.findById(editPrefixRequest.getPrefixId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        prefix.setPrefixName(editPrefixRequest.getPrefixName());
        prefixRepo.save(prefix);
        PrefixDto prefixDto = PrefixMapper.convertToPrefixDto(prefix);
        return prefixDto;
    }

    @Override
    public String deletePrefix(Long prefixId) {
        Prefix prefix = prefixRepo.findById(prefixId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        TopicPrefix topicPrefix = topicPrefixRepo.findByTopicTopicId(prefixId);
        if(topicPrefix != null){
            topicPrefixRepo.delete(topicPrefix);
        }
        prefixRepo.delete(prefix);
        return "Delete topic successfully!";
    }
}
