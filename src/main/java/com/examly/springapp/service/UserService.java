package com.examly.springapp.service;

import com.examly.springapp.Entity.User;
import com.examly.springapp.configuration.JWTUtil;
import com.examly.springapp.repository.UserRepository;

import com.examly.springapp.Entity.School;
import com.examly.springapp.repository.SchoolRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private SchoolRepository schoolRepository;


    public User register(User user) {
        // Check for duplicate username/email
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        // Enhanced: Allow creating a new school during user registration
        if (user.getSchool() != null) {
            School incomingSchool = user.getSchool();
            Long schoolId = incomingSchool.getId();
            if (schoolId != null && schoolId > 0) {
                // Existing school, fetch from DB
                School school = schoolRepository.findById(schoolId)
                        .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("School not found with id: " + schoolId));
                user.setSchool(school);
            } else if (incomingSchool.getName() != null && !incomingSchool.getName().isBlank()) {
                // Check if school with this name already exists
                Optional<School> existingSchool = schoolRepository.findByName(incomingSchool.getName());
                if (existingSchool.isPresent()) {
                    // Use existing school
                    user.setSchool(existingSchool.get());
                } else {
                    // Create new school
                    School newSchool = new School();
                    newSchool.setName(incomingSchool.getName());
                    newSchool.setDistrict(incomingSchool.getDistrict());
                    newSchool.setAddress(incomingSchool.getAddress());
                    newSchool.setContactInfo(incomingSchool.getContactInfo());
                    School savedSchool = schoolRepository.save(newSchool);
                    user.setSchool(savedSchool);
                }
            } else {
                user.setSchool(null);
            }
        } else {
            user.setSchool(null);
        }
        return userRepository.save(user);
    }

    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail));
    }

    public String login(String usernameOrEmail, String password) {
        Optional<User> userOpt = findByUsernameOrEmail(usernameOrEmail);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPasswordHash())) {
            User user = userOpt.get();
            user.setLastLogin(java.time.LocalDateTime.now());
            userRepository.save(user);
            // Provide authorities as a single role string (ROLE_ prefix for Spring Security)
            java.util.List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities = java.util.List.of(
                new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getRole().name())
            );
            return jwtUtil.generateToken(user.getUsername(), authorities);
        }
        throw new RuntimeException("Invalid credentials");
    }

    // Other CRUD methods as needed

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
