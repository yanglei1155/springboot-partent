package com.insigma.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.insigma.po.SysUser;


/**
 * 
 * 对系统用户表操作
 * @author haoxz11MyBatis 
 * @created Thu Mar 21 14:10:27 CST 2019
 * @version $Id: DefaultCommentGenerator.java,v 1.1 2013/10/28 07:59:58 haoxz11 Exp $
 * @haoxz11MyBatis
 */
public interface SysUserMapper {
	/**
	 * 插入系统用户表记录
	 *
	 * @haoxz11MyBatis
	 */
	int insertSysUser(SysUser record);

	/**
	 * 根据主键得到系统用户表表记录
	 *
	 * @haoxz11MyBatis
	 */
	SysUser getByPrimaryKey(Long id);

	/**
	 * 更新系统用户表记录
	 *
	 * @haoxz11MyBatis
	 */
	int updateSysUser(SysUser record);

	/**
	 * 搜索系统用户表列表，带分页
	 *
	 * @haoxz11MyBatis
	 */
	List<SysUser> getListByWhere(HashMap<String, Object> searchMap, RowBounds rowBounds);

	/**
	 * 搜索系统用户表列表
	 *
	 * @haoxz11MyBatis
	 */
	List<SysUser> getListByWhere(HashMap<String, Object> searchMap);

	/**
	 * 得到搜索系统用户表的记录数量
	 *
	 * @haoxz11MyBatis
	 */
	int getCountByWhere(HashMap<String, Object> searchMap);
	
	/**
	 * 查找用户对象
	 * @param loginName
	 * @return
	 */
	SysUser getByLoginName(@Param("loginName")String loginName);
	
	SysUser getByLogonNameAndUserIdNot(@Param("loginName")String loginName,@Param("userId")Long userId);
	
	List<SysUser> queryUserListByRoleIds(@Param("roleIds")String roleIds);
	
	List<SysUser> getListByIds(@Param("ids")String ids);
	/**
	 * @param ythUserId
	 * @return
	 */
	SysUser getCacheSysUserByYthUserId(@Param("ythUserId")String ythUserId);
}