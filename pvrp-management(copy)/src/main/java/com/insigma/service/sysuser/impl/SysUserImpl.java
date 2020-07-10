package com.insigma.service.sysuser.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insigma.dto.SysAreaDTO;
import com.insigma.dto.SysFunctionDTO;
import com.insigma.dto.SysOrgDTO;
import com.insigma.dto.SysRoleDTO;
import com.insigma.dto.SysUserAreaDTO;
import com.insigma.dto.SysUserDTO;
import com.insigma.dto.SysUserRoleDTO;
import com.insigma.service.sysuser.SysAreaService;
import com.insigma.service.sysuser.SysOrgService;
import com.insigma.service.sysuser.SysRoleService;
import com.insigma.service.sysuser.SysUserAreaService;
import com.insigma.service.sysuser.SysUserRoleService;
import com.insigma.service.sysuser.SysUserService;
import com.insigma.service.sysuser.facade.SysFunctionFacade;
import com.insigma.service.sysuser.facade.SysUserFacade;

import star.bizbase.exception.BizRuleException;
import star.util.StringUtil;

/**
 * 系统用户facade服务实现类
 * 
 * @Author:xhy
 * @since：2019年3月19日 下午1:46:43
 * @return:
 */
@Service
public class SysUserImpl implements SysUserFacade {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysFunctionFacade sysFunctionFacade;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysUserAreaService sysUserAreaService;

	@Override
	public Long addSysUser(SysUserDTO po){
		return sysUserService.addSysUser(po);
	}

	@Override
	public SysUserDTO getByPrimaryKey(Long id) {
		return sysUserService.getByPrimaryKey(id);// 实时db获取数据
	}

	@Override
	public int updatepo(SysUserDTO dto) {
		return sysUserService.updatepo(dto);
	}

	/**
	 * 分页查询
	 */
	@Override
	public List<SysUserDTO> getListByWhere(HashMap<String, Object> searchMap, int start, int size) {
		SysUserDTO currentUser = (SysUserDTO)searchMap.get("user");
//		if (!"1".equals(currentUser.getUserType())) {// 超级管理员
//			if ("2".equals(currentUser.getUserType())) {// 行政区管理员
//				searchMap.put("areaId", currentUser.getAreaId());
//				searchMap.put("notUserType", 1);
//			}
//			if ("3".equals(currentUser.getUserType())) {// 机构管理员
//				searchMap.put("orgId", currentUser.getOrgId());
//				searchMap.put("userType", 4);
////				searchMap.put("notUserType", 1);
////				searchMap.put("notUserType2", 2);
//			}
//			if ("4".equals(currentUser.getUserType())) {// 业务操作员
//				throw new RuntimeException("业务操作人员没权限查看用户列表");
//			}
//		}
		return sysUserService.getListByWhere(searchMap, start, size);
	}

	@Override
	public List<SysUserDTO> getListByWhere(HashMap<String, Object> searchMap) {
		return sysUserService.getListByWhere(searchMap);
	}

	@Override
	public int getCountByWhere(HashMap<String, Object> searchMap) {
		SysUserDTO currentUser = (SysUserDTO)searchMap.get("user");
		if (!"1".equals(currentUser.getUserType())) {// 超级管理员
			if ("2".equals(currentUser.getUserType())) {// 行政区管理员
				searchMap.put("areaId", currentUser.getAreaId());
				searchMap.put("notUserType", 1);
			}
			if ("3".equals(currentUser.getUserType())) {// 机构管理员
				searchMap.put("orgId", currentUser.getOrgId());
				searchMap.put("userType", 4);
//				searchMap.put("notUserType", 1);
//				searchMap.put("notUserType2", 2);
			}
			if ("4".equals(currentUser.getUserType())) {// 业务操作员
				throw new RuntimeException("业务操作人员没权限查看用户列表");
			}
		}
		return sysUserService.getCountByWhere(searchMap);
	}

	@Override
	public SysUserDTO getByLoginName(String loginName) {
		return sysUserService.getByLoginName(loginName);
	}

	/**
	 * 重置密码
	 */
	@Override
	@Transactional
	public int resetPassWD(Long userId){
		SysUserDTO sysUser = getByPrimaryKey(userId);
		sysUser.setPasswd(StringUtil.getMD5("000000"));
		int ret = sysUserService.updatepo(sysUser);
		return ret;
	}

