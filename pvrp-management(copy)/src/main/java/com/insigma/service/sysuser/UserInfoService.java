package com.insigma.service.sysuser;

import com.insigma.dto.SysUserDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统角色service
 * 
 * @author Administrator
 *
 */
@Service
public class UserInfoService {
	/**
	 * 从session里获取用户信息
	 */
	public SysUserDTO getUserInfo(HttpServletRequest request) {

		return (SysUserDTO)request.getSession().getAttribute(request.getSession().getId());
	}

}
