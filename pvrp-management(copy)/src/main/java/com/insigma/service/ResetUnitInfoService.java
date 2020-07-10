package com.insigma.service;

import java.util.List;
import java.util.Map;

import com.insigma.po.soldier.ResetUnitInfo;
import com.insigma.po.soldier.SoldierBasicInfoTotal;

public interface ResetUnitInfoService {
	
	public int insertResetUnitInfo(ResetUnitInfo resetUnitInfo);
	
	public List<ResetUnitInfo> getResetUnitList(Map<String, String> map);
	
	public ResetUnitInfo selectByUnitName(String unitName);
	
	public void updateResetUnitInfo(ResetUnitInfo resetUnitInfo);
	
	public void deleteResetUnitInfo(Integer ruiId);
	
	List<SoldierBasicInfoTotal> queryResetUnitInfoTreeNodes(Map<String, String> map);
	
	public ResetUnitInfo selectByPrimaryKey(Integer ruiId);
	
	List<ResetUnitInfo> queryAllUnit(Map<String, String> map);
	
	int queryUnitNameCount(ResetUnitInfo record);
}
