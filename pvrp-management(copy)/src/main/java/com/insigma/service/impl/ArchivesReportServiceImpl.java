package com.insigma.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.ObjectUtil;
import com.insigma.dto.ArchivesReportVo;
import com.insigma.po.SysOrg;
import com.insigma.po.soldier.*;
import com.insigma.report.SysOrgOfReportVo;
import com.insigma.service.SoldierBasicInfoService;
import com.insigma.service.SysRegionalService;
import com.insigma.service.sysuser.facade.SysOrgFacade;
import com.insigma.util.ParamUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insigma.mapper.ArchivesReportMapper;
import com.insigma.service.ArchivesReportService;
import org.thymeleaf.util.ObjectUtils;

/**
 * 
 * 类描述： 报表统计查询  
 * 创建人：刘伟民   
 * 创建时间：2020年4月8日 上午9:07:31   
 * @version
 */
@Service
public class ArchivesReportServiceImpl implements ArchivesReportService {
	
	@Autowired
	private ArchivesReportMapper mapper;

	@Autowired
	private SoldierBasicInfoService soldierMapper;

	@Autowired
	private SysOrgFacade orgMapper;

	@Autowired
	private SysRegionalService regionalMapper;
	@Override
	public List<ArchivesReport> queryReport1(Map<String, String> map) {
		return mapper.queryReport1(map);
	}

	@Override
	public List<ArchivesReport> queryReport2(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryReport2(map);
	}

	@Override
	public List<ArchivesReport> queryReport3(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryReport3(map);
	}

	@Override
	public List<ArchivesReport> queryReport4(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryReport4(map);
	}

	@Override
	public List<SoldierBasicInfoStatic> queryReport5(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryReport5(map);
	}

	@Override
	public List<ArchivesReport> queryReport6(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryReport6(map);
	}

	@Override
	public List<ArchivesReport> queryReport7(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryReport7(map);
	}

	@Override
	public List<ArchivesReport> queryReport8(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryReport8(map);
	}

	@Override
	public List<ArchivesReport> queryReport9(Map<String, String> map) {
		return mapper.queryReport9(map);
	}

	@Override
	public List<ArchivesReport> queryReport10(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryReport10(map);
	}

	@Override
	public List<SoldierBasicInfo> queryReport11(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryReport11(map);
	}

	@Override
	public List<ArchivesReport> queryReport12(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryReport12(map);
	}

	@Override
	public List<ArchivesReport> queryReport13(Map<String, String> map) {
		// TODO Auto-generated method stub
		return mapper.queryReport13(map);
	}

