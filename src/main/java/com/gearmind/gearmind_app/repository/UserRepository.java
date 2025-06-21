package com.gearmind.gearmind_app.repository;

import com.gearmind.gearmind_app.model.User;
import com.gearmind.gearmind_app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(Role role);
}