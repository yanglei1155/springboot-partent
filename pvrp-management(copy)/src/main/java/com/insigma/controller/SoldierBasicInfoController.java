package com.insigma.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.insigma.po.ResultVo;
import com.insigma.po.SysParamInfo;
import com.insigma.po.soldier.BatchManagement;
import com.insigma.po.soldier.PersonnelArchivesLog;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SoldierBasicInfoError;
import com.insigma.po.soldier.SoldierBasicInfoOse;
import com.insigma.po.soldier.SoldierResetInfo;
import com.insigma.po.soldier.SysRegional;
import com.insigma.po.soldier.TreeNodesParam;
import com.insigma.service.BatchManagementService;
import com.insigma.service.PersonnelArchivesLogService;
import com.insigma.service.SoldierBasicInfoOseService;
import com.insigma.service.SoldierBasicInfoService;
import com.insigma.service.SoldierResetInfoService;
import com.insigma.service.SysParamInfoService;
import com.insigma.service.SysRegionalService;
import com.insigma.util.CharUtils;
import com.insigma.util.CheckIdCard;
import com.insigma.util.DateUtils;
import com.insigma.util.ExcelUtils;
import com.insigma.util.ParamUtils;
import com.insigma.util.ValidatorUtils;

import net.minidev.json.JSONObject;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

/**
 *       
 * 类描述： 退役士兵数据校验  
 * 创建人：liuwm   
 * 创建时间：2020年3月3日 上午8:58:15   
 * @version
 */
@RestController
@RequestMapping("/pvrpm/check")
public class SoldierBasicInfoController {
	
	@Autowired
    HttpServletRequest request;
	
	@Autowired
	private SoldierBasicInfoService mapper;
	
	@Autowired
	private SysRegionalService srsMapper;
	
	public static String dateTime = "yyyy-MM-dd HH:mm:ss";
	
	@Autowired
	private SysParamInfoService paramMapper;
	
	@Autowired
	private BatchManagementService ybMapper;
	
	@Autowired
	private PersonnelArchivesLogService palMapper;
	
	@Autowired
	private SoldierResetInfoService sriMapper;
	
	@Autowired
	private BatchManagementService bmMapper;
	
	@Autowired
	private SoldierBasicInfoOseService sbioMapper;
	
	
	@Value("${rootpath}")
    private String rootPath;
	
	private String context="基础数据管理->";
	
	/**
	 * 保存导入数据
	 * @param filePath 上传的excel
	 * @param ybId 批次ID
	 * @return
	 */
	@RequestMapping(value = "impSoldierData", method = RequestMethod.GET,produces="application/json; charset=utf-8")
	public String impSoldierData(@RequestParam("filePath") MultipartFile filePath,String year,String batchName){
		ResultVo resultVo = new ResultVo();
		
		String displayName=CharUtils.getDisplayName(request);
		if(StringUtils.isEmpty(year)){
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败:年度不能为空");
			return resultVo.toString();
		}
		if(StringUtils.isEmpty(batchName)){
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败:所属批次不能为空");
			return resultVo.toString();
		}
		
		ExcelUtils uet = new ExcelUtils();
		List<String[][]> list  = uet.getExcelData(filePath,2);
		if (list == null || list.isEmpty()) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败：数据为空");
			return resultVo.toString();
		}
		
		//获取第一个sheet
		String[][] Sheets = list.get(0);
		
		String[] columnName = {"序号", "姓名", "公民身份证号", "性别","大单位","性别","警衔","出生年月",
				"婚姻状况","入伍前户籍所在地","拟安置地","联系电话","安置类别"};

		//判断导入的excel数据表头是否与定义的模板列名一致，不一致提示错误字段
		if(Sheets[0].length!=columnName.length){
			String result=ExcelUtils.verificationStudentExcelHeadLine(Sheets[0], columnName);												
			if(!StringUtils.isEmpty(result)){
				resultVo.setStatusCode(-1);
				resultVo.setMessage("导入失败：表头列名【"+result.substring(0,result.length()-1)+"】与模板列名不一致");
				return resultVo.toString();
			}
		}
		
		int successCount=0;//成功数量
		int errorCount=0;//失败数量
		int number = 0;//第几条数据
		
		//一次查询出所有参数数据
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		List<SysParamInfo> rlist= null;
		
		//一次查询出所有行政区划数据
		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
		List<SysRegional> regional= null;
		
		//存储日志vo
		List<PersonnelArchivesLog> logList = new ArrayList<>();
		
		//批量新增士兵信息
		List<SoldierBasicInfo> insertList = new ArrayList<>();
		
		//批量更新士兵信息
		List<SoldierBasicInfo> updateList = new ArrayList<>();
		
		List<SoldierBasicInfo> errList = new ArrayList<>();
		
		String date = DateUtils.getStringCurrentDate();
			
		List<SoldierBasicInfo> tmpList = new ArrayList<>();
			
