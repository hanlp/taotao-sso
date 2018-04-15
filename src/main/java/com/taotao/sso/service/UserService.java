package com.taotao.sso.service;

import com.taotao.pojo.TbUser;
import com.taotao.util.TaotaoResult;

public interface UserService {
	public TaotaoResult checkUser(String content, Integer type);

	public TaotaoResult createUser(TbUser tbUser);

	public TaotaoResult loginUser(String username, String pwd);

	public TaotaoResult getByTokenUser(String token);
}
