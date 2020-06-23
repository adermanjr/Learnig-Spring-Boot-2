package com.study.springboot.redis.app.controller;

import java.util.List;
import java.util.Optional;

import com.study.springboot.redis.app.entity.User;
import com.study.springboot.redis.app.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public @ResponseBody ResponseEntity<Optional<User>> getUser(@PathVariable String id) {
        Optional<User> u = userRepository.findById(Long.parseLong(id));
        return ResponseEntity.accepted().body(u);
    }
 
    @PostMapping("/users")
    void addUser(@RequestBody User user) {
        userRepository.save(user);
        System.out.println("Added user " + user);
    }

    @GetMapping("/get/{id}")
    public @ResponseBody ResponseEntity<String> getById(@PathVariable String id) {
        return new ResponseEntity<String>("GET Response : " + id, HttpStatus.OK);
    }

}