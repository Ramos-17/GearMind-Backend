package com.gearmind.gearmind_app.service;

import com.gearmind.gearmind_app.model.User;
import com.gearmind.gearmind_app.model.Role;
import com.gearmind.gearmind_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User registerUser(User user) {
        Optional<User> existing = userRepository.findByUsername(user.getUsername());
        if (existing.isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        
        if (user.getRole() == null) {
            throw new RuntimeException("Role is required");
        }

        
        try {
            Role.valueOf(user.getRole().name());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + user.getRole());
        }

        return userRepository.save(user);
    }

    public List<String> getUsersByRole(Role role) {
        return userRepository.findByRole(role)
            .stream()
            .map(User::getUsername)
            .toList();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found: " + username);
        }
        userRepository.delete(user.get());
    }
}