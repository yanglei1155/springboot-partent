package com.insigma.service;

import com.insigma.po.SysOrg;
import com.insigma.po.soldier.SysRegional;
import java.util.List;

public interface SysRegionalService {
    int deleteByPrimaryKey(Integer srId);

    int insert(SysRegional record);

    SysRegional selectByPrimaryKey(Integer srId);

    List<SysRegional> selectAll(SysRegional record);

    int updateByPrimaryKey(SysRegional record);
    
    List<SysRegional> selectAllName(SysRegional record);
    
    List<SysRegional> queryRegionalTotal(SysRegional record);
    /**
     * 根据行政区划编码查询父级信息
     * @param regionalCode
     * @return
     */
    SysRegional queryParentData(String regionalCode);
    /**
     * 获取所有地区全名 如 上城区全名为：浙江省杭州市上城区
     * @return
     */
    List<SysRegional>getFullNameOfAllArea();

}