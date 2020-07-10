package com.insigma.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insigma.mapper.SoldierResetInfoMapper;
import com.insigma.po.soldier.SoldierResetInfo;
import com.insigma.service.SoldierResetInfoService;

/**
 * 类描述：  退役士兵安置信息 
 * 创建人：刘伟民   
 * 创建时间：2020年4月30日 下午2:53:11   
 * @version
 */
@Service
public class SoldierResetInfoServiceImpl implements SoldierResetInfoService {

	@Autowired
	private SoldierResetInfoMapper mapper;
	
	@Override
	public int deleteByPrimaryKey(Integer sriId) {
		return mapper.deleteByPrimaryKey(sriId);
	}

	@Override
	public int insert(SoldierResetInfo record) {
		// TODO Auto-generated method stub
		return mapper.insert(record);
	}

	@Override
	public SoldierResetInfo selectByPrimaryKey(Integer sbiId) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(sbiId);
	}

	@Override
	public List<SoldierResetInfo> selectAll() {
		// TODO Auto-generated method stub
		return mapper.selectAll();
	}

	@Override
	public int updateByPrimaryKey(SoldierResetInfo record) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(record);
	}

	@Override
	public void insertList(List<SoldierResetInfo> list) {
		mapper.insertList(list);
	}

	@Override
	public void updateList(List<SoldierResetInfo> list) {
		mapper.updateList(list);
	}

}
