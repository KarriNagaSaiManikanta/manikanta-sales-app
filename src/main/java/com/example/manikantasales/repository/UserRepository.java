package com.example.manikantasales.repository;

import com.example.manikantasales.entity.User;
import com.example.manikantasales.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    boolean existsByRole(Role role);

    // =====================================
    // TOTAL USERS COUNT
    // =====================================
    long count();
    List<User> findAllByOrderByIdDesc();

}