		BatchManagement bm = new BatchManagement();
		bm.setOperator(displayName);
		bm.setYear(Integer.parseInt(year));
		bm.setBatchName(year+"年"+batchName);
		bm.setReceivingFilePath("部平台基础信息");
		List<BatchManagement> bmlist = bmMapper.selectAll(bm);
		int ybId = 0;
		//判断批次是否已存在，存在则取已有批次ID，不存在则新建批次并获取新的批次ID
		if(bmlist.size()>0){
			ybId=bmlist.get(0).getYbId();
		}else{
			bmMapper.insert(bm);
			ybId=bm.getYbId();
		}
		//根据批次ID 查询批次信息判断数据属于第几批次，设置退伍时间，第一批次数据默认4月1日，第二批次默认9月1日
		BatchManagement ybvo = ybMapper.selectByPrimaryKey(ybId);
		String retiredDate=ybvo.getYear()+".4.1";
		if(ybvo.getBatchName().indexOf("第二批")!=-1){
			retiredDate=ybvo.getYear()+".9.1";
		}
		int rowcount=0;
		for (String[] rows : Sheets) { // 遍历行
			if (rows != null && rows.length > 1 && rowcount>0) {
				if("".equals(rows[1])&&"".equals(rows[2])&&"".equals(rows[3])&&"".equals(rows[4])){
					break;
				}
				SoldierBasicInfo tmpvo = new SoldierBasicInfo();
				tmpvo.setRowno(Integer.parseInt(rows[0]));
				tmpvo.setName(CharUtils.replaceAllNull(rows[1]));
				tmpvo.setIdcard(CharUtils.replaceAllNull(rows[2]));
				tmpvo.setSex(CharUtils.replaceAllNull(rows[3]));
				tmpvo.setBigUnitName(CharUtils.replaceAllNull(rows[4]));
				tmpvo.setSoldiersMilitaryRank(CharUtils.replaceAllNull(rows[5]));
				tmpvo.setBirthday(CharUtils.replaceAllNull(rows[6]));
				tmpvo.setEnlistedDate(CharUtils.replaceAllNull(rows[7]));
				tmpvo.setMarriageStatus(CharUtils.replaceAllNull(rows[8]));
				tmpvo.setDomicile(CharUtils.replaceAllNull(rows[9]));
				tmpvo.setResetPlace(CharUtils.replaceAllNull(rows[10]));
				tmpvo.setPhone(CharUtils.replaceAllNull(rows[11]));
				tmpvo.setResetCategory(CharUtils.replaceAllNull(rows[12]));
				tmpList.add(tmpvo);
			}
			rowcount++;
		}
		number=tmpList.size();
		for (int i = 0; i < tmpList.size(); i++) {
			System.out.println("---------------"+i);
			String errorMsg="";//失败原因
			boolean flag=true;
			
			SoldierBasicInfo tmpvo=tmpList.get(i);
			//判断excel表中身份证号是否重复
			for (int j = 0; j < tmpList.size(); j++) {
				if(i!=j){
					if (tmpvo.getIdcard().equals(tmpList.get(j).getIdcard())){       
						flag=false;
						errorMsg="该人员身份证号与第【"+tmpList.get(j).getRowno()+"】条数据身份证号重复,";
						continue;
					} 
				}
			}
			
			SoldierBasicInfo vo = new SoldierBasicInfo();
			vo.setYbId(ybId);
			if(StringUtils.isEmpty(tmpvo.getName())){
				flag=false;
				errorMsg="姓名不能为空,";
			}else{
				vo.setName(tmpvo.getName());
			}
			if(StringUtils.isEmpty(tmpvo.getIdcard())){
				flag=false;
				errorMsg="身份证号不能为空,";
			}else{
				if(tmpvo.getIdcard().length()!=18){
					flag=false;
					errorMsg="身份证号不是18位,";
				}else{
					vo.setIdcard(tmpvo.getIdcard());
				}
			}
			
			if(!StringUtils.isEmpty(tmpvo.getSex())){
				rlist=  ParamUtils.checkList(plist, "sex", "", tmpvo.getSex());
				if(rlist.size()>0){
					vo.setSex(rlist.get(0).getParamKey());
				}
			}
			
			if(!StringUtils.isEmpty(tmpvo.getBigUnitName())){
				rlist=  ParamUtils.checkList(plist, "bigUnitName", "", tmpvo.getBigUnitName());
				if(rlist.size()>0){
					vo.setBigUnitName(rlist.get(0).getParamKey());
					int smrTmp=Integer.parseInt(rlist.get(0).getParamKey());
					//设置人员类型 1、退役士兵；2、退出消防兵
					if(smrTmp>=51 && smrTmp<=83){
						vo.setPersonnelType("2");
					}else{
						vo.setPersonnelType("1");
					}
				}
			}
			
			if(!StringUtils.isEmpty(tmpvo.getSoldiersMilitaryRank())){
				rlist=  ParamUtils.checkList(plist, "soldMilitRankCode", "", tmpvo.getSoldiersMilitaryRank());
				if(rlist.size()>0){
					vo.setSoldiersMilitaryRank(rlist.get(0).getParamKey());
//					int smrTmp=Integer.parseInt(rlist.get(0).getParamKey());
//					//设置人员类型 1、退役士兵；2、退出消防兵
//					if(smrTmp>=20 && smrTmp<=23){
//						vo.setPersonnelType("2");
//					}else{
//						vo.setPersonnelType("1");
//					}
				}
			}
			
			if(!StringUtils.isEmpty(tmpvo.getBirthday())){
				vo.setBirthday(tmpvo.getBirthday());
			}
			
			if(!StringUtils.isEmpty(tmpvo.getEnlistedDate())){
				vo.setEnlistedDate(tmpvo.getEnlistedDate());
				
				//根据入伍时间、退伍时间计算出服役总月数
				String enlistedDate =tmpvo.getEnlistedDate().replaceAll("\\.", "-")+"-01";
				String retiredDateTmp=retiredDate.replaceAll("\\.", "-");
				int serviceDuration = DateUtils.getMonthDiff(retiredDateTmp, enlistedDate);
				vo.setServiceDuration(serviceDuration+"个月");
			}
			
			if(!StringUtils.isEmpty(tmpvo.getMarriageStatus())){
				if("离婚".equals(tmpvo.getMarriageStatus())){
					tmpvo.setMarriageStatus("离异");
				}
				rlist=  ParamUtils.checkList(plist, "marriageStatus", "", tmpvo.getMarriageStatus());
				if(rlist.size()>0){
					vo.setMarriageStatus(rlist.get(0).getParamKey());
				}
			}
			
			if(!StringUtils.isEmpty(tmpvo.getDomicile())){
				regional=ParamUtils.getRegional(srList, tmpvo.getDomicile(), "");
				if(regional.size()>0){
					vo.setDomicile(regional.get(0).getRegionalCode());
				}
			}
			
			if(StringUtils.isEmpty(tmpvo.getResetPlace())){
				flag=false;
				errorMsg+="拟安置地不能为空,";
			}else{
				String resetPlace=CharUtils.checkRegional(CharUtils.strReplaceAll(tmpvo.getResetPlace()));
				if("-1".equals(resetPlace)){
					flag=false;
					errorMsg+="拟安置地不是浙江省开头,";
				}else{
					regional=ParamUtils.getRegional(srList, resetPlace, "");
					vo.setResetPlace(regional.get(0).getRegionalCode());
					String[] regionalCode=regional.get(0).getRegionalCode().split("\\,");
					vo.setRegionalCode(regionalCode[2]);
				}
			}
			
			if(!StringUtils.isEmpty(tmpvo.getPhone())){
				vo.setPhone(tmpvo.getPhone());
			}
			
			if(!StringUtils.isEmpty(tmpvo.getResetCategory())){
				rlist=  ParamUtils.checkList(plist, "resetCategory", "", tmpvo.getResetCategory());
				if(rlist.size()>0){
					vo.setResetCategory(rlist.get(0).getParamKey());
				}
			}
			
			if(flag){
				vo.setRetiredDate(retiredDate);//设置退伍时间
				vo.setCheckStatus(0);
				vo.setStatus(0);
				vo.setDataType(1);
				vo.setResetType(1);
				vo.setRegisterStatus(1);
				vo.setNewResetType(1);
				
				//根据身份证号查询数据，判断数据是否已存在
				SoldierBasicInfo sbiVo = mapper.checkSoldierBasicInfo(tmpvo.getIdcard());
				if(sbiVo==null){
					insertList.add(vo);
					successCount++;
				}else{
					//如果数据存在，判断是否属于同一个批次，不是同一批次则提示错误信息
					if(ybId==sbiVo.getYbId()){
						//如果状态为已校验或已审核则不更新数据
						if(sbiVo.getStatus()<1){
							vo.setSbiId(sbiVo.getSbiId());
							updateList.add(vo);
							successCount++;	
							
						}else{
							tmpvo.setRemark("已进入审核环节，不允许重复导入");
							errList.add(tmpvo);
							errorCount++;
						}
					}else{
						tmpvo.setRemark("该人员数据已存在于其他批次中！");
						errList.add(tmpvo);
						errorCount++;
					}
				}
			}else{
				tmpvo.setRemark(errorMsg.substring(0,errorMsg.length()-1));
				errList.add(tmpvo);
				errorCount++;
			}
		}				
			
			
		//批量新增数据
		if(insertList.size()>0){
			mapper.insertList(insertList);
			for (int i = 0; i < insertList.size(); i++) {
				//保存操作日志
				PersonnelArchivesLog log = new PersonnelArchivesLog();
				log.setSbiId(insertList.get(i).getSbiId());
				log.setOperator(displayName);
				log.setContext(context+"部平台数据导入-新增数据");
				logList.add(log);
			}
			//日志list结果大于0则新增保存日志信息
			if(logList.size()>0){
				palMapper.insertList(logList);
			}
			
			//导入数据成功后，修改批次状态为激活
			BatchManagement yb = new BatchManagement();
			yb.setYbId(ybId);
			yb.setStatus(1);
			ybMapper.updateByPrimaryKey(yb);
		}
		//批量更新数据
		if(updateList.size()>0){
			mapper.updateArchivesList(updateList);
			for (int i = 0; i < updateList.size(); i++) {
				//保存操作日志
				PersonnelArchivesLog log = new PersonnelArchivesLog();
				log.setSbiId(updateList.get(i).getSbiId());
				log.setOperator(displayName);
				log.setContext(context+"部平台数据导入-批量更新数据");
				logList.add(log);
			}
			//日志list结果大于0则新增保存日志信息
			if(logList.size()>0){
				palMapper.insertList(logList);
			}
		}
		
		//错误数据大于0则生成错误excel表
		if(errList.size()>0){
			try {
				Map<String, Object> beans = new HashMap<String, Object>();
				XLSTransformer transformer = new XLSTransformer();
				InputStream is = this.getClass().getResourceAsStream("/templates/error/部平台基础数据导入失败模板.xls");
				beans.put("datalist", errList);
				beans.put("title1", "浙江省"+year+"年由政府安排工作退役士兵、退出消防员安置计划名单");
				beans.put("title2", "浙江省："+errList.size()+"人");
				Workbook workbook = transformer.transformXLS(is, beans);
				workbook.setForceFormulaRecalculation(true);
				is.close();
				String fileName =  filePath.getOriginalFilename().substring(0,filePath.getOriginalFilename().length()-4)+"-失败数据.xls";
				String subdirectory = "导入部平台数据失败/"+date+"/";
				String path = rootPath+subdirectory;
				boolean isSuccess = ExcelUtils.createExcelFile(path,fileName, workbook);
				
				if(isSuccess){
					resultVo.setList("\"file/"+subdirectory+fileName+"\"");
				}
			} catch (ParsePropertyException e) {
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			resultVo.setStatusCode(-1);
		}else{
			resultVo.setStatusCode(200);
		}
		resultVo.setMessage("共导入【"+number+"】条数据,成功【"+successCount+"】条,失败【"+errorCount+"】条");
    	return resultVo.toString();
    }
	
	/**
	 * 校验士兵基础数据
	 * @param filePath 上传的excel
	 * @param year	所属年度
	 * @param batchName 所属批次
	 * @param bigUnitName 大单位
	 * @param uploadType 上传类型 1、导入部队数据-基础表 2、导入部队数据-完整表
	 * @return
	 */
	@RequestMapping(value = "checkSoldierData", method = RequestMethod.GET,produces="application/json; charset=utf-8")
	public String checkSoldierData(@RequestParam("filePath") MultipartFile filePath,String year,String batchName,String bigUnitName,int uploadType){
		ResultVo resultVo = new ResultVo();
		String displayName=CharUtils.getDisplayName(request);
		
		if(StringUtils.isEmpty(year)){
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败:年度不能为空");
			return resultVo.toString();
		}
		if(StringUtils.isEmpty(batchName)){
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败:所属批次不能为空");
			return resultVo.toString();
		}
//		if(StringUtils.isEmpty(bigUnitName)){
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("导入失败:大单位不能为空");
//			return resultVo.toString();
//		}
		BatchManagement bm = new BatchManagement();
		bm.setYear(Integer.parseInt(year));
		bm.setBatchName(year+"年"+batchName);
		List<BatchManagement> bmlist = bmMapper.selectAll(bm);
		int ybId = 0;
		//判断批次是否已存在，存在则取已有批次ID，不存在则新建批次并获取新的批次ID
		if(bmlist.size()>0){
			ybId=bmlist.get(0).getYbId();
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败:根据年度、所属批次未查询到数据,请核对校验批次条件！");
			return resultVo.toString();
		}
		
		int startNum=1;//从excel第几行开始读取数据
		//上传的文件不同，开始读取的行数不同 uploadType 上传类型 1、导入部队数据-基础表 2、导入部队数据-完整表
		if(uploadType==1){
			startNum=2;
		}
		ExcelUtils uet = new ExcelUtils();
		List<String[][]> list  = uet.getExcelData(filePath,startNum);
		if (list == null || list.isEmpty()) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败：数据为空");
			return resultVo.toString();
		}
		
		//获取文件名称，用于校验失败导出excel文件命名
		String fileName= "";
		if(filePath.getOriginalFilename().matches("^.+\\.(?i)(xls)$")){
			fileName= filePath.getOriginalFilename().substring(0,filePath.getOriginalFilename().length()-4);
		}else{
			fileName= filePath.getOriginalFilename().substring(0,filePath.getOriginalFilename().length()-5);
		}
		
		int successCount=0;//成功数量
		int errorCount=0;//失败数量
		//批量更新士兵信息
		List<SoldierBasicInfo> updateList = new ArrayList<>();
		//校验失败数据		
		List<SoldierBasicInfo> errList = new ArrayList<>();
		
		List<SoldierBasicInfo> tmpList = new ArrayList<>();
		
		//一次查询出所有参数数据
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		List<SysParamInfo> rlist= null;
				
		//一次查询出所有行政区划数据
		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
		List<SysRegional> regional= null;	
		
		//存储日志vo
		List<PersonnelArchivesLog> logList = new ArrayList<>();
		
