package com.taotao.sso.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.sso.dao.JedisClient;

import redis.clients.jedis.JedisCluster;

public class JedisClientCluster implements JedisClient {
	@Autowired
	private JedisCluster jedisCluter;

	@Override
	public String set(String key, String value) {

		return jedisCluter.set(key, value);
	}

	@Override
	public String get(String key) {
		return jedisCluter.get(key);
	}

	@Override
	public long del(String key) {
		return jedisCluter.del(key);
	}

	@Override
	public long expire(String key, int seconds) {
		return jedisCluter.expire(key, seconds);
	}

	@Override
	public long ttl(String key) {
		// TODO Auto-generated method stub
		return jedisCluter.ttl(key);
	}

	@Override
	public long hset(String hkey, String key, String value) {
		// TODO Auto-generated method stub
		return jedisCluter.hset(hkey, key, value);
	}

	@Override
	public String hget(String hkey, String key) {
		// TODO Auto-generated method stub
		return jedisCluter.hget(hkey, key);
	}

	@Override
	public long hdel(String hkey, String key) {
		// TODO Auto-generated method stub
		return jedisCluter.hdel(hkey, key);
	}

	@Override
	public long incr(String key) {
		// TODO Auto-generated method stub
		return jedisCluter.incr(key);
	}

	@Override
	public long decr(String key) {
		// TODO Auto-generated method stub
		return jedisCluter.decr(key);
	}

}
