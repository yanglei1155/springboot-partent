package com.insigma.po;

import java.util.List;

import com.insigma.dto.SysUserDTO;

import star.vo.BaseVo;

public class UserVo extends BaseVo {

	private static final long serialVersionUID = 1L;
	
	private SysUserDTO sysUserDTO;
	
	private List<SysUserDTO> userList;
	
	private int count;

	public SysUserDTO getSysUserDTO() {
		return sysUserDTO;
	}

	public void setSysUserDTO(SysUserDTO sysUserDTO) {
		this.sysUserDTO = sysUserDTO;
	}

	public List<SysUserDTO> getUserList() {
		return userList;
	}

	public void setUserList(List<SysUserDTO> userList) {
		this.userList = userList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	

}
