package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.bcel.TypeAnnotationAccessVar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.dao.JedisClient;
import com.taotao.sso.service.UserService;
import com.taotao.util.ExceptionUtil;
import com.taotao.util.IDUtils;
import com.taotao.util.JsonUtils;
import com.taotao.util.TaotaoResult;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private TbUserMapper userMapper;

	@Value("${USER_REDIS_TOKEN}")
	private String USER_REDIS_TOKEN;
	@Value("${USER_REDIS_EXPIRE}")
	private int USER_REDIS_EXPIRE;

	@Autowired
	private JedisClient jedisClient;

	/**
	 * 
	 * <p>
	 * Title: checkUser
	 * </p>
	 * <p>
	 * Description: 用户信息的校验
	 * </p>
	 * 
	 * @param content
	 * @param type
	 * @return
	 * @see com.taotao.sso.service.UserService#checkUser(java.lang.String,
	 *      java.lang.Integer)
	 */
	@Override
	public TaotaoResult checkUser(String content, Integer type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 1、2、3分别代表username、phone、email
		if (type == 1) {
			criteria.andUsernameEqualTo(content);
		} else if (type == 2) {
			criteria.andPhoneEqualTo(content);

		} else if (type == 3) {
			criteria.andEmailEqualTo(content);
		}
		List<TbUser> list = userMapper.selectByExample(example);
		// 没有查询出用户信息,说明这个用户在数据库中不存在.
		if (list.size() == 0 || list == null) {
			return TaotaoResult.ok(true);
		} else {
			return TaotaoResult.ok(false);
		}
	}

	// 用户注册功能
	@Override
	public TaotaoResult createUser(TbUser tbUser) {
		// 1.数据补全
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		// 密码: md5加密
		tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
		try {
			// 添加数据
			userMapper.insert(tbUser);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));

		}
		return TaotaoResult.ok();
	}

	// 登录功能
	@Override
	public TaotaoResult loginUser(String username, String pwd) {
		// 1.根据用户去系统中查询,查询是否有此用户
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if (list.size() == 0 || list == null) {
			return TaotaoResult.build(400, "该用户不存在");
		}
		if (list.get(0).getPassword().equals(DigestUtils.md5DigestAsHex(pwd.getBytes()))) {
			// 登录成功
			// 生成一个token码, key值 value user用户 把信息保存到redis中, 给redis设置一个生命周期.
			String token = IDUtils.genImageName();
			TbUser users = list.get(0);// 获取用户信息
			users.setPassword(null);// 清空用户密码
			// 把这个随机码为key值保存到redis中
			jedisClient.set(USER_REDIS_TOKEN + ":" + token, JsonUtils.objectToJson(users));
			// 设置redis生命周期
			jedisClient.expire(USER_REDIS_TOKEN + ":" + token, USER_REDIS_EXPIRE);
			// 返回token码
			return TaotaoResult.ok(token);
		} else {
			return TaotaoResult.build(400, "密码有误,请重新输入...");
		}
	}

	// 根据token取用户信息
	@Override
	public TaotaoResult getByTokenUser(String token) {
		String jsondata = jedisClient.get(USER_REDIS_TOKEN + ":" + token);// 获取token值
		if (StringUtils.isBlank(jsondata)) {
			return TaotaoResult.build(400, "未取到用户信息!!");
		} else {
			return TaotaoResult.ok(jsondata);// 返回用户信息
		}
	}
}
