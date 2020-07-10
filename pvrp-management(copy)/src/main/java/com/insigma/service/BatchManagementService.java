package com.insigma.service;

import com.insigma.po.soldier.BatchManagement;
import java.util.List;

public interface BatchManagementService {
    int deleteByPrimaryKey(Integer ybId);

    int insert(BatchManagement record);

    BatchManagement selectByPrimaryKey(Integer ybId);

    List<BatchManagement> selectAll(BatchManagement record);

    int updateByPrimaryKey(BatchManagement record);
}