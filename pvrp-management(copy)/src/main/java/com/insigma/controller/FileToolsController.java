package com.insigma.controller;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.insigma.dto.ArchivesReportVo;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.insigma.po.ResultVo;
import com.insigma.po.SysParamInfo;
import com.insigma.po.soldier.ArchivesReport;
import com.insigma.po.soldier.AuditLog;
import com.insigma.po.soldier.ReportVo;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SoldierBasicInfoOse;
import com.insigma.po.soldier.SoldierBasicInfoStatic;
import com.insigma.po.soldier.SysRegional;
import com.insigma.service.ArchivesReportService;
import com.insigma.service.AuditLogService;
import com.insigma.service.SoldierBasicInfoOseService;
import com.insigma.service.SoldierBasicInfoService;
import com.insigma.service.SysParamInfoService;
import com.insigma.service.SysRegionalService;
import com.insigma.util.CharUtils;
import com.insigma.util.DateUtils;
import com.insigma.util.ExcelUtils;
import com.insigma.util.FileUtils;
import com.insigma.util.ParamUtils;
import com.insigma.util.ReadTest;
import com.insigma.util.WordUtils;

import net.minidev.json.JSONObject;
import net.sf.json.JSONArray;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * 上传文件
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/pvrpm/file")
public class FileToolsController {
	 
	@Value("${rootpath}")
    private String rootPath;
	
	@Value("${downloadpath}")
    private String downloadpath;
	
	//简历人员照片存放路径
	@Value("${filepersonnel}")
	private String personnel;
	//公告上传附件存放路径
	@Value("${filenotice}")
	private String notice;
	//查看文件url
//	@Value("${fileurl}")
//	private String url;
	
	//站内信文件存放路径
	@Value("${communication}")
	private String communication;
	
	//生成excel文件存放路径
	@Value("${excelpath}")
	private String excelpath;
	
	//上传zip文件存放路径
	@Value("${zippath}")
	private String zippath;
	
	//解压的pdf文件存放路径
	@Value("${pdfpath}")
	private String pdfpath;
	
	@Autowired
	private SoldierBasicInfoService sbiMapper;
	
	@Autowired
    private HttpServletRequest request;
	
	@Autowired
	private SysRegionalService srsMapper;
	
	@Autowired
	private SysParamInfoService paramMapper;
	
	@Autowired
	private AuditLogService logMapper;
	
	@Autowired
	private ArchivesReportService arMapper;
	
	@Autowired
	private SoldierBasicInfoOseService sbioMapper;
	
