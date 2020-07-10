package com.insigma.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insigma.mapper.ResetUnitInfoMapper;
import com.insigma.po.soldier.ResetUnitInfo;
import com.insigma.po.soldier.SoldierBasicInfoTotal;
import com.insigma.service.ResetUnitInfoService;

@Service
public class ResetUnitInfoServiceImpl implements ResetUnitInfoService {
	@Autowired
	private ResetUnitInfoMapper ruMapper;

	@Override
	public int insertResetUnitInfo(ResetUnitInfo resetUnitInfo) {
		return ruMapper.insert(resetUnitInfo);
	}

	@Override
	public List<ResetUnitInfo> getResetUnitList(Map<String, String> map) {
		return ruMapper.getResetUnitList(map);
	}

	@Override
	public void updateResetUnitInfo(ResetUnitInfo resetUnitInfo) {
		ruMapper.updateByPrimaryKey(resetUnitInfo);

	}

	@Override
	public void deleteResetUnitInfo(Integer ruiId) {
		ruMapper.deleteByPrimaryKey(ruiId);

	}

	@Override
	public ResetUnitInfo selectByUnitName(String unitName) {
		return ruMapper.selectByUnitName(unitName);
	}

	@Override
	public List<SoldierBasicInfoTotal> queryResetUnitInfoTreeNodes(Map<String, String> map) {
		// TODO Auto-generated method stub
		return ruMapper.queryResetUnitInfoTreeNodes(map);
	}

	@Override
	public ResetUnitInfo selectByPrimaryKey(Integer ruiId) {
		// TODO Auto-generated method stub
		return ruMapper.selectByPrimaryKey(ruiId);
	}

	@Override
	public List<ResetUnitInfo> queryAllUnit(Map<String, String> map) {
		// TODO Auto-generated method stub
		return ruMapper.queryAllUnit(map);
	}

	@Override
	public int queryUnitNameCount(ResetUnitInfo record) {
		// TODO Auto-generated method stub
		return ruMapper.queryUnitNameCount(record);
	}

}
