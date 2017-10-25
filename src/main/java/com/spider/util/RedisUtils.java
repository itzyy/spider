package com.spider.util;


import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {
	public static String start_url = "start_url_s";
	
	public static String key = "spider_url_s";
	public static String p_key = "spider_proxy_s";
	public static String b_key = "spider_backip_proxy_s";
	public static String s_key = "spider_success_proxy_s";
	public static String u_key = "u_key";

	
	JedisPool jedisPool = null;
	public RedisUtils(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(10);
		poolConfig.setMaxTotal(100);
		poolConfig.setMaxWaitMillis(10000);
		poolConfig.setTestOnBorrow(true);
		jedisPool = new JedisPool(poolConfig, "192.168.239.12", 6379);
	}
	
	public List<String> lrange(String key,int start,int end){
		Jedis resource = jedisPool.getResource();
		
		List<String> list = resource.lrange(key, start, end);
		resource.close();
		return list;
		
	}


    public void add_s(String lowKey, String url) {
        Jedis resource = jedisPool.getResource();
        resource.sadd(lowKey, url);
        resource.close();
    }


	public String poll_s(String key) {
		Jedis resource = jedisPool.getResource();
		String result = resource.spop(key);
		resource.close();
		return result;
	}

	public String srandmember(String key){
        Jedis resource = jedisPool.getResource();
        resource.close();
        return resource.srandmember(key);
    }

	public  Set<String> smember(String key){
        Jedis resource = jedisPool.getResource();
        resource.close();
        return resource.smembers(key);

    }
	
}
