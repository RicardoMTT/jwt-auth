package com.example.jwt.demo_implement_jwt.services;

import com.example.jwt.demo_implement_jwt.entities.User;
import com.example.jwt.demo_implement_jwt.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);  // Agregar cada elemento de iterable a la lista

        return users;
    }
}
