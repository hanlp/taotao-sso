package com.taotao.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.util.TaotaoResult;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping("/check/{content}/{type}")
	@ResponseBody
	public Object checkUser(@PathVariable String content, @PathVariable Integer type, String callback) {

		// 1.判断type类别 不是123的情况
		TaotaoResult result = null;
		if (type != 1 && type != 2 && type != 3) {
			return TaotaoResult.build(400, "数据不可用!!");
		}

		// 校验数据
		result = userService.checkUser(content, type);
		if (callback != null) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {// 直接返回result
			return result;
		}
	}

	// 用户注册
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult registerUser(TbUser tbUser) {
		return userService.createUser(tbUser);
	}

	// 用户登录
	@RequestMapping(value = "/login")
	@ResponseBody
	public TaotaoResult loginUser(String username, String password) {
		return userService.loginUser(username, password);
	}

	// 根据token取用户信息
	@RequestMapping(value = "/token/{token}")
	@ResponseBody
	public Object getTokenUser(@PathVariable String token, String callback) {
		TaotaoResult result = userService.getByTokenUser(token);
		if (!StringUtils.isBlank(callback)) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {
			return result;
		}
	}

}
