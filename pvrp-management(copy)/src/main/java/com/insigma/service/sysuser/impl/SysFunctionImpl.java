package com.insigma.service.sysuser.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insigma.dto.SysFunctionDTO;
import com.insigma.dto.SysRoleFunctionDTO;
import com.insigma.dto.SysUserDTO;
import com.insigma.service.sysuser.SysFunctionService;
import com.insigma.service.sysuser.SysRoleFunctionService;
import com.insigma.service.sysuser.facade.SysFunctionFacade;

/**
 * 系统菜单facade服务实现类
 * 
 * @author Administrator
 *
 */
@Service
public class SysFunctionImpl implements SysFunctionFacade{
	
	@Autowired
	private SysFunctionService sysFunctionService;
	@Autowired
	private SysRoleFunctionService sysRoleFunctionService;

	@Override
	public List<SysFunctionDTO> queryFunctionListByRoleId(SysUserDTO sysUser) {
		return sysFunctionService.getFunctionList(sysUser);
	}

	@Override
	public SysFunctionDTO getByPrimaryKey(Long id) {
		return sysFunctionService.getByPrimaryKey(id);
	}

	@Override
	public List<SysFunctionDTO> findTreesByRoleType(String roleType) {
		return sysFunctionService.findTreesByRoleType(roleType);
	}

	@Override
	public List<SysFunctionDTO> queryAllFunctionList() {
		return sysFunctionService.queryAllFunctionList();
	}
	

	@Override
	public SysFunctionDTO getSysFunctionBean(Map<String, Object> map) {
		SysFunctionDTO function =new SysFunctionDTO();
        function.setTitle(map.get("title").toString());
        function.setLocation(map.get("location").toString());
        function.setFunType(map.get("funType").toString());
        function.setNodeType(map.get("nodeType").toString());
        function.setActive(map.get("active").toString());
        function.setDescription(map.get("description").toString());
        function.setDevEloper(map.get("devEloper").toString());
        if (!map.get("funOrder").toString().equals("")){
            function.setFunOrder(Integer.parseInt(map.get("funOrder").toString()));
        }else {
            function.setFunOrder(0);
        }
        function.setFunCode(map.get("funCode").toString());
        function.setIcon(map.get("icon").toString());
        function.setIsLog(map.get("isLog").toString());
        function.setAuFlag(map.get("auFlag").toString());
        function.setRbFlag(map.get("rbFlag").toString());
        function.setParentId((long) Integer.parseInt(map.get("parentId").toString()));
        if (!map.get("id").toString().equals("")){
            function.setId((long)Integer.parseInt(map.get("id").toString()));
        }
        return function;
	}

	@Override
	public boolean checkLocation(Long id, String location) {
		List<SysFunctionDTO> functionList  = sysFunctionService.checkLocation(id, location);
		if(functionList.size()>0){
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public int addSysFunction(SysFunctionDTO sysFunction) {
		return sysFunctionService.addSysFunction(sysFunction);
	}

	@Override
	@Transactional
	public int updateSysFunction(SysFunctionDTO sysFunction){
		return sysFunctionService.updateSysFunction(sysFunction);
	}

	@Override
	@Transactional
	public void deleteMenu(SysFunctionDTO sysFunction){
		sysFunctionService.deleteByPrimaryKey(sysFunction.getId());
		List<SysRoleFunctionDTO> rfList = sysRoleFunctionService.findByFunctionId(sysFunction.getId());
		if(null != rfList && rfList.size()>0){//有关联关系
			for(SysRoleFunctionDTO rf :rfList){
				sysRoleFunctionService.deleteByFunctionId(rf.getFunctionId());
			}
		}
	}

	@Override
	public List<SysRoleFunctionDTO> findByRoleId(String roleId) {
		return sysRoleFunctionService.findByRoleId(roleId);
	}

	@Override
	public List<SysFunctionDTO> queryNodeTypeListByFunType(String funType) {
		return sysFunctionService.queryNodeTypeListByFunType(funType);
	}

	@Override
	public List<SysFunctionDTO> findByFunTypeList(String funType) {
		return sysFunctionService.findByFunTypeList(funType);
	}

	@Override
	public List<SysFunctionDTO> getListByParentId(Long id) {
		return sysFunctionService.getListByParentId(id);
	}

	@Override
	@Transactional
	public int updateFunOrder(Map<String,Object> map){
		return sysFunctionService.updateFunOrder(map);
	}

	@Override
	public List<SysFunctionDTO> findByLocation(String location) {
		return sysFunctionService.findByLocation(location);
	}


	@Override
	public List<SysFunctionDTO> getListByWhere(HashMap<String, Object> searchMap) {
		return sysFunctionService.getListByWhere(searchMap);
	}

	@Override
	public List<SysFunctionDTO> findByFunTypeAndUserList(String funType, SysUserDTO sysUser) {
		return sysFunctionService.findByFunTypeAndUserList(funType, sysUser);
	}

	@Override
	public int updateFunNode(Map<String, Object> map) {
		return sysFunctionService.updateFunNode(map);
	}

	@Override
	public int findMaxFunOrder(Long parentId) {
		return sysFunctionService.findMaxFunOrder(parentId);
	}

	@Override
	public SysFunctionDTO findFunctionByBussiness(String number) {
		return sysFunctionService.findFunctionByBussiness(number);
	}

}