		BatchManagement ybvo = ybMapper.selectByPrimaryKey(ybId);
		String retiredDate=ybvo.getYear()+".4.1";
		if(ybvo.getBatchName().indexOf("第二批次")!=-1){
			retiredDate=ybvo.getYear()+".9.1";
		}
		//获取第一个sheet
		String[][] Sheets = list.get(0);
		int rowcount=0;
		if(uploadType==1){
			String[] columnName = {"序号", "姓名", "公民身份证号", "性别","大单位","性别","警衔","出生年月",
					"婚姻状况","入伍前户籍所在地","拟安置地","联系电话","安置类别"};

			//判断导入的excel数据表头是否与定义的模板列名一致，不一致提示错误字段
			if(Sheets[0].length!=columnName.length){
				String result=ExcelUtils.verificationStudentExcelHeadLine(Sheets[0], columnName);												
				if(!StringUtils.isEmpty(result)){
					resultVo.setStatusCode(-1);
					resultVo.setMessage("导入失败：表头列名【"+result.substring(0,result.length()-1)+"】与模板列名不一致");
					return resultVo.toString();
				}
			}
			for (String[] rows : Sheets) { // 遍历行
				if (rows != null && rows.length > 1 && rowcount>0) {
					if("".equals(rows[1])&&"".equals(rows[2])&&"".equals(rows[3])&&"".equals(rows[4])){
						break;
					}
					SoldierBasicInfo tmpvo = new SoldierBasicInfo();
					tmpvo.setRowno(Integer.parseInt(rows[0]));
					tmpvo.setName(CharUtils.replaceAllNull(rows[1]));
					tmpvo.setIdcard(CharUtils.replaceAllNull(rows[2]));
					tmpvo.setSex(CharUtils.replaceAllNull(rows[3]));
					tmpvo.setBigUnitName(CharUtils.replaceAllNull(rows[4]));
					tmpvo.setSoldiersMilitaryRank(CharUtils.replaceAllNull(rows[5]));
					tmpvo.setBirthday(CharUtils.replaceAllNull(rows[6]));
					tmpvo.setEnlistedDate(CharUtils.replaceAllNull(rows[7]));
					tmpvo.setMarriageStatus(CharUtils.replaceAllNull(rows[8]));
					tmpvo.setDomicile(CharUtils.replaceAllNull(rows[9]));
					tmpvo.setResetPlace(CharUtils.replaceAllNull(rows[10]));
					tmpvo.setPhone(CharUtils.replaceAllNull(rows[11]));
					tmpvo.setResetCategory(CharUtils.replaceAllNull(rows[12]));
					tmpList.add(tmpvo);
				}
				rowcount++;
			}
		}else{
			String[] columnName = {"序号", "姓名", "身份证号", "性别","大单位","性别","军(警)衔","出生年月（格式要求：yyyy.mm）",
					"入伍年月（格式要求：yyyy.mm）","婚姻状况","入伍所在地","拟安置地","联系电话","安置类别","原部别","民族",
					"现服役时间(格式：X年X个月)","易地安置原因","配偶姓名","政治面貌","入党/入团时间（格式要求：yyyy.mm）","文化程度",
					"是否烈士子女","伤残性质","伤残等级","退役士兵档案分",
					"是否属于艰苦地区","艰边地区(如是艰边地区请填写具体地点及年限)","立功及表彰情况","处分情况"}; 
			
			//判断导入的excel数据表头是否与定义的模板列名一致，不一致提示错误字段
			if(Sheets[0].length!=columnName.length){
				String result=ExcelUtils.verificationStudentExcelHeadLine(Sheets[0], columnName);												
				if(!StringUtils.isEmpty(result)){
					resultVo.setStatusCode(-1);
					resultVo.setMessage("导入失败：表头列名【"+result.substring(0,result.length()-1)+"】与模板列名不一致");
					return resultVo.toString();
				}
			}
			for (String[] rows : Sheets) { // 遍历行
				if (rows != null && rows.length > 1 && rowcount>0) {
					
					if("".equals(rows[1])&&"".equals(rows[2])&&"".equals(rows[3])&&"".equals(rows[4])){
						break;
					}
					SoldierBasicInfo tmpvo = new SoldierBasicInfo();
					tmpvo.setRowno(Integer.parseInt(rows[0]));
					tmpvo.setName(CharUtils.replaceAllNull(rows[1]));
					tmpvo.setIdcard(CharUtils.replaceAllNull(rows[2]));
					tmpvo.setSex(CharUtils.replaceAllNull(rows[3]));
					tmpvo.setBigUnitName(CharUtils.replaceAllNull(rows[4]));
					tmpvo.setSoldiersMilitaryRank(CharUtils.replaceAllNull(rows[5]));
					tmpvo.setBirthday(CharUtils.replaceAllNull(rows[6]));
					tmpvo.setEnlistedDate(CharUtils.replaceAllNull(rows[7]));
					tmpvo.setMarriageStatus(CharUtils.replaceAllNull(rows[8]));
					tmpvo.setDomicile(CharUtils.replaceAllNull(rows[9]));
					tmpvo.setResetPlace(CharUtils.replaceAllNull(rows[10]));
					tmpvo.setPhone(CharUtils.replaceAllNull(rows[11]));
					tmpvo.setResetCategory(CharUtils.replaceAllNull(rows[12]));
					tmpvo.setFormerUnit(CharUtils.replaceAllNull(rows[13]));
					tmpvo.setNation(CharUtils.replaceAllNull(rows[14]));
					tmpvo.setRetiredDate(CharUtils.replaceAllNull(rows[15]));
					tmpvo.setServiceDuration(CharUtils.replaceAllNull(rows[16]));
					tmpvo.setRelocateReasonCode(CharUtils.replaceAllNull(rows[17]));
					tmpvo.setSpouseName(CharUtils.replaceAllNull(rows[18]));
					tmpvo.setPoliAffiCode(CharUtils.replaceAllNull(rows[19]));
					tmpvo.setTimJoiParty(CharUtils.replaceAllNull(rows[20]));
					tmpvo.setEduLevCode(CharUtils.replaceAllNull(rows[21]));
					tmpvo.setIsMartyrChildren(CharUtils.replaceAllNull(rows[22]));
					tmpvo.setDisabilityCategoriesCode(CharUtils.replaceAllNull(rows[23]));
					tmpvo.setDisableGradeCode(CharUtils.replaceAllNull(rows[24]));
					tmpvo.setArchivesScore(CharUtils.replaceAllNull(rows[25]));
					tmpvo.setRetiredType(CharUtils.replaceAllNull(rows[26]));
					tmpvo.setIsHardArea(CharUtils.replaceAllNull(rows[27]));
					tmpvo.setHardAreaRemark(CharUtils.replaceAllNull(rows[28]));
					tmpvo.setRewardRemark(CharUtils.replaceAllNull(rows[29]));
					tmpvo.setPunishmentRemark(CharUtils.replaceAllNull(rows[30]));
					tmpList.add(tmpvo);
				}
				rowcount++;
			}
		}
		List<String> sqlList = new ArrayList<>();
		int number=tmpList.size();
		String personnelType="1";
		for (int i = 0; i < tmpList.size(); i++) {
			System.out.println("---------------"+i);
			String errorMsg="";//失败原因
			boolean flag=true;
			
			SoldierBasicInfo tmpvo=tmpList.get(i);
			//判断excel表中身份证号是否重复
			for (int j = 0; j < tmpList.size(); j++) {
				if(i!=j){
					if (tmpvo.getIdcard().equals(tmpList.get(j).getIdcard())){       
						flag=false;
						errorMsg="该人员身份证号与第【"+tmpList.get(j).getRowno()+"】条数据身份证号重复,";
						continue;
					} 
				}
			}
			
			SoldierBasicInfo newvo = mapper.checkSoldierBasicInfo(tmpvo.getIdcard());
			if(newvo==null){
				flag=false;
				errorMsg+="通过身份证号查询部平台基础数据无当前士兵基础数据,";
				tmpvo.setRemark(errorMsg.substring(0,errorMsg.length()-1));
				errList.add(tmpvo);
				errorCount++;
			}else{
				if(flag){
					//判断数据是否已进入审核，如已审核的数据进行校验不做任何修改
					if(newvo.getStatus()<1){
						
						StringBuffer sql = new StringBuffer();
						sql.append("update soldier_basic_info set ");
						//原部别
						if(!StringUtils.isEmpty(tmpvo.getFormerUnit())){
							sql.append("former_unit='"+tmpvo.getFormerUnit()+"',");
						}
						//民族
						if(!StringUtils.isEmpty(tmpvo.getNation())){
							rlist=  ParamUtils.checkList(plist, "nation", "", tmpvo.getNation());
							if(rlist.size()>0){
//								newvo.setNation(rlist.get(0).getParamKey());
								sql.append("nation='"+rlist.get(0).getParamKey()+"',");
							}
						}
						//根据入伍时间、退伍时间计算出服役总月数
						if(!StringUtils.isEmpty(tmpvo.getEnlistedDate())){
							String enlistedDate =tmpvo.getEnlistedDate().replaceAll("\\.", "-")+"-01";
							String retiredDateTmp="";
							
							if(!StringUtils.isEmpty(tmpvo.getRetiredDate())){
								retiredDateTmp=tmpvo.getRetiredDate().replaceAll("\\.", "-");
								sql.append("retired_date='"+tmpvo.getRetiredDate()+"',");
							}else{
								retiredDateTmp =retiredDate.replaceAll("\\.", "-");
								sql.append("retired_date='"+retiredDate+"',");
							}
							int serviceDuration = DateUtils.getMonthDiff(retiredDateTmp, enlistedDate);
							if(serviceDuration>0){
								sql.append("service_duration='"+serviceDuration+"个月',");
							}
						}
						//易地安置原因
						if(!StringUtils.isEmpty(tmpvo.getRelocateReasonCode())){
							rlist=  ParamUtils.checkList(plist, "relocateReasonCode", "", tmpvo.getRelocateReasonCode());
							if(rlist.size()>0){
								sql.append("relocate_reason_code='"+rlist.get(0).getParamKey()+"',");
							}
						}
						//配偶姓名
						if(!StringUtils.isEmpty(tmpvo.getSpouseName())){
							sql.append("spouse_name='"+tmpvo.getSpouseName()+"',");
						}
						//政治面貌
						if(!StringUtils.isEmpty(tmpvo.getPoliAffiCode())){
							rlist=  ParamUtils.checkList(plist, "poliAffiCode", "", tmpvo.getPoliAffiCode());
							if(rlist.size()>0){
								sql.append("poli_affi_code='"+rlist.get(0).getParamKey()+"',");
							}
						}
						//入党/团时间
						if(!StringUtils.isEmpty(tmpvo.getTimJoiParty())){
							sql.append("tim_joi_party='"+tmpvo.getTimJoiParty()+"',");
						}
						//文化程度
						if(!StringUtils.isEmpty(tmpvo.getEduLevCode())){
							rlist=  ParamUtils.checkList(plist, "eduLevCode", "", tmpvo.getEduLevCode());
							if(rlist.size()>0){
								newvo.setEduLevCode(rlist.get(0).getParamKey());
								sql.append("edu_lev_code='"+rlist.get(0).getParamKey()+"',");
							}
						}
						//部队从事专业
						if(!StringUtils.isEmpty(tmpvo.getProfessional())){
							sql.append("professional='"+tmpvo.getProfessional()+"',");
						}
						//是否烈士子女
						if(!StringUtils.isEmpty(tmpvo.getIsMartyrChildren())){
							rlist=  ParamUtils.checkList(plist, "isOrNo", "", tmpvo.getIsMartyrChildren());
							if(rlist.size()>0){
								sql.append("is_martyr_children='"+rlist.get(0).getParamKey()+"',");
							}
						}
						//伤残性质
						if(!StringUtils.isEmpty(tmpvo.getDisabilityCategoriesCode())){
							rlist=  ParamUtils.checkList(plist, "disabilityCategoriesCode", "", tmpvo.getDisabilityCategoriesCode());
							if(rlist.size()>0){
								sql.append("disability_categories_code='"+rlist.get(0).getParamKey()+"',");
							}
						}
						//伤残等级
						if(!StringUtils.isEmpty(tmpvo.getDisableGradeCode())){
							rlist=  ParamUtils.checkList(plist, "disableGradeCode", "", tmpvo.getDisableGradeCode());
							if(rlist.size()>0){
								sql.append("disable_grade_code='"+rlist.get(0).getParamKey()+"',");
							}
						}
						//退役士兵档案分
						if(!StringUtils.isEmpty(tmpvo.getArchivesScore())){
							sql.append("archives_score='"+tmpvo.getArchivesScore()+"',");
						}
						//是否全程退役
						if(!StringUtils.isEmpty(tmpvo.getRetiredType())){
							sql.append("retired_type='"+tmpvo.getRetiredType()+"',");
						}
						//是否属于艰苦地区
						if(!StringUtils.isEmpty(tmpvo.getIsHardArea())){
							rlist=  ParamUtils.checkList(plist, "isOrNo", "", tmpvo.getIsHardArea());
							if(rlist.size()>0){
								sql.append("is_hard_area='"+rlist.get(0).getParamKey()+"',");
							}
						}
						//艰边地区
						if(!StringUtils.isEmpty(tmpvo.getHardAreaRemark())){
							sql.append("hard_area_remark='"+tmpvo.getHardAreaRemark()+"',");
						}
						//立功及表彰情况
						if(!StringUtils.isEmpty(tmpvo.getRewardRemark())){
							sql.append("reward_remark='"+tmpvo.getRewardRemark()+"',");
						}
						//处分情况
						if(!StringUtils.isEmpty(tmpvo.getPunishmentRemark())){
							sql.append("punishment_remark='"+tmpvo.getPunishmentRemark()+"',");
						}
						
						sql.append(" check_status=1,check_time=sysdate() where idcard = "+tmpvo.getIdcard() );
						sqlList.add(sql.toString());
						
						successCount++;
						//保存操作日志
						PersonnelArchivesLog log = new PersonnelArchivesLog();
						log.setSbiId(newvo.getSbiId());
						log.setOperator(displayName);
						log.setContext(context+"批量数据校验");
						logList.add(log);
					}else{
						errorMsg+="已进入审核环节，不允许重复导入或校验,";
						tmpvo.setRemark(errorMsg.substring(0,errorMsg.length()-1));
						errList.add(tmpvo);
						errorCount++;
					}
				}else{
					tmpvo.setRemark(errorMsg.substring(0,errorMsg.length()-1));
					errList.add(tmpvo);
					errorCount++;
				}
			}
		}
		
