package com.example.manikantasales.serviceimpl;

import com.example.manikantasales.entity.User;
import com.example.manikantasales.repository.AddressRepository;
import com.example.manikantasales.repository.UserRepository;
import com.example.manikantasales.service.UserService;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserServiceImpl(UserRepository userRepository,
                           AddressRepository addressRepository) {

        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    // =====================================
    // GET USER BY EMAIL
    // =====================================

    @Override
    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

    }

    // =====================================
    // GET ALL USERS
    // =====================================

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAllByOrderByIdDesc();

    }

    // =====================================
    // GET USER BY ID
    // =====================================

    @Override
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

    }

    // =====================================
    // DELETE USER
    // =====================================

    @Override
    public void deleteUser(Long id) {

        User user = getUserById(id);

        // Delete all addresses of this user
        addressRepository.deleteByUser(user);

        // Delete user
        userRepository.delete(user);

    }

    // =====================================
    // BLOCK USER
    // =====================================

    @Override
    public void blockUser(Long id) {

        User user = getUserById(id);

        user.setAccountLocked(true);

        userRepository.save(user);

    }

    // =====================================
    // UNBLOCK USER
    // =====================================

    @Override
    public void unblockUser(Long id) {

        User user = getUserById(id);

        user.setAccountLocked(false);

        userRepository.save(user);

    }

    // =====================================
    // USER COUNT
    // =====================================

    @Override
    public Long getUserCount() {

        return userRepository.count();

    }

}