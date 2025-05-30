package com.gearmind.gearmind_app.controller;

import com.gearmind.gearmind_app.model.User;
import com.gearmind.gearmind_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // ✅ Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User newUser = userService.registerUser(user);

            // ✅ Optional: Return user object without password (better security practice)
            // Here's an example of a simple response:
            return ResponseEntity.ok(new UserResponse(newUser.getId(), newUser.getUsername(), newUser.getRole()));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Simple lookup by username
    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(u -> ResponseEntity.ok(new UserResponse(u.getId(), u.getUsername(), u.getRole())))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Helper response class — prevents sending the password field
    static class UserResponse {
        private Long id;
        private String username;
        private Enum<?> role;

        public UserResponse(Long id, String username, Enum<?> role) {
            this.id = id;
            this.username = username;
            this.role = role;
        }

        public Long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public Enum<?> getRole() {
            return role;
        }
    }
}