		if(!StringUtils.isEmpty(bigUnitName)){
			SoldierBasicInfo tmppo = new SoldierBasicInfo();
			tmppo.setYbId(ybId);
			tmppo.setBigUnitName(bigUnitName);
			List<SoldierBasicInfo> checkList = mapper.queryCheckData(tmppo);
			checkList.removeAll(tmpList);
			int rowno = errList.size();
			for (int i = 0; i < checkList.size(); i++) {
				if(!StringUtils.isEmpty(checkList.get(i).getBigUnitName())){
					rlist=  ParamUtils.checkList(plist, "bigUnitName", "", checkList.get(i).getBigUnitName());
					if(rlist.size()>0){
						checkList.get(i).setBigUnitName(rlist.get(0).getParamKey());
					}
				}
				if(!StringUtils.isEmpty(checkList.get(i).getNation())){
					rlist=  ParamUtils.checkList(plist, "nation", "", checkList.get(i).getNation());
					if(rlist.size()>0){
						checkList.get(i).setNation(rlist.get(0).getParamKey());
					}
				}
				if(!StringUtils.isEmpty(checkList.get(i).getSoldiersMilitaryRank())){
					rlist=  ParamUtils.checkList(plist, "soldMilitRankCode", "", checkList.get(i).getSoldiersMilitaryRank());
					if(rlist.size()>0){
						checkList.get(i).setSoldiersMilitaryRank(rlist.get(0).getParamKey());
					}
				}
				if(!StringUtils.isEmpty(checkList.get(i).getMarriageStatus())){
					rlist=  ParamUtils.checkList(plist, "marriageStatus", "", checkList.get(i).getMarriageStatus());
					if(rlist.size()>0){
						checkList.get(i).setMarriageStatus(rlist.get(0).getParamKey());
					}
				}
				if(!StringUtils.isEmpty(checkList.get(i).getPoliAffiCode())){
					rlist=  ParamUtils.checkList(plist, "poliAffiCode", "", checkList.get(i).getPoliAffiCode());
					if(rlist.size()>0){
						checkList.get(i).setPoliAffiCode(rlist.get(0).getParamKey());
					}
				}
				if(!StringUtils.isEmpty(checkList.get(i).getEduLevCode())){
					rlist=  ParamUtils.checkList(plist, "eduLevCode", "", checkList.get(i).getEduLevCode());
					if(rlist.size()>0){
						checkList.get(i).setEduLevCode(rlist.get(0).getParamKey());
					}
				}
				if(!StringUtils.isEmpty(checkList.get(i).getResetCategory())){
					rlist=  ParamUtils.checkList(plist, "resetCategory", "", checkList.get(i).getResetCategory());
					if(rlist.size()>0){
						checkList.get(i).setResetCategory(rlist.get(0).getParamKey());
					}
				}
				if(!StringUtils.isEmpty(checkList.get(i).getRelocateReasonCode())){
					rlist=  ParamUtils.checkList(plist, "relocateReasonCode", "", checkList.get(i).getRelocateReasonCode());
					if(rlist.size()>0){
						checkList.get(i).setRelocateReasonCode(rlist.get(0).getParamKey());
					}
				}
				if(!StringUtils.isEmpty(checkList.get(i).getIsMartyrChildren())){
					rlist=  ParamUtils.checkList(plist, "isOrNo", "", checkList.get(i).getIsMartyrChildren());
					if(rlist.size()>0){
						checkList.get(i).setIsMartyrChildren(rlist.get(0).getParamKey());
					}
				}
				if(!StringUtils.isEmpty(checkList.get(i).getDisabilityCategoriesCode())){
					rlist=  ParamUtils.checkList(plist, "disabilityCategoriesCode", "", checkList.get(i).getDisabilityCategoriesCode());
					if(rlist.size()>0){
						checkList.get(i).setDisabilityCategoriesCode(rlist.get(0).getParamKey());
					}
				}
				if(!StringUtils.isEmpty(checkList.get(i).getDisableGradeCode())){
					rlist=  ParamUtils.checkList(plist, "disableGradeCode", "", checkList.get(i).getDisableGradeCode());
					if(rlist.size()>0){
						checkList.get(i).setDisableGradeCode(rlist.get(0).getParamKey());
					}
				}
				if(!StringUtils.isEmpty(checkList.get(i).getIsHardArea())){
					rlist=  ParamUtils.checkList(plist, "isOrNo", "", checkList.get(i).getIsHardArea());
					if(rlist.size()>0){
						checkList.get(i).setIsHardArea(rlist.get(0).getParamKey());
					}
				}
				if(!StringUtils.isEmpty(checkList.get(i).getDomicile())){
					regional=ParamUtils.getRegional(srList, "", checkList.get(i).getDomicile());
					if(regional.size()>0){
						checkList.get(i).setDomicile(regional.get(0).getRegionalName());
					}
				}
				if(!StringUtils.isEmpty(checkList.get(i).getResetPlace())){
					regional=ParamUtils.getRegional(srList, "", checkList.get(i).getResetPlace());
					if(regional.size()>0){
						checkList.get(i).setResetPlace(regional.get(0).getRegionalName());
					}
				}
				rowno++;
				checkList.get(i).setRowno(rowno);
				checkList.get(i).setRemark("存在部平台基础数据，部队未提供士兵校验数据");
				errList.add(checkList.get(i));
			}
		}
		//批量更新数据
		if(sqlList.size()>0){
			mapper.updateListSql(sqlList);
		}
		//批量更新数据
//		if(updateList.size()>0){
//			mapper.updateArchivesList(updateList);
//		}
		
