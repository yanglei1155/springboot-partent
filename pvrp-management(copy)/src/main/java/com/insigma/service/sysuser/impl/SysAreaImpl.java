package com.insigma.service.sysuser.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insigma.dto.SysAreaDTO;
import com.insigma.service.sysuser.SysAreaService;
import com.insigma.service.sysuser.facade.SysAreaFacade;

/**
 * 系统区域facade服务实现类
 * 
 * @author Administrator
 *
 */
@Service
public class SysAreaImpl implements SysAreaFacade{
	
	@Autowired
	private SysAreaService sysAreaService;


	@Override
	public SysAreaDTO getByPrimaryKey(Long id) {
		return sysAreaService.getByPrimaryKey(id);
	}

	@Override
	public List<SysAreaDTO> findByParentId(Long areaId) {
		return sysAreaService.findByParentId(areaId);
	}
	

}
