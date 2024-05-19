package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Category;
import com.example.foruminforexchange.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findByCategoryId(Long id);

    List<Category> findAllByTopicTopicId(Long topicId);
}