		//日志list结果大于0则新增保存日志信息
		if(logList.size()>0){
			palMapper.insertList(logList);
		}		
		//错误数据大于0则生成错误excel表
		if(errList.size()>0){
			try {
				String date=DateUtils.getStringCurrentDate();
				Map<String, Object> beans = new HashMap<String, Object>();
				XLSTransformer transformer = new XLSTransformer();
				String modelFileName="退役士兵数据校验失败模板.xls";
				String title1=date+"年由政府安排工作退役士兵基础信息填写表";
				if("2".equals(personnelType)){
					modelFileName="退出消防员数据校验失败模板.xls";
					title1=date+"年由政府安排工作退出消防员基础信息填写表";
				}
				if(uploadType==1){
					modelFileName="部平台基础数据导入失败模板.xls";
				}
				InputStream is = this.getClass().getResourceAsStream("/templates/error/"+modelFileName);
				beans.put("datalist", errList);
				beans.put("title1", "浙江省"+title1);
				Workbook workbook = transformer.transformXLS(is, beans);
				workbook.setForceFormulaRecalculation(true);
				is.close();
				String newFileName =  fileName+"-失败数据.xls";
				String subdirectory = "校验数据失败/"+date+"/";
				String path = rootPath+subdirectory;
				boolean isSuccess = ExcelUtils.createExcelFile(path,newFileName, workbook);
				
				if(isSuccess){
					resultVo.setList("\"file/"+subdirectory+newFileName+"\"");
				}
			} catch (ParsePropertyException e) {
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			resultVo.setStatusCode(-1);
		}else{
			resultVo.setStatusCode(200);
		}		
		resultVo.setMessage("共导入【"+number+"】条数据,成功【"+successCount+"】条,失败【"+errorCount+"】条");
    	return resultVo.toString();
    }
	
//	/**
//	 * 数据校验 –修改数据
//	 * @param vo
//	 * @return
//	 */
//	@RequestMapping(value = "updateSoldierData",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
//    public String updateSoldierData(@RequestBody SoldierBasicInfo vo){
//		ResultVo resultVo = new ResultVo();
//		String displayName=CharUtils.getDisplayName(request);
//		try {
//			if(!CheckIdCard.isValidatedAllIdcard(vo.getIdcard())){
//				resultVo.setStatusCode(-1);
//				resultVo.setMessage("修改失败：身份证号不合法");
//				return resultVo.toString();
//			}
//			mapper.updateByPrimaryKey(vo);
//			//存储日志vo
//			List<PersonnelArchivesLog> logList = new ArrayList<>();
//			PersonnelArchivesLog log = new PersonnelArchivesLog();
//			log.setSbiId(vo.getSbiId());
//			log.setOperator(displayName);
//			log.setContext(context+"数据校验–修改数据");
//			logList.add(log);
//			
//			//查询所有参数配置表所有参数信息
//			List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
//			//一次查询出所有行政区划数据
//			List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
//			
//			SoldierBasicInfo oldVo = mapper.selectByPrimaryKey(vo.getSbiId());
//			String validatorMsg=ValidatorUtils.validatorData(plist, srList, vo, oldVo,2);
//			if(!StringUtils.isEmpty(validatorMsg)){
//				log = new PersonnelArchivesLog();
//				log.setSbiId(vo.getSbiId());
//				log.setOperator(displayName);
//				log.setContext(context+"数据更新-"+vo.getValidatorMsg());
//				logList.add(log);
//			}
//			//保存更新日志
//			palMapper.insertList(logList);
//			
//			resultVo.setStatusCode(200);
//			resultVo.setMessage("修改成功");
//		} catch (Exception e) {
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("修改失败");
//		}
//    	return resultVo.toString();
//    }
	/**
	 * 删除数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "deleteSoldierData",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public String deleteSoldierData(@RequestBody SoldierBasicInfo vo){
		ResultVo resultVo = new ResultVo();
		
		String displayName=CharUtils.getDisplayName(request);
		
		try {
//			if(vo.getStatus()< 1){
				mapper.deleteByPrimaryKey(vo.getSbiId());
				
				PersonnelArchivesLog log = new PersonnelArchivesLog();
				log.setSbiId(vo.getSbiId());
				log.setOperator(displayName);
				log.setContext(context+"删除士兵基础信息");
				palMapper.insert(log);
				
				resultVo.setStatusCode(200);
				resultVo.setMessage("删除成功");
//			}else{
//				resultVo.setStatusCode(-1);
//				resultVo.setMessage("删除失败：当前数据已进入联审环节,不可执行删除操作");
//			}
		} catch (Exception e) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("删除失败");
		}
    	return resultVo.toString();
    }
	
	/**
	 * 根据ID查询数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "selectByPrimaryKey", method = RequestMethod.POST, produces = "application/json")
	public SoldierBasicInfo selectByPrimaryKey(@RequestBody SoldierBasicInfo vo) {
		SoldierBasicInfo sbivo = mapper.selectByPrimaryKey(vo.getSbiId());
		SoldierResetInfo tmpsrivo = new SoldierResetInfo();
		tmpsrivo.setSbiId(sbivo.getSbiId());
		sbivo.setSrivo(tmpsrivo);
		return sbivo;
	}
	
	/**
	 * 查询数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "querySoldierData", method = RequestMethod.POST, produces = "application/json")
	public PageInfo<SoldierBasicInfo> querySoldierData(@RequestBody SoldierBasicInfo vo) {
		vo.setFlag(1);
		if(!StringUtils.isEmpty(vo.getRegionalCode())&&!"330000".equals(vo.getRegionalCode())){//判断用户所属机构，330000为省级用户
			if(vo.getRegionalCode().endsWith("00")){//机构编号00结尾的 为地市用户
				vo.setRegionalCode(vo.getRegionalCode().substring(0,4));
			}
		}else{
			vo.setRegionalCode("");
		}
		//查询所有参数配置表所有参数信息
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		
		//一次查询出所有行政区划数据
		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
		
		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		List<SoldierBasicInfo> list = mapper.querySoldierList(vo);
		CharUtils.codeToName(plist, srList, list);
		PageInfo<SoldierBasicInfo> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}
	
	
	
	/**
	 * 导入2019年士兵安置信息
	 * @param filePath 上传的excel
	 * @param ybId 批次ID
	 * @return
	 */
	@RequestMapping(value = "impSoldierResetData", method = RequestMethod.GET,produces="application/json; charset=utf-8")
	public String impSoldierResetData(@RequestParam("filePath") MultipartFile filePath,String year,String batchName){
		ResultVo resultVo = new ResultVo();
		
		String displayName=CharUtils.getDisplayName(request);
		if(StringUtils.isEmpty(year)){
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败:年度不能为空");
			return resultVo.toString();
		}
		if(StringUtils.isEmpty(batchName)){
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败:所属批次不能为空");
			return resultVo.toString();
		}
		
		ExcelUtils uet = new ExcelUtils();
		List<String[][]> list  = uet.getExcelData(filePath,2);
		if (list == null || list.isEmpty()) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败：数据为空");
			return resultVo.toString();
		}
		
		//获取第一个sheet
		String[][] Sheets = list.get(0);
		
		String[] columnName = {"序号", "大单位", "地区", "姓名","身份证号","性别","民族","出生年月（格式要求：yyyy.mm）",
				"原部别","入伍年月（格式要求：yyyy.mm）","退伍时间（格式要求：yyyy.mm.dd）","现军衔等级","现服役时间(格式：X年X个月)",
				"入伍地","安置地","安排工作条件","易地安置原因","婚姻状况","配偶姓名","政治面貌","入党/入团时间（格式要求：yyyy.mm）",
				"文化程度","部队从事专业","是否烈士子女","伤残性质","伤残等级","退役士兵档案分","是否属于艰苦地区","艰边地区(如是艰边地区请填写具体地点及年限)",
				"立功及表彰情况","处分情况","手机号","报到时间（格式要求：yyyy.mm.dd）","现安置方式","是否发放自谋职业补助金","发放自谋职业补助金金额(元)",
				"是否发放生活补助","发放月数(月)","发放金额(元)","接收单位","接收单位类别","介绍信开具时间（格式要求：yyyy.mm.dd）",
				"单位接收时间（格式要求：yyyy.mm.dd）","安置岗位","是否在编","是否已上岗","未上岗原因"};
		//判断导入的excel数据表头是否与定义的模板列名一致，不一致提示错误字段
		if(Sheets[0].length!=columnName.length){
			String result=ExcelUtils.verificationStudentExcelHeadLine(Sheets[0], columnName);												
			if(!StringUtils.isEmpty(result)){
				resultVo.setStatusCode(-1);
				resultVo.setMessage("导入失败：表头列名【"+result.substring(0,result.length()-1)+"】与模板列名不一致");
				return resultVo.toString();
			}
		}
		
		int successCount=0;//成功数量
		int errorCount=0;//失败数量
		int number = 0;//第几条数据
		int upCount=0;//数据变更数量
		//一次查询出所有参数数据
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		List<SysParamInfo> rlist= null;
		
		//一次查询出所有行政区划数据
		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
		List<SysRegional> regional= null;
		
		//存储基础信息变更数据，用于返回导入页面提示
		List<Map<String, String>> arrList = new ArrayList<>();
		//存储日志vo
		List<PersonnelArchivesLog> logList = new ArrayList<>();
		
		//批量新增士兵信息
		List<SoldierBasicInfo> insertList = new ArrayList<>();
		
		//批量更新士兵信息
		List<SoldierBasicInfo> updateList = new ArrayList<>();
		
		List<SoldierBasicInfoError> errList = new ArrayList<>();
		
		String date = DateUtils.getStringCurrentDate();
			
		List<SoldierBasicInfo> tmpList = new ArrayList<>();
		//批量新增士兵安置信息
		List<SoldierResetInfo> insertResetList = new ArrayList<>();
		//批量更新士兵安置信息
		List<SoldierResetInfo> updateResetList = new ArrayList<>();	
		
