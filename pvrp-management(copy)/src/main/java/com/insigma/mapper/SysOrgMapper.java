package com.insigma.mapper;

import java.util.HashMap;
import java.util.List;

import com.insigma.po.soldier.SysRegional;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.insigma.po.SysOrg;

/**
 * 
 * 对系统机构表操作
 * @author haoxz11MyBatis 
 * @created Mon Mar 25 14:59:55 CST 2019
 * @version $Id: DefaultCommentGenerator.java,v 1.1 2013/10/28 07:59:58 haoxz11 Exp $
 * @haoxz11MyBatis
 */
public interface SysOrgMapper {
	/**
	 * 插入系统机构表记录
	 *
	 * @haoxz11MyBatis
	 */
	Long insertSysOrg(SysOrg record);

	/**
	 * 根据主键得到系统机构表表记录
	 *
	 * @haoxz11MyBatis
	 */
	SysOrg getByPrimaryKey(Long id);

	/**
	 * 更新系统机构表记录
	 *
	 * @haoxz11MyBatis
	 */
	int updateSysOrg(SysOrg record);

	/**
	 * 搜索系统机构表列表，带分页
	 *
	 * @haoxz11MyBatis
	 */
	List<SysOrg> getListByWhere(HashMap<String, Object> searchMap, RowBounds rowBounds);

	/**
	 * 搜索系统机构表列表
	 *
	 * @haoxz11MyBatis
	 */
	List<SysOrg> getListByWhere(HashMap<String, Object> searchMap);

	/**
	 * 得到搜索系统机构表的记录数量
	 *
	 * @haoxz11MyBatis
	 */
	int getCountByWhere(HashMap<String, Object> searchMap);
	
	/**
	 * 根据区域ID查询机构列表
	 * @param regionCode
	 * @return
	 */
	List<SysOrg> findByRegioncode(String regionCode);
	
	/**
	 * 根据id查询下级机构
	 * @param id
	 * @return
	 */
	List<SysOrg> findByParentId(Long id);
	
	/**
	 * 根据idpath查询下级机构
	 * @param idpath
	 * @return
	 */
	List<SysOrg> findByIdpath(String idpath);
	
	/**
	 * 根据机构名称，机构代码查询是否存在
	 * @param orgName
	 * @param orgenterCode
	 * @return
	 */
	List<SysOrg> checkOrgNameOrgenterCode(@Param("orgName")String orgName,@Param("orgenterCode")String orgenterCode);
	
	/**
	 * 根据机构名称查询
	 * @param orgName
	 * @return
	 */
	SysOrg findByName(String orgName);
	
	/**
	 * 根据主键ID删除
	 * @param id
	 */
	void deleteByPrimaryKey(Long id);
	
	/**
	 * 通过行政区和险种获取机构信息
	 * @param searchMap
	 * @return
	 */
	public List<SysOrg> findByAreaIdAndInsId(HashMap<String, Object> searchMap);

}