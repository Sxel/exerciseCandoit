package com.exercise.candoit.services;

import com.exercise.candoit.model.Authority;
import com.exercise.candoit.model.User;
import com.exercise.candoit.repositories.AuthorityRepository;
import com.exercise.candoit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public User addUser(User user) {
        List<Authority> authorities = user.getAuthorities();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Guardar las entidades Authority relacionadas antes de guardar el usuario
        if (authorities != null && !authorities.isEmpty()) {
            for (Authority authority : authorities) {
                Authority savedAuthority = authorityRepository.save(authority);
                authority.setId(savedAuthority.getId());
            }
        }

        // Guardar el usuario después de haber guardado las entidades Authority relacionadas
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(updatedUser.getUsername());
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));


            return userRepository.save(user);
        } else {

            throw new RuntimeException("User not found with ID: " + id);
        }
    }


    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    }