		List<SoldierBasicInfoOse> oseList = new ArrayList<>();
		
		
		BatchManagement bm = new BatchManagement();
		bm.setOperator(displayName);
		bm.setYear(Integer.parseInt(year));
		bm.setBatchName(year+"年"+batchName);
		bm.setReceivingFilePath("士兵安置信息");
		List<BatchManagement> bmlist = bmMapper.selectAll(bm);
		int ybId = 0;
		//判断批次是否已存在，存在则取已有批次ID，不存在则新建批次并获取新的批次ID
		if(bmlist.size()>0){
			ybId=bmlist.get(0).getYbId();
		}else{
			bmMapper.insert(bm);
			ybId=bm.getYbId();
		}
		
		int colNum=46;//定义表格多少列
		int rowcount=0;
		for (String[] rows : Sheets) { // 遍历行
			if (rows != null && rows.length > 1 && rowcount>0) {
				if("".equals(rows[1])&&"".equals(rows[2])&&"".equals(rows[3])&&"".equals(rows[4])){
					break;
				}
				//最后一列未读取到会报错，找不到数据。new一个新的数组存放空值。
				String[] newRows = new String[colNum];
				if(rows.length<colNum){
					for (int i = 0; i < rows.length; i++) {
						newRows[i]=rows[i];
					}
					rows = newRows;
				}
				
				SoldierBasicInfo tmpvo = new SoldierBasicInfo();
				tmpvo.setRowno(Integer.parseInt(rows[0]));
				tmpvo.setBigUnitName(CharUtils.replaceAllNull(rows[1]));
				tmpvo.setRegionalName(CharUtils.replaceAllNull(rows[2]));
				tmpvo.setName(CharUtils.replaceAllNull(rows[3]));
				tmpvo.setIdcard(CharUtils.replaceAllNull(rows[4]));
				tmpvo.setSex(CharUtils.replaceAllNull(rows[5]));
				tmpvo.setNation(CharUtils.replaceAllNull(rows[6]));
				tmpvo.setBirthday(CharUtils.replaceAllNull(rows[7]));
				tmpvo.setFormerUnit(CharUtils.replaceAllNull(rows[8]));
				tmpvo.setEnlistedDate(CharUtils.replaceAllNull(rows[9]));
				tmpvo.setRetiredDate(CharUtils.replaceAllNull(rows[10]));
				tmpvo.setSoldiersMilitaryRank(CharUtils.replaceAllNull(rows[11]));
				tmpvo.setServiceDuration(CharUtils.replaceAllNull(rows[12]));
				tmpvo.setDomicile(CharUtils.replaceAllNull(rows[13]));
				tmpvo.setResetPlace(CharUtils.replaceAllNull(rows[14]));
				tmpvo.setResetCategory(CharUtils.replaceAllNull(rows[15]));
				tmpvo.setRelocateReasonCode(CharUtils.replaceAllNull(rows[16]));
				tmpvo.setMarriageStatus(CharUtils.replaceAllNull(rows[17]));
				tmpvo.setSpouseName(CharUtils.replaceAllNull(rows[18]));
				tmpvo.setPoliAffiCode(CharUtils.replaceAllNull(rows[19]));
				tmpvo.setTimJoiParty(CharUtils.replaceAllNull(rows[20]));
				tmpvo.setEduLevCode(CharUtils.replaceAllNull(rows[21]));
				tmpvo.setProfessional(CharUtils.replaceAllNull(rows[22]));
				tmpvo.setIsMartyrChildren(CharUtils.replaceAllNull(rows[23]));
				tmpvo.setDisabilityCategoriesCode(CharUtils.replaceAllNull(rows[24]));
				tmpvo.setDisableGradeCode(CharUtils.replaceAllNull(rows[25]));
				tmpvo.setArchivesScore(CharUtils.replaceAllNull(rows[26]));
				tmpvo.setIsHardArea(CharUtils.replaceAllNull(rows[27]));
				tmpvo.setHardAreaRemark(CharUtils.replaceAllNull(rows[28]));
				tmpvo.setRewardRemark(CharUtils.replaceAllNull(rows[29]));
				tmpvo.setPunishmentRemark(CharUtils.replaceAllNull(rows[30]));
				tmpvo.setPhone(CharUtils.replaceAllNull(rows[31]));
				
				SoldierResetInfo tmpsrivo = new SoldierResetInfo();
				tmpsrivo.setRegisterTime(CharUtils.replaceAllNull(rows[32]));
				tmpsrivo.setNewResetType(CharUtils.replaceAllNull(rows[33]));
				tmpsrivo.setWheReceSelfSubs(CharUtils.replaceAllNull(rows[34]));
				tmpsrivo.setSelfSubsAmount(CharUtils.replaceAllNull(rows[35]));
				tmpsrivo.setWheLivingExpenses(CharUtils.replaceAllNull(rows[36]));
				tmpsrivo.setLivingExpensesMonth(CharUtils.replaceAllNull(rows[37]));
				tmpsrivo.setLivingExpensesAmout(CharUtils.replaceAllNull(rows[38]));
				tmpsrivo.setUnitName(CharUtils.replaceAllNull(rows[39]));
				tmpsrivo.setUnitCategory(CharUtils.replaceAllNull(rows[40]));
				tmpsrivo.setRecommendationTime(CharUtils.replaceAllNull(rows[41]));
				tmpsrivo.setReceivingTime(CharUtils.replaceAllNull(rows[42]));
				tmpsrivo.setPostsName(CharUtils.replaceAllNull(rows[43]));
				tmpsrivo.setWheOnTheStaff(CharUtils.replaceAllNull(rows[44]));
				tmpsrivo.setWheMouGuard(CharUtils.replaceAllNull(rows[45]));
				tmpsrivo.setUnrecReaExpl(CharUtils.replaceAllNull(rows[46]));
				tmpvo.setSrivo(tmpsrivo);
				tmpList.add(tmpvo);
			}
			rowcount++;
		}
		number=tmpList.size();
		for (int i = 0; i < tmpList.size(); i++) {
			System.out.println("---------------"+i);
			String errorMsg="";//失败原因
			boolean flag=true;
			
			SoldierBasicInfo tmpvo=tmpList.get(i);
			//判断excel表中身份证号是否重复
			for (int j = 0; j < tmpList.size(); j++) {
				if(i!=j){
					if (tmpvo.getIdcard().equals(tmpList.get(j).getIdcard())){       
						flag=false;
						errorMsg="该人员身份证号与第【"+tmpList.get(j).getRowno()+"】条数据身份证号重复,";
						continue;
					} 
				}
			}
			SoldierBasicInfo vo = new SoldierBasicInfo();
			BeanUtils.copyProperties(tmpvo,vo);   
			
			String score = CharUtils.stringToDouble(tmpvo.getArchivesScore());
			vo.setArchivesScore(score);	
			
			vo.setYbId(ybId);
			if(!StringUtils.isEmpty(tmpvo.getName())){
//				flag=false;
//				errorMsg="姓名不能为空,";
//			}else{
				vo.setName(tmpvo.getName());
			}
			if(!StringUtils.isEmpty(tmpvo.getIdcard())){
//				flag=false;
//				errorMsg="身份证号不能为空,";
//			}else{
//				if(!CheckIdCard.isValidatedAllIdcard(vo.getIdcard())){
//					flag=false;
//					errorMsg+="请输入合法的身份证号,";
//				}else{
					vo.setIdcard(tmpvo.getIdcard());
//				}
			}
			
			if(!StringUtils.isEmpty(tmpvo.getBirthday())){
				String[] tmp = tmpvo.getBirthday().split("\\.");
				if(tmp.length==2){
					vo.setBirthday(tmpvo.getBirthday());
				}else if(tmp.length==3){
					vo.setBirthday(tmp[0]+"."+tmp[1]);
				}else{
					vo.setBirthday(null);
				}
			}
			
			if(!StringUtils.isEmpty(tmpvo.getSex())){
				rlist=  ParamUtils.checkList(plist, "sex", "", tmpvo.getSex());
				if(rlist.size()>0){
					vo.setSex(rlist.get(0).getParamKey());
				}
			}else{
				vo.setSex(null);
			}
			
			if(!StringUtils.isEmpty(tmpvo.getNation())){
				rlist=  ParamUtils.checkList(plist, "nation", "", tmpvo.getNation());
				if(rlist.size()>0){
					vo.setNation(rlist.get(0).getParamKey());
				}
			}else{
				vo.setNation(null);
			}
			if(!StringUtils.isEmpty(tmpvo.getBigUnitName())){
				rlist=  ParamUtils.checkList(plist, "bigUnitName", "", tmpvo.getBigUnitName());
				if(rlist.size()>0){
					vo.setBigUnitName(rlist.get(0).getParamKey());
					int smrTmp=Integer.parseInt(rlist.get(0).getParamKey());
					//设置人员类型 1、退役士兵；2、退出消防兵
					if(smrTmp>=51 && smrTmp<=83){
						vo.setPersonnelType("2");
					}else{
						vo.setPersonnelType("1");
					}
				}
			}else{
				vo.setBigUnitName(null);
			}
			
			if(!StringUtils.isEmpty(tmpvo.getSoldiersMilitaryRank())){
				vo.setPersonnelType("1");
				rlist=  ParamUtils.checkList(plist, "soldMilitRankCode", "", tmpvo.getSoldiersMilitaryRank());
				if(rlist.size()>0){
					vo.setSoldiersMilitaryRank(rlist.get(0).getParamKey());
//					int smrTmp=Integer.parseInt(rlist.get(0).getParamKey());
//					//设置人员类型 1、退役士兵；2、退出消防兵
//					if(smrTmp>=20 && smrTmp<=23){
//						vo.setPersonnelType("2");
//					}else{
//						vo.setPersonnelType("1");
//					}
				}
			}else{
				vo.setSoldiersMilitaryRank(null);
			}
			
			if(!StringUtils.isEmpty(tmpvo.getMarriageStatus())){
				rlist=  ParamUtils.checkList(plist, "marriageStatus", "", tmpvo.getMarriageStatus());
				if(rlist.size()>0){
					vo.setMarriageStatus(rlist.get(0).getParamKey());
				}
			}else{
				vo.setMarriageStatus(null);
			}
			
			if(!StringUtils.isEmpty(tmpvo.getDomicile())){
				regional=ParamUtils.getRegional(srList, tmpvo.getDomicile(), "");
				if(regional.size()>0){
					vo.setDomicile(regional.get(0).getRegionalCode());
				}
			}else{
				vo.setDomicile(null);
			}
			
			if(StringUtils.isEmpty(tmpvo.getResetPlace())){
				flag=false;
				errorMsg+="拟安置地不能为空,";
			}else{
				regional=ParamUtils.getRegional(srList, tmpvo.getResetPlace(), "");
				if(regional.size()<1){
					flag=false;
					errorMsg+="拟安置地填写有误,";
				}else{
					vo.setResetPlace(regional.get(0).getRegionalCode());
					String[] regionalCode=regional.get(0).getRegionalCode().split("\\,");
					vo.setRegionalCode(regionalCode[2]);
					
					//判断是否易地安置 1省内易地安置 2省外易地安置
					if(!StringUtils.isEmpty(vo.getDomicile())){
						if(!vo.getDomicile().startsWith("330000")){
							vo.setIsRelocateReason("2");
						}else{
							if(!vo.getDomicile().substring(0,13).equals(vo.getResetPlace().substring(0,13))){
								vo.setIsRelocateReason("1");
							}
						}
					}
				}
			}
			
			if(!StringUtils.isEmpty(tmpvo.getPhone())){
				vo.setPhone(tmpvo.getPhone());
			}else{
				vo.setPhone(null);
			}
			
			if(!StringUtils.isEmpty(tmpvo.getResetCategory())){
				rlist=  ParamUtils.checkList(plist, "resetCategory", "", tmpvo.getResetCategory());
				if(rlist.size()>0){
					vo.setResetCategory(rlist.get(0).getParamKey());
				}
			}else{
				vo.setResetCategory(null);
			}
			
			//异地安置原因
			if(!StringUtils.isEmpty(tmpvo.getRelocateReasonCode())){
				String relocateReasonCode=tmpvo.getRelocateReasonCode();
				if(tmpvo.getRelocateReasonCode().indexOf("安置")!= -1){
					relocateReasonCode=relocateReasonCode.replace("安置", "");
				}
				rlist=  ParamUtils.checkList(plist, "relocateReasonCode", "", relocateReasonCode);
				if(rlist.size()>0){
					vo.setRelocateReasonCode(rlist.get(0).getParamKey());
				}
			}else{
				vo.setRelocateReasonCode(null);
			}
			
			//政治面貌
			if(!StringUtils.isEmpty(tmpvo.getPoliAffiCode())){
				rlist=  ParamUtils.checkList(plist, "poliAffiCode", "", tmpvo.getPoliAffiCode());
				if(rlist.size()>0){
					vo.setPoliAffiCode(rlist.get(0).getParamKey());
				}
			}else{
				vo.setPoliAffiCode(null);
			}
			
			//文化程度
			if(!StringUtils.isEmpty(tmpvo.getEduLevCode())){
				rlist=  ParamUtils.checkList(plist, "eduLevCode", "", tmpvo.getEduLevCode());
				if(rlist.size()>0){
					vo.setEduLevCode(rlist.get(0).getParamKey());
				}
			}else{
				vo.setEduLevCode(null);
			}
			//是否烈士子女
			if(!StringUtils.isEmpty(tmpvo.getIsMartyrChildren())){
				rlist=  ParamUtils.checkList(plist, "isOrNo", "", tmpvo.getIsMartyrChildren());
				if(rlist.size()>0){
					vo.setIsMartyrChildren(rlist.get(0).getParamKey());
				}
			}else{
				vo.setIsMartyrChildren(null);
			}
			//伤残性质
			if(!StringUtils.isEmpty(tmpvo.getDisabilityCategoriesCode())){
				rlist=  ParamUtils.checkList(plist, "disabilityCategoriesCode", "", tmpvo.getDisabilityCategoriesCode());
				if(rlist.size()>0){
					vo.setDisabilityCategoriesCode(rlist.get(0).getParamKey());
				}
			}else{
				vo.setDisabilityCategoriesCode(null);
			}
			//伤残等级
			if(!StringUtils.isEmpty(tmpvo.getDisableGradeCode())){
				rlist=  ParamUtils.checkList(plist, "disableGradeCode", "", tmpvo.getDisableGradeCode());
				if(rlist.size()>0){
					vo.setDisableGradeCode(rlist.get(0).getParamKey());
				}
			}else{
				vo.setDisableGradeCode(null);
			}
			
			//是否属于艰苦地区
			if(!StringUtils.isEmpty(tmpvo.getIsHardArea())){
				rlist=  ParamUtils.checkList(plist, "isOrNo", "", tmpvo.getIsHardArea());
				if(rlist.size()>0){
					vo.setIsHardArea(rlist.get(0).getParamKey());
				}
			}else{
				vo.setIsHardArea(null);
			}
			
			//--------------士兵安置信息---------------
			SoldierResetInfo srivo = new SoldierResetInfo();
			BeanUtils.copyProperties(tmpvo.getSrivo(),srivo);
			//现安置方式
			if(!StringUtils.isEmpty(tmpvo.getSrivo().getNewResetType())){
				rlist=  ParamUtils.checkList(plist, "newResetType", "", tmpvo.getSrivo().getNewResetType());
				if(rlist.size()>0){
					srivo.setNewResetType(rlist.get(0).getParamKey());
				}
			}else{
				srivo.setNewResetType(null);
			}
			if(StringUtils.isEmpty(tmpvo.getSrivo().getSelfSubsAmount())){
				srivo.setSelfSubsAmount("0");
			}else{
				srivo.setSelfSubsAmount(null);
			}
			
			//是否发放自谋职业补助金
			if(!StringUtils.isEmpty(tmpvo.getSrivo().getWheReceSelfSubs())){
				rlist=  ParamUtils.checkList(plist, "isOrNo", "", tmpvo.getSrivo().getWheReceSelfSubs());
				if(rlist.size()>0){
					srivo.setWheReceSelfSubs(rlist.get(0).getParamKey());
				}
			}else{
				srivo.setWheReceSelfSubs(null);
			}
			//是否发放生活补助
			if(!StringUtils.isEmpty(tmpvo.getSrivo().getWheLivingExpenses())){
				rlist=  ParamUtils.checkList(plist, "isOrNo", "", tmpvo.getSrivo().getWheLivingExpenses());
				if(rlist.size()>0){
					srivo.setWheLivingExpenses(rlist.get(0).getParamKey());
				}
			}else{
				srivo.setWheLivingExpenses(null);
			}
			if(StringUtils.isEmpty(tmpvo.getSrivo().getLivingExpensesAmout())){
				srivo.setLivingExpensesAmout("0");
			}else{
				srivo.setLivingExpensesAmout(null);
			}
			//接收单位类别
			if(!StringUtils.isEmpty(tmpvo.getSrivo().getUnitCategory())){
				rlist=  ParamUtils.checkList(plist, "unitCategory", "", tmpvo.getSrivo().getUnitCategory());
				if(rlist.size()>0){
					srivo.setUnitCategory(rlist.get(0).getParamKey());
				}
			}else{
				srivo.setUnitCategory(null);
			}
			//是否在编
			if(!StringUtils.isEmpty(tmpvo.getSrivo().getWheOnTheStaff())){
				rlist=  ParamUtils.checkList(plist, "isOrNo", "", tmpvo.getSrivo().getWheOnTheStaff());
				if(rlist.size()>0){
					srivo.setWheOnTheStaff(rlist.get(0).getParamKey());
				}
			}else{
				srivo.setWheOnTheStaff(null);
			}
			//是否上岗
			if(!StringUtils.isEmpty(tmpvo.getSrivo().getWheMouGuard())){
				rlist=  ParamUtils.checkList(plist, "isOrNo", "", tmpvo.getSrivo().getWheMouGuard());
				if(rlist.size()>0){
					srivo.setWheMouGuard(rlist.get(0).getParamKey());
				}
			}else{
				srivo.setWheMouGuard(null);
			}
			//未上岗原因
			if(!StringUtils.isEmpty(tmpvo.getSrivo().getUnrecReaExpl())){
				rlist=  ParamUtils.checkList(plist, "unrecReaExpl", "", tmpvo.getSrivo().getUnrecReaExpl());
				if(rlist.size()>0){
					srivo.setUnrecReaExpl(rlist.get(0).getParamKey());
				}
			}else{
				srivo.setUnrecReaExpl(null);
			}
			//根据入伍时间、退伍时间计算出服役总月数
			String enlistedDateTmp="";
			String retiredDateTmp="";
			if(!StringUtils.isEmpty(tmpvo.getRetiredDate())){
				String[] tmp = tmpvo.getRetiredDate().split("\\.");
				if(tmp.length<3){
					retiredDateTmp =tmpvo.getRetiredDate().replaceAll("\\.", "-")+"-01";
				}else{
					retiredDateTmp =tmpvo.getRetiredDate().replaceAll("\\.", "-");
				}
			}else{
				String retiredDate=year+".9.1";
				vo.setRetiredDate(retiredDate);
				retiredDateTmp =retiredDate.replaceAll("\\.", "-");
			}
			if(!StringUtils.isEmpty(retiredDateTmp)&&!StringUtils.isEmpty(enlistedDateTmp)){
				int serviceDuration = DateUtils.getMonthDiff(retiredDateTmp, enlistedDateTmp);
				vo.setServiceDuration(serviceDuration+"个月");
			}else{
				vo.setServiceDuration(null);
			}
			
			vo.setSrivo(srivo);
			SoldierBasicInfoError ervo = new SoldierBasicInfoError();
			BeanUtils.copyProperties(tmpvo,ervo);
			BeanUtils.copyProperties(tmpvo.getSrivo(),ervo);
			
			if(flag){
				vo.setCheckStatus(1);
				vo.setStatus(1);
				vo.setRegisterStatus(1);
				vo.setDataType(1);
				vo.setResetType(1);
				
				if(!StringUtils.isEmpty(tmpvo.getSrivo().getNewResetType())){
					rlist=  ParamUtils.checkList(plist, "newResetType", "", tmpvo.getSrivo().getNewResetType());
					if(rlist.size()>0){
						vo.setNewResetType(Integer.parseInt(rlist.get(0).getParamKey()));//设置现安置方式
						//判断新的安置方式，不安排工作则自主就业表也需要添加数据
						if(vo.getNewResetType()>1){
							SoldierBasicInfoOse osevo = new SoldierBasicInfoOse();
							BeanUtils.copyProperties(vo,osevo);
							osevo.setYear(year);
							
							SoldierBasicInfoOse record = new SoldierBasicInfoOse();
							record.setIdcard(vo.getIdcard());
							//根据身份证号查询数据，判断数据是否已存在
							List<SoldierBasicInfoOse> sbioList = sbioMapper.selectAll(record);
							if(sbioList.size()>0){
								osevo.setSbiId(sbioList.get(0).getSbiId());
								sbioMapper.updateByPrimaryKey(osevo);
							}else{
								sbioMapper.insert(osevo);
							}
						}
					}
				}
				//根据身份证号查询数据，判断数据是否已存在
				SoldierBasicInfo sbivo = mapper.checkSoldierBasicInfo(vo.getIdcard());
				if(sbivo==null){
					insertList.add(vo);
					successCount++;
				}else{
					//如果数据存在，判断是否属于同一个批次，不是同一批次则提示错误信息
					if(ybId==sbivo.getYbId()){
//						int newResetType = sbivo.getNewResetType()==null?0:sbivo.getNewResetType();
//						if(newResetType>1){
//							ervo.setRemark("该人员已选择灵活就业或自主就业！");
//							errList.add(ervo);
//							errorCount++;
//						}else{
							String validatorMsg=ValidatorUtils.validatorData(plist, srList, vo, sbivo,1);
							if(!StringUtils.isEmpty(validatorMsg)){
								Map<String, String> map = new HashMap<String, String>();
								map.put("sbiId", sbivo.getSbiId().toString());
								map.put("name", sbivo.getName());
								map.put("idcard", sbivo.getIdcard());
								map.put("validatorMsg", validatorMsg.substring(0,validatorMsg.length()-1));
								arrList.add(map);
								upCount++;
								
								ervo.setRemark(validatorMsg.substring(0,validatorMsg.length()-1));
								errList.add(ervo);
								errorCount++;
							}else{
								vo.setSbiId(sbivo.getSbiId());
								updateList.add(vo);
								successCount++;	
							}
//						}
					}else{
						ervo.setRemark("该人员数据已存在于其他批次中！");
						errList.add(ervo);
						errorCount++;
					}
				}
			}else{
				ervo.setRemark(errorMsg.substring(0,errorMsg.length()-1));
				errList.add(ervo);
				errorCount++;
			}
		}				
			
