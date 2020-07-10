package com.insigma.service.sysuser.facade;

import java.util.HashMap;
import java.util.List;

import com.insigma.dto.SysOrgDTO;

import com.insigma.po.SysOrg;
import com.insigma.po.soldier.SysRegional;
import star.bizbase.exception.BizRuleException;

/**
 * 系统机构服务接口
 * 
 * @author Administrator
 *
 */
public interface SysOrgFacade {
	/**
	 * 新增机构po信息
	 * 
	 * @param po
	 * @return
	 * @throws BizRuleException
	 */
	public Long addSysOrg(SysOrgDTO po) throws BizRuleException;

	/**
	 * 根据主键得到系统机构表记录
	 * 
	 * @param id
	 * @return
	 */
	public SysOrgDTO getByPrimaryKey(Long id);

	/**
	 * 更新po信息
	 * 
	 * @param po
	 * @return
	 */
	public int updatepo(SysOrgDTO po) throws BizRuleException;

	/**
	 * 根据参数查询 获取机构列表 带分页
	 * 
	 * @param searchMap
	 * @param start
	 * @param size
	 * @return
	 */
	public List<SysOrgDTO> getListByWhere(HashMap<String, Object> searchMap, int start, int size);

	/**
	 * 根据参数查询机构列表
	 * 
	 * @param searchMap
	 * @return
	 */
	public List<SysOrgDTO> getListByWhere(HashMap<String, Object> searchMap);

	/**
	 * 根据参数查询系统机构的记录数量
	 * 
	 * @param searchMap
	 * @return
	 */
	public int getCountByWhere(HashMap<String, Object> searchMap);
	
	/**
	 * 校验用机构名和机构代码是否重复
	 * @param SysOrgDTO
	 * @return
	 */
    boolean checkOrgNameOrgenterCode(SysOrgDTO sysOrgDTO);
    
    /**
     * 根据机构id删除
     * @param id
     */
    public void deleteByPrimaryKey(Long id) throws BizRuleException;
    
    /**
     * 根据机构名称查询
     * @param orgName
     * @return
     */
    public SysOrgDTO findByName(String orgName);
    
    /**
     * 根据区域ID查询机构列表
     * @param areaId
     * @return
     */
    public List<SysOrgDTO> queryOrgNodes(String areaId);
    
    
    /**
     * 通过行政区和险种获取机构信息
     * @param areaId
     * @param insId
     * @return
     */
    public SysOrgDTO findByAreaIdAndInsId(String areaId,String insId);

}
