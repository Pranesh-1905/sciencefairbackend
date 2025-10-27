package com.examly.springapp.controller;

import com.examly.springapp.Entity.User;
import com.examly.springapp.service.UserService;

import lombok.Data;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    // Get logged-in user info
    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestAttribute(value = "username", required = false) String username) {
        if (username == null) {
            return ResponseEntity.status(401).body(java.util.Map.of("error", "Unauthorized or missing token"));
        }

        return userService.findByUsernameOrEmail(username)
                .map(user -> {
                    String role = user.getRole() != null ? user.getRole().toString() : "USER";

                    java.util.Map<String, Object> response = new java.util.HashMap<>();
                    response.put("id", user.getId());
                    response.put("username", user.getUsername());
                    response.put("email", user.getEmail());
                    response.put("role", role);
                    response.put("studentId", user.getStudentId());
                    response.put("gradeLevel", user.getGradeLevel());
                    response.put("school", user.getSchool() != null ? user.getSchool().getName() : null);
                    response.put("createdDate", user.getCreatedDate());
                    response.put("lastLogin", user.getLastLogin());
                    response.put("isActive", user.getIsActive());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(404).body(java.util.Map.of("error", "User not found")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User created = userService.register(user);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String token = userService.login(req.getUsernameOrEmail(), req.getPassword());
        User user = userService.findByUsernameOrEmail(req.getUsernameOrEmail()).orElse(null);
        System.out.println("User logged in: " + user);
        String role = user != null ? user.getRole().name() : null;
        return ResponseEntity.ok(new JwtResponse(token, role));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // For JWT, logout is handled client-side (just delete token).
        return ResponseEntity.ok("Logged out");
    }
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    @Data
    public static class LoginRequest {
        private String usernameOrEmail;
        private String password;
    }

    @Data
    public static class JwtResponse {
        private final String token;
        private final String role;
    }
}
