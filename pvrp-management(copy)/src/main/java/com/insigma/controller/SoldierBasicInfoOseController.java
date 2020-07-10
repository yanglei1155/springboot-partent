package com.insigma.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.insigma.po.ResultVo;
import com.insigma.po.SysParamInfo;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SoldierBasicInfoError;
import com.insigma.po.soldier.SoldierBasicInfoOse;
import com.insigma.po.soldier.SoldierRegister;
import com.insigma.po.soldier.SysRegional;
import com.insigma.service.SoldierBasicInfoOseService;
import com.insigma.service.SoldierRegisterService;
import com.insigma.service.SysParamInfoService;
import com.insigma.service.SysRegionalService;
import com.insigma.util.CharUtils;
import com.insigma.util.DateUtils;
import com.insigma.util.ExcelUtils;
import com.insigma.util.ParamUtils;

import net.minidev.json.JSONObject;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * 
 * 类描述：自主就业退役士兵数据校   
 * 创建人：刘伟民   
 * 创建时间：2020年6月4日 下午5:31:50   
 * @version
 */
@RestController
@RequestMapping("/pvrpm/ose")
public class SoldierBasicInfoOseController {
	
	@Autowired
    HttpServletRequest request;
	
	@Autowired
	private SysRegionalService srsMapper;
	
	public static String dateTime = "yyyy-MM-dd HH:mm:ss";
	
	@Autowired
	private SysParamInfoService paramMapper;
	
	@Autowired
	private SoldierBasicInfoOseService sbioMapper;
	
	@Autowired
	private SoldierRegisterService srMapper;
	
	@Value("${rootpath}")
    private String rootPath;
	
//	private String context="基础数据管理->";
	
	
	/**
	 * 导入2019年自主就业士兵信息
	 * @param filePath 上传的excel
	 * @param ybId 批次ID
	 * @return
	 */
	@RequestMapping(value = "impSoldierSelfData", method = RequestMethod.GET,produces="application/json; charset=utf-8")
	public String impSoldierSelfData(@RequestParam("filePath") MultipartFile filePath,String year){
		ResultVo resultVo = new ResultVo();
		
		if(StringUtils.isEmpty(year)){
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败:年度不能为空");
			return resultVo.toString();
		}
		
		ExcelUtils uet = new ExcelUtils();
		List<String[][]> list  = uet.getExcelData(filePath,1);
		if (list == null || list.isEmpty()) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败：数据为空");
			return resultVo.toString();
		}
		//获取第一个sheet
		String[][] Sheets = list.get(0);
		
//		String[] columnName = {"序号", "大单位", "地区", "姓名","身份证号","性别","民族","出生年月（格式要求：yyyy.mm）",
//				"原部别","入伍年月（格式要求：yyyy.mm）","退伍时间（格式要求：yyyy.mm.dd）","现军衔等级","现服役时间(格式：X年X个月)",
//				"入伍地","安置地","易地安置原因","婚姻状况","配偶姓名","政治面貌","入党/入团时间（格式要求：yyyy.mm）",
//				"文化程度","伤残性质","伤残等级","是否属于艰苦地区","艰边地区(如是艰边地区请填写具体地点及年限)",
//				"立功及表彰情况","处分情况","手机号","就业情况"};
//
//		//判断导入的excel数据表头是否与定义的模板列名一致，不一致提示错误字段
//		if(Sheets[0].length!=columnName.length){
//			String result=ExcelUtils.verificationStudentExcelHeadLine(Sheets[0], columnName);												
//			if(!StringUtils.isEmpty(result)){
//				resultVo.setStatusCode(-1);
//				resultVo.setMessage("导入失败：表头列名【"+result.substring(0,result.length()-1)+"】与模板列名不一致");
//				return resultVo.toString();
//			}
//		}
		
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
		
		List<SoldierBasicInfoError> errList = new ArrayList<>();
		
		String date = DateUtils.getStringCurrentDate();
		
		//存储基础信息变更数据，用于返回导入页面提示
		List<Map<String, String>> arrList = new ArrayList<>();
			
		List<SoldierBasicInfoOse> tmpList = new ArrayList<>();
		
		int colNum=29;//定义表格多少列
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
				
