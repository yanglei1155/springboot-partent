package com.insigma.mapper;

import java.util.List;

import com.insigma.po.soldier.AuditLog;

public interface AuditLogMapper {
    int deleteByPrimaryKey(Integer alId);

    int insert(AuditLog record);

    AuditLog selectByPrimaryKey(Integer alId);

    List<AuditLog> selectAll(AuditLog record);

    int updateByPrimaryKey(AuditLog record);
    
    void insertList(List<AuditLog> list);
}