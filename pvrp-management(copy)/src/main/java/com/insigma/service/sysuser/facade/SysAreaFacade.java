package com.insigma.service.sysuser.facade;

import java.util.List;

import com.insigma.dto.SysAreaDTO;


/**
 * 系统区域服务接口
 * 
 * @author Administrator
 *
 */
public interface SysAreaFacade {

    
	/**
	 * 根据主键得到系统区域表记录
	 * 
	 * @param id
	 * @return
	 */
	public SysAreaDTO getByPrimaryKey(Long id);
	
    /**
     * 根据区域ID查询下级
     * @param areaId
     * @return
     */
    public List<SysAreaDTO> findByParentId(Long areaId);

}
