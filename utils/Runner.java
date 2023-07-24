package com.exercise.candoit.utils;

import com.exercise.candoit.model.Authority;
import com.exercise.candoit.model.User;
import com.exercise.candoit.repositories.AuthorityRepository;
import com.exercise.candoit.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Runner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public Runner(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (this.authorityRepository.count() == 0) {
            this.authorityRepository.saveAll(List.of(
                    new Authority(AuthorityName.ADMIN),
                    new Authority(AuthorityName.READ)
            ));
        }

        if (this.userRepository.count() == 0) {
            List<Authority> adminAuthorities = this.authorityRepository.findByName(AuthorityName.ADMIN);
            List<Authority> readAuthorities = this.authorityRepository.findByName(AuthorityName.READ);

            if (adminAuthorities.isEmpty()) {
                throw new RuntimeException("No se encontró la autoridad ADMIN");
            }
            if (readAuthorities.isEmpty()) {
                throw new RuntimeException("No se encontró la autoridad READ");
            }

            String encodedPassword1 = passwordEncoder.encode("admin1");
            String encodedPassword2 = passwordEncoder.encode("admin2");

            this.userRepository.save(new User("admin1", encodedPassword1, adminAuthorities));

            this.userRepository.save(new User("admin2", encodedPassword2, readAuthorities));
        }
    }
}
