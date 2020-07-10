package com.insigma.service.sysuser.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.insigma.dto.SysAreaDTO;
import com.insigma.dto.SysOrgDTO;
import com.insigma.dto.SysRoleDTO;
import com.insigma.dto.SysUserAreaDTO;
import com.insigma.dto.SysUserDTO;
import com.insigma.dto.SysUserRoleDTO;

import star.bizbase.exception.BizRuleException;

/**
 * 系统用户服务接口
 * 
 * @Author:xhy
 * @since：2019年3月19日 下午1:41:37
 * @return:
 */
public interface SysUserFacade {
	/**
	 * 新增用户po信息
	 * 
	 * @param po
	 * @return
	 * @throws BizRuleException
	 */
	public Long addSysUser(SysUserDTO po);


	/**
	 * 
	 * @author xhy
	 * @since:2019年3月22日下午4:16:45
	 * @param id
	 * @return
	 */
	public SysUserDTO getByPrimaryKey(Long id);

	/**
	 * 更新po信息
	 * 
	 * @param po
	 * @return
	 */
	public int updatepo(SysUserDTO po);

	/**
	 * 根据参数查询 获取用户列表 带分页
	 * 
	 * @author xhy
	 * @since:2019年3月21日下午2:23:30
	 * @param searchMap
	 * @param start
	 * @param size
	 * @return
	 */
	public List<SysUserDTO> getListByWhere(HashMap<String, Object> searchMap, int start, int size);

	/**
	 * 根据参数查询用户列表
	 * 
	 * @author xhy
	 * @since:2019年3月21日下午2:23:52
	 * @param searchMap
	 * @return
	 */
	public List<SysUserDTO> getListByWhere(HashMap<String, Object> searchMap);

	/**
	 * 根据参数 获取买家的系统用户列表 以userId为主
	 * 
	 * @param searchMap
	 * @return
	 */
	public int getCountByWhere(HashMap<String, Object> searchMap);

	/**
	 * 根据登录名查找用户
	 * 
	 * @param loginName
	 * @return
	 */
	public SysUserDTO getByLoginName(String loginName);

	/**
	 * 重置用户密码，重置密码为000000
	 * 
	 * @param userId
	 * @throws AppException
	 */
	int resetPassWD(Long userId);

	/**
	 * 注销用户
	 * 
	 * @param userId
	 * @throws AppException
	 */
	void logoutUser(Long userId);

	/**
	 * 解锁用户
	 * 
	 * @param userId
	 * @throws AppException
	 */
	void unlockUser(Long userId);

	/**
	 * 锁定用户
	 * 
	 * @param userId
	 * @throws AppException
	 */
	void lockUser(Long userId);

	/**
	 * 校验用户登录名是否重复
	 * 
	 * @param logonName
	 * @param userId
	 * @return
	 */
	boolean checkLogonName(String logonName, Long userId);

	/**
	 * 查询用户绑定的角色
	 * 
	 * @param userId
	 * @return
	 */
	List<SysUserRoleDTO> queryUserRole(Long userId);
	
	/**
	 * 查询用户绑定的区域
	 * 
	 * @param userId
	 * @return
	 */
	List<SysUserAreaDTO> queryUserArea(Long userId) throws BizRuleException;
	
	/**
     * 根据区域查询区域下的机构
     * @param areaId
     * @return
     */
    List<SysOrgDTO> queryOrgNodes(String areaId,SysUserDTO sysUser);
    
    /**
     * 获取全部区域
     * @return
     */
    List<SysAreaDTO> queryAreaNodes(SysUserDTO sysUser);
	
	
    /**
     * 根据机构ID获取角色
     * @param roleType
     * @return
     */
    List<SysRoleDTO> findByRoleType(String orgId);
    
    /**
     * 保存和修改用户
     * @param sysUser
     * @param map
     * @throws BizRuleException
     */
    void saveUser(SysUserDTO sysUser,Map<String,List<String>> map);
    
    /**
     * 根据菜单ID查询用户
     * @param functionId
     * @return
     */
    public List<SysUserDTO> getUserListByFunctionId(Long functionId);
    
    /**
     * 判断用户请求路径权限
     * @param url
     * @param userId
     * @return
     */
    public boolean getAuthByUrlAndUserId(String url,Long userId);
    
    /**
     * 根据角色查询关联用户
     * @param roleId
     * @return
     * @throws BizRuleException
     */
    List<SysUserRoleDTO> findUserByRoleId(String roleId);
    
    /**
     * 根据id集合查询用户list
     * @param ids
     * @return
     */
    public List<SysUserDTO> getListByIds(String ids);
    
    /**
     * 根据用户ID获取操作员姓名，为空则返回id
     * @param id
     * @return
     */
    public String findUserNameById(Long id);
    
}
