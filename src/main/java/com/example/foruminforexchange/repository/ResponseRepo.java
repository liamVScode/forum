package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Poll;
import com.example.foruminforexchange.model.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ResponseRepo extends JpaRepository<Response, Long> {
    List<Response> findAllByPollPollId(Long pollId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Response ic WHERE ic.poll.pollId = :pollId")
    void deleteByPollId(Long pollId);



    @Query("SELECT SUM(r.voteCount) FROM Response r WHERE r.poll.pollId = ?1")
    Long getTotalVotesByPollId(Long pollId);
}
