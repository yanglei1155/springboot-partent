package com.insigma.webtool.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.insigma.dto.SysUserDTO;
import com.insigma.service.sysuser.UserInfoService;
import com.insigma.service.sysuser.facade.SysUserFacade;

import star.bizbase.exception.BizRuleException;
import star.fw.web.util.RequestUtil;
import star.util.StringUtil;

/**
 * 
 * 
 * 
 * Title: 登录拦截器
 * 
 * Description:
 * 
 * Copyright: (c) 2014
 * 
 * @author haoxz11
 * @created 上午9:02:07
 * @version $Id: LoginInterceptor.java 89113 2015-06-13 10:17:14Z zhjy $
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private SysUserFacade sysUserFacade;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//从缓存获取用户信息
		SysUserDTO sysUser = userInfoService.getUserInfo(request);
		

		if (null != sysUser) {
			String uri = request.getRequestURI();
			if (uri.equals("/") || uri.equals("")) {
				request.getRequestDispatcher("/index.html").forward(request, response);
				return false;
			}
//			if (!sysUserFacade.getAuthByUrlAndUserId(uri, sysUser.getId())) {
//				throw new BizRuleException("权限不足");
//			}
			return super.preHandle(request, response, handler);
		} else {
			RequestUtil.send302(response, "/login.html?url=" + request);
		}
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

		super.postHandle(request, response, handler, modelAndView);
	}
}
