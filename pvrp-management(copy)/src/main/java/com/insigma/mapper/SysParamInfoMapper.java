package com.insigma.mapper;

import java.util.List;
import java.util.Map;

import com.insigma.po.SysParamInfo;

public interface SysParamInfoMapper {
	
	/**
	 * 查询系统参数
	 * @param map
	 * @return
	 */
	List<SysParamInfo> getSysParamInfo(Map<String, String> map);
}
