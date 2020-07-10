package com.insigma.service.sysuser.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.insigma.po.SysOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insigma.dto.SysOrgDTO;
import com.insigma.service.sysuser.SysOrgService;
import com.insigma.service.sysuser.facade.SysOrgFacade;

import star.bizbase.exception.BizRuleException;
import star.bizbase.util.RuleCheck;

/**
 * 系统机构facade服务实现类
 * 
 * @author Administrator
 *
 */
@Service
public class SysOrgImpl implements SysOrgFacade{
	
	@Autowired
	private SysOrgService sysOrgService;

	@Override
	@Transactional
	public Long addSysOrg(SysOrgDTO po) throws BizRuleException {
		/* 非空校验 */
		RuleCheck.validateByAnnotation(po);
		return sysOrgService.addSysOrg(po);
	}

	@Override
	public SysOrgDTO getByPrimaryKey(Long id) {
		return sysOrgService.getByPrimaryKey(id);
	}

	@Override
	@Transactional
	public int updatepo(SysOrgDTO po) throws BizRuleException{
		return sysOrgService.updatepo(po);
	}

	@Override
	public List<SysOrgDTO> getListByWhere(HashMap<String, Object> searchMap, int start, int size) {
		return sysOrgService.getListByWhere(searchMap, start, size);
	}

	@Override
	public List<SysOrgDTO> getListByWhere(HashMap<String, Object> searchMap) {
		return sysOrgService.getListByWhere(searchMap);
	}

	@Override
	public int getCountByWhere(HashMap<String, Object> searchMap) {
		return sysOrgService.getCountByWhere(searchMap);
	}

	@Override
	public boolean checkOrgNameOrgenterCode(SysOrgDTO sysOrgDTO) {
		Boolean flag=false;
		List<SysOrgDTO> sysOrgList= sysOrgService.checkOrgNameOrgenterCode(sysOrgDTO.getOrgName(), sysOrgDTO.getOrgenterCode());
		if(sysOrgList.size()>0){
			for(SysOrgDTO org : sysOrgList){
				if(null != sysOrgDTO.getId() || sysOrgDTO.getId() == org.getId()){
					flag=false;
				}else{
					flag=true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 根据主键ID删除
	 */
	@Override
	@Transactional
	public void deleteByPrimaryKey(Long id) throws BizRuleException{
		sysOrgService.deleteByPrimaryKey(id);
	}
	
	/**
	 * 
	 * 根据区域ID查询机构列表
	 */
	@Override
	public List<SysOrgDTO> queryOrgNodes(String areaId) {
		List<SysOrgDTO> orgList = sysOrgService.findByRegioncode(areaId);
		List<SysOrgDTO> orgTree = new ArrayList<>();
		for (SysOrgDTO sysOrg : orgList) {
			orgTree.add(sysOrg);// 添加自身节点
//			orgTree.addAll(sysOrgService.findByIdpath(sysOrg.getIdpath()+"/"));
		}
		return orgTree;
	}

	/**
	 * 根据机构名称查询
	 */
	@Override
	public SysOrgDTO findByName(String orgName) {
		return sysOrgService.findByName(orgName);
	}

	/**
     * 通过行政区和险种获取机构信息
     * @param areaId
     * @param insId
     * @return
     */
	@Override
	public SysOrgDTO findByAreaIdAndInsId(String areaId, String insId) {
		return sysOrgService.findByAreaIdAndInsId(areaId, insId);
	}


}