				SoldierBasicInfoOse tmpvo = new SoldierBasicInfoOse();
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
				tmpvo.setRelocateReasonCode(CharUtils.replaceAllNull(rows[15]));
				tmpvo.setMarriageStatus(CharUtils.replaceAllNull(rows[16]));
				tmpvo.setSpouseName(CharUtils.replaceAllNull(rows[17]));
				tmpvo.setPoliAffiCode(CharUtils.replaceAllNull(rows[18]));
				tmpvo.setTimJoiParty(CharUtils.replaceAllNull(rows[19]));
				tmpvo.setEduLevCode(CharUtils.replaceAllNull(rows[20]));
				tmpvo.setProfessional(CharUtils.replaceAllNull(rows[21]));
				tmpvo.setDisabilityCategoriesCode(CharUtils.replaceAllNull(rows[22]));
				tmpvo.setDisableGradeCode(CharUtils.replaceAllNull(rows[23]));
				tmpvo.setIsHardArea(CharUtils.replaceAllNull(rows[24]));
				tmpvo.setHardAreaRemark(CharUtils.replaceAllNull(rows[25]));
				tmpvo.setRewardRemark(CharUtils.replaceAllNull(rows[26]));
				tmpvo.setPunishmentRemark(CharUtils.replaceAllNull(rows[27]));
				tmpvo.setPhone(CharUtils.replaceAllNull(rows[28]));
				tmpvo.setJobMessage(CharUtils.replaceAllNull(rows[29]));
				
