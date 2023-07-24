package com.exercise.candoit.services;

import com.exercise.candoit.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User addUser(User user);
    User updateUser(Long id, User updatedUser);
    boolean deleteUser(Long id);
}
