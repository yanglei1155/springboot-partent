package com.insigma.mapper;

import com.insigma.po.soldier.AreaCascadeData;
import com.insigma.po.soldier.SoldierBasicInfoOse;
import com.insigma.po.soldier.SoldierRegister;

import java.util.List;
import java.util.Map;

public interface SoldierBasicInfoOseMapper {
    int deleteByPrimaryKey(Integer sbiId);

    int insert(SoldierBasicInfoOse record);

    SoldierBasicInfoOse selectByPrimaryKey(Integer sbiId);

    List<SoldierBasicInfoOse> selectAll(SoldierBasicInfoOse record);

    int updateByPrimaryKey(SoldierBasicInfoOse record);
    
    /**
     * 数据管理-查询地区级联数据
     * @param map
     * @return
     */
    List<AreaCascadeData> queryAreaCascadeData(Map<String, String> map);
    
    /**
     * 查询自主就业士兵报到数据
     * @param record
     * @return
     */
    List<SoldierRegister> selectAllOseRegister(SoldierRegister record);
    
    /**
     * 根据身份证号更新报到状态
     * @param idcard
     */
    void updateRegisterStatus(String idcard);
    
}