	@Override
	public Map<String,ArchivesReport> queryReport14(HashMap<String,String>map) {
		 Map<String,ArchivesReport>reportMap=new HashMap<>();
		 //存储市区集合
		 List<ArchivesReport>sysOrgOfReportVoOfCityList=new ArrayList<>();
		 //存储省
		 ArchivesReport sysOrgOfReportVoOfProvince=new ArchivesReport();
		 sysOrgOfReportVoOfProvince.setRegionalCode("330000");
		 sysOrgOfReportVoOfProvince.setRegionalName("浙江省");
		 SysRegional sysRegiona=new SysRegional();
		 sysRegiona.setParentId(330000);
		 //获取浙江所有市
		 List<SysRegional> cityList = regionalMapper.selectAll(sysRegiona);
		 ArchivesReport totalReport=new ArchivesReport();//用来存浙江省档案统计情况
		 Integer totalReturnReceiveCount=0;//省内退档统计
		 Integer totalActualReceptionCount=0;//省内实际接收
		 Integer totalNotReceptionCount=0;//省内未接收
		 Integer totalPlanReceptionCount=0;//省内预计接收
		  for(SysRegional city:cityList){
		  	  //个个市
			  ArchivesReport sysOrgOfReportVoOfCity=new ArchivesReport();
			  //存储个个市的下属区
			  List<ArchivesReport>areaReportList=new ArrayList<>();
			  sysOrgOfReportVoOfCity.setRegionalName(city.getRegionalName());
			  sysOrgOfReportVoOfCity.setRegionalCode(city.getRegionalCode());
		  	  SysRegional area=new SysRegional();
			  area.setParentId(Integer.parseInt(city.getRegionalCode()));
			  //查询所有市的区
			  List<SysRegional> orgList = regionalMapper.selectAll(area);
			  for(SysRegional org:orgList){
				  //统计区内的所有档案情况
				  ArchivesReport archivesReport=new ArchivesReport();
				  //设置区名
				  archivesReport.setRegionalName(org.getRegionalName());
				  archivesReport.setRegionalCode(org.getRegionalCode());
				  map.put("regionalCode",org.getRegionalCode());
				  map.put("status","4");
				  //统计退档
				  SoldierBasicInfo sold = soldierMapper.getCountByWhere(map);
				  if(sold==null) {
					  archivesReport.setReturnReceivePlaceCount(0);
				  }else {
					  archivesReport.setReturnReceivePlaceCount(sold.getCount());
				  }

				  //统计实际接收
				  map.put("regionalCode",org.getRegionalCode());
				  map.put("status","1，2");
				  SoldierBasicInfo actualSold = soldierMapper.getCountByWhere(map);
				  if(actualSold==null) {
					  archivesReport.setActualReceptionCount(0);
				  }else {
					  archivesReport.setActualReceptionCount(actualSold.getCount());
				  }
				  //统计未接收
				  map.put("regionalCode",org.getRegionalCode());
				  map.put("status","0");
				  SoldierBasicInfo notSold = soldierMapper.getCountByWhere(map);
				  if(notSold==null) {
					  archivesReport.setNotReceptionCount(0);
				  }else {
					  archivesReport.setNotReceptionCount(notSold.getCount());
				  }
				  archivesReport.setPlanReceptionCount(archivesReport.getActualReceptionCount()+archivesReport.getReturnReceivePlaceCount()+archivesReport.getNotReceptionCount());
				  totalReturnReceiveCount+=archivesReport.getReturnReceivePlaceCount();
				  totalActualReceptionCount+=archivesReport.getActualReceptionCount();
				  totalNotReceptionCount+=archivesReport.getNotReceptionCount();
				  totalPlanReceptionCount+=archivesReport.getPlanReceptionCount();
				  areaReportList.add(archivesReport);

			  }
			  //将统计市的所有信息
			  sysOrgOfReportVoOfCity.setChildren(areaReportList);
			  //将市添加到市集合
			  sysOrgOfReportVoOfCityList.add(sysOrgOfReportVoOfCity);
		  }


		 //对省的子节点集合赋值
		 sysOrgOfReportVoOfProvince.setChildren(sysOrgOfReportVoOfCityList);
		 reportMap.put("province",sysOrgOfReportVoOfProvince);
		 totalReport.setTotalActualReceptionCount(totalActualReceptionCount);
		 totalReport.setTotalNotReceptionCount(totalNotReceptionCount);
		 totalReport.setTotalReturnReceiveCount(totalReturnReceiveCount);
		 totalReport.setTotalPlanReceptionCount(totalPlanReceptionCount);
		 sysOrgOfReportVoOfProvince.setTotalActualReceptionCount(totalActualReceptionCount);
		 sysOrgOfReportVoOfProvince.setTotalNotReceptionCount(totalNotReceptionCount);
		 sysOrgOfReportVoOfProvince.setTotalReturnReceiveCount(totalReturnReceiveCount);
		 sysOrgOfReportVoOfProvince.setTotalPlanReceptionCount(totalPlanReceptionCount);
		 reportMap.put("totalReport",totalReport);
		return reportMap ;
	}

