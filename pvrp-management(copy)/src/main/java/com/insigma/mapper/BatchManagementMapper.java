package com.insigma.mapper;

import com.insigma.po.soldier.BatchManagement;
import java.util.List;

public interface BatchManagementMapper {
    int deleteByPrimaryKey(Integer ybId);

    int insert(BatchManagement record);

    BatchManagement selectByPrimaryKey(Integer ybId);

    List<BatchManagement> selectAll(BatchManagement record);

    int updateByPrimaryKey(BatchManagement record);
}