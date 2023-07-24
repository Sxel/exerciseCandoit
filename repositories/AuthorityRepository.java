package com.exercise.candoit.repositories;

import com.exercise.candoit.model.Authority;
import com.exercise.candoit.utils.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    List<Authority> findByName(AuthorityName name);
}
