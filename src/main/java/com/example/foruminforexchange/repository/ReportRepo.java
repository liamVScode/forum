package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepo extends JpaRepository<Report, Long> {
    Report findByUserUserIdAndPostPostId(Long userId, Long postId);
}
