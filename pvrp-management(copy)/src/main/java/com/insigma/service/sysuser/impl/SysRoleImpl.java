package com.insigma.service.sysuser.impl;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insigma.dto.SysRoleDTO;
import com.insigma.dto.SysRoleFunctionDTO;
import com.insigma.service.sysuser.SysRoleFunctionService;
import com.insigma.service.sysuser.SysRoleService;
import com.insigma.service.sysuser.facade.SysRoleFacade;

/**
 * 系统角色facade服务实现类
 * 
 * @author Administrator
 *
 */
@Service
public class SysRoleImpl implements SysRoleFacade {

	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleFunctionService sysRoleFunctionService;

	@Override
	public int addSysRole(SysRoleDTO po){
		return sysRoleService.addSysRole(po);
	}

	@Override
	public SysRoleDTO getByPrimaryKey(String id) {
		return sysRoleService.getByPrimaryKey(id);
	}

	@Override
	public int updatepo(SysRoleDTO po) {
		return sysRoleService.updatepo(po);
	}

	@Override
	public List<SysRoleDTO> getListByWhere(HashMap<String, Object> searchMap, int start, int size) {
		return sysRoleService.getListByWhere(searchMap, start, size);
	}

	@Override
	public List<SysRoleDTO> getListByWhere(HashMap<String, Object> searchMap) {
		return sysRoleService.getListByWhere(searchMap);
	}

	@Override
	public int getCountByWhere(HashMap<String, Object> searchMap) {
		return sysRoleService.getCountByWhere(searchMap);
	}

	@Override
	public boolean checkRoleName(String roleName, String roleId) {
		HashMap<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("roleName", roleName);
		searchMap.put("roleId", roleId);
		SysRoleDTO sysRole = sysRoleService.getByRoleNameAndRoleId(searchMap);
		if (null == sysRole) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public void deleteByPrimaryKey(String roleId){
		sysRoleService.deleteByPrimaryKey(roleId);
		//删除角色功能关系
		sysRoleFunctionService.deleteByRoleId(roleId);
	}

	@Override
	public List<SysRoleDTO> queryRoleByUserId(String userId) {
		return sysRoleService.queryRoleByUserId(userId);
	}

	@Override
	@Transactional
	public void deleteRoleFunctionRefAndAddNewRef(String roleId, List<SysRoleFunctionDTO> list) {
		 /**
         * 先删除老的角色资源关系，后增加新的角色资源关系
         */
		try {
			//删除角色功能关系
			sysRoleFunctionService.deleteByRoleId(roleId);
			SysRoleFunctionDTO sysRoleFunction=null;
            for(int i=0;i<list.size();i++){
            	sysRoleFunction =list.get(i);
                //生成主键ID
    			String uuid = UUID.randomUUID().toString();
    			String relationId = uuid.replace("-", "");
    			sysRoleFunction.setRelationId(relationId);
                sysRoleFunction.setRoleId(roleId);
                sysRoleFunctionService.addSysRoleFunction(sysRoleFunction);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public List<SysRoleDTO> findRoleByFunType(String funType,Long orgId) {
		return sysRoleService.findRoleByFunType(funType,orgId);
	}

}