		//批量新增数据
		if(insertList.size()>0){
			mapper.insertList(insertList);
			for (int i = 0; i < insertList.size(); i++) {
				//保存操作日志
				PersonnelArchivesLog log = new PersonnelArchivesLog();
				log.setSbiId(insertList.get(i).getSbiId());
				log.setOperator(displayName);
				log.setContext(context+"由政府安排工作数据导入-新增数据");
				logList.add(log);
				
				SoldierResetInfo tmpvo = insertList.get(i).getSrivo();
				tmpvo.setSbiId(insertList.get(i).getSbiId());
				insertResetList.add(tmpvo);
			}
		}
		//批量更新数据
		if(updateList.size()>0){
			mapper.updateArchivesList(updateList);
			for (int i = 0; i < updateList.size(); i++) {
				//保存操作日志
				PersonnelArchivesLog log = new PersonnelArchivesLog();
				log.setSbiId(updateList.get(i).getSbiId());
				log.setOperator(displayName);
				log.setContext(context+"由政府安排工作数据导入-批量更新数据");
				logList.add(log);
				
				SoldierResetInfo sripo = sriMapper.selectByPrimaryKey(updateList.get(i).getSbiId());
				if(sripo!=null){
					SoldierResetInfo tmpvo = updateList.get(i).getSrivo();
					tmpvo.setSbiId(updateList.get(i).getSbiId());
					updateResetList.add(tmpvo);
				}else{
					SoldierResetInfo tmpvo = updateList.get(i).getSrivo();
					tmpvo.setSbiId(updateList.get(i).getSbiId());
					insertResetList.add(tmpvo);
				}
				
			}
		}
		//日志list结果大于0则新增保存日志信息
		if(logList.size()>0){
			palMapper.insertList(logList);
		}
		//批量新增安置数据
		if(insertResetList.size()>0){
			sriMapper.insertList(insertResetList);
		}
		//批量更新安置数据
		if(updateResetList.size()>0){
			sriMapper.updateList(updateResetList);
		}
		
