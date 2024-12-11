package com.example.jwt.demo_implement_jwt.repositories;

import com.example.jwt.demo_implement_jwt.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
* Data Access Layer for the user Entity
* */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
