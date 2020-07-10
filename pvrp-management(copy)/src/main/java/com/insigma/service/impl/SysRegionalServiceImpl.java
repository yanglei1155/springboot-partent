package com.insigma.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insigma.mapper.SysRegionalMapper;
import com.insigma.po.soldier.SysRegional;
import com.insigma.service.SysRegionalService;

/**
 *       
 * 类描述：行政区划操作类   
 * 创建人：liuwm   
 * 创建时间：2020年3月11日 下午3:25:10   
 * @version
 */
@Service
public class SysRegionalServiceImpl implements SysRegionalService {
	
	@Autowired
	private SysRegionalMapper mapper;
	
	@Override
	public int deleteByPrimaryKey(Integer srId) {
		return mapper.deleteByPrimaryKey(srId);
	}

	@Override
	public int insert(SysRegional record) {
		return mapper.insert(record);
	}

	@Override
	public SysRegional selectByPrimaryKey(Integer srId) {
		return mapper.selectByPrimaryKey(srId);
	}

	@Override
	public List<SysRegional> selectAll(SysRegional record) {
		return mapper.selectAll(record);
	}

	@Override
	public int updateByPrimaryKey(SysRegional record) {
		return mapper.updateByPrimaryKey(record);
	}

	@Override
	public List<SysRegional> selectAllName(SysRegional record) {
		return mapper.selectAllName(record);
	}

	@Override
	public List<SysRegional> queryRegionalTotal(SysRegional record) {
		return mapper.queryRegionalTotal(record);
	}

	@Override
	public SysRegional queryParentData(String regionalCode) {
		// TODO Auto-generated method stub
		return mapper.queryParentData(regionalCode);
	}

	@Override
	public List<SysRegional> getFullNameOfAllArea() {
		return mapper.getFullNameOfAllArea();
	}


}