	/***
	 * 注销用户
	 * 
	 * @param userId
	 */
	@Override
	@Transactional
	public void logoutUser(Long userId){
		SysUserDTO sysUser = getByPrimaryKey(userId);
		sysUser.setUserState("3");
		sysUserService.updatepo(sysUser);
	}

	/***
	 * 解锁用户
	 * 
	 * @param userId
	 */
	@Override
	@Transactional
	public void unlockUser(Long userId) {
		SysUserDTO sysUser = getByPrimaryKey(userId);
		sysUser.setUserState("1");
		sysUser.setUnlockTime(new Date());
		sysUserService.updatepo(sysUser);

	}

	/***
	 * 锁定用户
	 * 
	 * @param userId
	 */
	@Override
	@Transactional
	public void lockUser(Long userId){
		SysUserDTO sysUser = getByPrimaryKey(userId);
		sysUser.setUserState("2");
		sysUser.setLockTime(new Date());
		sysUserService.updatepo(sysUser);
	}

	/**
	 * 校验用户登录名是否重复
	 * 
	 * @param logonName
	 * @param userId
	 * @return
	 */
	@Override
	public boolean checkLogonName(String logonName, Long userId) {
		SysUserDTO sysuser;
		if (userId == null) {
			sysuser = sysUserService.getByLoginName(logonName);
		} else {
			sysuser = sysUserService.getByLogonNameAndUserIdNot(logonName, userId);
		}
		if (null == sysuser) {// 查询结果为空，则表示没有登录名称重复
			return false;
		}
		return true;
	}

	/**
	 * 校验用户是否有效
	 *
	 * @param userId
	 */
	private boolean checkUser(Long userId) {
		SysUserDTO sysUser = getByPrimaryKey(userId);
		if (sysUser == null) {
			return false;
		}
		if ("3".equals(sysUser.getUserState())) {
			return false;
		}
		return true;
	}

	/**
	 * 查询用户绑定的角色
	 */
	@Override
	public List<SysUserRoleDTO> queryUserRole(Long userId) {
		return sysUserRoleService.findByUserId(String.valueOf(userId));
	}
	
	/**
	 * 查询用户绑定的区域
	 */
	@Override
	public List<SysUserAreaDTO> queryUserArea(Long userId) throws BizRuleException {
		if (!checkUser(userId)) {
			throw new BizRuleException("该用户无效");
		}
		return sysUserAreaService.findByUserId(String.valueOf(userId));
	}
	
	/**
	 * 
	 * 根据区域ID查询机构列表
	 */
	@Override
	public List<SysOrgDTO> queryOrgNodes(String areaId,SysUserDTO sysUser) {
		List<SysOrgDTO> orgTree = new ArrayList<>();
		if ("1".equals(sysUser.getUserType())) {// 超级管理员可以根据区域选择机构
			List<SysOrgDTO> orgList = sysOrgService.findByRegioncode(areaId);
			for (SysOrgDTO sysOrg : orgList) {
				orgTree.add(sysOrg);// 添加自身节点
//				orgTree.addAll(sysOrgService.findByIdpath(sysOrg.getIdpath()+"/"));
			}
		}else{//其他类型只能选择当前用户默认机构
			List<SysOrgDTO> orgList = sysOrgService.findByRegioncode(areaId);
//			SysOrgDTO sysOrg = sysOrgService.getByPrimaryKey(sysUser.getOrgId());
//			orgTree.add(sysOrg);// 添加自身节点
			for (SysOrgDTO org : orgList) {
				orgTree.add(org);
			}
			
		}
		return orgTree;
	}

	/**
	 * 
	 * 查找区域树结构数据
	 */
	@Override
	public List<SysAreaDTO> queryAreaNodes(SysUserDTO sysUser) {
		List<SysAreaDTO> list = new ArrayList<>();
		if ("1".equals(sysUser.getUserType())) {// 超级管理员
			list = sysAreaService.findAll();
		}
		if ("2".equals(sysUser.getUserType())) {// 区域管理员
			SysAreaDTO area = sysAreaService.getByPrimaryKey(sysUser.getAreaId());
			list = sysAreaService.findByIdpath(area.getIdpath());
		}
		if ("3".equals(sysUser.getUserType())) {// 机构管理员不查询所属行政区，默认跟随自身机构
			SysAreaDTO sysArea3 = sysAreaService.getByPrimaryKey(sysUser.getAreaId());
			list.add(sysArea3);
			if(StringUtil.isNotEmpty(sysArea3.getParentId())){
				SysAreaDTO sysArea2 = sysAreaService.getByPrimaryKey(Long.parseLong(sysArea3.getParentId()));
				list.add(sysArea2);
				if(StringUtil.isNotEmpty(sysArea2.getParentId())){
					SysAreaDTO sysArea1 = sysAreaService.getByPrimaryKey(Long.parseLong(sysArea2.getParentId()));
					list.add(sysArea1);
				}
			}
		}
		return list;
	}


