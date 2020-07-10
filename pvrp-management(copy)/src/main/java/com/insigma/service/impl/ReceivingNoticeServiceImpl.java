package com.insigma.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insigma.mapper.ReceivingNoticeMapper;
import com.insigma.po.soldier.ReceivingNotice;
import com.insigma.service.ReceivingNoticeService;

/**
 *       
 * 类描述：开具接收安置通知书    
 * 创建人：liuwm   
 * 创建时间：2020年3月7日 下午3:19:17   
 * @version
 */
@Service
public class ReceivingNoticeServiceImpl implements ReceivingNoticeService {

	@Autowired
	private ReceivingNoticeMapper mapper;
	
	@Override
	public int deleteByPrimaryKey(Integer rnId) {
		return mapper.deleteByPrimaryKey(rnId);
	}

	@Override
	public int insert(ReceivingNotice record) {
		return mapper.insert(record);
	}

	@Override
	public ReceivingNotice selectByPrimaryKey(Integer sbiId) {
		return mapper.selectByPrimaryKey(sbiId);
	}

	@Override
	public List<ReceivingNotice> selectAll(Map<String, String> map) {
		return mapper.selectAll(map);
	}

	@Override
	public int updateByPrimaryKey(ReceivingNotice record) {
		return mapper.updateByPrimaryKey(record);
	}

	@Override
	public int getMaxNum(Map<String, String> map) {
		return mapper.getMaxNum(map);
	}

	@Override
	public int insertNextval(Map<String, String> map) {
		return mapper.insertNextval(map);
	}

}
