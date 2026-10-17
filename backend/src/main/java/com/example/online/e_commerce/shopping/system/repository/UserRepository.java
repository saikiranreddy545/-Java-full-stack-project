package com.example.online.e_commerce.shopping.system.repository;

import com.example.online.e_commerce.shopping.system.model.Role;
import com.example.online.e_commerce.shopping.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(Role role);
}
