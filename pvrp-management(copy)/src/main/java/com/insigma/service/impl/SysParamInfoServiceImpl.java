package com.insigma.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insigma.mapper.SysParamInfoMapper;
import com.insigma.po.SysParamInfo;
import com.insigma.service.SysParamInfoService;

@Service
public class SysParamInfoServiceImpl implements SysParamInfoService {
	@Autowired
	private SysParamInfoMapper mapper;
	
	@Override
	public List<SysParamInfo> getSysParamInfo(Map<String, String> map) {
		return mapper.getSysParamInfo(map);
	}

}