	@Override
	public List<SoldierBasicInfo> queryReport15(HashMap<String,String>map) {
		List<ArchivesReport>reportList=new ArrayList<>();//浙江所有市的安置情况统计
		SysRegional sysRegiona=new SysRegional();
		sysRegiona.setParentId(330000);
		List<SysRegional> orgList = regionalMapper.selectAll(sysRegiona);
		for(SysRegional org:orgList){
			ArchivesReport archivesReport=new ArchivesReport();//当前市的安置情况统计
			String regionalCode=org.getRegionalCode().substring(0,4);
			map.put("regionalCode",regionalCode);
			map.put("status","4");
			//统计退档
			List<SoldierBasicInfo> soldierListByWhere = soldierMapper.getSoldierListByWhere(map);
			for(SoldierBasicInfo basicInfo:soldierListByWhere){
				basicInfo.setIdcard(basicInfo.getIdcard().replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
			}
            archivesReport.setReturnOPlace(soldierListByWhere);
            archivesReport.setRegionalCode(org.getRegionalCode());
            archivesReport.setRegionalName(org.getRegionalName());
            reportList.add(archivesReport);
		}
		List<SoldierBasicInfo> allReturnPlace = getAllReturnPlace(reportList);
		return allReturnPlace;
	}

	/**
	 * 将基础统计信息封装到ArchivesReport报表中
	 * @param infoList
	 */
      public ArrayList<SoldierBasicInfo>tryToArchivesReport(List<SoldierBasicInfo>infoList){
        ArrayList<SoldierBasicInfo>list=new ArrayList<>();
      	for(SoldierBasicInfo soldierBasicInfo:infoList){
      		list.add(soldierBasicInfo);
		}
      	return  list;
	  }
	@Override
	public Map<String, Object> getAllCity(Map<String, ArchivesReport> map, Map<String, Object> beans) {
		List<ArchivesReportVo>voList=new ArrayList<>();
		//用来存市得退档，实际接受。。等总数
		ArchivesReportVo totalArchivesReport=new ArchivesReportVo();
		List<ArchivesReport> AllCity = map.get("province").getChildren();
		List<ArchivesReport> hzArchivesReports=null;
		List<ArchivesReport> huzArchivesReports=null;
		List<ArchivesReport> tzArchivesReports=null;
		List<ArchivesReport> lsArchivesReports=null;
		List<ArchivesReport> nbArchivesReports=null;
		List<ArchivesReport> zsArchivesReports=null;
		List<ArchivesReport> jhArchivesReports=null;
		List<ArchivesReport> sxArchivesReports=null;
		List<ArchivesReport> qzArchivesReports=null;
		List<ArchivesReport> wzArchivesReports=null;
		List<ArchivesReport> jxArchivesReports=null;
		for(ArchivesReport archivesReport:AllCity){
			if(archivesReport.getRegionalName().equals("杭州市")){
				hzArchivesReports=archivesReport.getChildren();
			}
			if(archivesReport.getRegionalName().equals("宁波市")){
				nbArchivesReports=archivesReport.getChildren();
			}
			if(archivesReport.getRegionalName().equals("绍兴市")){
				sxArchivesReports=archivesReport.getChildren();
			}
			if(archivesReport.getRegionalName().equals("金华市")){
				jhArchivesReports=archivesReport.getChildren();
			}
			if(archivesReport.getRegionalName().equals("丽水市")){
				lsArchivesReports=archivesReport.getChildren();
			}
			if(archivesReport.getRegionalName().equals("温州市")){
				wzArchivesReports=archivesReport.getChildren();
			}
			if(archivesReport.getRegionalName().equals("衢州市")){
				qzArchivesReports=archivesReport.getChildren();
			}
			if(archivesReport.getRegionalName().equals("湖州市")){
				huzArchivesReports=archivesReport.getChildren();
			}
			if(archivesReport.getRegionalName().equals("舟山市")){
				zsArchivesReports=archivesReport.getChildren();
			}
			if(archivesReport.getRegionalName().equals("嘉兴市")){
				jxArchivesReports=archivesReport.getChildren();
			}
			if(archivesReport.getRegionalName().equals("台州市")){
				tzArchivesReports=archivesReport.getChildren();
			}
		}

		for(int i=0;i<100;i++){
			if(hzArchivesReports.size()>i||nbArchivesReports.size()>i||jxArchivesReports.size()>i||sxArchivesReports.size()>i
					||huzArchivesReports.size()>i||tzArchivesReports.size()>i||wzArchivesReports.size()>i||lsArchivesReports.size()>i
					||qzArchivesReports.size()>i||zsArchivesReports.size()>i||jhArchivesReports.size()>i){
				ArchivesReportVo archivesReportVo=new ArchivesReportVo();
				if(hzArchivesReports.size()>i){//杭州
					archivesReportVo.setHzActualReceptionCount(hzArchivesReports.get(i).getActualReceptionCount());
					archivesReportVo.setHzAreaName(hzArchivesReports.get(i).getRegionalName());
					archivesReportVo.setHzBackPlaceCount(hzArchivesReports.get(i).getReturnReceivePlaceCount());
					archivesReportVo.setHzNotReceptionCount(hzArchivesReports.get(i).getNotReceptionCount());
					archivesReportVo.setHzPlanReceptionCount(hzArchivesReports.get(i).getPlanReceptionCount());
					//存市得个个统计总数
					totalArchivesReport.setHzTotalActualReceptionCount(totalArchivesReport.getHzTotalActualReceptionCount()+hzArchivesReports.get(i).getActualReceptionCount());
					totalArchivesReport.setHzTotalNotReceptionCount(totalArchivesReport.getHzTotalNotReceptionCount()+hzArchivesReports.get(i).getNotReceptionCount());
					totalArchivesReport.setHzTotalPlanReceptionCount(totalArchivesReport.getHzTotalPlanReceptionCount()+hzArchivesReports.get(i).getPlanReceptionCount());
					totalArchivesReport.setHzTotalBackPlaceCount(totalArchivesReport.getHzTotalBackPlaceCount()+hzArchivesReports.get(i).getReturnReceivePlaceCount());
				}
				if(nbArchivesReports.size()>i){//宁波
					archivesReportVo.setNbActualReceptionCount(nbArchivesReports.get(i).getActualReceptionCount());
					archivesReportVo.setNbAreaName(nbArchivesReports.get(i).getRegionalName());
					archivesReportVo.setNbBackPlaceCount(nbArchivesReports.get(i).getReturnReceivePlaceCount());
					archivesReportVo.setNbNotReceptionCount(nbArchivesReports.get(i).getNotReceptionCount());
					archivesReportVo.setNbPlanReceptionCount(nbArchivesReports.get(i).getPlanReceptionCount());
					//存市得个个统计总数
					totalArchivesReport.setNbActualReceptionCount(totalArchivesReport.getNbTotalActualReceptionCount()+nbArchivesReports.get(i).getActualReceptionCount());
					totalArchivesReport.setNbTotalNotReceptionCount(totalArchivesReport.getNbTotalNotReceptionCount()+nbArchivesReports.get(i).getNotReceptionCount());
					totalArchivesReport.setNbTotalPlanReceptionCount(totalArchivesReport.getNbTotalPlanReceptionCount()+nbArchivesReports.get(i).getPlanReceptionCount());
					totalArchivesReport.setNbTotalBackPlaceCount(totalArchivesReport.getNbTotalBackPlaceCount()+nbArchivesReports.get(i).getReturnReceivePlaceCount());
				}
				if(sxArchivesReports.size()>i){//绍兴
					archivesReportVo.setSxActualReceptionCount(sxArchivesReports.get(i).getActualReceptionCount());
					archivesReportVo.setSxAreaName(sxArchivesReports.get(i).getRegionalName());
					archivesReportVo.setSxBackPlaceCount(sxArchivesReports.get(i).getReturnReceivePlaceCount());
					archivesReportVo.setSxNotReceptionCount(sxArchivesReports.get(i).getNotReceptionCount());
					archivesReportVo.setSxPlanReceptionCount(sxArchivesReports.get(i).getPlanReceptionCount());
					//存市得个个统计总数
					totalArchivesReport.setSxActualReceptionCount(totalArchivesReport.getSxTotalActualReceptionCount()+sxArchivesReports.get(i).getActualReceptionCount());
					totalArchivesReport.setSxTotalNotReceptionCount(totalArchivesReport.getSxTotalNotReceptionCount()+sxArchivesReports.get(i).getNotReceptionCount());
					totalArchivesReport.setSxTotalPlanReceptionCount(totalArchivesReport.getSxTotalPlanReceptionCount()+sxArchivesReports.get(i).getPlanReceptionCount());
					totalArchivesReport.setSxTotalBackPlaceCount(totalArchivesReport.getSxTotalBackPlaceCount()+sxArchivesReports.get(i).getReturnReceivePlaceCount());
				}
				if(jxArchivesReports.size()>i){//嘉兴
					archivesReportVo.setJxActualReceptionCount(jxArchivesReports.get(i).getActualReceptionCount());
					archivesReportVo.setJxAreaName(jxArchivesReports.get(i).getRegionalName());
					archivesReportVo.setJxBackPlaceCount(jxArchivesReports.get(i).getReturnReceivePlaceCount());
					archivesReportVo.setJxNotReceptionCount(jxArchivesReports.get(i).getNotReceptionCount());
					archivesReportVo.setJxPlanReceptionCount(jxArchivesReports.get(i).getPlanReceptionCount());
					//存市得个个统计总数
					totalArchivesReport.setJxActualReceptionCount(totalArchivesReport.getJxTotalActualReceptionCount()+jxArchivesReports.get(i).getActualReceptionCount());
					totalArchivesReport.setJxTotalNotReceptionCount(totalArchivesReport.getJxTotalNotReceptionCount()+jxArchivesReports.get(i).getNotReceptionCount());
					totalArchivesReport.setJxTotalPlanReceptionCount(totalArchivesReport.getJxTotalPlanReceptionCount()+jxArchivesReports.get(i).getPlanReceptionCount());
					totalArchivesReport.setJxTotalBackPlaceCount(totalArchivesReport.getJxTotalBackPlaceCount()+jxArchivesReports.get(i).getReturnReceivePlaceCount());
				}
				if(zsArchivesReports.size()>i){//舟山
					archivesReportVo.setZsActualReceptionCount(zsArchivesReports.get(i).getActualReceptionCount());
					archivesReportVo.setZsAreaName(zsArchivesReports.get(i).getRegionalName());
					archivesReportVo.setZsBackPlaceCount(zsArchivesReports.get(i).getReturnReceivePlaceCount());
					archivesReportVo.setZsNotReceptionCount(zsArchivesReports.get(i).getNotReceptionCount());
					archivesReportVo.setZsPlanReceptionCount(zsArchivesReports.get(i).getPlanReceptionCount());
					//存市得个个统计总数
					totalArchivesReport.setZsActualReceptionCount(totalArchivesReport.getZsTotalActualReceptionCount()+zsArchivesReports.get(i).getActualReceptionCount());
					totalArchivesReport.setZsTotalNotReceptionCount(totalArchivesReport.getZsTotalNotReceptionCount()+zsArchivesReports.get(i).getNotReceptionCount());
					totalArchivesReport.setZsTotalPlanReceptionCount(totalArchivesReport.getZsTotalPlanReceptionCount()+zsArchivesReports.get(i).getPlanReceptionCount());
					totalArchivesReport.setZsTotalBackPlaceCount(totalArchivesReport.getZsTotalBackPlaceCount()+zsArchivesReports.get(i).getReturnReceivePlaceCount());
				}
				if(qzArchivesReports.size()>i){//衢州
					archivesReportVo.setQzActualReceptionCount(qzArchivesReports.get(i).getActualReceptionCount());
					archivesReportVo.setQzAreaName(qzArchivesReports.get(i).getRegionalName());
					archivesReportVo.setQzBackPlaceCount(qzArchivesReports.get(i).getReturnReceivePlaceCount());
					archivesReportVo.setQzNotReceptionCount(qzArchivesReports.get(i).getNotReceptionCount());
					archivesReportVo.setQzPlanReceptionCount(qzArchivesReports.get(i).getPlanReceptionCount());
					//存市得个个统计总数
					totalArchivesReport.setQzActualReceptionCount(totalArchivesReport.getQzTotalActualReceptionCount()+qzArchivesReports.get(i).getActualReceptionCount());
					totalArchivesReport.setQzTotalNotReceptionCount(totalArchivesReport.getQzTotalNotReceptionCount()+qzArchivesReports.get(i).getNotReceptionCount());
					totalArchivesReport.setQzTotalPlanReceptionCount(totalArchivesReport.getQzTotalPlanReceptionCount()+qzArchivesReports.get(i).getPlanReceptionCount());
					totalArchivesReport.setQzTotalBackPlaceCount(totalArchivesReport.getQzTotalBackPlaceCount()+qzArchivesReports.get(i).getReturnReceivePlaceCount());
				}
				if(lsArchivesReports.size()>i){//丽水
					archivesReportVo.setLsActualReceptionCount(lsArchivesReports.get(i).getActualReceptionCount());
					archivesReportVo.setLsAreaName(lsArchivesReports.get(i).getRegionalName());
					archivesReportVo.setLsBackPlaceCount(lsArchivesReports.get(i).getReturnReceivePlaceCount());
					archivesReportVo.setLsNotReceptionCount(lsArchivesReports.get(i).getNotReceptionCount());
					archivesReportVo.setLsPlanReceptionCount(lsArchivesReports.get(i).getPlanReceptionCount());
					//存市得个个统计总数
					totalArchivesReport.setLsActualReceptionCount(totalArchivesReport.getLsTotalActualReceptionCount()+lsArchivesReports.get(i).getActualReceptionCount());
					totalArchivesReport.setLsTotalNotReceptionCount(totalArchivesReport.getLsTotalNotReceptionCount()+lsArchivesReports.get(i).getNotReceptionCount());
					totalArchivesReport.setLsTotalPlanReceptionCount(totalArchivesReport.getLsTotalPlanReceptionCount()+lsArchivesReports.get(i).getPlanReceptionCount());
					totalArchivesReport.setLsTotalBackPlaceCount(totalArchivesReport.getLsTotalBackPlaceCount()+lsArchivesReports.get(i).getReturnReceivePlaceCount());
				}
				if(tzArchivesReports.size()>i){//台州
					archivesReportVo.setTzActualReceptionCount(tzArchivesReports.get(i).getActualReceptionCount());
					archivesReportVo.setTzAreaName(tzArchivesReports.get(i).getRegionalName());
					archivesReportVo.setTzBackPlaceCount(tzArchivesReports.get(i).getReturnReceivePlaceCount());
					archivesReportVo.setTzNotReceptionCount(tzArchivesReports.get(i).getNotReceptionCount());
					archivesReportVo.setTzPlanReceptionCount(tzArchivesReports.get(i).getPlanReceptionCount());
					//存市得个个统计总数
					totalArchivesReport.setTzActualReceptionCount(totalArchivesReport.getTzTotalActualReceptionCount()+tzArchivesReports.get(i).getActualReceptionCount());
					totalArchivesReport.setTzTotalNotReceptionCount(totalArchivesReport.getTzTotalNotReceptionCount()+tzArchivesReports.get(i).getNotReceptionCount());
					totalArchivesReport.setTzTotalPlanReceptionCount(totalArchivesReport.getTzTotalPlanReceptionCount()+tzArchivesReports.get(i).getPlanReceptionCount());
					totalArchivesReport.setTzTotalBackPlaceCount(totalArchivesReport.getTzTotalBackPlaceCount()+tzArchivesReports.get(i).getReturnReceivePlaceCount());
				}
				if(wzArchivesReports.size()>i){//温州
					archivesReportVo.setWzActualReceptionCount(wzArchivesReports.get(i).getActualReceptionCount());
					archivesReportVo.setWzAreaName(wzArchivesReports.get(i).getRegionalName());
					archivesReportVo.setWzBackPlaceCount(wzArchivesReports.get(i).getReturnReceivePlaceCount());
					archivesReportVo.setWzNotReceptionCount(wzArchivesReports.get(i).getNotReceptionCount());
					archivesReportVo.setWzPlanReceptionCount(wzArchivesReports.get(i).getPlanReceptionCount());
					//存市得个个统计总数
					totalArchivesReport.setWzActualReceptionCount(totalArchivesReport.getWzTotalActualReceptionCount()+wzArchivesReports.get(i).getActualReceptionCount());
					totalArchivesReport.setWzTotalNotReceptionCount(totalArchivesReport.getWzTotalNotReceptionCount()+wzArchivesReports.get(i).getNotReceptionCount());
					totalArchivesReport.setWzTotalPlanReceptionCount(totalArchivesReport.getWzTotalPlanReceptionCount()+wzArchivesReports.get(i).getPlanReceptionCount());
					totalArchivesReport.setWzTotalBackPlaceCount(totalArchivesReport.getWzTotalBackPlaceCount()+wzArchivesReports.get(i).getReturnReceivePlaceCount());
				}
				if(jhArchivesReports.size()>i){//金华
					archivesReportVo.setJhActualReceptionCount(jhArchivesReports.get(i).getActualReceptionCount());
					archivesReportVo.setJhAreaName(jhArchivesReports.get(i).getRegionalName());
					archivesReportVo.setJhBackPlaceCount(jhArchivesReports.get(i).getReturnReceivePlaceCount());
					archivesReportVo.setJhNotReceptionCount(jhArchivesReports.get(i).getNotReceptionCount());
					archivesReportVo.setJhPlanReceptionCount(jhArchivesReports.get(i).getPlanReceptionCount());
					//存市得个个统计总数
					totalArchivesReport.setJhActualReceptionCount(totalArchivesReport.getJhTotalActualReceptionCount()+jhArchivesReports.get(i).getActualReceptionCount());
					totalArchivesReport.setJhTotalNotReceptionCount(totalArchivesReport.getJhTotalNotReceptionCount()+jhArchivesReports.get(i).getNotReceptionCount());
					totalArchivesReport.setJhTotalPlanReceptionCount(totalArchivesReport.getJhTotalPlanReceptionCount()+jhArchivesReports.get(i).getPlanReceptionCount());
					totalArchivesReport.setJhTotalBackPlaceCount(totalArchivesReport.getJhTotalBackPlaceCount()+jhArchivesReports.get(i).getReturnReceivePlaceCount());
				}
				if(huzArchivesReports.size()>i){//湖州
					archivesReportVo.setHuzActualReceptionCount(huzArchivesReports.get(i).getActualReceptionCount());
					archivesReportVo.setHuzAreaName(huzArchivesReports.get(i).getRegionalName());
					archivesReportVo.setHuzBackPlaceCount(huzArchivesReports.get(i).getReturnReceivePlaceCount());
					archivesReportVo.setHuzNotReceptionCount(huzArchivesReports.get(i).getNotReceptionCount());
					archivesReportVo.setHuzPlanReceptionCount(huzArchivesReports.get(i).getPlanReceptionCount());
					//存市得个个统计总数
					totalArchivesReport.setHuzActualReceptionCount(totalArchivesReport.getHuzTotalActualReceptionCount()+huzArchivesReports.get(i).getActualReceptionCount());
					totalArchivesReport.setHuzTotalNotReceptionCount(totalArchivesReport.getHuzTotalNotReceptionCount()+huzArchivesReports.get(i).getNotReceptionCount());
					totalArchivesReport.setHuzTotalPlanReceptionCount(totalArchivesReport.getHuzTotalPlanReceptionCount()+huzArchivesReports.get(i).getPlanReceptionCount());
					totalArchivesReport.setHuzTotalBackPlaceCount(totalArchivesReport.getHuzTotalBackPlaceCount()+huzArchivesReports.get(i).getReturnReceivePlaceCount());
				}
				voList.add(archivesReportVo);
			}
		}
		beans.put("allCityList",voList);
		beans.put("totalOfCity",totalArchivesReport);
		return beans;
	}

	@Override
	public List<SoldierBasicInfo> getAllReturnPlace(List<ArchivesReport> archivesReportList) {
		List<SoldierBasicInfo>list=new ArrayList<>();
		for(ArchivesReport archivesReport:archivesReportList){
			if(archivesReport.getReturnOPlace().size()>0){
				list.addAll(archivesReport.getReturnOPlace());
			}
		}
		return  list;
	}
}