				tmpList.add(tmpvo);
			}
			rowcount++;
		}
		number=tmpList.size();
		for (int i = 0; i < tmpList.size(); i++) {
			System.out.println("---------------"+i);
			String errorMsg="";//失败原因
			boolean flag=true;
			
			SoldierBasicInfoOse tmpvo=tmpList.get(i);
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
			SoldierBasicInfoOse vo = new SoldierBasicInfoOse();
			BeanUtils.copyProperties(tmpvo,vo);   
			
			if(!StringUtils.isEmpty(tmpvo.getName())){
				vo.setName(tmpvo.getName());
			}
			if(!StringUtils.isEmpty(tmpvo.getIdcard())){
				vo.setIdcard(tmpvo.getIdcard());
			}
			if(!StringUtils.isEmpty(tmpvo.getSex())){
				rlist=  ParamUtils.checkList(plist, "sex", "", tmpvo.getSex());
				if(rlist.size()>0){
					vo.setSex(rlist.get(0).getParamKey());
				}else{
					vo.setSex(null);
				}
			}else{
				vo.setSex(null);
			}
			if(!StringUtils.isEmpty(tmpvo.getNation())){
				rlist=  ParamUtils.checkList(plist, "nation", "", tmpvo.getNation());
				if(rlist.size()>0){
					vo.setNation(rlist.get(0).getParamKey());
				}else{
					vo.setNation(null);
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
				}else{
					vo.setBigUnitName(null);
				}
			}else{
				vo.setBigUnitName(null);
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
				}else{
					vo.setSoldiersMilitaryRank(null);
				}
			}else{
				vo.setSoldiersMilitaryRank(null);
			}
			
			if(!StringUtils.isEmpty(tmpvo.getMarriageStatus())){
				rlist=  ParamUtils.checkList(plist, "marriageStatus", "", tmpvo.getMarriageStatus());
				if(rlist.size()>0){
					vo.setMarriageStatus(rlist.get(0).getParamKey());
				}else{
					vo.setMarriageStatus(null);
				}
			}else{
				vo.setMarriageStatus(null);
			}
			
			if(!StringUtils.isEmpty(tmpvo.getDomicile())){
				
				regional=ParamUtils.getRegional(srList, tmpvo.getDomicile(), "");
				if(regional.size()>0){
					vo.setDomicile(regional.get(0).getRegionalCode());
				}else{
					vo.setDomicile(null);
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
				}
			}
			
			if(!StringUtils.isEmpty(tmpvo.getPhone())){
				vo.setPhone(tmpvo.getPhone());
			}else{
				vo.setPhone(null);
			}
			
			
			//异地安置原因
			if(!StringUtils.isEmpty(tmpvo.getRelocateReasonCode())){
				rlist=  ParamUtils.checkList(plist, "relocateReasonCode", "", tmpvo.getRelocateReasonCode());
				if(rlist.size()>0){
					vo.setRelocateReasonCode(rlist.get(0).getParamKey());
				}else{
					vo.setRelocateReasonCode(null);
				}
			}else{
				vo.setRelocateReasonCode(null);
			}
			
			//政治面貌
			if(!StringUtils.isEmpty(tmpvo.getPoliAffiCode())){
				rlist=  ParamUtils.checkList(plist, "poliAffiCode", "", tmpvo.getPoliAffiCode());
				if(rlist.size()>0){
					vo.setPoliAffiCode(rlist.get(0).getParamKey());
				}else{
					vo.setPoliAffiCode(null);
				}
			}else{
				vo.setPoliAffiCode(null);
			}
			
			//文化程度
			if(!StringUtils.isEmpty(tmpvo.getEduLevCode())){
				rlist=  ParamUtils.checkList(plist, "eduLevCode", "", tmpvo.getEduLevCode());
				if(rlist.size()>0){
					vo.setEduLevCode(rlist.get(0).getParamKey());
				}else{
					vo.setEduLevCode(null);
				}
			}else{
				vo.setEduLevCode(null);
			}
			//伤残性质
			if(!StringUtils.isEmpty(tmpvo.getDisabilityCategoriesCode())){
				rlist=  ParamUtils.checkList(plist, "disabilityCategoriesCode", "", tmpvo.getDisabilityCategoriesCode());
				if(rlist.size()>0){
					vo.setDisabilityCategoriesCode(rlist.get(0).getParamKey());
				}else{
					vo.setDisabilityCategoriesCode(null);
				}
			}else{
				vo.setDisabilityCategoriesCode(null);
			}
			//伤残等级
			if(!StringUtils.isEmpty(tmpvo.getDisableGradeCode())){
				rlist=  ParamUtils.checkList(plist, "disableGradeCode", "", tmpvo.getDisableGradeCode());
				if(rlist.size()>0){
					vo.setDisableGradeCode(rlist.get(0).getParamKey());
				}else{
					vo.setDisableGradeCode(null);
				}
			}else{
				vo.setDisableGradeCode(null);
			}
			
			//是否属于艰苦地区
			if(!StringUtils.isEmpty(tmpvo.getIsHardArea())){
				rlist=  ParamUtils.checkList(plist, "isOrNo", "", tmpvo.getIsHardArea());
				if(rlist.size()>0){
					vo.setIsHardArea(rlist.get(0).getParamKey());
				}else{
					vo.setIsHardArea(null);
				}
			}else{
				vo.setIsHardArea(null);
			}
			
			
			//根据入伍时间、退伍时间计算出服役总月数
			String enlistedDateTmp="";
			if(!StringUtils.isEmpty(tmpvo.getEnlistedDate())){
				String[] tmp = tmpvo.getEnlistedDate().split("\\.");
				if(tmp.length<3){
					enlistedDateTmp=tmpvo.getEnlistedDate().replaceAll("\\.", "-")+"-01";
				}else{
					enlistedDateTmp=tmpvo.getEnlistedDate().replaceAll("\\.", "-");
				}
			}
			
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
			
			
			SoldierBasicInfoError ervo = new SoldierBasicInfoError();
			BeanUtils.copyProperties(tmpvo,ervo);
			
			if(flag){
				vo.setYear(year);
				vo.setRegisterStatus("1");
				vo.setNewResetType("2");//设置选择安置方式为 自主就业
				
				SoldierBasicInfoOse record = new SoldierBasicInfoOse();
				record.setIdcard(vo.getIdcard());
				//根据身份证号查询数据，判断数据是否已存在
				List<SoldierBasicInfoOse> sbioList = sbioMapper.selectAll(record);
				if(sbioList.size()>0){
					vo.setSbiId(sbioList.get(0).getSbiId());
					sbioMapper.updateByPrimaryKey(vo);
				}else{
					sbioMapper.insert(vo);
				}
				successCount++;
			}else{
				ervo.setRemark(errorMsg.substring(0,errorMsg.length()-1));
				errList.add(ervo);
				errorCount++;
			}
		}				
			
		
		//错误数据大于0则生成错误excel表
		if(errList.size()>0){
			try {
				Map<String, Object> beans = new HashMap<String, Object>();
				XLSTransformer transformer = new XLSTransformer();
				InputStream is = this.getClass().getResourceAsStream("/templates/error/自主就业退役士兵信息导入失败模板.xls");
				String title1="浙江省"+DateUtils.getStringYearDate()+"年自主就业退役士兵信息汇总表";
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
		resultVo.setArrList(JSON.toJSON(arrList).toString());
    	return resultVo.toString();
    }
	
	
	/**
	 * 人员综合信息库-查询自主就业地区级联数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "querySoldierTreeNodes",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public JSONArray querySoldierTreeNodes(@RequestBody JSONObject json){
		String areaId=request.getHeader("areaId");
		
		Map<String, String> map=new HashMap<>();
		map.put("year", json.get("year").toString());
		map.put("personnelType", json.get("personnelType").toString());
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
		arr1 = JSONArray.parseArray(JSONArray.toJSONString(sbioMapper.queryAreaCascadeData(map)));//查询出总人数
		
		map.put("flag", "2");
		JSONArray arr2 = JSONArray.parseArray(JSONArray.toJSONString(sbioMapper.queryAreaCascadeData(map)));
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
			JSONArray arr3 = JSONArray.parseArray(JSONArray.toJSONString(sbioMapper.queryAreaCascadeData(map)));
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
	
	/**
	 * 人员综合信息库-查询自主就业退役人员综合信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryOseSoldierData", method = RequestMethod.POST, produces = "application/json")
	public PageInfo<SoldierBasicInfoOse> queryOseSoldierData(@RequestBody SoldierBasicInfoOse vo) {
		String areaId=request.getHeader("areaId");
		String regionalCode = vo.getRegionalCode();
		//判断用户归属地 330000为省级用户
		if(!areaId.equals("330000")){
			if(areaId.endsWith("00")){//行政区划编码00结尾的 为地市用户
				if(StringUtils.isEmpty(regionalCode)){
					vo.setRegionalCode(areaId.substring(0,4));
				}else{
					if(regionalCode.endsWith("00")){
						vo.setRegionalCode(areaId.substring(0,4));
					}else{
						vo.setRegionalCode(regionalCode);
					}	
				}
			}else{
				vo.setRegionalCode(areaId);
			}
		}else{
			if(!StringUtils.isEmpty(regionalCode)){
				if(!regionalCode.equals("330000")){
					if(regionalCode.endsWith("00")){
						vo.setRegionalCode(regionalCode.substring(0,4));
					}else{
						vo.setRegionalCode(regionalCode);
					}
				}else{
					vo.setRegionalCode("");
				}
			}
		}
		
		//查询所有参数配置表所有参数信息
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		List<SysParamInfo> rlist= null;
		
		//一次查询出所有行政区划数据
		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
		List<SysRegional> regional= null;
		
		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		List<SoldierBasicInfoOse> list = sbioMapper.selectAll(vo);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setIdcard(list.get(i).getIdcard().replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
			 
			if(!StringUtils.isEmpty(list.get(i).getBigUnitName())){
				rlist=  ParamUtils.checkList(plist, "bigUnitName", list.get(i).getBigUnitName() , "");
				if(rlist.size()>0){
					list.get(i).setBigUnitName(rlist.get(0).getParamValue());
				}
			}
			 
			if(!StringUtils.isEmpty(list.get(i).getDomicile())){
				regional=ParamUtils.getRegional(srList, "", list.get(i).getDomicile());
				if(regional.size()>0){
					list.get(i).setDomicile(regional.get(0).getRegionalName());
				}
			}
			if(!StringUtils.isEmpty(list.get(i).getResetPlace())){
				regional=ParamUtils.getRegional(srList, "", list.get(i).getResetPlace());
				if(regional.size()>0){
					list.get(i).setResetPlace(regional.get(0).getRegionalName());
				}
			}
		}
		PageInfo<SoldierBasicInfoOse> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 根据ID查询详细数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "selectByPrimaryKey", method = RequestMethod.POST, produces = "application/json")
	public SoldierBasicInfoOse selectByPrimaryKey(@RequestBody SoldierBasicInfoOse vo) {
		SoldierBasicInfoOse sbivo = sbioMapper.selectByPrimaryKey(vo.getSbiId());
		return sbivo;
	}
	
	//------------------地市报到管理 自主就业-----------------
	/**
	 * 人员综合信息库-查询自主就业地区级联数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryRegisterTreeNodes",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public JSONArray queryRegisterTreeNodes(@RequestBody JSONObject json){
		String areaId=request.getHeader("areaId");
		
		Map<String, String> map=new HashMap<>();
		map.put("year", json.get("year").toString());
		map.put("personnelType", json.get("personnelType").toString());
		if(!StringUtils.isEmpty(json.get("registerStatus").toString())){
			map.put("registerStatus", json.get("registerStatus").toString());
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
		arr1 = JSONArray.parseArray(JSONArray.toJSONString(sbioMapper.queryAreaCascadeData(map)));//查询出总人数
		
		map.put("flag", "2");
		JSONArray arr2 = JSONArray.parseArray(JSONArray.toJSONString(sbioMapper.queryAreaCascadeData(map)));
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
			JSONArray arr3 = JSONArray.parseArray(JSONArray.toJSONString(sbioMapper.queryAreaCascadeData(map)));
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
	
	/**
	 * 地市报到管理-查询自主就业报到士兵数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryOseRegisterData", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageInfo<SoldierRegister> queryOseRegisterData(@RequestBody SoldierRegister vo) {
		String areaId=request.getHeader("areaId");
		String regionalCode = vo.getRegionalCode();
		if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
			if(areaId.endsWith("00")){//机构编号00结尾的 为地市用户
				if(StringUtils.isEmpty(regionalCode)){
					vo.setRegionalCode(areaId.substring(0,4));
				}else{
					if(regionalCode.equals("330000")){
						vo.setRegionalCode(areaId.substring(0,4));
					}else if(regionalCode.endsWith("00")){
						vo.setRegionalCode(regionalCode.substring(0,4));
					}else{
						vo.setRegionalCode(regionalCode);
					}
				}
			}else{
				vo.setRegionalCode(areaId);
			}
		}else{
			if(!StringUtils.isEmpty(regionalCode) && !regionalCode.equals("330000")){
				if(regionalCode.endsWith("00")){
					vo.setRegionalCode(regionalCode.substring(0,4));
				}else{
					vo.setRegionalCode(regionalCode);
				}
			}else{
				vo.setRegionalCode("");
			}
		}
		
		//查询所有参数配置表所有参数信息
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		List<SysParamInfo> rlist= null;
		//一次查询出所有行政区划数据
		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
		List<SysRegional> regional= null;
		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		List<SoldierRegister> list = sbioMapper.selectAllOseRegister(vo);
		for (int i = 0; i < list.size(); i++) {
			String idcard = list.get(i).getIdcard();
			list.get(i).setIdcardStr(idcard.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
			list.get(i).setIdcard(idcard);
			if(!StringUtils.isEmpty(list.get(i).getResetType())){
				rlist=  ParamUtils.checkList(plist, "resetType", list.get(i).getResetType() , "");
				if(rlist.size()>0){
					list.get(i).setResetType(rlist.get(0).getParamValue());
				}
			}
			if(!StringUtils.isEmpty(list.get(i).getNewResetType())){
				rlist=  ParamUtils.checkList(plist, "newResetType", list.get(i).getNewResetType() , "");
				if(rlist.size()>0){
					list.get(i).setNewResetType(rlist.get(0).getParamValue());
				}
			}
			if(!StringUtils.isEmpty(list.get(i).getRegisterStatus())){
				rlist=  ParamUtils.checkList(plist, "registerStatus", list.get(i).getRegisterStatus() , "");
				if(rlist.size()>0){
					list.get(i).setRegisterStatus(rlist.get(0).getParamValue());
				}
			}
			if(!StringUtils.isEmpty(list.get(i).getDomicile())){
				regional=ParamUtils.getRegional(srList, "", list.get(i).getDomicile());
				if(regional.size()>0){
					list.get(i).setDomicile(regional.get(0).getRegionalName());
				}
			}
			if(!StringUtils.isEmpty(list.get(i).getResetPlace())){
				regional=ParamUtils.getRegional(srList, "", list.get(i).getResetPlace());
				if(regional.size()>0){
					list.get(i).setResetPlace(regional.get(0).getRegionalName());
				}
				list.get(i).setRegionalName(list.get(i).getResetPlace().substring(3));
			}
		}
		
		PageInfo<SoldierRegister> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 保存自主就业士兵报到信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "saveOseRegister",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public String saveOseRegister(@RequestBody SoldierRegister vo){
		String displayName=CharUtils.getDisplayName(request);
		
		ResultVo resultVo = new ResultVo();
		
		try {
			//报到变更士兵基础信息表中的报到状态
			sbioMapper.updateRegisterStatus(vo.getIdcard());
			
			SoldierRegister sr = new SoldierRegister();
			sr.setIdcard(vo.getIdcard());
			sr.setOperator(displayName);
			sr.setRegisterTime(vo.getRegisterTime());
			SoldierRegister check = srMapper.querySR(vo.getIdcard());
			//判断是否存在报到记录，存在则更新，不存在则新增
			if(check!=null){
				srMapper.updateSR(sr);
			}else{
				srMapper.insertSR(sr);
			}
			resultVo.setStatusCode(200);
			resultVo.setMessage("报到成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultVo.setStatusCode(-1);
			resultVo.setMessage("报到失败：保存出现异常");
		}
    	return resultVo.toString();
    }
	
	//------------------安置方式管理 自主就业-----------------
	/**
	 * 数据管理-查询地区级联数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryResetTreeNodes",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public JSONArray queryResetTreeNodes(@RequestBody JSONObject json){
		String areaId=request.getHeader("areaId");
		
		Map<String, String> map=new HashMap<>();
		map.put("year", json.get("year").toString());
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
		arr1 = JSONArray.parseArray(JSONArray.toJSONString(sbioMapper.queryAreaCascadeData(map)));//查询出总人数
		
		map.put("flag", "2");
		JSONArray arr2 = JSONArray.parseArray(JSONArray.toJSONString(sbioMapper.queryAreaCascadeData(map)));
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
			JSONArray arr3 = JSONArray.parseArray(JSONArray.toJSONString(sbioMapper.queryAreaCascadeData(map)));
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
	
	/**
	 * 安置方式管理查询自主就业士兵数据列表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryOseResetData", method = RequestMethod.POST, produces = "application/json")
	public PageInfo<SoldierBasicInfoOse> queryOseResetData(@RequestBody SoldierBasicInfoOse vo) {
		String areaId=request.getHeader("areaId");
		String regionalCode = vo.getRegionalCode();
		//判断用户归属地 330000为省级用户
		if(!areaId.equals("330000")){
			if(areaId.endsWith("00")){//行政区划编码00结尾的 为地市用户
				if(StringUtils.isEmpty(regionalCode)){
					vo.setRegionalCode(areaId.substring(0,4));
				}else{
					if(regionalCode.endsWith("00")){
						vo.setRegionalCode(areaId.substring(0,4));
					}else{
						vo.setRegionalCode(regionalCode);
					}	
				}
			}else{
				vo.setRegionalCode(areaId);
			}
		}else{
			if(!StringUtils.isEmpty(regionalCode)){
				if(!regionalCode.equals("330000")){
					if(regionalCode.endsWith("00")){
						vo.setRegionalCode(regionalCode.substring(0,4));
					}else{
						vo.setRegionalCode(regionalCode);
					}
				}else{
					vo.setRegionalCode("");
				}
			}
		}
		//查询所有参数配置表所有参数信息
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		List<SysParamInfo> rlist= null;
		
		//一次查询出所有行政区划数据
		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
		List<SysRegional> regional= null;
		
		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		List<SoldierBasicInfoOse> list = sbioMapper.selectAll(vo);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setIdcardStr(list.get(i).getIdcard().replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
			if(!StringUtils.isEmpty(list.get(i).getNewResetType())){
				rlist=  ParamUtils.checkList(plist, "newResetType", list.get(i).getNewResetType() , "");
				if(rlist.size()>0){
					list.get(i).setNewResetType(rlist.get(0).getParamValue());
				}
			}
			
			if(!StringUtils.isEmpty(list.get(i).getDomicile())){
				regional=ParamUtils.getRegional(srList, "", list.get(i).getDomicile());
				if(regional.size()>0){
					list.get(i).setDomicile(regional.get(0).getRegionalName());
				}
			}
			if(!StringUtils.isEmpty(list.get(i).getResetPlace())){
				regional=ParamUtils.getRegional(srList, "", list.get(i).getResetPlace());
				if(regional.size()>0){
					list.get(i).setResetPlace(regional.get(0).getRegionalName());
				}
			}
		}
		PageInfo<SoldierBasicInfoOse> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}
}
