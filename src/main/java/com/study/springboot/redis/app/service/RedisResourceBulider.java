package com.study.springboot.redis.app.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

@Component
public class RedisResourceBulider {

    private StatefulRedisConnection<String, String> connection;
    private InetAddress address = null; 
   
    @Value("${endpointUrl}")
    String redisUrl;
    
    @Value("${endpointPort}")
    String redisPort;

    @Value("${REDIS_PASS:NONE}") //Enviroment variable
    String redisPass;

    @PostConstruct
    void init() throws UnknownHostException  {
        this.address = InetAddress.getByName(this.redisUrl);
        this.createConnection();			
    }

    private void createConnection() {
        //String stringConexao = "redis://sac3062:30311";
        final String stringConnection = "redis:"
                                + "//" + this.redisPass
                                + "@"  + this.redisUrl 
                                + ":"  + this.redisPort;
        System.out.println(stringConnection);
        final RedisClient redisClient =  RedisClient.create(stringConnection);
        connection = redisClient.connect();
    }
	
    public RedisCommands<String, String> connectionSync() {		
        final RedisCommands<String, String> commands = connection.sync();
        return commands;
    }
}