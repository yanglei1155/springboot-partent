package com.insigma.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insigma.mapper.PersonnelArchivesLogMapper;
import com.insigma.po.soldier.PersonnelArchivesLog;
import com.insigma.service.PersonnelArchivesLogService;

/**
 * 
 * 类描述：  士兵信息日志
 * 创建人：刘伟民   
 * 创建时间：2020年4月14日 下午3:09:52   
 * @version
 */
@Service
public class PersonnelArchivesLogServiceImpl implements PersonnelArchivesLogService {

	@Autowired
	private PersonnelArchivesLogMapper mapper;
	
	@Override
	public int deleteByPrimaryKey(Integer palId) {
		return mapper.deleteByPrimaryKey(palId);
	}

	@Override
	public int insert(PersonnelArchivesLog record) {
		return mapper.insert(record);
	}

	@Override
	public PersonnelArchivesLog selectByPrimaryKey(Integer palId) {
		return mapper.selectByPrimaryKey(palId);
	}

	@Override
	public List<PersonnelArchivesLog> selectAll(PersonnelArchivesLog record) {
		return mapper.selectAll(record);
	}

	@Override
	public int updateByPrimaryKey(PersonnelArchivesLog record) {
		return mapper.updateByPrimaryKey(record);
	}

	@Override
	public void insertList(List<PersonnelArchivesLog> list) {
		mapper.insertList(list);
	}

}
