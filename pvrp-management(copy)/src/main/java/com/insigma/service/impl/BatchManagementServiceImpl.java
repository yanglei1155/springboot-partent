package com.insigma.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insigma.mapper.BatchManagementMapper;
import com.insigma.po.soldier.BatchManagement;
import com.insigma.service.BatchManagementService;

@Service
public class BatchManagementServiceImpl implements BatchManagementService {
	
	@Autowired
	private BatchManagementMapper mapper;
	
	@Override
	public int deleteByPrimaryKey(Integer ybId) {
		return mapper.deleteByPrimaryKey(ybId);
	}

	@Override
	public int insert(BatchManagement record) {
		return mapper.insert(record);
	}

	@Override
	public BatchManagement selectByPrimaryKey(Integer ybId) {
		return mapper.selectByPrimaryKey(ybId);
	}

	@Override
	public List<BatchManagement> selectAll(BatchManagement record) {
		return mapper.selectAll(record);
	}

	@Override
	public int updateByPrimaryKey(BatchManagement record) {
		return mapper.updateByPrimaryKey(record);
	}

}
