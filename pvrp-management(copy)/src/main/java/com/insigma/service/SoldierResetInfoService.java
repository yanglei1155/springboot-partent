package com.insigma.service;

import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SoldierResetInfo;
import java.util.List;

public interface SoldierResetInfoService {
    int deleteByPrimaryKey(Integer sriId);

    int insert(SoldierResetInfo record);

    SoldierResetInfo selectByPrimaryKey(Integer sbiId);

    List<SoldierResetInfo> selectAll();

    int updateByPrimaryKey(SoldierResetInfo record);
    
    /**
     * 批量新增士兵安置信息
     * @param list
     */
    void insertList(List<SoldierResetInfo> list);
    
    /**
     * 批量更新士兵安置信息
     * @param list
     */
    void updateList(List<SoldierResetInfo> list);
}