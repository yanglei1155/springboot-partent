package com.insigma.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insigma.po.soldier.AreaCascadeData;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SoldierBasicInfoStatic;
import com.insigma.po.soldier.SoldierBasicInfoTotal;
public interface SoldierBasicInfoMapper {
	/**
	 * 删除数据
	 * @param vo
	 */
	int deleteByPrimaryKey(Integer sbiId);
	
	/**
	 * 保存数据
	 * @param vo
	 */
    int insert(SoldierBasicInfo record);

    /**
     * 根据ID查询数据
     * @param sbiId
     * @return
     */
    SoldierBasicInfo selectByPrimaryKey(Integer sbiId);
    
//    /**
//	 * 查询数据列表
//	 * @param vo
//	 * @return
//	 */
//    List<SoldierBasicInfo> selectAll(SoldierBasicInfo record);
    
    /**
	 * 修改数据
	 * @param vo
	 */
    int updateByPrimaryKey(SoldierBasicInfo record);
    
//    /**
//	 * 查询部队名称
//	 * @param vo
//	 * @return
//	 */
//    List<SoldierBasicInfo> selectTroopsName(@Param("troopsName")String troopsName);
    
//    /**
//     * 查询可开具接收安置通知书的数据
//     * @param record
//     * @return
//     */
//    List<SoldierBasicInfo> selectExpInfo(SoldierBasicInfo record);
    
    /**
	 * 集中审核查询数据列表
	 * @param vo
	 * @return
	 */
//    List<SoldierBasicInfo> queryAuditData(SoldierBasicInfo record);
    
    /**
	 * 非集中审核查询数据列表
	 * @param vo
	 * @return
	 */
//    List<SoldierBasicInfo> querySporadicAuditData(SoldierBasicInfo record);
    
    /**
	 * 查询档案库数据列表
	 * @param vo
	 * @return
	 */
//    List<SoldierBasicInfo> queryArchivesDetailed(SoldierBasicInfo record);
    
//    /**
//     * 根据ID查询数据
//     * @param sbiId
//     * @return
//     */
//    List<SoldierBasicInfo> querySoldierBasicInfo(SoldierBasicInfo record);
    
    /**
     * 根据行政区划查询级联数据
     * @param map
     * @return
     */
//    List<SoldierBasicInfoTotal> querySoldierBasicInfoTreeNodes(Map<String, String> map);
    
    /**
     * 报表查询及导出
     * @param record
     * @return
     */
    List<SoldierBasicInfoStatic> queryArchivesReport(SoldierBasicInfoStatic record);
    
    /**
     * 批量新增士兵信息
     * @param list
     */
    void insertList(List<SoldierBasicInfo> list);
    
//    /**
//     * 批量更新士兵信息
//     * @param list
//     */
//    void updateList(List<SoldierBasicInfo> list);
    
    /**
     * 查询可开具接收安置通知书人员
     * @param record
     * @return
     */
    List<SoldierBasicInfo> queryPrintReceivingNotice(SoldierBasicInfo record);
    
    /**
	 * 根据批次ID、大单位编码查询数据，用于校验导入数据数量是否完整
	 * @param vo
	 * @return
	 */
    List<SoldierBasicInfo> queryCheckData(SoldierBasicInfo record);
    
    /**
  	 * 保存审档数据
  	 * @param vo
  	 */
    int updateArchivesAudit(SoldierBasicInfo record);
    
    /**
     * 批量更新士兵档案信息
     * @param list
     */
    void updateArchivesList(List<SoldierBasicInfo> list);
    
    
    /**
     * 查询士兵基础信息列表
     * @param po
     * @return
     */
    List<SoldierBasicInfo> querySoldierList(SoldierBasicInfo po);
    
    /**
     * 导入数据校验是否存在
     * @param idcard
     * @return
     */
    SoldierBasicInfo checkSoldierBasicInfo(String idcard);
    
    /**
     * 数据管理-查询地区级联数据
     * @param map
     * @return
     */
    List<AreaCascadeData> queryAreaCascadeData(Map<String, String> map);
    
    void updateSql(String sql);
    
    void updateListSql(List<String> list);

	/**
	 * 根据具体条件获取具体的Soldier基本信息
	 * @param map
	 * @return
	 */
	List<SoldierBasicInfo>getSoldierListByWhere(Map<String,String>map);

	/**
	 * 根据档案状态查询各区归档，未接收，已接收数量统计
	 * @param map
	 * @return
	 */
	SoldierBasicInfo getCountByWhere(Map<String,String>map);
}
