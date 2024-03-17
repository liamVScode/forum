package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Role;
import com.example.foruminforexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    User findByRole(Role role);


}
