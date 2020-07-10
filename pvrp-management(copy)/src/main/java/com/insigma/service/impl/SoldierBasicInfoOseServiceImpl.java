package com.insigma.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insigma.mapper.SoldierBasicInfoOseMapper;
import com.insigma.po.soldier.AreaCascadeData;
import com.insigma.po.soldier.SoldierBasicInfoOse;
import com.insigma.po.soldier.SoldierRegister;
import com.insigma.service.SoldierBasicInfoOseService;

@Service
public class SoldierBasicInfoOseServiceImpl implements SoldierBasicInfoOseService {
	
	@Autowired
	private SoldierBasicInfoOseMapper mapper;
	
	@Override
	public int deleteByPrimaryKey(Integer sbiId) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(sbiId);
	}

	@Override
	public int insert(SoldierBasicInfoOse record) {
		// TODO Auto-generated method stub
		return mapper.insert(record);
	}

	@Override
	public SoldierBasicInfoOse selectByPrimaryKey(Integer sbiId) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(sbiId);
	}

	@Override
	public List<SoldierBasicInfoOse> selectAll(SoldierBasicInfoOse record) {
		// TODO Auto-generated method stub
		return mapper.selectAll(record);
	}

	@Override
	public int updateByPrimaryKey(SoldierBasicInfoOse record) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(record);
	}

	@Override
	public List<AreaCascadeData> queryAreaCascadeData(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryAreaCascadeData(map);
	}

	@Override
	public List<SoldierRegister> selectAllOseRegister(SoldierRegister record) {
		// TODO Auto-generated method stub
		return mapper.selectAllOseRegister(record);
	}

	@Override
	public void updateRegisterStatus(String idcard) {
		// TODO Auto-generated method stub
		mapper.updateRegisterStatus(idcard);
	}

}
