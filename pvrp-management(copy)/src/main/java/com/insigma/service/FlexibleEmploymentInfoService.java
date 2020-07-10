package com.insigma.service;

import com.insigma.po.soldier.FlexibleEmploymentInfo;
import java.util.List;

public interface FlexibleEmploymentInfoService {
    int deleteByPrimaryKey(Integer feiId);

    int insert(FlexibleEmploymentInfo record);

    FlexibleEmploymentInfo selectByPrimaryKey(Integer sbiId);

    List<FlexibleEmploymentInfo> selectAll();

    int updateByPrimaryKey(FlexibleEmploymentInfo record);
}