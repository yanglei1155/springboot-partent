package com.insigma.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insigma.mapper.ArchivesScoringFormMapper;
import com.insigma.po.soldier.ArchivesScoringForm;
import com.insigma.service.ArchivesScoringFormService;

@Service
public class ArchivesScoringFormServiceImpl implements
		ArchivesScoringFormService {

	@Autowired
	private ArchivesScoringFormMapper mapper;
	
	@Override
	public int deleteByPrimaryKey(Integer asfId) {
		return mapper.deleteByPrimaryKey(asfId);
	}

	@Override
	public int insert(ArchivesScoringForm record) {
		return mapper.insert(record);
	}

	@Override
	public ArchivesScoringForm selectByPrimaryKey(Integer asfId) {
		return mapper.selectByPrimaryKey(asfId);
	}

	@Override
	public List<ArchivesScoringForm> selectAll(ArchivesScoringForm record) {
		return mapper.selectAll(record);
	}

	@Override
	public int updateByPrimaryKey(ArchivesScoringForm record) {
		return mapper.updateByPrimaryKey(record);
	}

}
