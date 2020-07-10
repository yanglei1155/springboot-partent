package com.insigma.mapper;

import com.insigma.po.soldier.ResetUnitInfo;
import com.insigma.po.soldier.SoldierBasicInfoTotal;

import java.util.List;
import java.util.Map;

public interface ResetUnitInfoMapper {
    int deleteByPrimaryKey(Integer ruiId);

    int insert(ResetUnitInfo record);

    ResetUnitInfo selectByPrimaryKey(Integer ruiId);
    
    ResetUnitInfo selectByUnitName(String unitName);

    List<ResetUnitInfo> getResetUnitList(Map<String, String> map);

    int updateByPrimaryKey(ResetUnitInfo record);
    
    List<SoldierBasicInfoTotal> queryResetUnitInfoTreeNodes(Map<String, String> map);
    
    List<ResetUnitInfo> queryAllUnit(Map<String, String> map);
    
    int queryUnitNameCount(ResetUnitInfo record);
}