package com.gearmind.gearmind_app.controller;

import com.gearmind.gearmind_app.model.User;
import com.gearmind.gearmind_app.model.Role;
import com.gearmind.gearmind_app.service.UserService;
import com.gearmind.gearmind_app.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "https://gear-mind-frontend.vercel.app"})
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User newUser = userService.registerUser(user);
            return ResponseEntity.ok(
                new UserResponse(newUser.getId(), newUser.getUsername(), newUser.getRole())
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
            )
        );
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthResponse(jwt));
    }

    
    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(u -> 
                ResponseEntity.ok(new UserResponse(u.getId(), u.getUsername(), u.getRole()))
            )
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @GetMapping("/users/tech")
    public ResponseEntity<List<String>> getTechUsers() {
        List<String> techUsernames = userService.getUsersByRole(Role.ROLE_TECH);
        return ResponseEntity.ok(techUsernames);
    }

    
    @GetMapping("/users/painter")
    public ResponseEntity<List<String>> getPainterUsers() {
        List<String> painterUsernames = userService.getUsersByRole(Role.ROLE_PAINTER);
        return ResponseEntity.ok(painterUsernames);
    }

    
    @GetMapping("/users/detailer")
    public ResponseEntity<List<String>> getDetailerUsers() {
        List<String> detailerUsernames = userService.getUsersByRole(Role.ROLE_DETAILER);
        return ResponseEntity.ok(detailerUsernames);
    }

    
    @GetMapping("/users/all")
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        List<UserInfo> allUsers = userService.getAllUsers().stream()
            .map(user -> new UserInfo(user.getId(), user.getUsername(), user.getRole()))
            .toList();
        return ResponseEntity.ok(allUsers);
    }

    
    @DeleteMapping("/users/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            userService.deleteUserByUsername(username);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
    public static record LoginRequest(String username, String password) {}

    public static class JwtAuthResponse {
        private final String accessToken;
        private final String tokenType = "Bearer";

        public JwtAuthResponse(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getTokenType() {
            return tokenType;
        }
    }

    public static class UserResponse {
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

    public static class UserInfo {
        private Long id;
        private String username;
        private Enum<?> role;

        public UserInfo(Long id, String username, Enum<?> role) {
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
