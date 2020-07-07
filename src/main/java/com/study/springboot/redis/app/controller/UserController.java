package com.study.springboot.redis.app.controller;

import java.util.List;
import java.util.Optional;

import com.study.springboot.redis.app.entity.User;
import com.study.springboot.redis.app.service.UserService;

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
    private UserService userService;

    @GetMapping("/users")
    public List<User> getUsers() {
        return (List<User>) userService.getAll();
    }

    @GetMapping("/users/{id}")
    public @ResponseBody ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> u = userService.getById(id);
        if (u.isPresent()) {
            return ResponseEntity.ok(u.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
 
    @PostMapping("/users")
    void addUser(@RequestBody User user) {
        userService.add(user);
        System.out.println("Added user " + user);
    }

    @GetMapping("/get/{id}")
    public @ResponseBody ResponseEntity<String> getById(@PathVariable String id) {
        return new ResponseEntity<String>("GET Response : " + id, HttpStatus.OK);
    }

}