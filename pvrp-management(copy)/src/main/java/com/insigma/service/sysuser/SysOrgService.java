package com.insigma.service.sysuser;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.insigma.dto.SysOrgDTO;
import com.insigma.mapper.SysOrgMapper;
import com.insigma.po.SysOrg;

import star.vo.BaseVo;

/**
 * 系统机构service
 * @author Administrator
 *
 */
@Service
public class SysOrgService {
	private static Logger logger = LoggerFactory.getLogger(SysOrgService.class);
	@Resource
	private SysOrgMapper sysOrgMapper;
	
	/**
	 * 新增机构po信息
	 * @param po
	 * @return
	 */
	public Long addSysOrg(SysOrgDTO dto){
		if(dto == null) {
			logger.info("sysOrgService.addSysOrg dto={}",dto);
			return 0L;
		}
		SysOrg org = dto.copyTo(SysOrg.class);
		sysOrgMapper.insertSysOrg(org);
		return org.getId();
	}

	/**
	 *  根据主键得到系统机构表记录
	 * @param id
	 * @return
	 */
	public SysOrgDTO getByPrimaryKey(Long id){
		if(id == null || id == 0) {
			logger.info("sysOrgService.getByPrimaryKey error:id={}",id);
			return null;
		}
		SysOrg po = sysOrgMapper.getByPrimaryKey(id);
		if(po==null) return null;
		return po.copyTo(SysOrgDTO.class);
	}
	
	/**
	 * 更新po信息
	 * @param po
	 * @return
	 */
	public int updatepo(SysOrgDTO dto){
		if(dto == null || dto.getId() == null || dto.getId() == 0) {
			logger.info("sysOrgService.updatepo dto={}",dto);
			return 0;
		}
		return sysOrgMapper.updateSysOrg(dto.copyTo(SysOrg.class));//object转换 如果数量不多，尽量自己采用assember方法进行转换处理。
	}
	
	
	/**
	 * 根据参数查询 获取机构列表  带分页
	 * @param searchMap
	 * @param start
	 * @param size
	 * @return
	 */
	public List<SysOrgDTO> getListByWhere(HashMap<String, Object> searchMap,int start,int size){
		if(searchMap==null || searchMap.isEmpty() || size<1) {
			logger.info("sysOrgService.getListByWhere searchMap={}，start={},size={}",searchMap,start,size);
			return Collections.emptyList();
		}
		List<SysOrg> sysOrgList = sysOrgMapper.getListByWhere(searchMap,new RowBounds(start,size));
		if(CollectionUtils.isEmpty(sysOrgList)) return Collections.emptyList();
		return BaseVo.copyListTo(sysOrgList, SysOrgDTO.class);//列表转换 如果数量不多，尽量自己采用assember方法进行转换处理。
	}

	/**
	 * 根据参数查询机构列表 
	 * @param searchMap
	 * @return
	 */
	public List<SysOrgDTO> getListByWhere(HashMap<String, Object> searchMap){
		if(searchMap==null || searchMap.isEmpty()) {
			logger.info("sysOrgService.getListByWhere searchMap={}",searchMap);
			return Collections.emptyList();
		}
		List<SysOrg> sysOrgList = sysOrgMapper.getListByWhere(searchMap);
		if(CollectionUtils.isEmpty(sysOrgList)) return Collections.emptyList();
		return BaseVo.copyListTo(sysOrgList, SysOrgDTO.class);//列表转换 如果数量不多，尽量自己采用assember方法进行转换处理。
	}
	
	
	/**
	 * 根据参数获取系统机构数量
	 * @param searchMap
	 * @return
	 */
	public int getCountByWhere(HashMap<String,Object> searchMap){
		if(searchMap==null || searchMap.isEmpty()) {
			return 0;
		}
		return sysOrgMapper.getCountByWhere(searchMap);
	}
	
	/**
	 * 根据区域ID查询机构列表
	 * @param regionCode
	 * @return
	 */
	public List<SysOrgDTO> findByRegioncode(String regionCode){
		List<SysOrg> sysOrgList = sysOrgMapper.findByRegioncode(regionCode);
		return BaseVo.copyListTo(sysOrgList, SysOrgDTO.class);
	}
	
	/**
	 * 根据id查询下级机构
	 * @param id
	 * @return
	 */
	public List<SysOrgDTO> findByParentId(Long id){
		List<SysOrg> sysOrgList = sysOrgMapper.findByParentId(id);
		return BaseVo.copyListTo(sysOrgList, SysOrgDTO.class);
	}
	
	/**
	 * 根据idpath查询下级机构
	 * @param idpath
	 * @return
	 */
	public List<SysOrgDTO> findByIdpath(String idpath){
		List<SysOrg> sysOrgList = sysOrgMapper.findByIdpath(idpath);
		return BaseVo.copyListTo(sysOrgList, SysOrgDTO.class);
	}
	
	/**
	 * 根据机构名称，机构代码查询是否存在
	 * @param orgName
	 * @param orgenterCode
	 * @return
	 */
	public List<SysOrgDTO> checkOrgNameOrgenterCode(String orgName,String orgenterCode){
		List<SysOrg> sysOrgList = sysOrgMapper.checkOrgNameOrgenterCode(orgName, orgenterCode);
		return BaseVo.copyListTo(sysOrgList, SysOrgDTO.class);
	}
	
	/**
	 * 根据机构名称查询
	 * @param orgName
	 * @return
	 */
	public SysOrgDTO findByName(String orgName){
		SysOrg po = sysOrgMapper.findByName(orgName);
		if(po==null) return null;
		return po.copyTo(SysOrgDTO.class);
	}
	
	/**
	 * 根据主键删除
	 * @param id
	 */
	public void deleteByPrimaryKey(Long id){
		sysOrgMapper.deleteByPrimaryKey(id);
	}
	
	/**
	 * 通过行政区和险种获取机构信息
	 * @param areaId
	 * @param insId
	 * @return
	 */
	public SysOrgDTO findByAreaIdAndInsId(String areaId, String insId){
		HashMap<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("areaId", areaId);
		searchMap.put("insId", insId);
		List<SysOrg> sysOrgList = sysOrgMapper.findByAreaIdAndInsId(searchMap);
		SysOrg po = null;
		if(sysOrgList.size()>0){
			po = sysOrgList.get(0);
			return po.copyTo(SysOrgDTO.class);
		}
		return null;
		
	}
}
