package com.insigma.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insigma.mapper.SoldierRegisterMapper;
import com.insigma.po.soldier.FlexibleEmploymentInfo;
import com.insigma.po.soldier.PostSelectionManagement;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SoldierBasicInfoStatic;
import com.insigma.po.soldier.SoldierBasicInfoTotal;
import com.insigma.po.soldier.SoldierRegister;
import com.insigma.po.soldier.SoldierScore;
import com.insigma.po.soldier.TreeNodesParam;
import com.insigma.service.SoldierRegisterService;

/**
 * 
 * 类描述：地市报到管理   
 * 创建人：刘伟民   
 * 创建时间：2020年3月19日 下午5:06:38   
 * @version
 */
@Service
public class SoldierRegisterServiceImpl implements SoldierRegisterService {

	@Autowired
	private SoldierRegisterMapper mapper;
	
	@Override
	public List<SoldierRegister> selectAll(SoldierRegister record) {
		return mapper.selectAll(record);
	}

	@Override
	public int insert(FlexibleEmploymentInfo record) {
		// TODO Auto-generated method stub
		return mapper.insert(record);
	}

	@Override
	public FlexibleEmploymentInfo selectByPrimaryKey(Integer feiId) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(feiId);
	}

	@Override
	public int updateByPrimaryKey(FlexibleEmploymentInfo record) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(record);
	}

	@Override
	public int insertSR(SoldierRegister record) {
		// TODO Auto-generated method stub
		return mapper.insertSR(record);
	}

	@Override
	public SoldierRegister querySR(String idcard) {
		// TODO Auto-generated method stub
		return mapper.querySR(idcard);
	}

	@Override
	public int updateSR(SoldierRegister record) {
		// TODO Auto-generated method stub
		return mapper.updateSR(record);
	}

	@Override
	public List<SoldierBasicInfoTotal> querySoldierRegisterTreeNodes(TreeNodesParam po) {
		// TODO Auto-generated method stub
		return mapper.querySoldierRegisterTreeNodes(po);
	}

	@Override
	public List<SoldierBasicInfoStatic> queryRegisterData(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryRegisterData(map);
	}

	@Override
	public List<SoldierBasicInfo> querySoldierBasicInfo(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.querySoldierBasicInfo(map);
	}

	@Override
	public List<SoldierScore> queryScoreList(SoldierScore po) {
		// TODO Auto-generated method stub
		return mapper.queryScoreList(po);
	}

	@Override
	public int updateSoldierScore(SoldierScore record) {
		// TODO Auto-generated method stub
		return mapper.updateSoldierScore(record);
	}

	@Override
	public List<SoldierBasicInfoTotal> queryPSMSoldierDataTreeNodes(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryPSMSoldierDataTreeNodes(map);
	}

	@Override
	public List<PostSelectionManagement> queryPSMSoldierData(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryPSMSoldierData(map);
	}

}
