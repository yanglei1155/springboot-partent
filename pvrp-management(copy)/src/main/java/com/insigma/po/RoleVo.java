package com.insigma.po;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.insigma.dto.SysRoleDTO;
import com.insigma.dto.SysRoleFunctionDTO;

import star.vo.BaseVo;

public class RoleVo extends BaseVo {

	private static final long serialVersionUID = 1L;
	
	
	private List<SysRoleDTO> roleList;
	
	private int count;
	
	List<SysRoleFunctionDTO> rfList;
	
	JSONArray jsonArray;
	
	Map<Integer,JSONArray> mapArray;



	public List<SysRoleDTO> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<SysRoleDTO> roleList) {
		this.roleList = roleList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<SysRoleFunctionDTO> getRfList() {
		return rfList;
	}

	public void setRfList(List<SysRoleFunctionDTO> rfList) {
		this.rfList = rfList;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public Map<Integer, JSONArray> getMapArray() {
		return mapArray;
	}

	public void setMapArray(Map<Integer, JSONArray> mapArray) {
		this.mapArray = mapArray;
	}

	

}