	@RequestMapping(value = "uploadFile", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public String uploadImg(@RequestParam("file") MultipartFile file,String type,int sbiId) {
		
		FileUtils ft = new FileUtils();
		ResultVo resultVo = new ResultVo();
		String filePath="";
		String retUrl="";
		String archivesFilePath="/archives/"+sbiId;
		if(type.equals("1")){
			filePath=rootPath+archivesFilePath;
			retUrl=notice;
		}
		if(type.equals("2")){
			filePath=rootPath+personnel;
			retUrl=personnel;
		}
		if(type.equals("3")){
			filePath=rootPath+communication;
			retUrl=communication;
		}
		String filename="";
		try {
			filename = ft.upload(file, filePath);
//			map.put("filePath", retUrl+filename);
			resultVo.setStatusCode(200);
			resultVo.setMessage("文件上传成功");
			resultVo.setList("\"file/"+retUrl+filename+"\"");
		} catch (IllegalStateException e) {
			e.printStackTrace();
			resultVo.setStatusCode(-1);
			resultVo.setMessage("文件上传处理失败");
		} catch (IOException e) {
			e.printStackTrace();
			resultVo.setStatusCode(-1);
			resultVo.setMessage("文件上传处理失败");
		}
		return resultVo.toString();
	}
	
	@RequestMapping(value = "expExcel",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
	public String expExcel(@RequestBody JSONObject json) throws Exception {
		ResultVo resultVo = new ResultVo();
		Map<String, String> map = new HashMap<>();
		List<?> list =null;
		String fileName ="";//导出文件名
		String resource ="";//模板路径
		String title1 ="";//一级标题
		String title2 ="";//二级标题
		Map<String, Object> beans = new HashMap<String, Object>();
		String subdirectory="";
		
		String date = DateUtils.getStringCurrentDate();
		String year = DateUtils.getStringYearDate();
		String expType=json.get("expType").toString();
		
//		if(expType.equals("1")){//开具接收安置通知书人员信息导出
//			SoldierBasicInfo record = new SoldierBasicInfo();
//			record.setYbId(Integer.parseInt(json.get("ybId").toString()));
//			record.setName(json.get("name").toString());
//			record.setRegionalCode(json.get("regionalCode").toString());
//			list = sbiMapper.selectExpInfo(record);
//			fileName = "批量开具接收安置通知书人员信息"+date+".xls";
//			resource = "/templates/接收安置通知书模板.xls";
//			subdirectory="接收安置通知书/"+date+"/";
//		}
		
		
		if(expType.equals("20")){//安置类别统计表导出
			Map<String, String> pmap=new HashMap<>();
			pmap.put("year", json.get("year").toString());
			pmap.put("ybId", json.get("ybId").toString());
			pmap.put("personnelType", json.get("personnelType").toString());
			List<ArchivesReport> arlist = arMapper.queryReport1(pmap);
			if(arlist.size()>0){
				for (int i = 0; i < arlist.size(); i++) {
					Integer total = arlist.get(i).getHzcount()+arlist.get(i).getNbcount() 
							+arlist.get(i).getWzcount() +arlist.get(i).getJxcount() +arlist.get(i).getHuzcount() 
							+arlist.get(i).getSxcount() +arlist.get(i).getJhcount() +arlist.get(i).getQzcount() 
							+arlist.get(i).getZscount() +arlist.get(i).getTzcount() +arlist.get(i).getLscount() ;
					arlist.get(i).setTotal(total);
				}
				list = arlist;
			}
			String titleType="退役士兵、退出消防员";
			if(!StringUtils.isEmpty(json.get("personnelType").toString())){
				if("1".equals(json.get("personnelType").toString())){
					titleType="退役士兵";
				}else{
					titleType="退出消防员";
				}
			}
			
			fileName = "符合条件士兵安置信息统计表"+date+".xls";
			resource = "/templates/report/安置类别统计表模板.xls";
			title1="浙江省"+year+"年由政府安排工作"+titleType+"安置类型统计表";
			subdirectory="统计报表/"+date+"/";
		}
		
		if(expType.equals("21")){//易地安置统计表导出
			Map<String, String> pmap=new HashMap<>();
			pmap.put("year", json.get("year").toString());
			pmap.put("ybId", json.get("ybId").toString());
			pmap.put("personnelType", json.get("personnelType").toString());
			List<ArchivesReport> arlist = arMapper.queryReport2(pmap);
			if(arlist.size()>0){
				for (int i = 0; i < arlist.size(); i++) {
					Integer total = arlist.get(i).getHzcount()+arlist.get(i).getNbcount() 
							+arlist.get(i).getWzcount() +arlist.get(i).getJxcount() +arlist.get(i).getHuzcount() 
							+arlist.get(i).getSxcount() +arlist.get(i).getJhcount() +arlist.get(i).getQzcount() 
							+arlist.get(i).getZscount() +arlist.get(i).getTzcount() +arlist.get(i).getLscount() ;
					arlist.get(i).setTotal(total);
				}
				list = arlist;
			}
			String titleType="退役士兵、退出消防员";
			if(!StringUtils.isEmpty(json.get("personnelType").toString())){
				if("1".equals(json.get("personnelType").toString())){
					titleType="退役士兵";
				}else{
					titleType="退出消防员";
				}
			}
			fileName = "易地安置统计表"+date+".xls";
			resource = "/templates/report/易地安置统计表模板.xls";
			title1="浙江省"+year+"年由政府安排工作"+titleType+"易地安置统计表";
			subdirectory="统计报表/"+date+"/";
		}
		
		if(expType.equals("22")){//档案审核问题汇总表导出
			Map<String, String> pmap=new HashMap<>();
			pmap.put("year", json.get("year").toString());
			pmap.put("ybId", json.get("ybId").toString());
			pmap.put("personnelType", json.get("personnelType").toString());
			List<ArchivesReport> arlist = arMapper.queryReport3(pmap);
			if(arlist.size()>0){
				for (int i = 0; i < arlist.size(); i++) {
					Integer total = arlist.get(i).getMaterials1()+arlist.get(i).getMaterials2()+arlist.get(i).getMaterials3()+
							arlist.get(i).getMaterials4()+arlist.get(i).getMaterials5()+arlist.get(i).getMaterials6()+
							arlist.get(i).getMaterials7()+arlist.get(i).getMaterials8()+arlist.get(i).getMaterials9()+
							arlist.get(i).getMaterials10()+arlist.get(i).getMaterials11()+arlist.get(i).getMaterials12()+
							arlist.get(i).getMaterials13()+arlist.get(i).getMaterials14()+arlist.get(i).getMaterials15()+
							arlist.get(i).getMaterials16()+arlist.get(i).getMaterials17()+arlist.get(i).getMaterials18()+
							arlist.get(i).getMaterials19()+arlist.get(i).getMaterials20()+arlist.get(i).getMaterials21()+
							arlist.get(i).getMaterials22()+arlist.get(i).getAuditRemarkNum()+arlist.get(i).getRemarkNum();
					arlist.get(i).setTotal(total);
				}
				list = arlist;
			}
			String titleType="退役士兵、退出消防员";
			if(!StringUtils.isEmpty(json.get("personnelType").toString())){
				if("1".equals(json.get("personnelType").toString())){
					titleType="退役士兵";
				}else{
					titleType="退出消防员";
				}
			}
			fileName = "档案审核问题汇总表"+date+".xls";
			resource = "/templates/report/档案审核问题汇总表模板.xls";
			title1="浙江省"+year+"年由政府安排工作"+titleType+"档案审核问题汇总表";
			subdirectory="统计报表/"+date+"/";
		}
		
		if(expType.equals("23")){//档案审核问题明细表导出
			Map<String, String> pmap=new HashMap<>();
			pmap.put("year", json.get("year").toString());
			pmap.put("ybId", json.get("ybId").toString());
			pmap.put("personnelType", json.get("personnelType").toString());
			pmap.put("name", json.get("name").toString());
			String regionalCode=json.get("regionalCode").toString();
			if(!StringUtils.isEmpty(regionalCode)){
				pmap.put("regionalCode", regionalCode.substring(0,4));
			}
			list = arMapper.queryReport4(pmap);
			String titleType="退役士兵、退出消防员";
			if(!StringUtils.isEmpty(json.get("personnelType").toString())){
				if("1".equals(json.get("personnelType").toString())){
					titleType="退役士兵";
				}else{
					titleType="退出消防员";
				}
			}
			fileName = "档案审核问题明细表"+date+".xls";
			resource = "/templates/report/档案审核问题明细表模板.xls";
			title1="浙江省"+year+"年由政府安排工作"+titleType+"档案审核问题明细表";
			subdirectory="统计报表/"+date+"/";
		}
		
		
		if(expType.equals("24")){//人员综合信息表导出
			
			Map<String, String> pmap=new HashMap<>();
			pmap.put("ybId", json.get("ybId").toString());
			pmap.put("personnelType", json.get("personnelType").toString());
			
			String tmpName="人员综合信息表";
			String queryType=json.get("queryType").toString();
			if(!StringUtils.isEmpty(queryType)){
				pmap.put("queryType", queryType);
				if("1".equals(queryType)){
					tmpName= "人员综合信息表-政府安排";
				}else{
					tmpName= "人员综合信息表-灵活就业";
				}
			}
			
			String titleType="退役士兵、退出消防员";
			if(!StringUtils.isEmpty(json.get("personnelType").toString())){
				if("1".equals(json.get("personnelType").toString())){
					titleType="退役士兵";
				}else{
					titleType="退出消防员";
				}
			}
			String regionalCode=json.get("regionalCode").toString();
			if(!regionalCode.equals("330000")){
				if(regionalCode.endsWith("00")){//机构编号00结尾的 为地市用户
					pmap.put("regionalCode", regionalCode.substring(0,4));
				}else{
					pmap.put("regionalCode", regionalCode);
				}
			}
			//查询所有参数配置表所有参数信息
			List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
			List<SysParamInfo> rlist= null;
			
			//一次查询出所有行政区划数据
			List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
			List<SysRegional> regional= null;
			
			SysRegional area = new SysRegional();
			area.setParentId(330000);
			List<SysRegional> arealist= srsMapper.selectAll(area);
			
			List<SoldierBasicInfoStatic> list5 = arMapper.queryReport5(pmap);
			
			for (int i = 0; i < list5.size(); i++) {
				if(!StringUtils.isEmpty(list5.get(i).getBigUnitName())){
					rlist=  ParamUtils.checkList(plist, "bigUnitName", list5.get(i).getBigUnitName() , "");
					if(rlist.size()>0){
						list5.get(i).setBigUnitName(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getSex())){
					rlist=  ParamUtils.checkList(plist, "sex", list5.get(i).getSex() , "");
					if(rlist.size()>0){
						list5.get(i).setSex(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getNation())){
					rlist=  ParamUtils.checkList(plist, "nation", list5.get(i).getNation() , "");
					if(rlist.size()>0){
						list5.get(i).setNation(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getSoldiersMilitaryRank())){
					rlist=  ParamUtils.checkList(plist, "soldMilitRankCode", list5.get(i).getSoldiersMilitaryRank() , "");
					if(rlist.size()>0){
						list5.get(i).setSoldiersMilitaryRank(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getResetCategory())){
					rlist=  ParamUtils.checkList(plist, "resetCategory", list5.get(i).getResetCategory() , "");
					if(rlist.size()>0){
						list5.get(i).setResetCategory(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getRelocateReasonCode())){
					rlist=  ParamUtils.checkList(plist, "relocateReasonCode", list5.get(i).getRelocateReasonCode() , "");
					if(rlist.size()>0){
						list5.get(i).setRelocateReasonCode(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getMarriageStatus())){
					rlist=  ParamUtils.checkList(plist, "marriageStatus", list5.get(i).getMarriageStatus() , "");
					if(rlist.size()>0){
						list5.get(i).setMarriageStatus(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getPoliAffiCode())){
					rlist=  ParamUtils.checkList(plist, "poliAffiCode", list5.get(i).getPoliAffiCode() , "");
					if(rlist.size()>0){
						list5.get(i).setPoliAffiCode(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getEduLevCode())){
					rlist=  ParamUtils.checkList(plist, "eduLevCode", list5.get(i).getEduLevCode() , "");
					if(rlist.size()>0){
						list5.get(i).setEduLevCode(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getIsMartyrChildren())){
					rlist=  ParamUtils.checkList(plist, "isOrNo", list5.get(i).getIsMartyrChildren() , "");
					if(rlist.size()>0){
						list5.get(i).setIsMartyrChildren(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getDisabilityCategoriesCode())){
					rlist=  ParamUtils.checkList(plist, "disabilityCategoriesCode", list5.get(i).getDisabilityCategoriesCode() , "");
					if(rlist.size()>0){
						list5.get(i).setDisabilityCategoriesCode(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getDisableGradeCode())){
					rlist=  ParamUtils.checkList(plist, "disableGradeCode", list5.get(i).getDisableGradeCode() , "");
					if(rlist.size()>0){
						list5.get(i).setDisableGradeCode(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getIsHardArea())){
					rlist=  ParamUtils.checkList(plist, "isOrNo", list5.get(i).getIsHardArea() , "");
					if(rlist.size()>0){
						list5.get(i).setIsHardArea(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getNewResetType())){
					rlist=  ParamUtils.checkList(plist, "newResetType", list5.get(i).getNewResetType() , "");
					if(rlist.size()>0){
						list5.get(i).setNewResetType(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getWheReceSelfSubs())){
					rlist=  ParamUtils.checkList(plist, "isOrNo", list5.get(i).getWheReceSelfSubs() , "");
					if(rlist.size()>0){
						list5.get(i).setWheReceSelfSubs(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getWheLivingExpenses())){
					rlist=  ParamUtils.checkList(plist, "isOrNo", list5.get(i).getWheLivingExpenses() , "");
					if(rlist.size()>0){
						list5.get(i).setWheLivingExpenses(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getUnitCategory())){
					rlist=  ParamUtils.checkList(plist, "unitCategory", list5.get(i).getUnitCategory() , "");
					if(rlist.size()>0){
						list5.get(i).setUnitCategory(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getWheOnTheStaff())){
					rlist=  ParamUtils.checkList(plist, "isOrNo", list5.get(i).getWheOnTheStaff() , "");
					if(rlist.size()>0){
						list5.get(i).setWheOnTheStaff(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getWheMouGuard())){
					rlist=  ParamUtils.checkList(plist, "isOrNo", list5.get(i).getWheMouGuard() , "");
					if(rlist.size()>0){
						list5.get(i).setWheMouGuard(rlist.get(0).getParamValue());
					}
				}
				
				if(!StringUtils.isEmpty(list5.get(i).getDomicile())){
					regional=ParamUtils.getRegional(srList, "", list5.get(i).getDomicile());
					if(regional.size()>0){
						list5.get(i).setDomicile(regional.get(0).getRegionalName());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getResetPlace())){
					regional=ParamUtils.getRegional(srList, "", list5.get(i).getResetPlace());
					if(regional.size()>0){
						list5.get(i).setResetPlace(regional.get(0).getRegionalName());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getRegionalCode())){
					regional=ParamUtils.getRegional(arealist, "", list5.get(i).getRegionalCode().substring(0,4)+"00");
					if(regional.size()>0){
						list5.get(i).setRegionalName(regional.get(0).getRegionalName());
					}
				}
			}
			list = list5;
			
			fileName = tmpName+date+".xls";
//			resource = "/templates/report/档案综合信息表模板.xls";
			resource = "/templates/report/由政府安排工作退役士兵信息导出模板.xls";
			title1="浙江省"+year+"年由政府安排工作"+titleType+"综合信息表";
			title2="总人数："+list.size()+" 人";
			subdirectory="统计报表/"+date+"/";
		}
		
		if(expType.equals("25")){//艰边立功处分统计表导出
			Map<String, String> pmap=new HashMap<>();
			pmap.put("year", json.get("year").toString());
			pmap.put("ybId", json.get("ybId").toString());
			pmap.put("personnelType", json.get("personnelType").toString());
			list = arMapper.queryReport6(pmap);
//			if(arlist.size()>0){
//				for (int i = 0; i < arlist.size(); i++) {
//					Integer total = arlist.get(i).getIsHardAreaCount()+arlist.get(i).getRewardCount()+arlist.get(i).getPunishmentCount();
//					arlist.get(i).setTotal(total);
//				}
//				list = arlist;
//			}
			String titleType="退役士兵、退出消防员";
			if(!StringUtils.isEmpty(json.get("personnelType").toString())){
				if("1".equals(json.get("personnelType").toString())){
					titleType="退役士兵";
				}else{
					titleType="退出消防员";
				}
			}
			fileName = "艰边立功处分汇总表"+date+".xls";
			resource = "/templates/report/艰边立功处分统计表模板.xls";
			title1="浙江省"+year+"年由政府安排工作"+titleType+"艰边立功处分汇总表";
			subdirectory="统计报表/"+date+"/";
		}
		
		if(expType.equals("26")){//综合成绩明细表导出
			Map<String, String> pmap=new HashMap<>();
			pmap.put("ybId", json.get("ybId").toString());
			pmap.put("name", json.get("name").toString());
			
			String regionalCode=json.get("regionalCode").toString();
			if(!regionalCode.equals("330000")){
				if(regionalCode.endsWith("00")){//机构编号00结尾的 为地市用户
					pmap.put("regionalCode", regionalCode.substring(0,4));
				}else{
					pmap.put("regionalCode", regionalCode);
				}
			}
			list = arMapper.queryReport7(pmap);
			
			fileName = "综合成绩明细表"+date+".xls";
			resource = "/templates/report/综合成绩明细表模板.xls";
			title1="浙江省"+year+"年由政府安排工作退役士兵、退出消防员综合成绩明细表";
			subdirectory="统计报表/"+date+"/";
		}
		
		if(expType.equals("27")){//易地安置原因统计表导出
			Map<String, String> pmap=new HashMap<>();
			pmap.put("year", json.get("year").toString());
			pmap.put("ybId", json.get("ybId").toString());
			pmap.put("personnelType", json.get("personnelType").toString());
			List<ArchivesReport> arlist = arMapper.queryReport8(pmap);
			if(arlist.size()>0){
				for (int i = 0; i < arlist.size(); i++) {
					Integer total = arlist.get(i).getHzcount()+arlist.get(i).getNbcount() 
							+arlist.get(i).getWzcount() +arlist.get(i).getJxcount() +arlist.get(i).getHuzcount() 
							+arlist.get(i).getSxcount() +arlist.get(i).getJhcount() +arlist.get(i).getQzcount() 
							+arlist.get(i).getZscount() +arlist.get(i).getTzcount() +arlist.get(i).getLscount() ;
					arlist.get(i).setTotal(total);
				}
				list = arlist;
			}
			
			String titleType="退役士兵、退出消防员";
			if(!StringUtils.isEmpty(json.get("personnelType").toString())){
				if("1".equals(json.get("personnelType").toString())){
					titleType="退役士兵";
				}else{
					titleType="退出消防员";
				}
			}
			fileName = "易地安置原因统计表"+date+".xls";
			resource = "/templates/report/易地安置原因统计表模板.xls";
			title1="浙江省"+year+"年由政府安排工作"+titleType+"易地安置原因统计表";
			subdirectory="统计报表/"+date+"/";
		}
		
		if(expType.equals("28")){//符合条件退役人员分类表导出
			Map<String, String> pmap=new HashMap<>();
			pmap.put("year", json.get("year").toString());
			pmap.put("ybId", json.get("ybId").toString());
			List<ArchivesReport> arlist = arMapper.queryReport9(pmap);
			if(arlist.size()>0){
				for (int i = 0; i < arlist.size(); i++) {
					Integer total = arlist.get(i).getHzcount()+arlist.get(i).getNbcount() 
							+arlist.get(i).getWzcount() +arlist.get(i).getJxcount() +arlist.get(i).getHuzcount() 
							+arlist.get(i).getSxcount() +arlist.get(i).getJhcount() +arlist.get(i).getQzcount() 
							+arlist.get(i).getZscount() +arlist.get(i).getTzcount() +arlist.get(i).getLscount() ;
					arlist.get(i).setTotal(total);
				}
				list = arlist;
			}
			fileName = "符合条件退役人员分类表"+date+".xls";
			resource = "/templates/report/符合条件退役人员分类表模板.xls";
			title1="浙江省"+year+"年由政府安排工作退役士兵、退出消防员分类统计表";
			subdirectory="统计报表/"+date+"/";
		}
		
		if(expType.equals("29")){//退役人员历年数量统计表导出
			Map<String, String> pmap=new HashMap<>();
			pmap.put("year", json.get("year").toString());
			pmap.put("personnelType", json.get("personnelType").toString());
			if(!StringUtils.isEmpty(json.get("resetType").toString())){
				pmap.put("resetType", json.get("resetType").toString());
			}else{
				pmap.put("resetType", "3");
			}
			List<ArchivesReport> arlist = arMapper.queryReport10(pmap);
			if(arlist.size()>0){
				for (int i = 0; i < arlist.size(); i++) {
					Integer total = arlist.get(i).getHzcount()+arlist.get(i).getNbcount() 
							+arlist.get(i).getWzcount() +arlist.get(i).getJxcount() +arlist.get(i).getHuzcount() 
							+arlist.get(i).getSxcount() +arlist.get(i).getJhcount() +arlist.get(i).getQzcount() 
							+arlist.get(i).getZscount() +arlist.get(i).getTzcount() +arlist.get(i).getLscount() ;
					arlist.get(i).setTotal(total);
				}
			}
			list = arlist;
			
			String titleType="退役士兵、退出消防员";
			if(!StringUtils.isEmpty(json.get("personnelType").toString())){
				if("1".equals(json.get("personnelType").toString())){
					titleType="退役士兵";
				}else{
					titleType="退出消防员";
				}
			}
			fileName = "浙江省历年退役人员(退出消防员)数量汇总表"+date+".xls";
			resource = "/templates/report/退役人员历年数量统计表模板.xls";
			title1="浙江省历年"+titleType+"数量汇总表";
			subdirectory="统计报表/"+date+"/";
		}
		
		if(expType.equals("30")){//档案审核通过数据基础信息导出
			String areaId=request.getHeader("areaId");
			String regionalCode =json.get("regionalCode").toString();
			
			Map<String, String> pmap=new HashMap<>();
			pmap.put("ybId", json.get("ybId").toString());
			pmap.put("status", json.get("status").toString());
			pmap.put("personnelType", json.get("personnelType").toString());
			//判断用户归属地 330000为省级用户
			if(!areaId.equals("330000")){
				if(areaId.endsWith("00")){//行政区划编码00结尾的 为地市用户
					if(StringUtils.isEmpty(regionalCode)){
						pmap.put("regionalCode", areaId.substring(0,4));
					}else{
						if(regionalCode.endsWith("00")){
							pmap.put("regionalCode", areaId.substring(0,4));
						}else{
							pmap.put("regionalCode", regionalCode);
						}	
					}
				}else{
					pmap.put("regionalCode", areaId);
				}
			}else{
				if(!StringUtils.isEmpty(regionalCode)){
					if(!regionalCode.equals("330000")){
						if(regionalCode.endsWith("00")){
							pmap.put("regionalCode", regionalCode.substring(0,4));
						}else{
							pmap.put("regionalCode", regionalCode);
						}
					}
				}
			}
			String titleType="退役士兵、退出消防员";
			if(!StringUtils.isEmpty(json.get("personnelType").toString())){
				if("1".equals(json.get("personnelType").toString())){
					titleType="退役士兵";
				}else{
					titleType="退出消防员";
				}
			}
			
			String regionalName="浙江省";
			SysRegional sr = new SysRegional();
			if(!StringUtils.isEmpty(pmap.get("regionalCode"))){
				if(pmap.get("regionalCode").length()==4){
					sr.setRegionalCode(pmap.get("regionalCode")+"00");
				}else{
					sr.setRegionalCode(pmap.get("regionalCode"));
				}
				List<SysRegional> srList = srsMapper.selectAll(sr);
				regionalName = srList.get(0).getRegionalName();
				
				list = arMapper.queryReport11(pmap);
				fileName = regionalName+year+"年退役军人人员名册.xls";
				resource = "/templates/report/部平台基础数据导出模板.xls";
				title1="浙江省"+year+"年"+titleType+"人员名册";
				title2= regionalName+"："+list.size()+"人";
				subdirectory="统计报表/"+date+"/";
			}else{
				sr = new SysRegional();
				sr.setParentId(330000);
				List<SysRegional> srList = srsMapper.selectAll(sr);
				List<ReportVo> newlist  = new ArrayList<>();
				int total=0;
				for (int i = 0; i < srList.size(); i++) {
					regionalName=srList.get(i).getRegionalName();
					pmap.put("regionalCode", srList.get(i).getRegionalCode().substring(0,4));
					List<SoldierBasicInfo> sbilist = arMapper.queryReport11(pmap);
					
					fileName = "浙江省"+year+"年退役军人人员名册.xls";
					resource = "/templates/report/退役军人人员名册导出模板.xls";
					title1="浙江省"+year+"年"+titleType+"人员名册";
					title2= regionalName+"："+sbilist.size()+"人";
					subdirectory="统计报表/"+date+"/";
					
					ReportVo rv = new ReportVo();
					rv.setNum(sbilist.size());
					rv.setParam2(srList.get(i).getRegionalName()+": "+sbilist.size()+"人");
					rv.setList(sbilist);
					newlist.add(rv);
					total+=sbilist.size();
				}
				beans.put("param1", "浙江省: "+total+"人");
				list = newlist;
			}
		}
		
		if(expType.equals("31")){//档案审核通过数据基础信息导出
			
			Map<String, String> pmap=new HashMap<>();
			pmap.put("ybId", json.get("ybId").toString());
			pmap.put("status", json.get("status").toString());
			pmap.put("personnelType", json.get("personnelType").toString());
			
			String titleType="退役士兵、退出消防员";
			if(!StringUtils.isEmpty(json.get("personnelType").toString())){
				if("1".equals(json.get("personnelType").toString())){
					titleType="退役士兵";
				}else{
					titleType="退出消防员";
				}
			}
			
			SysRegional sr = new SysRegional();
			sr.setParentId(330000);
			List<SysRegional> srList = srsMapper.selectAll(sr);
			List<String> fileList = new ArrayList<>();
			for (int i = 0; i < srList.size(); i++) {
				String regionalName=srList.get(i).getRegionalName();
				pmap.put("regionalCode", srList.get(i).getRegionalCode().substring(0,4));
				list = arMapper.queryReport11(pmap);
				
				fileName = regionalName+year+"年退役军人人员名册.xls";
				resource = "/templates/report/部平台基础数据导出模板.xls";
				title1=regionalName+year+"年"+titleType+"人员名册";
				title2= regionalName+"："+list.size()+"人";
				subdirectory="统计报表/"+date+"/";
				
				beans.put("datalist", list);
				beans.put("title1", title1);
				beans.put("title2", title2);
				String path = rootPath+subdirectory;
				
				String result=this.createExcel(beans, path, subdirectory, fileName, resource);
				if(!StringUtils.isEmpty(result)){
					fileList.add(result);
				}
			}
			if(fileList.size()>0){
				//根据生成的word接收安置通知书，达成zip压缩包
				long start = System.currentTimeMillis();
				String path="统计报表/退役军人人员名册zip/";
				FileUtils.createFilePath(rootPath+path);
				
				String zipFile = path+"浙江省"+year+"年"+titleType+"人员名册.zip";
				WordUtils.fileToZip(fileList,zipFile ,rootPath);
				
				long end = System.currentTimeMillis();
				System.out.println("压缩完成，耗时：" + (end - start) + " ms");
				resultVo.setStatusCode(200);
				resultVo.setMessage("成功下载浙江省"+year+"年"+titleType+"役军人人员名册");
				resultVo.setList("\"file/"+zipFile+"\"");
			}else{
				resultVo.setStatusCode(-1);
				resultVo.setMessage("未查询到可开具接收安置通知书的数据");
			}
			return resultVo.toString();
		}
		
//		if(expType.equals("32")){//浙江省退役军人人员名册导出，所有地市数据同一
//			
//			Map<String, String> pmap=new HashMap<>();
//			pmap.put("ybId", json.get("ybId").toString());
//			pmap.put("status", json.get("status").toString());
//			pmap.put("personnelType", json.get("personnelType").toString());
//			
//			SysRegional sr = new SysRegional();
//			sr.setParentId(330000);
//			List<SysRegional> srList = srsMapper.selectAll(sr);
//			List<ReportVo> newlist  = new ArrayList<>();
//			int total=0;
//			for (int i = 0; i < srList.size(); i++) {
//				String regionalName=srList.get(i).getRegionalName();
//				pmap.put("regionalCode", srList.get(i).getRegionalCode().substring(0,4));
//				List<SoldierBasicInfo> sbilist = arMapper.queryReport11(pmap);
//				
//				fileName = "浙江省"+year+"年退役军人人员名册.xls";
//				resource = "/templates/report/退役军人人员名册导出模板.xls";
//				title1="浙江省"+year+"年退役军人人员名册";
//				title2= regionalName+"："+sbilist.size()+"人";
//				subdirectory="统计报表/"+date+"/";
//				
//				ReportVo rv = new ReportVo();
//				rv.setNum(sbilist.size());
//				rv.setParam2(srList.get(i).getRegionalName()+": "+sbilist.size()+"人");
//				rv.setList(sbilist);
//				newlist.add(rv);
//				total+=sbilist.size();
//			}
//			beans.put("param1", "浙江省: "+total+"人");
//			list = newlist;
//		}
		
		if(expType.equals("33")){//根据大单位统计各地市人数导出
			Map<String, String> pmap=new HashMap<>();
			pmap.put("year", json.get("year").toString());
			pmap.put("ybId", json.get("ybId").toString());
			pmap.put("personnelType", json.get("personnelType").toString());
			List<ArchivesReport> arlist = arMapper.queryReport12(pmap);
			if(arlist.size()>0){
				for (int i = 0; i < arlist.size(); i++) {
					if(arlist.get(i).getHzcount()<1 && arlist.get(i).getWzcount()<1 && arlist.get(i).getNbcount()<1 
							&& arlist.get(i).getJxcount()<1 && arlist.get(i).getHuzcount()<1 && arlist.get(i).getSxcount()<1 &&
							 arlist.get(i).getJhcount()<1 && arlist.get(i).getQzcount()<1 && arlist.get(i).getZscount()<1 &&
							 arlist.get(i).getTzcount()<1 && arlist.get(i).getLscount()<1 ){
						arlist.remove(i);
						i--;
					}else{
						Integer total = arlist.get(i).getHzcount()+arlist.get(i).getNbcount() 
								+arlist.get(i).getWzcount() +arlist.get(i).getJxcount() +arlist.get(i).getHuzcount() 
								+arlist.get(i).getSxcount() +arlist.get(i).getJhcount() +arlist.get(i).getQzcount() 
								+arlist.get(i).getZscount() +arlist.get(i).getTzcount() +arlist.get(i).getLscount() ;
						arlist.get(i).setTotal(total);
					}
					
				}
				list = arlist;
			}
			String titleType="退役士兵、退出消防员";
			if(!StringUtils.isEmpty(json.get("personnelType").toString())){
				if("1".equals(json.get("personnelType").toString())){
					titleType="退役士兵";
				}else{
					titleType="退出消防员";
				}
			}
			
			fileName = "符合条件退役人员大单位统计表"+date+".xls";
			resource = "/templates/report/符合条件退役人员大单位统计表模板.xls";
			title1="浙江省"+year+"年由政府安排工作"+titleType+"大单位统计表";
			subdirectory="统计报表/"+date+"/";
		}
		if(expType.equals("34")){//生成测试数据用
			int flag = 1;
			List<SoldierBasicInfo> listaa = new ArrayList<>();
			for (int i = 0; i < 3000; i++) {
				SoldierBasicInfo vo = new SoldierBasicInfo();
				int num=i+1;
				vo.setRowno(i+1);
				vo.setName("姓名"+num);
				
				vo.setIdcard(test2(flag)+String.format("%04d", num));
				if(num%2==0){
					vo.setSex("女");
					vo.setMarriageStatus("未婚");
				}else{
					vo.setSex("男");
					vo.setMarriageStatus("已婚");
				}
				vo.setBigUnitName(test3(num+""));
				vo.setSoldiersMilitaryRank("上士");
				vo.setBirthday("1986.01");
				vo.setEnlistedDate("2006.12");
				vo.setDomicile(test4(flag));
				vo.setResetPlace(test4(flag));
				vo.setPhone("1861"+String.format("%07d", num));
				vo.setResetCategory("服现役满12年（含）以上的士官");
				listaa.add(vo);
				flag++;
				if(flag==12){
					flag=1;
				}
			}
			list=listaa;
			fileName = "部平台基础数据"+date+".xls";
			resource = "/templates/error/部平台基础数据导入失败模板.xls";
			title1="浙江省2020年由政府安排工作人员信息名称";
			subdirectory="统计报表/"+date+"/";
		}
		
		if(expType.equals("35")){//自主就业人员综合信息表导出
			SoldierBasicInfoOse pmap = new SoldierBasicInfoOse();
			pmap.setYear(json.get("year").toString());
			pmap.setPersonnelType(json.get("personnelType").toString());
			
			String regionalCode=json.get("regionalCode").toString();
			if(!regionalCode.equals("330000")){
				if(regionalCode.endsWith("00")){//机构编号00结尾的 为地市用户
					pmap.setRegionalCode(regionalCode.substring(0,4));
				}else{
					pmap.setRegionalCode(regionalCode);
				}
			}
			//查询所有参数配置表所有参数信息
			List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
			List<SysParamInfo> rlist= null;
			
			//一次查询出所有行政区划数据
			List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
			List<SysRegional> regional= null;
			
			SysRegional area = new SysRegional();
			area.setParentId(330000);
			List<SysRegional> arealist= srsMapper.selectAll(area);
			
			List<SoldierBasicInfoOse> list5 = sbioMapper.selectAll(pmap);
			for (int i = 0; i < list5.size(); i++) {
				if(!StringUtils.isEmpty(list5.get(i).getBigUnitName())){
					rlist=  ParamUtils.checkList(plist, "bigUnitName", list5.get(i).getBigUnitName() , "");
					if(rlist.size()>0){
						list5.get(i).setBigUnitName(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getSex())){
					rlist=  ParamUtils.checkList(plist, "sex", list5.get(i).getSex() , "");
					if(rlist.size()>0){
						list5.get(i).setSex(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getNation())){
					rlist=  ParamUtils.checkList(plist, "nation", list5.get(i).getNation() , "");
					if(rlist.size()>0){
						list5.get(i).setNation(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getSoldiersMilitaryRank())){
					rlist=  ParamUtils.checkList(plist, "soldMilitRankCode", list5.get(i).getSoldiersMilitaryRank() , "");
					if(rlist.size()>0){
						list5.get(i).setSoldiersMilitaryRank(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getRelocateReasonCode())){
					rlist=  ParamUtils.checkList(plist, "relocateReasonCode", list5.get(i).getRelocateReasonCode() , "");
					if(rlist.size()>0){
						list5.get(i).setRelocateReasonCode(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getMarriageStatus())){
					rlist=  ParamUtils.checkList(plist, "marriageStatus", list5.get(i).getMarriageStatus() , "");
					if(rlist.size()>0){
						list5.get(i).setMarriageStatus(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getPoliAffiCode())){
					rlist=  ParamUtils.checkList(plist, "poliAffiCode", list5.get(i).getPoliAffiCode() , "");
					if(rlist.size()>0){
						list5.get(i).setPoliAffiCode(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getEduLevCode())){
					rlist=  ParamUtils.checkList(plist, "eduLevCode", list5.get(i).getEduLevCode() , "");
					if(rlist.size()>0){
						list5.get(i).setEduLevCode(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getDisabilityCategoriesCode())){
					rlist=  ParamUtils.checkList(plist, "disabilityCategoriesCode", list5.get(i).getDisabilityCategoriesCode() , "");
					if(rlist.size()>0){
						list5.get(i).setDisabilityCategoriesCode(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getDisableGradeCode())){
					rlist=  ParamUtils.checkList(plist, "disableGradeCode", list5.get(i).getDisableGradeCode() , "");
					if(rlist.size()>0){
						list5.get(i).setDisableGradeCode(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getIsHardArea())){
					rlist=  ParamUtils.checkList(plist, "isOrNo", list5.get(i).getIsHardArea() , "");
					if(rlist.size()>0){
						list5.get(i).setIsHardArea(rlist.get(0).getParamValue());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getDomicile())){
					regional=ParamUtils.getRegional(srList, "", list5.get(i).getDomicile());
					if(regional.size()>0){
						list5.get(i).setDomicile(regional.get(0).getRegionalName());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getResetPlace())){
					regional=ParamUtils.getRegional(srList, "", list5.get(i).getResetPlace());
					if(regional.size()>0){
						list5.get(i).setResetPlace(regional.get(0).getRegionalName());
					}
				}
				if(!StringUtils.isEmpty(list5.get(i).getRegionalCode())){
					regional=ParamUtils.getRegional(arealist, "", list5.get(i).getRegionalCode().substring(0,4)+"00");
					if(regional.size()>0){
						list5.get(i).setRegionalName(regional.get(0).getRegionalName());
					}
				}
			}
			list = list5;
			
			String titleType="退役士兵、退出消防员";
			if(!StringUtils.isEmpty(json.get("personnelType").toString())){
				if("1".equals(json.get("personnelType").toString())){
					titleType="退役士兵";
				}else{
					titleType="退出消防员";
				}
			}
			
			fileName = "人员综合信息表-自主就业"+date+".xls";
//			resource = "/templates/report/档案综合信息表模板.xls";
			resource = "/templates/report/自主就业退役士兵信息导出模板.xls";
			title1="浙江省"+year+"年自主就业"+titleType+"综合信息表";
			title2="总人数："+list.size()+" 人";
			subdirectory="统计报表/"+date+"/";
		}
		
		if(expType.equals("99")){//单机版档案待审核数据导出
			String regionalCode =json.get("regionalCode").toString();
			String titlename="浙江省";
			SoldierBasicInfoStatic record = new SoldierBasicInfoStatic();
			record.setYbId(Integer.parseInt(json.get("ybId").toString()));
			if(!StringUtils.isEmpty(json.get("status").toString())){
				record.setStatus(json.get("status").toString());
			}
			
			SysRegional sr = new SysRegional();
			if(!StringUtils.isEmpty(regionalCode)){
				if(!regionalCode.equals("330000")){
					if(regionalCode.endsWith("00")){//机构编号00结尾的 为地市用户
						record.setRegionalCode(regionalCode.substring(0,4));
					}else{
						record.setRegionalCode(regionalCode);
					}
					sr.setRegionalCode(regionalCode.substring(0,4)+"00");
					List<SysRegional> srList = srsMapper.selectAll(sr);
					titlename=srList.get(0).getRegionalName();
				}else{
					titlename="浙江省";
				}
			}
			List<SoldierBasicInfoStatic> sbilist = sbiMapper.queryArchivesReport(record);
			
			for (int i = 0; i < sbilist.size(); i++) {
				//档案审核结果
				AuditLog al = new AuditLog();
				al.setSbiId(sbilist.get(i).getSbiId());
				List<AuditLog> alList = logMapper.selectAll(al);
				if(alList.size()>0){
					sbilist.get(i).setOperator(alList.get(0).getOperator());
//					sbilist.get(i).setRemark(alList.get(0).getRemark());
					//判断状态是否属于退档,退档数据需查询审核日志中的退档原因
					sbilist.get(i).setReturnRemark(alList.get(0).getReturnRemark());
				}	
			}
			list = sbilist;
//			list = sbiMapper.queryAuditData(record);
			
			fileName = titlename+"退役士兵待审核档案.xls";
			resource = "/templates/archives/档案审核汇总表模板.xls";
			title1="浙江省"+year+"年由政府安排工作退役士兵、退出消防员档案审核汇总表";
			subdirectory="档案待审核数据/"+date+"/";
		}

		if(expType.equals("98")){//单机版档案审核结果数据导出
			String regionalCode =json.get("regionalCode").toString();
			
			SoldierBasicInfoStatic record = new SoldierBasicInfoStatic();
			record.setYbId(Integer.parseInt(json.get("ybId").toString()));
			if(!StringUtils.isEmpty(json.get("status").toString())){
				record.setStatus(json.get("status").toString());
			}
			
			String areaId=request.getHeader("areaId");
			if(!areaId.equals("330000")){
				if(areaId.endsWith("00")){//行政区划编码00结尾的 为地市用户
					if(StringUtils.isEmpty(regionalCode)){
						record.setRegionalCode(areaId.substring(0,4));
						regionalCode = areaId;
					}else{
						if(regionalCode.endsWith("00")){
							record.setRegionalCode(areaId.substring(0,4));
						}else{
							record.setRegionalCode(regionalCode);
						}	
					}
				}else{
					record.setRegionalCode(areaId);
				}
			}else{
				if(!StringUtils.isEmpty(regionalCode)){
					if(!regionalCode.equals("330000")){
						if(regionalCode.endsWith("00")){
							record.setRegionalCode(regionalCode.substring(0,4));
						}else{
							record.setRegionalCode(regionalCode);
						}
					}else{
						record.setRegionalCode("");
					}
				}
			}
			
//			record.setRegionalCode(regionalCode.substring(0,4));
			record.setFlag(2);//设置查询审核过的数据
			
			List<SoldierBasicInfoStatic> sbilist = null;
			
			String sbiIdList = json.get("sbiIdList").toString();
			if(!StringUtils.isEmpty(sbiIdList)){
				String[] idArr = sbiIdList.split("\\,");
				if(idArr.length>0){
					sbilist= new ArrayList<>();
					for (int i = 0; i < idArr.length; i++) {
						SoldierBasicInfoStatic newpo = new SoldierBasicInfoStatic();
						newpo.setSbiId(Integer.parseInt(idArr[i]));
						List<SoldierBasicInfoStatic> sbivo = sbiMapper.queryArchivesReport(newpo);
						sbilist.add(sbivo.get(0));
					}
				}
			}else{
				sbilist = sbiMapper.queryArchivesReport(record);
			}
			
			for (int i = 0; i < sbilist.size(); i++) {
				//档案审核结果
				AuditLog al = new AuditLog();
				al.setSbiId(sbilist.get(i).getSbiId());
				List<AuditLog> alList = logMapper.selectAll(al);
				if(alList.size()>0){
					sbilist.get(i).setOperator(alList.get(0).getOperator());
//					sbilist.get(i).setRemark(alList.get(0).getRemark());
					//判断状态是否属于退档,退档数据需查询审核日志中的退档原因
					sbilist.get(i).setReturnRemark(alList.get(0).getReturnRemark());
				}	
			}
			list = sbilist;
			SysRegional sr = new SysRegional();
			sr.setRegionalCode(regionalCode);
			List<SysRegional> srList = srsMapper.selectAll(sr);
			fileName = srList.get(0).getRegionalName()+"-档案审核结果汇总数据.xls";
			resource = "/templates/archives/档案审核汇总表模板.xls";
			title1="浙江省"+year+"年由政府安排工作退役士兵、退出消防员档案审核汇总表";
			subdirectory="档案审核结果数据/"+date+"/";
		}
		if(expType.equals("100")){
			HashMap<String, String> pmap=new HashMap<>();
			pmap.put("year", json.get("year").toString());
			pmap.put("ybId", json.get("ybId").toString());
			pmap.put("personnelType", json.get("personnelType").toString());
			List<SoldierBasicInfo> soliderList = arMapper.queryReport15(pmap);//退档详情
			Map<String, ArchivesReport> stringListMap = arMapper.queryReport14(pmap);//接收安置统计
			Map<String,Object>newBean=arMapper.getAllCity(stringListMap,beans);
			beans=newBean;
			beans.put("total",stringListMap.get("totalReport"));
			list=soliderList;
			fileName = "各县市区人员接收安置情况统计表.xls";
			resource = "/templates/archives/各县市区人员接收安置情况统计表.xls";
			title1="浙江省"+year+"转业士官接收安置情况统计表";
			title2="浙江省"+year+"转业士官接收安置退档详情统计表";
			subdirectory="统计报表/"+date+"/";

		}
		beans.put("datalist", list);
		beans.put("title1", title1);
		beans.put("title2", title2);
		String path = rootPath+subdirectory;
		
		String result=this.createExcel(beans, path, subdirectory, fileName, resource);
		if(!StringUtils.isEmpty(result)){
			resultVo.setStatusCode(200);
			resultVo.setMessage("导出excel成功");
			resultVo.setList("\"file/"+result+"\"");
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导出excel失败");
		}
		return resultVo.toString();
		
//		Map<String, Object> beans = new HashMap<String, Object>();
//		try {
//			XLSTransformer transformer = new XLSTransformer();
//			InputStream is = this.getClass().getResourceAsStream(resource);
//			beans.put("datalist", list);
//			beans.put("title1", title1);
//			beans.put("title2", title2);
//			Workbook workbook = transformer.transformXLS(is, beans);
//			workbook.setForceFormulaRecalculation(true);
//			is.close();
//			
//			
//			String path = rootPath+subdirectory;
////			fileName = date+fileName;
//			boolean flag = ExcelUtils.createExcelFile(path,fileName, workbook);
//			if(flag){
//				resultVo.setStatusCode(200);
//				resultVo.setMessage("导出excel成功");
//				resultVo.setList("\"file/"+subdirectory+fileName+"\"");
//			}else{
//				resultVo.setStatusCode(-1);
//				resultVo.setMessage("导出excel失败");
//			}
////			ExcelUtils.responseDownloadWorkbook(request1, response, fileName, workbook);
//		} catch (Exception e) {
//			e.printStackTrace();
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("导出excel出现异常");
//		}
//		return resultVo.toString();
	}
	/**
	 * 生成Excel报表文件
	 * @param beans 存放生成excel数据
	 * @param path 存放文件根目录
	 * @param subdirectory 存放文件夹名称
	 * @param fileName 生成的文件名
	 * @param resource 模板文件
	 * @return
	 */
	public String createExcel(Map<String, Object> beans,String path,String subdirectory,String fileName,String resource){
		String result="";
		try {
			XLSTransformer transformer = new XLSTransformer();
			InputStream is = this.getClass().getResourceAsStream(resource);
			Workbook workbook = transformer.transformXLS(is, beans);
			workbook.setForceFormulaRecalculation(true);
			is.close();
			boolean flag = ExcelUtils.createExcelFile(path,fileName, workbook);
			
			if(flag){
				result=subdirectory+fileName;
//				resultVo.setStatusCode(200);
//				resultVo.setMessage("导出excel成功");
//				resultVo.setList("\"file/"+subdirectory+fileName+"\"");
			}else{
//				resultVo.setStatusCode(-1);
//				resultVo.setMessage("导出excel失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("导出excel出现异常");
		}
		return result;
	}

	
	@RequestMapping(value = "insertRegioanl", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String insertRegioanl() {
		File file = new File("E:\\epsoft\\退役军人\\二期\\test.txt");
        String str = ReadTest.txt2String(file);
        
        JSONArray array = JSONArray.fromObject(str);
        for (int i = 0; i < array.size(); i++) {
        	net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(array.get(i));
			String regionalcode = jsonObject.get("value").toString();
			String regionalname = jsonObject.get("label").toString();
			
			SysRegional sr = new SysRegional();
			sr.setRegionalCode(regionalcode);
			sr.setRegionalName(regionalname);
			sr.setStatus(1);
			sr.setCreateTime(new Date());
			sr.setModifyTime(new Date());
			sr.setParentId(0);
			srsMapper.insert(sr);
			if(!jsonObject.get("children").toString().equals("[]")){
				JSONArray array2 = JSONArray.fromObject(jsonObject.get("children").toString());
				for (int j = 0; j < array2.size(); j++) {
					net.sf.json.JSONObject json2 = net.sf.json.JSONObject.fromObject(array2.get(j));
					String regionalcode2 = json2.get("value").toString();
					
					sr = new SysRegional();
					sr.setRegionalCode(json2.get("value").toString());
					sr.setRegionalName(json2.get("label").toString());
					sr.setStatus(1);
					sr.setCreateTime(new Date());
					sr.setModifyTime(new Date());
					sr.setParentId(Integer.parseInt(regionalcode));
					srsMapper.insert(sr);
					if(!json2.get("children").toString().equals("[]")){
						JSONArray array3 = JSONArray.fromObject(json2.get("children").toString());
						for (int k = 0; k < array3.size(); k++) {
							net.sf.json.JSONObject json3 = net.sf.json.JSONObject.fromObject(array3.get(k));
							
							sr = new SysRegional();
							sr.setRegionalCode(json3.get("value").toString());
							sr.setRegionalName(json3.get("label").toString());
							sr.setStatus(1);
							sr.setCreateTime(new Date());
							sr.setModifyTime(new Date());
							sr.setParentId(Integer.parseInt(regionalcode2));
							srsMapper.insert(sr);
						}
					}
				}
			}
		}
		return "test";
	}
	public static void main(String[] args) {
		FileToolsController f = new FileToolsController();
		f.test();
	}
	public  void test(){
		try {
			int flag = 1;
			List<SoldierBasicInfo> list = new ArrayList<>();
			for (int i = 0; i < 10000; i++) {
				SoldierBasicInfo vo = new SoldierBasicInfo();
				int num=i+1;
				vo.setRowno(i+1);
				vo.setName("姓名"+num);
				
				vo.setIdcard(test2(flag)+String.format("%04d", num));
				if(num%2==0){
					vo.setSex("女");
					vo.setMarriageStatus("未婚");
				}else{
					vo.setSex("男");
					vo.setMarriageStatus("已婚");
				}
				vo.setBigUnitName(test3(num+""));
				vo.setSoldiersMilitaryRank("上士");
				vo.setBirthday("1986.01");
				vo.setEnlistedDate("2006.12");
				vo.setDomicile(test4(flag));
				vo.setResetPlace(test4(flag));
				vo.setPhone("1861"+String.format("%07d", num));
				vo.setResetCategory("服现役满12年（含）以上的士官");
				list.add(vo);
				flag++;
				if(flag==12){
					flag=1;
				}
			}
			
			Map<String, Object> beans = new HashMap<String, Object>();
			
			XLSTransformer transformer = new XLSTransformer();
			InputStream is = this.getClass().getResourceAsStream("/templates/error/部平台基础数据导入失败模板.xls");
			Workbook workbook = transformer.transformXLS(is, beans);
			workbook.setForceFormulaRecalculation(true);
			is.close();
			String path ="D:\\epsoft\\省级军转\\二期\\测试数据\\";
			ExcelUtils.createExcelFile(path,"2020部平台基础数据.xls", workbook);
		} catch (ParsePropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String test2(int num){
		String idcard ="";
		if(num==1){
			idcard="33010219860101";
		}
		if(num==2){
			idcard="33020219860101";
		}
		if(num==3){
			idcard="33030219860101";
		}
		if(num==4){
			idcard="33040219860101";
		}
		if(num==5){
			idcard="33050219860101";
		}
		if(num==6){
			idcard="33060219860101";
		}
		if(num==7){
			idcard="33070219860101";
		}
		if(num==8){
			idcard="33080219860101";
		}
		if(num==9){
			idcard="33090219860101";
		}
		if(num==10){
			idcard="33100219860101";
		}
		if(num==11){
			idcard="33110219860101";
		}
		return idcard;
	}
	public static String test3(String num){
		String ddw ="";
		if(num.endsWith("1")||num.endsWith("2")){
			ddw="东部战区";
		}
		if(num.endsWith("3")||num.endsWith("4")){
			ddw="南部战区";
		}
		if(num.endsWith("5")||num.endsWith("6")){
			ddw="西部战区";
		}
		if(num.endsWith("7")||num.endsWith("8")){
			ddw="北部战区";
		}
		if(num.endsWith("9")||num.endsWith("0")){
			ddw="公安现役";
		}
		return ddw;
	}
	public static String test4(int num){
		String idcard ="";
		if(num==1){
			idcard="浙江省杭州市上城区";
		}
		if(num==2){
			idcard="浙江省宁波市海曙区";
		}
		if(num==3){
			idcard="浙江省温州市鹿城区";
		}
		if(num==4){
			idcard="浙江省嘉兴市南湖区";
		}
		if(num==5){
			idcard="浙江省湖州市吴兴区";
		}
		if(num==6){
			idcard="浙江省绍兴市越城区";
		}
		if(num==7){
			idcard="浙江省金华市婺城区";
		}
		if(num==8){
			idcard="浙江省衢州市柯城区";
		}
		if(num==9){
			idcard="浙江省舟山市定海区";
		}
		if(num==10){
			idcard="浙江省台州市椒江区";
		}
		if(num==11){
			idcard="浙江省丽水市莲都区";
		}
		return idcard;
	}

}
