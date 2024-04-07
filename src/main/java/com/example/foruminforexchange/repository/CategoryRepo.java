package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Category;
import com.example.foruminforexchange.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findByCategoryId(Long id);
}
