package com.taotao.sso.dao;

public interface JedisClient {

	// 设置内容
	public String set(String key, String value);

	// 取参数
	public String get(String key);

	// 删除
	public long del(String key);
	// 设置生命周期

	public long expire(String key, int seconds);

	// 获取剩余生命周期
	public long ttl(String key);

	// hset
	public long hset(String hkey, String key, String value);

	// hget
	public String hget(String hkey, String key);

	// 删除方法
	public long hdel(String hkey, String key);

	// 自增
	public long incr(String key);

	// 自减
	public long decr(String key);

}
