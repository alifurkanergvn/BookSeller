package com.afe.bookseller.service.concretes;

import com.afe.bookseller.model.Role;
import com.afe.bookseller.model.User;
import com.afe.bookseller.repository.IUserRepository;
import com.afe.bookseller.service.abstracts.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setCreateTime(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional //Update/delete işlemlerişnde Transactional gereklidir.
    public void makeAdmin(String username) {
        userRepository.updateUserRole(username, Role.ADMIN);
    }
}
