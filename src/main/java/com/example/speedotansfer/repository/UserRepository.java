package com.example.speedotansfer.repository;

import com.example.speedotansfer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    User findCustomerByUsername(String username);
    User findTopByOrderByCreationTimeStampDesc();
    User findCustomerByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findUserByEmail(String email);
}
