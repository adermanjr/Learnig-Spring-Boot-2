package com.study.springboot.redis.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.study.springboot.redis.app.entity.User;
import com.study.springboot.redis.app.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = "singleton")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisServiceClient redisServiceClient;
    

    public List<User> getAll() {
        List<User> list = getAllRedis();
        if (list.isEmpty()) list = getAllDB();
        return list;
    }

    private List<User> getAllDB() {
        System.out.println("Get all on DB");
        return (List<User>) userRepository.findAll();
    }

    private List<User> getAllRedis() {
        List<String> listKeys = redisServiceClient.getAllKeys();
        List<User> listUsers = new ArrayList<User>();
        listKeys.stream().sorted().forEach(key -> {
            Optional<User> u = getByIdRedis(Long.parseLong(key));
            listUsers.add(u.get());
        });
        System.out.println("Get all on Redis");
        return listUsers;
    }

    public Optional<User> getById(Long id) {
        Optional<User> optUser = getByIdRedis(id);
        if (!optUser.isPresent()) {
            optUser = getByIdBD(id);
        }
        return optUser;
    }

    private Optional<User> getByIdRedis(Long id){
        System.out.println("Get on Redis -> " + id);
        String u = redisServiceClient.getValue(id.toString());
        User user = getGson().fromJson(u, User.class);
        return Optional.ofNullable(user);
    }

    private Optional<User> getByIdBD(Long id){
        System.out.println("Get on BD -> " + id);
        return userRepository.findById(id);
    }

    public void add(User user) {
        addBD(user);
        addRedis(user);
    }

    private void addBD(User user) {
        userRepository.save(user);
        System.out.println("Added on BD -> " + user.getId());
    }

    private void addRedis(User user) {
        redisServiceClient.add(user.getId().toString(), 
                    getGson().toJson(user), 
                    86400);
        System.out.println("Added on Redis -> " + user.getId());
    }

    protected Gson getGson() {
		return new GsonBuilder().create();
	}

	public void fillDB() {
        List<User> listUsers = getAllRedis();
        if (listUsers.isEmpty()) {
            this.baseStart();
        }
        else {
            listUsers.forEach( user -> {
                this.addBD(user);
            });
        }
    }
    
    private void baseStart() {
        Stream.of("John", "Julie", "Jennifer", "Helen", "Rachel").forEach(name -> {
            User user = new User(name, name.toLowerCase() + "@domain.com");
            this.add(user);
        });
    }
}