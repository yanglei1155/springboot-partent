package com.insigma.service;

import com.insigma.po.soldier.FlexibleEmploymentInfo;
import com.insigma.po.soldier.PostSelectionManagement;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SoldierBasicInfoStatic;
import com.insigma.po.soldier.SoldierBasicInfoTotal;
import com.insigma.po.soldier.SoldierRegister;
import com.insigma.po.soldier.SoldierScore;
import com.insigma.po.soldier.TreeNodesParam;

import java.util.List;
import java.util.Map;

public interface SoldierRegisterService {

    List<SoldierRegister> selectAll(SoldierRegister record);
    
    int insert(FlexibleEmploymentInfo record);

    FlexibleEmploymentInfo selectByPrimaryKey(Integer feiId);

    int updateByPrimaryKey(FlexibleEmploymentInfo record);

    int insertSR(SoldierRegister record);
    
    SoldierRegister querySR(String idcard);
    
    int updateSR(SoldierRegister record);
    
    /**
     * 获取省市区人员信息综合表数量
     * @param map
     * @return
     */
    List<SoldierBasicInfoTotal> querySoldierRegisterTreeNodes(TreeNodesParam po);
    
    List<SoldierBasicInfoStatic> queryRegisterData(Map<String, String> map);
    
    List<SoldierBasicInfo> querySoldierBasicInfo(Map<String, String> map);
    
    List<SoldierScore> queryScoreList(SoldierScore po); 
    
    int updateSoldierScore(SoldierScore record);
    
    List<SoldierBasicInfoTotal> queryPSMSoldierDataTreeNodes(Map<String, String> map);
    
    List<PostSelectionManagement> queryPSMSoldierData(Map<String, String> map); 
}