	/**
	 * 根据用户类型查询角色列表
	 */
	@Override
	public List<SysRoleDTO> findByRoleType(String orgId) {
		return sysRoleService.queryByOrgId(Long.valueOf(orgId));
//		if ("1".equals(userType)) {// 新增的用户类型为超级管理员
//			return sysRoleService.queryByAdmin();
//		}
//		if ("2".equals(userType)) {// 新增的用户类型为行政区管理员
//			return sysRoleService.queryRoleByArea(currentUser.getAreaId());
//		}
//		if ("3".equals(userType)) {// 新增的用户类型为机构管理员
//			return sysRoleService.queryByOrgId(Long.valueOf(orgId));
//		}
//		if ("4".equals(userType)) {// 新增的用户类型为业务操作员（普通用户）
//			if ("1".equals(currentUser.getUserType())) {// 当前登录用户为超级管理员
//				return sysRoleService.findAllByRoletype("2");
//			}
//			if ("2".equals(currentUser.getUserType())) {// 当前登录用户为行政区管理员
//				return sysRoleService.queryRoleByRoletypeAndAreaid(currentUser.getAreaId());
//			}
//			if ("3".equals(currentUser.getUserType())) {// 当前登录用户为机构管理员
//				return sysRoleService.findByRoletypeAndOrgid(Long.valueOf(orgId));
//			}
//		}
//		return sysRoleService.findAll();
	}

	/**
	 * 用户保存和修改
	 */
	@Override
	@Transactional
	public void saveUser(SysUserDTO sysUser, Map<String, List<String>> map) {
		Long userId = null;
		// 如果是更新操作，将原密码查询到新的更新对象中
		if (sysUser.getId() != null) {
			userId = sysUser.getId();
			SysUserDTO temUser = sysUserService.getByPrimaryKey(userId);
			if (!temUser.getPasswd().equals(sysUser.getPasswd())) {
				 // 密码有更新，需要重新加密
				 sysUser.setPasswd(StringUtil.getMD5(sysUser.getPasswd()));
			}
			//修改用户
			sysUserService.updatepo(sysUser);
		} else {
			sysUser.setPasswd(StringUtil.getMD5(sysUser.getPasswd()));
			//生成主键ID
			String uuid = UUID.randomUUID().toString();
			String code = uuid.replace("-", "");
			sysUser.setCode(code);
			sysUser.setUserState("1");
			//保存用户
			userId = sysUserService.addSysUser(sysUser);
		}
		
		//保存用户角色关系
		SysUserRoleDTO sysUserRoleDTO = new SysUserRoleDTO();
		sysUserRoleDTO.setUserId(userId.toString());
		sysUserRoleService.deleteByUserId(sysUserRoleDTO);
		List<SysUserRoleDTO> list = sysUser.getSysUserRoleList();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setUserId(userId.toString());
        }
        sysUserRoleService.insertByBatch(list);
        
	}

	@Override
	public List<SysUserDTO> getUserListByFunctionId(Long functionId) {
		return sysUserService.getUserListByFunctionId(functionId);
	}

	@Override
	public boolean getAuthByUrlAndUserId(String uri, Long userId) {
		if(StringUtil.isEmpty(uri) || null ==userId) return false;
		SysUserDTO sysUser = sysUserService.getByPrimaryKey(userId);
		if(null != sysUser){
			if("1".equals(sysUser.getUserType())){//超级管理员
				return true;
			}
			List<SysFunctionDTO> functionList =sysFunctionFacade.queryFunctionListByRoleId(sysUser);
			if(functionList.size()>0){
				for(SysFunctionDTO fun : functionList){
					if(uri.equals(fun.getLocation())){
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public List<SysUserRoleDTO> findUserByRoleId(String roleId){
		return sysUserRoleService.findByRoleId(roleId);
	}

	@Override
	public List<SysUserDTO> getListByIds(String ids) {
		return sysUserService.getListByIds(ids);
	}

	@Override
	public String findUserNameById(Long id) {
		SysUserDTO user = sysUserService.getByPrimaryKey(id);
		if(null != user){
			return user.getDisplayName();
		}else{
			return id.toString();
		}
	}


}
