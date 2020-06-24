package com.study.springboot.redis.app;

import java.util.stream.Stream;

import com.study.springboot.redis.app.entity.User;
import com.study.springboot.redis.app.service.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
    CommandLineRunner init(UserService userService) {
        return args -> {
            Stream.of("John", "Julie", "Jennifer", "Helen", "Rachel").forEach(name -> {
                User user = new User(name, name.toLowerCase() + "@domain.com");
                userService.add(user);
            });
            userService.getAll().forEach(System.out::println);
        };
    }
}
