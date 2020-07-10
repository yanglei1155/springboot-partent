package com.insigma.mapper;

import java.util.List;
import java.util.Map;

import com.insigma.po.soldier.ArchivesReport;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SoldierBasicInfoStatic;

public interface ArchivesReportMapper {

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

}