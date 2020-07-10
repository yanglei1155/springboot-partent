package com.insigma.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.insigma.po.SysOrg;
import com.insigma.po.soldier.ArchivesReport;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SoldierBasicInfoStatic;
import com.insigma.po.soldier.SoldierBasicInfoTotal;
import com.insigma.po.soldier.TreeNodesParam;

public interface ArchivesReportService {

	/**
	 * 安置类别统计表
	 * @param record
	 * @return
	 */
	List<ArchivesReport> queryReport1(Map<String, String> map);

	/**
	 * 易地安置统计表
	 * @param map
	 * @return
	 */
	List<ArchivesReport> queryReport2(Map<String, String> map);
    
	/**
	 * 档案审核问题表
	 * @param map
	 * @return
	 */
    List<ArchivesReport> queryReport3(Map<String, String> map);
    /**
	 * 档案审核问题明细表
	 * @param map
	 * @return
	 */
    List<ArchivesReport> queryReport4(Map<String, String> map);
    /**
     * 档案综合表
     * @param map
     * @return
     */
    List<SoldierBasicInfoStatic> queryReport5(Map<String, String> map);
    
    /**
     * 艰边/立功/处分统计表
     * @param map
     * @return
     */
    List<ArchivesReport> queryReport6(Map<String, String> map);
    /**
     * 成绩综合表
     * @param map
     * @return
     */
    List<ArchivesReport> queryReport7(Map<String, String> map);
    
    /**
	 * 易地安置原因统计表
	 * @param map
	 * @return
	 */
	List<ArchivesReport> queryReport8(Map<String, String> map);
	
	/**
	 * 符合条件退役人员分类表
	 * @param map
	 * @return
	 */
	List<ArchivesReport> queryReport9(Map<String, String> map);
	
	/**
	 * 退役人员数量统计表
	 * @param map
	 * @return
	 */
	List<ArchivesReport> queryReport10(Map<String, String> map);
	
	/**
	 * 档案审核通过数据基础信息导出
	 * @param map
	 * @return
	 */
	List<SoldierBasicInfo> queryReport11(Map<String, String> map);
	
	/**
	 * 根据大单位统计各地市人数
	 * @param record
	 * @return
	 */
	List<ArchivesReport> queryReport12(Map<String, String> map);
	
	/**
	 * 符合条件退役人员接收单位类别统计表
	 * @param map
	 * @return
	 */
	List<ArchivesReport> queryReport13(Map<String, String> map);

	/**个个市的退档，实际接收，未接收，计划接收统计
	 * @param  map
	 * @return
	 */
	Map<String,ArchivesReport> queryReport14( HashMap<String,String> map);

	/**
	 * 按条件统计退档的详细信息
	 * @param map
	 * @return
	 */
	List<SoldierBasicInfo>queryReport15(HashMap<String,String> map);

	/**
	 * 将所有市统计信息封装到ArchivesReportVo对象中
	 * @param map
	 * @return
	 */
	 Map<String,Object> getAllCity(Map<String,ArchivesReport> map,Map<String,Object>beans);
	/**
	 * 筛选出统计报告中所有市的退档人员的详细信息
	 * @param archivesReportList
	 * @return
	 */
	List<SoldierBasicInfo> getAllReturnPlace(List<ArchivesReport>archivesReportList);
}