		//错误数据大于0则生成错误excel表
		if(errList.size()>0){
			try {
				Map<String, Object> beans = new HashMap<String, Object>();
				XLSTransformer transformer = new XLSTransformer();
				InputStream is = this.getClass().getResourceAsStream("/templates/error/由政府安排工作退役士兵信息导入失败模板.xls");
				String title1="浙江省"+DateUtils.getStringYearDate()+"年由政府安排工作退役士兵信息汇总表";
				beans.put("datalist", errList);
				beans.put("title1", title1);
				Workbook workbook = transformer.transformXLS(is, beans);
				workbook.setForceFormulaRecalculation(true);
				is.close();
				String fileName =  filePath.getOriginalFilename().substring(0,filePath.getOriginalFilename().length()-4)+"-失败数据.xls";
				String subdirectory = "导入安置数据失败/"+date+"/";
				String path = rootPath+subdirectory;
				boolean isSuccess = ExcelUtils.createExcelFile(path,fileName, workbook);
				
				if(isSuccess){
					resultVo.setList("\"file/"+subdirectory+fileName+"\"");
				}
			} catch (ParsePropertyException e) {
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(errList.size()>0 || arrList.size()>0){
			resultVo.setStatusCode(-1);
		}else{
			resultVo.setStatusCode(200);
		}
		resultVo.setMessage("共导入【"+number+"】条数据,成功【"+successCount+"】条,失败【"+errorCount+"】条,数据变更【"+upCount+"】条");
//		resultVo.setArrList(JSON.toJSON(arrList).toString());
    	return resultVo.toString();
    }
	

	/**
	 * 数据管理-查询地区级联数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "querySoldierTreeNodes",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public JSONArray querySoldierTreeNodes(@RequestBody JSONObject json){
		String areaId=request.getHeader("areaId");
		
		Map<String, String> map=new HashMap<>();
		map.put("ybId", json.get("ybId").toString());
		if(!StringUtils.isEmpty(json.get("checkStatus")+"")){
			map.put("checkStatus", json.get("checkStatus").toString());
		}
		
		JSONArray arr1 = new JSONArray();
		JSONArray arr2tmp = new JSONArray();
		if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
			if(areaId.endsWith("00")){//机构编号00结尾的 为地市用户
				map.put("regionalCode", areaId.substring(0,4));
			}else{
				map.put("regionalCode", areaId);
			}
		}
		map.put("flag", "1");
		arr1 = JSONArray.parseArray(JSONArray.toJSONString(mapper.queryAreaCascadeData(map)));//查询出总人数
		
		map.put("flag", "2");
		JSONArray arr2 = JSONArray.parseArray(JSONArray.toJSONString(mapper.queryAreaCascadeData(map)));
		for (int i = 0; i < arr2.size(); i++) {
			com.alibaba.fastjson.JSONObject json2 = (com.alibaba.fastjson.JSONObject) arr2.get(i);
			
			map.put("flag", "3");//根据地市查询各区县安置总人数
			if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
				if(!areaId.endsWith("00")){//机构编号00结尾的 为地市用户
					map.put("regionalCode", areaId);
				}
			}else{
				map.put("regionalCode", json2.getString("regionalCode").substring(0,4));
			}
			JSONArray arr3 = JSONArray.parseArray(JSONArray.toJSONString(mapper.queryAreaCascadeData(map)));
			json2.put("children",arr3);
			arr2tmp.add(json2);
		}	
		
		JSONArray rjson = new JSONArray();
			
		com.alibaba.fastjson.JSONObject json1=(com.alibaba.fastjson.JSONObject)arr1.get(0);
		json1.put("children",arr2tmp);
		json1.put("regionalName", "浙江省");
		json1.put("regionalCode", "330000");
		rjson.add(json1);
		
		return rjson;
    } 
	
	@RequestMapping(value = "validatorData",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public String validatorData(@RequestBody SoldierBasicInfo vo){
		ResultVo resultVo = new ResultVo();
		try {
			List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
			List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
			SoldierBasicInfo oldVo = mapper.selectByPrimaryKey(vo.getSbiId());
			String validatorMsg=ValidatorUtils.validatorData(plist, srList, vo, oldVo,1);
			
			if(validatorMsg.length()>0){
				resultVo.setStatusCode(1);
				resultVo.setMessage(validatorMsg.substring(0,validatorMsg.length()-1));
			}else{
				resultVo.setStatusCode(200);
				resultVo.setMessage("核对完成");
			}
		} catch (Exception e) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("核对失败");
		}
    	return resultVo.toString();
    }

	/**
	 * 读取接收安置退役士兵统计表
	 * @return type 类型 1央企 2省属事业 3省属企业
	 */
	@PostMapping("readReceiveExcel")
   @ResponseBody
    public ResultVo readReceiveExcel(String type,MultipartFile file){
		ResultVo resultVo = mapper.readReceiveExcel(type, file);
		return resultVo;
	}
}
