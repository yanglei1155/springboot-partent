package com.insigma.mapper;

import com.insigma.po.soldier.ReceivingNotice;

import java.util.List;
import java.util.Map;

public interface ReceivingNoticeMapper {
    int deleteByPrimaryKey(Integer rnId);

    int insert(ReceivingNotice record);

    ReceivingNotice selectByPrimaryKey(Integer sbiId);

    List<ReceivingNotice> selectAll(Map<String, String> map);

    int updateByPrimaryKey(ReceivingNotice record);
    
    int getMaxNum(Map<String, String> map);
    
    int insertNextval(Map<String, String> map);
}