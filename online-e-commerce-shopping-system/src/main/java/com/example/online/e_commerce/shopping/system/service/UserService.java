package com.example.online.e_commerce.shopping.system.service;

import com.example.online.e_commerce.shopping.system.model.Role;
import com.example.online.e_commerce.shopping.system.model.User;
import com.example.online.e_commerce.shopping.system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User registerUser(String name, String email, String password, String phone, String address, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("An account with this email already exists: " + email);
        }
        User user = new User(name, email, password, phone, address, role != null ? role : Role.ROLE_CUSTOMER);
        return userRepository.save(user);
    }

    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }
        return Optional.empty();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public long countUsers() {
        return userRepository.count();
    }
}
