package com.insigma.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insigma.mapper.RenounceResettlementNoticeMapper;
import com.insigma.po.soldier.RenounceResettlementNotice;
import com.insigma.service.RenounceResettlementNoticeService;

/**
 * 
 * 类描述： 开具放弃安置待遇告知书 
 * 创建人：刘伟民   
 * 创建时间：2020年3月24日 上午11:48:25   
 * @version
 */
@Service
public class RenounceResettlementNoticeServiceImpl implements RenounceResettlementNoticeService {
	@Autowired
	private RenounceResettlementNoticeMapper mapper;
	
	@Override
	public int deleteByPrimaryKey(Integer rrnId) {
		return mapper.deleteByPrimaryKey(rrnId);
	}

	@Override
	public int insert(RenounceResettlementNotice record) {
		return mapper.insert(record);
	}

	@Override
	public RenounceResettlementNotice selectByPrimaryKey(String idcard) {
		return mapper.selectByPrimaryKey(idcard);
	}

	@Override
	public List<RenounceResettlementNotice> selectAll() {
		return mapper.selectAll();
	}

	@Override
	public int updateByPrimaryKey(RenounceResettlementNotice record) {
		return mapper.updateByPrimaryKey(record);
	}

}
