package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepo extends JpaRepository<Report, Long> {
    Report findByUserUserIdAndPostPostId(Long userId, Long postId);

    Page<Report> findByPostPostId(Long postId, Pageable pageable);
}
