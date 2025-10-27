package com.examly.springapp.repository;

import com.examly.springapp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // Filtering by role
    Page<User> findByRole(User.Role role, Pageable pageable);

    // Filtering by schoolId
    Page<User> findBySchool_Id(Long schoolId, Pageable pageable);

    // Filtering by role and schoolId
    Page<User> findByRoleAndSchool_Id(User.Role role, Long schoolId, Pageable pageable);
}
