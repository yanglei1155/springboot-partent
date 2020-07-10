package com.insigma.po;

import java.util.List;

import com.insigma.dto.SysOrgDTO;

import star.vo.BaseVo;

public class OrgVo extends BaseVo {

	private static final long serialVersionUID = 1L;
	
	private SysOrgDTO sysOrgDTO;
	
	private List<String> insList;

	public SysOrgDTO getSysOrgDTO() {
		return sysOrgDTO;
	}

	public void setSysOrgDTO(SysOrgDTO sysOrgDTO) {
		this.sysOrgDTO = sysOrgDTO;
	}

	public List<String> getInsList() {
		return insList;
	}

	public void setInsList(List<String> insList) {
		this.insList = insList;
	}
	
	

}
