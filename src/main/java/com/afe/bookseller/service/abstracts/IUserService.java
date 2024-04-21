package com.afe.bookseller.service.abstracts;

import com.afe.bookseller.model.User;

import java.util.Optional;


public interface IUserService
{
    User saveUser(User user);

    Optional<User> findByUsername(String username);

    void makeAdmin(String username);
}
