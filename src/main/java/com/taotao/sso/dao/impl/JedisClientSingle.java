package com.taotao.sso.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.sso.dao.JedisClient;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisClientSingle implements JedisClient {
	@Autowired
	private JedisPool jedisPool;

	@Override
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		String flag = jedis.set(key, value);
		jedis.close();
		return flag;
	}

	@Override
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.get(key);
		jedis.close();
		return result;
	}

	@Override
	public long del(String key) {
		Jedis jedis = jedisPool.getResource();
		long flag = jedis.del(key);
		jedis.close();
		return flag;
	}

	@Override
	public long expire(String key, int seconds) {
		Jedis jedis = jedisPool.getResource();
		long flag = jedis.expire(key, seconds);
		jedis.close();
		return flag;
	}

	@Override
	public long ttl(String key) {
		Jedis jedis = jedisPool.getResource();
		long flag = jedis.ttl(key);
		jedis.close();
		return flag;
	}

	@Override
	public long hset(String hkey, String key, String value) {
		Jedis jedis = jedisPool.getResource();
		long flag = jedis.hset(hkey, key, value);
		jedis.close();
		return flag;
	}

	@Override
	public String hget(String hkey, String key) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.hget(hkey, key);
		jedis.close();
		return result;
	}

	@Override
	public long hdel(String hkey, String key) {
		Jedis jedis = jedisPool.getResource();
		long flag = jedis.hdel(hkey, key);
		jedis.close();
		return flag;
	}

	@Override
	public long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		long falg = jedis.incr(key);
		jedis.close();
		return falg;
	}

	@Override
	public long decr(String key) {
		Jedis jedis = jedisPool.getResource();
		long falg = jedis.decr(key);
		jedis.close();
		return falg;
	}

}
