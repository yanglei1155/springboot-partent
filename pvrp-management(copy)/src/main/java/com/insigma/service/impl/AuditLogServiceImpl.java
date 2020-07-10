package com.insigma.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.insigma.mapper.AuditLogMapper;
import com.insigma.po.soldier.AuditLog;
import com.insigma.service.AuditLogService;

/**
 *       
 * 类描述：审核档案操作日志  
 * 创建人：liuwm   
 * 创建时间：2020年3月4日 下午5:26:24   
 * @version
 */
@Service
public class AuditLogServiceImpl implements AuditLogService {

	@Autowired
	private AuditLogMapper mapper;
	
	@Override
	public int deleteByPrimaryKey(Integer alId) {
		return mapper.deleteByPrimaryKey(alId);
	}

	@Override
	@Transactional
	public int insert(AuditLog record) {
		int ret=0;
		try {
			ret=mapper.insert(record);
		} catch (Exception e) {
			e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return ret;
	}

	@Override
	public AuditLog selectByPrimaryKey(Integer alId) {
		return mapper.selectByPrimaryKey(alId);
	}

	@Override
	public List<AuditLog> selectAll(AuditLog record) {
		return mapper.selectAll(record);
	}

	@Override
	public int updateByPrimaryKey(AuditLog record) {
		int ret=0;
		try {
			ret=mapper.updateByPrimaryKey(record);
		} catch (Exception e) {
			e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return ret;
	}

	@Override
	public void insertList(List<AuditLog> list) {
		try {
			mapper.insertList(list);
		} catch (Exception e) {
			e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
	}

}
