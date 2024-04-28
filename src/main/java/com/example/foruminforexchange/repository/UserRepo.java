package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Role;
import com.example.foruminforexchange.model.Status;
import com.example.foruminforexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    User findUserByEmail(String email);

    User findByRole(Role role);

    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status AND u.role = :role")
    Long countByStatusAndRole(@Param("status") Status status, @Param("role") Role role);

    @Query("SELECT u FROM User u WHERE u.status = :status AND u.role = :role")
    List<User> findAllByRole(@Param("status") Status status, @Param("role") Role role);


}
