package com.study.springboot.redis.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.lettuce.core.SetArgs;
import io.lettuce.core.api.sync.RedisCommands;

@Component
public class RedisServiceClient {

    @Autowired
    private RedisResourceBulider redisResourceBulider;
    
    public String getValue(String key) {
		String retorno = null;
		try {
			RedisCommands<String, String> connectionSync = this.redisResourceBulider.connectionSync();
			retorno = connectionSync.get(key);
		} catch (Exception e) {
			System.out.println("Error on getValue("+ key + ") - " + e.getMessage());
		}
		return retorno;
	}

	public void add(String key, String value, Integer time) {
		try {
			RedisCommands<String, String> connectionSync = this.redisResourceBulider.connectionSync();
			connectionSync.set(key, value, SetArgs.Builder.ex(time));
		} catch (Exception e) {
			System.out.println("Error on add("+ key + ", " + value + ", " + time + ") - " + e.getMessage());
		}	
	}
}