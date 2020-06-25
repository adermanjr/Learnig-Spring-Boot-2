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
        List<String> listKeys = redisServiceClient.getAll();
        List<User> listUsers = new ArrayList<User>();
        listKeys.stream().sorted().forEach(key -> {
            User u = getByIdRedis(Long.parseLong(key));
            listUsers.add(u);
        });
        System.out.println("Get all on Redis");
        return listUsers;
    }

    public User getById(Long id) {
        User u = getByIdRedis(id);
        if (u == null) {
            u = getByIdBD(id);
        }
        return u;
    }

    private User getByIdRedis(Long id){
        String u = redisServiceClient.getValue(id.toString());
        System.out.println("Get on Redis -> " + id);
        return getGson().fromJson(u, User.class);
    }

    private User getByIdBD(Long id){
        Optional<User> u = userRepository.findById(id);
        System.out.println("Get on BD -> " + id);
        return (u.isPresent() ? u.get() : null); 
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