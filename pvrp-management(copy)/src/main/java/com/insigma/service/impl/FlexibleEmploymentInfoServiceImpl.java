package com.insigma.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.insigma.mapper.FlexibleEmploymentInfoMapper;
import com.insigma.po.soldier.FlexibleEmploymentInfo;
import com.insigma.service.FlexibleEmploymentInfoService;
@Service
public class FlexibleEmploymentInfoServiceImpl implements FlexibleEmploymentInfoService {
	@Autowired
	private FlexibleEmploymentInfoMapper mapper;
	@Override
	public int deleteByPrimaryKey(Integer feiId) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(feiId);
	}

	@Override
	public int insert(FlexibleEmploymentInfo record) {
		int flag=0;
		try {
			flag=mapper.insert(record);
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new RuntimeException();
		}
		return flag;
	}

	@Override
	public FlexibleEmploymentInfo selectByPrimaryKey(Integer sbiId) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(sbiId);
	}

	@Override
	public List<FlexibleEmploymentInfo> selectAll() {
		// TODO Auto-generated method stub
		return mapper.selectAll();
	}

	@Override
	public int updateByPrimaryKey(FlexibleEmploymentInfo record) {
		int flag=0;
		try {
			flag=mapper.updateByPrimaryKey(record);
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new RuntimeException();
		}
		return flag;
	}

}
