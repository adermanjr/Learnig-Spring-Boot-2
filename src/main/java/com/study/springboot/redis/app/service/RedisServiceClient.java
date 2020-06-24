package com.study.springboot.redis.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.lettuce.core.SetArgs;
import io.lettuce.core.api.sync.RedisCommands;

@Component
public class RedisServiceClient {

    @Autowired
    private RedisResourceBulider redisResourceBulider;
    
    public String getValue(String key) {
		String ret = null;
		try {
			RedisCommands<String, String> connectionSync = this.redisResourceBulider.connectionSync();
			ret = connectionSync.get(key);
		} catch (Exception e) {
			System.out.println("Error on getValue("+ key + ") - " + e.getMessage());
		}
		return ret;
	}

	public void add(String key, String value, Integer time) {
		try {
			RedisCommands<String, String> connectionSync = this.redisResourceBulider.connectionSync();
			connectionSync.set(key, value, SetArgs.Builder.ex(time));
		} catch (Exception e) {
			System.out.println("Error on add("+ key + ", " + value + ", " + time + ") - " + e.getMessage());
		}	
	}

	public List<String> getAll(){
		List<String> list = null;
		try {
			RedisCommands<String, String> connectionSync = this.redisResourceBulider.connectionSync();
			list = connectionSync.keys("*");
		} catch (Exception e) {
			System.out.println("Error on getAll() - " + e.getMessage());
		}
		return list;
	}
}