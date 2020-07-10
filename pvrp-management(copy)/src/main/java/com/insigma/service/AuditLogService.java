package com.insigma.service;

import com.insigma.po.soldier.AuditLog;
import java.util.List;

public interface AuditLogService {
    int deleteByPrimaryKey(Integer alId);

    int insert(AuditLog record);

    AuditLog selectByPrimaryKey(Integer alId);

    List<AuditLog> selectAll(AuditLog record);

    int updateByPrimaryKey(AuditLog record);
    
    void insertList(List<AuditLog> list);
}