package com.insigma.service.sysuser.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.insigma.dto.SysFunctionDTO;
import com.insigma.dto.SysRoleFunctionDTO;
import com.insigma.dto.SysUserDTO;

/**
 * 系统功能服务接口
 * 
 * @author Administrator
 *
 */
public interface SysFunctionFacade {
	
	/**
	 * 根据主键查询功能信息
	 * @param id
	 * @return
	 */
	public SysFunctionDTO getByPrimaryKey(Long id);

	/**
	 * 查询角用户对应色所拥有的功能
	 * 
	 * @param roleIds
	 * @return
	 */
	public List<SysFunctionDTO> queryFunctionListByRoleId(SysUserDTO sysUser);
	
	/**
	 * 根据角色类型查询可授权菜单
	 * @param roleType
	 * @return
	 */
	List<SysFunctionDTO> findTreesByRoleType(String roleType);
	
	/**
	 * 查询所有菜单
	 * @return
	 */
	List<SysFunctionDTO> queryAllFunctionList();
	
	//将pagedata转化为SysFunctionbean
	SysFunctionDTO getSysFunctionBean(Map<String,Object> map);
	
	/**
	 * 根据id和链接查询是否存在
	 * @param id
	 * @param location
	 * @return
	 */
	boolean checkLocation(Long id ,String location);
	
	/**
	 * 新增菜单
	 * @param sysFunction
	 * @return
	 * @throws BizRuleException
	 */
	public int addSysFunction(SysFunctionDTO sysFunction);
	
	/**
	 * 修改菜单
	 * @param sysFunction
	 * @return
	 * @throws BizRuleException
	 */
	public int updateSysFunction(SysFunctionDTO sysFunction);
	
	/**
	 * 菜单删除
	 * @param sysFunction
	 */
	public void deleteMenu(SysFunctionDTO sysFunction);
	
	/**
	 * 查询角色拥有的功能
	 * @param roleId
	 * @return
	 */
	List<SysRoleFunctionDTO> findByRoleId(String roleId);
	
	/**
	 * 根据菜单类型查询所有功能类型菜单
	 * @param funType
	 * @return
	 */
	List<SysFunctionDTO> queryNodeTypeListByFunType(String funType);
	
	
	/**
	 * 根据菜单类型查询菜单
	 * @param funType
	 * @return
	 */
	List<SysFunctionDTO> findByFunTypeList(String funType);
	
	/**
	 * 根据用户和菜单类型查询
	 * @param funType
	 * @param sysUser
	 * @return
	 */
	List<SysFunctionDTO> findByFunTypeAndUserList(String funType,SysUserDTO sysUser);
	
	/**
	 * 
	 * 根据id查询是否有下级菜单
	 * @param id
	 * @return
	 */
	List<SysFunctionDTO> getListByParentId(Long id);
	
	/**
	 * 根据当前排序值和父节点修改排序
	 * @param map
	 * @return
	 * @throws BizRuleException
	 */
	public int updateFunOrder(Map<String,Object> map);
	
	/**
	 * 根据菜单链接查询菜单
	 * @param location
	 * @return
	 */
	List<SysFunctionDTO> findByLocation(String location);
	/**
	 * 根据参数获取菜单列表
	 * @param searchMap
	 * @return
	 */
	List<SysFunctionDTO> getListByWhere(HashMap<String, Object> searchMap);
	
	/**
	 * 修改菜单所属节点和排序
	 * @param map
	 * @return
	 * @throws BizRuleException
	 */
	public int updateFunNode(Map<String,Object> map);
	
	/**
	 * 根据父节点查询最大排序值
	 * @param parentId
	 * @return
	 */
	public int findMaxFunOrder(Long parentId);
	
	/**
	 * 根据业务类型编号查询菜单
	 * @param number
	 * @return
	 */
	SysFunctionDTO findFunctionByBussiness(String number);

}
