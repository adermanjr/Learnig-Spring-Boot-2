package com.study.springboot.redis.app.repository;

import com.study.springboot.redis.app.entity.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{}