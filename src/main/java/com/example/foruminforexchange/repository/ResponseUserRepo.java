package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.dto.ResponseDto;
import com.example.foruminforexchange.model.ResponseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ResponseUserRepo extends JpaRepository<ResponseUser, Long> {
    List<ResponseUser> findAllByResponseResponseId(Long pollId);

    ResponseUser findByResponseResponseIdAndUserUserId(Long responseId, Long userId);

    List<ResponseUser> findByUserUserId(Long userId);

    List<ResponseUser> findByUserUserIdAndResponsePollPollId(Long userId, Long pollId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ResponseUser ic WHERE ic.response.responseId = :responseId")
    void deleteByResponseId(Long responseId);
}
