package com.insigma.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.insigma.po.ResultVo;
import com.insigma.po.SysParamInfo;
import com.insigma.po.soldier.ArchivesScoringForm;
import com.insigma.po.soldier.AuditLog;
import com.insigma.po.soldier.BatchManagement;
import com.insigma.po.soldier.PersonnelArchivesLog;
import com.insigma.po.soldier.ReceivingNotice;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SoldierBasicInfoStatic;
import com.insigma.po.soldier.SoldierResetInfo;
import com.insigma.po.soldier.SysRegional;
import com.insigma.service.ArchivesScoringFormService;
import com.insigma.service.AuditLogService;
import com.insigma.service.BatchManagementService;
import com.insigma.service.PersonnelArchivesLogService;
import com.insigma.service.ReceivingNoticeService;
import com.insigma.service.SoldierBasicInfoService;
import com.insigma.service.SysParamInfoService;
import com.insigma.service.SysRegionalService;
import com.insigma.util.CharUtils;
import com.insigma.util.DateUtils;
import com.insigma.util.ExcelUtils;
import com.insigma.util.FileUtils;
import com.insigma.util.InetAddressInUse;
import com.insigma.util.JavaBeanUtils;
import com.insigma.util.ParamUtils;
import com.insigma.util.ValidatorUtils;
import com.insigma.util.WordUtils;

import net.sf.json.JSONObject;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;


/**
 *       
 * 类描述：档案管理->审核   
 * 创建人：liuwm   
 * 创建时间：2020年3月4日 下午1:56:29   
 * @version
 */
@RestController
@RequestMapping("/pvrpm/archives")
public class ArchivesManagementController {
	
	@Autowired
    HttpServletRequest request;
	
	@Autowired
	private SoldierBasicInfoService mapper;
	
	@Autowired
	private SysParamInfoService paramMapper;
	
	@Autowired
	private AuditLogService logMapper;
	
	@Autowired
	private ArchivesScoringFormService asfMapper;
	
	@Autowired
	private ReceivingNoticeService rnMapper;
	
	@Autowired
	private SysRegionalService srsMapper;
	
	@Autowired
	private BatchManagementService bmMapper;

	@Autowired
	private PersonnelArchivesLogService palMapper;
	
	@Value("${rootpath}")
    private String rootPath;//文件存放根目录
	
	private String context="档案管理->";
	
	private static final  Logger logger =LoggerFactory.getLogger(ArchivesManagementController.class);
	
	/**
	 * 查询审核数据列表
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "querySoldierList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageInfo<SoldierBasicInfo> querySoldierList(@RequestBody SoldierBasicInfo vo) {
		String areaId=request.getHeader("areaId");
		
		//查询所有参数配置表所有参数信息
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		//一次查询出所有行政区划数据
		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
		
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
		
		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		List<SoldierBasicInfo> list = mapper.querySoldierList(vo);
		CharUtils.codeToName(plist, srList, list);
		PageInfo<SoldierBasicInfo> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 档案管理-查询地区级联数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "querySoldierTreeNodes",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public JSONArray querySoldierTreeNodes(@RequestBody JSONObject json){
		String areaId=request.getHeader("areaId");
		
		Map<String, String> map=new HashMap<>();
		map.put("ybId", json.get("ybId").toString());
		if(!StringUtils.isEmpty(json.get("status").toString())){
			map.put("status", json.get("status").toString());
		}
		
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
	
	/**
	 * 根据ID查询数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "selectByPrimaryKey", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public SoldierBasicInfo selectByPrimaryKey(@RequestBody SoldierBasicInfo vo) {
		SoldierBasicInfo sbivo = mapper.selectByPrimaryKey(vo.getSbiId());
		SoldierResetInfo tmpsrivo = new SoldierResetInfo();
		tmpsrivo.setSbiId(sbivo.getSbiId());
		sbivo.setSrivo(tmpsrivo);
		return sbivo;
	}
	
	/**
	 * 核对数据是否有做变更，修改则提示变更字段
	 * @param vo
	 * @return
	 */
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
	 * 省级审核档案
	 * 1、通过；2、审核材料待补充；3、 材料补充完成待审核；4、退档
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "updateData",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public String updateData(@RequestBody SoldierBasicInfo vo){
		ResultVo resultVo = new ResultVo();
		String displayName=CharUtils.getDisplayName(request);
		String areaId=request.getHeader("areaId");
		
//		if(!CheckIdCard.isValidatedAllIdcard(vo.getIdcard())){
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("审核失败：请输入合法的身份证号");
//			return resultVo.toString();
//		}
		try {
			//根据身份证号查询数据，判断是否被其他人占用
			SoldierBasicInfo sbiVo = mapper.checkSoldierBasicInfo(vo.getIdcard());
			if(sbiVo!=null){
				if(!vo.getSbiId().equals(sbiVo.getSbiId()) && !vo.getName().equals(sbiVo.getName())){
					resultVo.setStatusCode(-1);
					resultVo.setMessage("审核失败：当前身份证号已被【"+sbiVo.getName()+"】占用");
					return resultVo.toString();
				}
			}
			
			CharUtils.nullToEmpty(vo);
			if(StringUtils.isEmpty(vo.getBigUnitName())){
				vo.setBigUnitName(null);
			}else{
				int smrTmp=Integer.parseInt(vo.getBigUnitName());
				//设置人员类型 1、退役士兵；2、退出消防兵
				if(smrTmp>=51 && smrTmp<=83){
					vo.setPersonnelType("2");
				}else{
					vo.setPersonnelType("1");
				}
			}
//			if(StringUtils.isEmpty(vo.getSoldiersMilitaryRank())){
//				vo.setSoldiersMilitaryRank(null);
//			}else{
//				int smrTmp=Integer.parseInt(vo.getSoldiersMilitaryRank());
//				//设置人员类型 1、退役士兵；2、退出消防兵
//				if(smrTmp>=20 && smrTmp<=23){
//					vo.setPersonnelType("2");
//				}else{
//					vo.setPersonnelType("1");
//				}
//			}
			if(StringUtils.isEmpty(vo.getResetPlace())){
				vo.setResetPlace(null);
			}else{
				//判断安置地是否变更到外省，如变更到外省数据归属地regionalCode不做变更
				if(vo.getResetPlace().startsWith("330000")){
					String[] regionalCode=vo.getResetPlace().split("\\,");
					vo.setRegionalCode(regionalCode[2]);
				}
				
				//判断是否易地安置 1省内易地安置 2省外易地安置
				if(!StringUtils.isEmpty(vo.getDomicile())){
					if(!vo.getDomicile().substring(0,6).equals("330000")){
						vo.setIsRelocateReason("2");
					}else{
						if(!vo.getDomicile().substring(0,13).equals(vo.getResetPlace().substring(0,13))){
							vo.setIsRelocateReason("1");
						}
					}
				}
			}
			
			if(!StringUtils.isEmpty(vo.getArchivesScore()) ){
				BigDecimal xs1 = new BigDecimal("0.6");
				BigDecimal s1 = new BigDecimal(vo.getArchivesScore());
				vo.setTotalScore(s1.multiply(xs1).toString());
			}
			
			int resultFlag=mapper.updateArchivesAudit(vo);
			if(resultFlag==1){
				//根据审核状态获取中文描述
				Map<String, String> map = new HashMap<>();
				map.put("groupKey", "archivesStatus");
				map.put("paramKey", vo.getStatus()+"");
				List<SysParamInfo> list= paramMapper.getSysParamInfo(map);
				
				String auditDept="省级审核";
				if(!areaId.equals("330000")){
					if(areaId.endsWith("00")){
						auditDept="地市审核";
					}else{
						auditDept="区县审核";
					}
				}
				
				//保存审核日志
				AuditLog al = new AuditLog();
				al.setSbiId(vo.getSbiId());
				al.setAuditDept(auditDept);
				al.setAuditResult(list.get(0).getParamValue());
				al.setOperator(displayName);
				al.setAuditTime(DateUtils.parseDate(new Date(),DateUtils.dateTime));
				al.setRemark(vo.getRemark());
				al.setReturnRemark(vo.getReturnRemark());
				logMapper.insert(al);
				
				PersonnelArchivesLog log = null;
				
				//审核档案过程中关键字段值变更，记录变更日志
				if(!StringUtils.isEmpty(vo.getValidatorMsg())){
					log = new PersonnelArchivesLog();
					log.setSbiId(vo.getSbiId());
					log.setOperator(displayName);
					log.setContext(context+"数据变更-"+vo.getValidatorMsg());	
					//保存更新日志
					palMapper.insert(log);
				}
				//保存操作日志
				log = new PersonnelArchivesLog();
				log.setSbiId(vo.getSbiId());
				log.setOperator(displayName);
				log.setContext(context+"档案审核-"+list.get(0).getParamValue());
				palMapper.insert(log);
				
				resultVo.setStatusCode(200);
				resultVo.setMessage(list.get(0).getParamValue()+"成功");
				logger.info("========审核成功========");
			}else{
				resultVo.setStatusCode(-1);
				resultVo.setMessage("审核失败：审核出现异常");
				logger.info("========审核失败：审核更新出现异常========");
			}
		} catch (Exception e) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("审核失败");
			logger.info("========审核失败：审核出现异常========");
		}
    	return resultVo.toString();
    }
	
	/**
	 * 保存士兵档案信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "saveSoldierBasicInfo",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public String saveSoldierBasicInfo(@RequestBody SoldierBasicInfo vo){
		ResultVo resultVo = new ResultVo();
		String displayName=CharUtils.getDisplayName(request);
		try {
			int resultFlag=0;
			int sbiId = 0;
			CharUtils.nullToEmpty(vo);
			//保存操作日志
			PersonnelArchivesLog log = new PersonnelArchivesLog();
			
			//计算综合分，档案分乘0.6系数
			if(!StringUtils.isEmpty(vo.getArchivesScore()) ){
				BigDecimal xs1 = new BigDecimal("0.6");
				BigDecimal s1 = new BigDecimal(vo.getArchivesScore());
				vo.setTotalScore(s1.multiply(xs1).toString());
			}
			
			//如果数据不存在，则新增数据
			if(vo.getSbiId()== null){
				
				//判断安置地是否变更到外省，如变更到外省数据归属地regionalCode不做变更
				if(vo.getResetPlace().startsWith("330000")){
					String[] regionalCode=vo.getResetPlace().split("\\,");
					vo.setRegionalCode(regionalCode[2]);
				}
				
				vo.setDataType(2);
				vo.setResetType(1);
				vo.setCheckStatus(1);
				vo.setCheckTime(DateUtils.parseDate(new Date(),DateUtils.dateTime));
				resultFlag=mapper.insert(vo);
				sbiId = vo.getSbiId();
				
				log.setSbiId(sbiId);
				log.setOperator(displayName);
				log.setContext(context+"档案审核-新增数据");
				
			}else{
				resultFlag=mapper.updateArchivesAudit(vo);
				sbiId = vo.getSbiId();
				
				log.setSbiId(sbiId);
				log.setOperator(displayName);
				log.setContext(context+"档案审核-保存数据");
			}
			//保存操作日志
			palMapper.insert(log);
			
			if(resultFlag==1){
				resultVo.setStatusCode(200);
				resultVo.setMessage("保存档案成功");
				resultVo.setList("[{\"sbiId\":\""+sbiId+"\"}]");
			}else{
				resultVo.setStatusCode(-1);
				resultVo.setMessage("保存档案失败：保存出现异常");
			}
		} catch (Exception e) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("保存档案失败：保存出现异常");
		}
    	return resultVo.toString();
    }
	
	
	/**
	 * 导入档案审核结果汇总数据
	 * @param filePath 上传的excel
	 * @param ybId 批次ID
	 * @return
	 */
	@RequestMapping(value = "impArchivesData", method = RequestMethod.GET,produces="application/json; charset=utf-8")
	public String impArchivesData(@RequestParam("filePath") MultipartFile filePath){
		ResultVo resultVo = new ResultVo();
		String displayName=CharUtils.getDisplayName(request);
		//获取文件名称，用于校验失败导出excel文件命名
		String fileName= "";
		if(filePath.getOriginalFilename().matches("^.+\\.(?i)(xls)$")){
			fileName= filePath.getOriginalFilename().substring(0,filePath.getOriginalFilename().length()-4);
		}else{
			fileName= filePath.getOriginalFilename().substring(0,filePath.getOriginalFilename().length()-5);
		}
		ExcelUtils uet = new ExcelUtils();
		List<String[][]> list  = uet.getExcelData(filePath,5);
		if (list == null || list.isEmpty()) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败：数据为空");
			return resultVo.toString();
		}
		int successCount=0;//成功数量
		int errorCount=0;//失败数量
		int number = 0;//第几条数据
		int upCount=0;//数据变更数量
		//查询所有参数配置表所有参数信息
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		List<SysParamInfo> rlist= null;
		
		//一次查询出所有行政区划数据
		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
		List<SysRegional> regional= null;
		//校验失败数据		
		List<SoldierBasicInfoStatic> errList = new ArrayList<>();
		
		//存储操作日志
		List<PersonnelArchivesLog> logList = new ArrayList<>();
		//存储基础信息变更数据，用于返回导入页面提示
		List<Map<String, String>> arrList = new ArrayList<>();
		
		List<AuditLog> auditLogList = new ArrayList<>();
		
		//批量更新士兵信息
		List<SoldierBasicInfo> updateList = new ArrayList<>();
		
		List<SoldierBasicInfoStatic> tmpList = new ArrayList<>();
		String[][] Sheets = list.get(0);
		int colNum=60;//定义表格多少列
		for (String[] rows : Sheets) { // 遍历行
			if (rows != null && rows.length > 1) {
				
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
				SoldierBasicInfoStatic tmpvo = new SoldierBasicInfoStatic();
				tmpvo.setRowno(Integer.parseInt(rows[0]));
				tmpvo.setSbiId(Integer.parseInt(CharUtils.replaceAllNull(rows[1])));
				tmpvo.setBigUnitName(CharUtils.replaceAllNull(rows[2]));
				tmpvo.setRegionalName(CharUtils.replaceAllNull(rows[3]));
				tmpvo.setName(CharUtils.replaceAllNull(rows[4]));
				tmpvo.setEnlistedMaterials1(CharUtils.replaceAllNull(rows[5]));
				tmpvo.setEnlistedMaterials2(CharUtils.replaceAllNull(rows[6]));
				tmpvo.setEnlistedMaterials3(CharUtils.replaceAllNull(rows[7]));
				tmpvo.setRetiredMaterials1(CharUtils.replaceAllNull(rows[8]));
				tmpvo.setRetiredMaterials2(CharUtils.replaceAllNull(rows[9]));
				tmpvo.setRetiredMaterials3(CharUtils.replaceAllNull(rows[10]));
				tmpvo.setArchivesScore(CharUtils.replaceAllNull(rows[11]));
				tmpvo.setOfficersMaterials1(CharUtils.replaceAllNull(rows[12]));
				tmpvo.setOfficersMaterials2(CharUtils.replaceAllNull(rows[13]));
				tmpvo.setOfficersMaterials3(CharUtils.replaceAllNull(rows[14]));
				tmpvo.setOfficersMaterials4(CharUtils.replaceAllNull(rows[15]));
				tmpvo.setOfficersMaterials5(CharUtils.replaceAllNull(rows[16]));
				tmpvo.setOfficersMaterials6(CharUtils.replaceAllNull(rows[17]));
				tmpvo.setIsHardArea(CharUtils.replaceAllNull(rows[18]));
				tmpvo.setHardAreaRemark(CharUtils.replaceAllNull(rows[19]));
				tmpvo.setRewardMaterials(CharUtils.replaceAllNull(rows[20]));
				tmpvo.setRewardRemark(CharUtils.replaceAllNull(rows[21]));
				tmpvo.setPunishmentMaterials(CharUtils.replaceAllNull(rows[22]));
				tmpvo.setPunishmentRemark(CharUtils.replaceAllNull(rows[23]));
				tmpvo.setRelocateReasonMaterials1(CharUtils.replaceAllNull(rows[24]));
				tmpvo.setRelocateReasonMaterials2(CharUtils.replaceAllNull(rows[25]));
				tmpvo.setRelocateReasonMaterials3(CharUtils.replaceAllNull(rows[26]));
				tmpvo.setRelocateReasonCode(CharUtils.replaceAllNull(rows[27]));
				tmpvo.setRetiredType(CharUtils.replaceAllNull(rows[28]));
				tmpvo.setIdcardMaterials(CharUtils.replaceAllNull(rows[29]));
				tmpvo.setIdcard(CharUtils.replaceAllNull(rows[30]));
				tmpvo.setMarriageMaterials(CharUtils.replaceAllNull(rows[31]));
				tmpvo.setSpouseName(CharUtils.replaceAllNull(rows[32]));
				tmpvo.setMartyrChildrenMaterials(CharUtils.replaceAllNull(rows[33]));
				tmpvo.setIsMartyrChildren(CharUtils.replaceAllNull(rows[34]));
				tmpvo.setDisabilityCategoriesMaterials(CharUtils.replaceAllNull(rows[35]));
				tmpvo.setDisabilityCategoriesCode(CharUtils.replaceAllNull(rows[36]));
				tmpvo.setDisableGradeCode(CharUtils.replaceAllNull(rows[37]));
				tmpvo.setArchivesAuditRemark(CharUtils.replaceAllNull(rows[38]));
				tmpvo.setSex(CharUtils.replaceAllNull(rows[39]));
				tmpvo.setSoldiersMilitaryRank(CharUtils.replaceAllNull(rows[40]));
				tmpvo.setBirthday(CharUtils.replaceAllNull(rows[41]));
				tmpvo.setEnlistedDate(CharUtils.replaceAllNull(rows[42]));
				tmpvo.setRetiredDate(CharUtils.replaceAllNull(rows[43]));
				tmpvo.setMarriageStatus(CharUtils.replaceAllNull(rows[44]));
				tmpvo.setDomicile(CharUtils.replaceAllNull(rows[45]));
				tmpvo.setResetPlace(CharUtils.replaceAllNull(rows[46]));
				tmpvo.setPhone(CharUtils.replaceAllNull(rows[47]));
				tmpvo.setResetCategory(CharUtils.replaceAllNull(rows[48]));
				tmpvo.setNation(CharUtils.replaceAllNull(rows[49]));
				tmpvo.setFormerUnit(CharUtils.replaceAllNull(rows[50]));
				tmpvo.setServiceDuration(CharUtils.replaceAllNull(rows[51]));
				tmpvo.setPoliAffiCode(CharUtils.replaceAllNull(rows[52]));
				tmpvo.setTimJoiParty(CharUtils.replaceAllNull(rows[53]));
				tmpvo.setEduLevCode(CharUtils.replaceAllNull(rows[54]));
				tmpvo.setProfessional(CharUtils.replaceAllNull(rows[55]));
				tmpvo.setStatus(CharUtils.replaceAllNull(rows[56]));
				tmpvo.setReturnRemark(CharUtils.replaceAllNull(rows[57]));
				tmpvo.setOperator(CharUtils.replaceAllNull(rows[58]));
				tmpvo.setRemark(CharUtils.replaceAllNull(rows[59]));
				tmpList.add(tmpvo);
			}
		}
		
		number=tmpList.size();
		for (int i = 0; i < tmpList.size(); i++) {
			String errorMsg="";//失败原因
			boolean flag=true;
			SoldierBasicInfoStatic tmpvo = tmpList.get(i);
			SoldierBasicInfo vo = new SoldierBasicInfo();
			BeanUtils.copyProperties(tmpvo,vo);
			
			if(StringUtils.isEmpty(tmpvo.getSbiId()+"")){
				flag=false;
				errorMsg="ID不能为空,";
			}
			if(StringUtils.isEmpty(tmpvo.getName())){
				flag=false;
				errorMsg="姓名不能为空,";
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
			if(!StringUtils.isEmpty(vo.getArchivesScore()) ){
				BigDecimal xs1 = new BigDecimal("0.6");
				BigDecimal s1 = new BigDecimal(vo.getArchivesScore());
				vo.setTotalScore(s1.multiply(xs1).toString());
			}else{
				vo.setArchivesScore(null);
			}
			//入伍批准书、入伍审查表、公民体检表
			if(CharUtils.checkMaterialsStatus(tmpvo.getEnlistedMaterials1())){
				vo.setEnlistedMaterials1("1");
			}else{
				vo.setEnlistedMaterials1("0");
			}
			//士兵登记表
			if(CharUtils.checkMaterialsStatus(tmpvo.getEnlistedMaterials2())){
				vo.setEnlistedMaterials2("1");
			}else{
				vo.setEnlistedMaterials2("0");
			}
			//入党（团）志愿书
			if(CharUtils.checkMaterialsStatus(tmpvo.getEnlistedMaterials3())){
				vo.setEnlistedMaterials3("1");
			}else{
				vo.setEnlistedMaterials3("0");
			}
			//《义务兵退出现役登记表》（一式三份）
			if(CharUtils.checkMaterialsStatus(tmpvo.getRetiredMaterials1())){
				vo.setRetiredMaterials1("1");
			}else{
				vo.setRetiredMaterials1("0");
			}
			//《士官退出现役登记表》（一式三份）
			if(CharUtils.checkMaterialsStatus(tmpvo.getRetiredMaterials2())){
				vo.setRetiredMaterials2("1");
			}else{
				vo.setRetiredMaterials2("0");
			}
			//《符合政府安排工作条件退役士兵服役表现量化评分表》（一式两份）
			if(CharUtils.checkMaterialsStatus(tmpvo.getRetiredMaterials3())){
				vo.setRetiredMaterials3("1");
			}else{
				vo.setRetiredMaterials3("0");
			}
			//各期士官选取表
			if(CharUtils.checkMaterialsStatus(tmpvo.getOfficersMaterials1())){
				vo.setOfficersMaterials1("1");
			}else{
				vo.setOfficersMaterials1("0");
			}
			//士官学校毕业的入学批准书
			if(CharUtils.checkMaterialsStatus(tmpvo.getOfficersMaterials2())){
				vo.setOfficersMaterials2("1");
			}else{
				vo.setOfficersMaterials2("0");
			}
			//非军事部门直接招收的《招收士官学历专业审定表》
			if(CharUtils.checkMaterialsStatus(tmpvo.getOfficersMaterials3())){
				vo.setOfficersMaterials3("1");
			}else{
				vo.setOfficersMaterials3("0");
			}
			//非军事部门直接招收的《招收士官专业技能考核评定表》
			if(CharUtils.checkMaterialsStatus(tmpvo.getOfficersMaterials4())){
				vo.setOfficersMaterials4("1");
			}else{
				vo.setOfficersMaterials4("0");
			}
			//普通高等学校毕业生中直接招收士官协议书
			if(CharUtils.checkMaterialsStatus(tmpvo.getOfficersMaterials5())){
				vo.setOfficersMaterials5("1");
			}else{
				vo.setOfficersMaterials5("0");
			}
			//普通高等学校毕业生中直接招收士官学历专业审定表
			if(CharUtils.checkMaterialsStatus(tmpvo.getOfficersMaterials6())){
				vo.setOfficersMaterials6("1");
			}else{
				vo.setOfficersMaterials6("0");
			}
			//是否属于艰苦地区
			if(!StringUtils.isEmpty(tmpvo.getIsHardArea())){
				rlist=  ParamUtils.checkList(plist, "isOrNo", "", tmpvo.getIsHardArea());
				if(rlist.size()>0){
					vo.setIsHardArea(rlist.get(0).getParamKey());
				}
			}
			//奖励及表彰(核验材料)
			if(CharUtils.checkMaterialsStatus(tmpvo.getRewardMaterials())){
				vo.setRewardMaterials("1");
			}else{
				vo.setRewardMaterials("0");
			}
			//处分登记表
			if(CharUtils.checkMaterialsStatus(tmpvo.getPunishmentMaterials())){
				vo.setPunishmentMaterials("1");
			}else{
				vo.setPunishmentMaterials("0");
			}
			//随配偶材料
			if(CharUtils.checkMaterialsStatus(tmpvo.getRelocateReasonMaterials1())){
				vo.setRelocateReasonMaterials1("1");
			}else{
				vo.setRelocateReasonMaterials1("0");
			}
			//随父母材料
			if(CharUtils.checkMaterialsStatus(tmpvo.getRelocateReasonMaterials2())){
				vo.setRelocateReasonMaterials2("1");
			}else{
				vo.setRelocateReasonMaterials2("0");
			}
			//随配偶父母材料
			if(CharUtils.checkMaterialsStatus(tmpvo.getRelocateReasonMaterials3())){
				vo.setRelocateReasonMaterials3("1");
			}else{
				vo.setRelocateReasonMaterials3("0");
			}
			//异地安置原因
			if(!StringUtils.isEmpty(tmpvo.getRelocateReasonCode())){
				rlist=  ParamUtils.checkList(plist, "relocateReasonCode", "", tmpvo.getRelocateReasonCode());
				if(rlist.size()>0){
					vo.setRelocateReasonCode(rlist.get(0).getParamKey());
				}
			}
			//军人公民身份号码表或身份证复印件
			if(CharUtils.checkMaterialsStatus(tmpvo.getIdcardMaterials())){
				vo.setIdcardMaterials("1");
			}else{
				vo.setIdcardMaterials("0");
			}
			
			//婚姻材料
			if(CharUtils.checkMaterialsStatus(tmpvo.getMarriageMaterials())){
				vo.setMarriageMaterials("1");
			}else{
				vo.setMarriageMaterials("0");
			}
			//烈士子女证明材料
			if(CharUtils.checkMaterialsStatus(tmpvo.getMartyrChildrenMaterials())){
				vo.setMartyrChildrenMaterials("1");
			}else{
				vo.setMartyrChildrenMaterials("0");
			}
			//是否是烈士子女
			if(!StringUtils.isEmpty(tmpvo.getIsMartyrChildren())){
				rlist=  ParamUtils.checkList(plist, "isOrNo", "", tmpvo.getIsMartyrChildren());
				if(rlist.size()>0){
					vo.setIsMartyrChildren(rlist.get(0).getParamKey());
				}
			}
			//伤病残评定材料
			if(CharUtils.checkMaterialsStatus(tmpvo.getDisabilityCategoriesMaterials())){
				vo.setDisabilityCategoriesMaterials("1");
			}else{
				vo.setDisabilityCategoriesMaterials("0");
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
			//性别
			if(!StringUtils.isEmpty(tmpvo.getSex())){
				rlist=  ParamUtils.checkList(plist, "sex", "", tmpvo.getSex());
				if(rlist.size()>0){
					vo.setSex(rlist.get(0).getParamKey());
				}
			}else{
				vo.setSex(null);
			}
			//军(警)衔
			if(!StringUtils.isEmpty(tmpvo.getSoldiersMilitaryRank())){
				rlist=  ParamUtils.checkList(plist, "soldMilitRankCode", "", tmpvo.getSoldiersMilitaryRank());
				if(rlist.size()>0){
					vo.setSoldiersMilitaryRank(rlist.get(0).getParamKey());
				}
			}else{
				vo.setSoldiersMilitaryRank(null);
			}
			//退伍时间
			if(!StringUtils.isEmpty(tmpvo.getEnlistedDate()) && !StringUtils.isEmpty(tmpvo.getRetiredDate())){
				//根据入伍时间、退伍时间计算出服役总月数
				String enlistedDate =tmpvo.getEnlistedDate().replaceAll("\\.", "-")+"-01";
				String retiredDateTmp =tmpvo.getRetiredDate().replaceAll("\\.", "-");
				int serviceDuration = DateUtils.getMonthDiff(retiredDateTmp, enlistedDate);
				//现服役时间
				if(!StringUtils.isEmpty(tmpvo.getServiceDuration())){
					if(!tmpvo.getServiceDuration().endsWith("月")){
						vo.setServiceDuration(serviceDuration+"个月");
					}
				}
			}else{
				vo.setServiceDuration(null);
			}
			//婚姻状况
			if(!StringUtils.isEmpty(tmpvo.getMarriageStatus())){
				rlist=  ParamUtils.checkList(plist, "marriageStatus", "", tmpvo.getMarriageStatus());
				if(rlist.size()>0){
					vo.setMarriageStatus(rlist.get(0).getParamKey());
				}
			}else{
				vo.setMarriageStatus(null);
			}
			//入伍所在地
			if(!"".equals(tmpvo.getDomicile())){
				regional=ParamUtils.getRegional(srList, tmpvo.getDomicile(), "");
				if(regional.size()>0){
					vo.setDomicile(regional.get(0).getRegionalCode());
				}else{
					vo.setDomicile(null);
				}
			}
			//拟安置地
			if("".equals(tmpvo.getResetPlace())){
				flag=false;
				errorMsg+="拟安置地不能为空,";
			}else{
				regional=ParamUtils.getRegional(srList, tmpvo.getResetPlace(), "");
				if(regional.size()<1){
					flag=false;
					errorMsg+="拟安置地填写有误,";
				}else{
					vo.setResetPlace(regional.get(0).getRegionalCode());
					
					//判断安置地是否变更到外省，如变更到外省数据归属地regionalCode不做变更
					if(vo.getResetPlace().startsWith("330000")){
						String[] regionalCode=regional.get(0).getRegionalCode().split("\\,");
						vo.setRegionalCode(regionalCode[2]);
					}
					//判断是否易地安置 1省内易地安置 2省外易地安置
					if(!StringUtils.isEmpty(vo.getDomicile())){
						if(!vo.getDomicile().substring(0,6).equals("330000")){
							vo.setIsRelocateReason("2");
						}else{
							if(!vo.getDomicile().substring(0,13).equals(vo.getResetPlace().substring(0,13))){
								vo.setIsRelocateReason("1");
							}
						}
					}
				}
			}
			//安排工作条件
			if(!StringUtils.isEmpty(tmpvo.getResetCategory())){
				rlist=  ParamUtils.checkList(plist, "resetCategory", "", tmpvo.getResetCategory());
				if(rlist.size()>0){
					vo.setResetCategory(rlist.get(0).getParamKey());
				}
			}else{
				vo.setResetCategory(null);
			}
			//民族
			if(!StringUtils.isEmpty(tmpvo.getNation())){
				rlist=  ParamUtils.checkList(plist, "nation", "", tmpvo.getNation());
				if(rlist.size()>0){
					vo.setNation(rlist.get(0).getParamKey());
				}
			}else{
				vo.setNation(null);
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
			//档案审核结果
			if(StringUtils.isEmpty(tmpvo.getStatus())){
				flag=false;
				errorMsg+="档案审核结果不能为空,";
			}else{
				rlist=  ParamUtils.checkList(plist, "archivesStatus", "", tmpvo.getStatus());
				vo.setStatus(Integer.parseInt(rlist.get(0).getParamKey()));
				if(vo.getStatus()>0){
					//档案审核人
					if(StringUtils.isEmpty(tmpvo.getOperator())){
						flag=false;
						errorMsg+="档案审核人不能为空,";
					}
				}
			}
			
			String returnRemark="";
			//退档原因(如档案审核结果为退档可填此项内容)
			if(vo.getStatus()==4){
				if(StringUtils.isEmpty(tmpvo.getReturnRemark())){
					flag=false;
					errorMsg+="退档原因不能为空,";
				}else{
					rlist=  ParamUtils.checkList(plist, "returnRemark", "", tmpvo.getReturnRemark());
					returnRemark=rlist.get(0).getParamKey();
				}
			}
			if(flag){
				vo.setNewResetType(1);
				vo.setCheckStatus(1);
				updateList.add(vo);
				
				//保存审核日志
				AuditLog al = new AuditLog();
				al.setSbiId(vo.getSbiId());
				al.setAuditDept("审核结果导入");
				al.setAuditResult(tmpvo.getStatus());
				al.setOperator(vo.getOperator());
				al.setReturnRemark(returnRemark);
				al.setRemark(vo.getRemark());
				auditLogList.add(al);
				
				//保存操作日志
				PersonnelArchivesLog log = new PersonnelArchivesLog();
				log.setSbiId(vo.getSbiId());
				log.setOperator(displayName);
				log.setContext(context+"审核结果导入");
				logList.add(log);
				
				successCount++;
			}else{
				tmpvo.setErrRemark(errorMsg.substring(0,errorMsg.length()-1));
				errList.add(tmpvo);
				errorCount++;
			}
		}
		
		//批量更新数据
		if(updateList.size()>0){
			mapper.updateArchivesList(updateList);
		}
		//新增操作日志
		if(logList.size()>0){
			palMapper.insertList(logList);
		}
		//新增审核日志
		if(auditLogList.size()>0){
			logMapper.insertList(auditLogList);
		}
		
		//错误数据大于0则生成错误excel表
		if(errList.size()>0){
			try {
				String date=DateUtils.getStringCurrentDate();
				Map<String, Object> beans = new HashMap<String, Object>();
				XLSTransformer transformer = new XLSTransformer();
				
				
				InputStream is = this.getClass().getResourceAsStream("/templates/error/档案审核信息汇总表导入失败模板.xls");
				beans.put("datalist", errList);
				beans.put("title1", "浙江省"+DateUtils.getStringYearDate()+"年由政府安排工作退役士兵、退出消防员档案审核汇总表");
				Workbook workbook = transformer.transformXLS(is, beans);
				workbook.setForceFormulaRecalculation(true);
				is.close();
				String newFileName =  fileName+"-失败数据.xls";
				String subdirectory = "档案审核结果数据/"+date+"/";
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
		}	
		if(errList.size()>0 || arrList.size()>0){
			resultVo.setStatusCode(-1);
		}else{
			resultVo.setStatusCode(200);
		}
		resultVo.setMessage("共导入【"+number+"】条数据,成功【"+successCount+"】条,失败【"+errorCount+"】条,数据变更【"+upCount+"】条");
    	return resultVo.toString();
    }
	
	
	/**
	 * 根据士兵ID查询审核日志信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "selectAuditLog", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String selectAuditLog(@RequestBody AuditLog vo) {
		ResultVo resultVo = new ResultVo();
		if(vo.getSbiId()==null){
			resultVo.setStatusCode(-1);
			resultVo.setMessage("暂无数据");
			return resultVo.toString();
		}
		List<AuditLog> asfList = logMapper.selectAll(vo);
		if(asfList.size()>0){
			resultVo.setStatusCode(200);
			resultVo.setMessage("查询成功");
			resultVo.setList(JavaBeanUtils.beanToJsonStr(asfList));
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("暂无数据");
		}
		return resultVo.toString();
	}
	
	/**
	 * 单个开具接收安置通知书
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "createReceivingNoticePdf",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public String createReceivingNoticePdf(@RequestBody ReceivingNotice vo){
		ResultVo resultVo = new ResultVo();
		String displayName=CharUtils.getDisplayName(request);
		if(StringUtils.isEmpty(vo.getUnitName())){
			resultVo.setStatusCode(-1);
			resultVo.setMessage("部队集中移交机关不能为空");
			return resultVo.toString();
		}
		if(vo.getStartTime().startsWith("undefined月") || vo.getStartTime().endsWith("undefined日")){
			resultVo.setStatusCode(-1);
			resultVo.setMessage("开始日期不能为空");
			return resultVo.toString();
		}
		if(vo.getEndTime().startsWith("undefined月") || vo.getEndTime().endsWith("undefined日")){
			resultVo.setStatusCode(-1);
			resultVo.setMessage("截止日期不能为空");
			return resultVo.toString();
		}
		//查询所有参数配置表所有参数信息
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		List<SysParamInfo> rlist= null;
		
		//一次查询出所有行政区划数据
		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
		List<SysRegional> regional= null;
		
		SoldierBasicInfo sbivo = mapper.selectByPrimaryKey(vo.getSbiId());
		if(sbivo!=null){
			String startTime=vo.getYear()+"-"+vo.getStartTime().replace("月", "-").replace("日", "");
			String endTime=vo.getYear()+"-"+vo.getEndTime().replace("月", "-").replace("日", "");
			
			int days = DateUtils.caculateTotalTime(startTime, endTime);
			vo.setDays(days);
			String bigUnitName="";
			if(!StringUtils.isEmpty(sbivo.getBigUnitName())){
				rlist=  ParamUtils.checkList(plist, "bigUnitName", sbivo.getBigUnitName() , "");
				if(rlist.size()>0){
					bigUnitName=rlist.get(0).getParamValue();
				}
			}
			String regionaoName="";
			if(!StringUtils.isEmpty(sbivo.getResetPlace())){
				regional=ParamUtils.getRegional(srList, "", sbivo.getResetPlace());
				if(regional.size()>0){
					regionaoName=regional.get(0).getRegionalName();
				}
			}
			
			vo.setIdcard(sbivo.getIdcard());
			vo.setName(sbivo.getName());
			vo.setRegionalName(regionaoName.replace("浙江省", ""));
			vo.setYbId(sbivo.getYbId());
			vo.setUnitName(bigUnitName);
			vo.setPersonnelType(Integer.parseInt(sbivo.getPersonnelType()));
			
			WordUtils wu = new WordUtils();
			ReceivingNotice rvo = wu.addMap(vo,rootPath);
			
			if(sbivo.getNoticeStatus()<1){
				rnMapper.insert(rvo);
			}else{
				rnMapper.updateByPrimaryKey(rvo);
			}
			SoldierBasicInfo po = new SoldierBasicInfo();
			po.setSbiId(sbivo.getSbiId());
			po.setNoticeStatus(1);
			mapper.updateByPrimaryKey(po);
			
			resultVo.setStatusCode(200);
			resultVo.setMessage("生成接收安置通知书成功");
			resultVo.setList("\"file/"+rvo.getFilePath()+"\"");
			
			PersonnelArchivesLog log = new PersonnelArchivesLog();
			log.setSbiId(vo.getSbiId());
			log.setOperator(displayName);
			log.setContext(context+"开具通知书–零星开具");
			palMapper.insert(log);
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("生成接收安置通知书异常");
		}
    	return resultVo.toString();
    }
	
	/**
	 * 批量开具接收安置通知书
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "batchCreateReceivingNoticePdf", method = RequestMethod.POST,produces="application/json; charset=utf-8")
//	public String batchCreateReceivingNoticePdf(@RequestParam("filePath") MultipartFile filePath,String startTime,int ybId){
	public String batchCreateReceivingNoticePdf(@RequestBody ReceivingNotice vo){	
		ResultVo resultVo = new ResultVo();
		
		String displayName=CharUtils.getDisplayName(request);
		
		String startTime=vo.getStartTime();
		String endTime=vo.getEndTime();
		String tempStartTime="";
		String tempEndtime="";
		
		int days = DateUtils.caculateTotalTime(startTime, endTime);
		
		try {
			tempStartTime=DateUtils.parseDate(DateUtils.paserStringToDate(startTime, "yyyy-MM-dd"),"yyyy年M月d日");
			tempEndtime=DateUtils.parseDate(DateUtils.paserStringToDate(endTime, "yyyy-MM-dd"),"yyyy年M月d日");
			//截至时间根据开始时间增加30天
//			Date tmpdate = DateUtils.calculateDate(DateUtils.paserStringToDate(startTime, "yyyy-MM-dd"), 0, 0, 30);
//			tempEndtime=DateUtils.parseDate(tmpdate, "yyyy年M月d日");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String year = DateUtils.getStringYearDate();
		List<String> fileList = new ArrayList<>();
		List<PersonnelArchivesLog> logList = new ArrayList<>();
		
		SoldierBasicInfo record = new SoldierBasicInfo();
		record.setYbId(vo.getYbId());
		List<SoldierBasicInfo> list = mapper.queryPrintReceivingNotice(record);
		
		//查询所有参数配置表所有参数信息
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		List<SysParamInfo> rlist= null;
		
		//一次查询出所有行政区划数据
		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
		List<SysRegional> regional= null;
		long start = System.currentTimeMillis();
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				System.out.println("开具通知书-----"+i);
				Map<String, String> seqmap = new HashMap<String, String>();
				seqmap.put("name", "SEQ_RN_NUM");
				seqmap.put("year", DateUtils.getStringYearDate());
				int num = rnMapper.getMaxNum(seqmap);
				//判断序列是否存在，不存在则新增
				if(num<1){
					num=1;
					rnMapper.insertNextval(seqmap);
				}
				//根据所属区域查询父级地市名称
//				SysRegional srvo =srsMapper.queryParentData(list.get(i).getRegionalCode());
				String bigUnitName="";
				if(!StringUtils.isEmpty(list.get(i).getBigUnitName())){
					rlist=  ParamUtils.checkList(plist, "bigUnitName", list.get(i).getBigUnitName() , "");
					if(rlist.size()>0){
						bigUnitName=rlist.get(0).getParamValue();
					}
				}
				String regionaoName="";
				if(!StringUtils.isEmpty(list.get(i).getResetPlace())){
					regional=ParamUtils.getRegional(srList, "", list.get(i).getResetPlace());
					if(regional.size()>0){
						regionaoName=regional.get(0).getRegionalName();
					}
				}
				
				
				ReceivingNotice rnvo = new ReceivingNotice();
				rnvo.setYbId(vo.getYbId());
				rnvo.setYear(Integer.parseInt(year));
				rnvo.setNum(num);
				rnvo.setUnitName(bigUnitName);
				rnvo.setName(list.get(i).getName());
				rnvo.setIdcard(list.get(i).getIdcard());
				rnvo.setRegionalName(regionaoName.substring(3));
				rnvo.setStartTime(tempStartTime.substring(5));
				rnvo.setEndTime(tempEndtime.substring(5));
				rnvo.setPersonnelType(Integer.parseInt(list.get(i).getPersonnelType()));
				rnvo.setDays(days);
				WordUtils wu = new WordUtils();
				//生成word接收安置通知书
				ReceivingNotice rvo = wu.addMap(rnvo,rootPath);
				
				if(list.get(i).getNoticeStatus()<1){
					rnMapper.insert(rvo);
				}else{
					rnMapper.updateByPrimaryKey(rvo);
				}
				 
				SoldierBasicInfo po = new SoldierBasicInfo();
				po.setSbiId(list.get(i).getSbiId());
				po.setNoticeStatus(1);
				mapper.updateByPrimaryKey(po);
				fileList.add(rvo.getFilePath());
				
				PersonnelArchivesLog log = new PersonnelArchivesLog();
				log.setSbiId(list.get(i).getSbiId());
				log.setOperator(displayName);
				log.setContext(context+"开具通知书–批量开具");
				logList.add(log);
			}
		}
		String date = DateUtils.getStringCurrentDate();
		String dir = "接收安置通知书word/"  ;
		
		if(fileList.size()>0){
			try {
				FileUtils.createFilePath(rootPath+dir+"zip/");
				FileOutputStream fos1 = new FileOutputStream(new File(rootPath+dir+"zip/"+date+".zip"));
				WordUtils.toZip(rootPath+dir+date ,fos1,true);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
//			//根据生成的word接收安置通知书，达成zip压缩包
			
//			String path="接收安置通知书word/zip/";
//			FileUtils.createFilePath(rootPath+path);
//			
//			String zipFile = path+DateUtils.getStringCurrentDate()+".zip";
//			WordUtils.fileToZip(fileList,zipFile ,rootPath);
//			
			long end = System.currentTimeMillis();
			System.out.println("压缩完成，耗时：" + (end - start) + " ms");
			resultVo.setStatusCode(200);
			resultVo.setMessage("开具接收安置通知书成功,共开具【"+list.size()+"】张");
			resultVo.setList("\"file/"+dir+"zip/"+date+".zip\"");
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("未查询到可开具接收安置通知书的数据");
		}
		if(logList.size()>0){
			//保存更新日志
			palMapper.insertList(logList);
		}
    	return resultVo.toString();
    }
	
	/**
	 * 获取序列-安置接收通知书字号
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "getSeq", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String getSeq() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "SEQ_RN_NUM");
		map.put("year", DateUtils.getStringYearDate());
		int num = rnMapper.getMaxNum(map);
		if(num<1){
			num=1;
			rnMapper.insertNextval(map);
		}
		ResultVo resultVo = new ResultVo();
		resultVo.setStatusCode(200);
		resultVo.setMessage("查询成功");
		Map<String, String> rmap = new HashMap<String, String>();
		rmap.put("year", DateUtils.getStringYearDate());
		rmap.put("num", num+"");
		JSONObject jsonobject = JSONObject.fromObject(rmap);
		resultVo.setList(jsonobject.toString());
		return resultVo.toString();
	}
	
	/**
	 * 根据身份证号查询接收安置通知书详细信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryReceivingNotice", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReceivingNotice queryReceivingNotice(@RequestBody SoldierBasicInfo vo) {
		ReceivingNotice rnvo = rnMapper.selectByPrimaryKey(vo.getSbiId());
		rnvo.setFilePath("file/"+rnvo.getFilePath());
		return rnvo;
	}
	
	/**
	 * 根据身份证号查询接收安置通知书详细信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "batchExpReceivingNotice", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String batchExpReceivingNotice(@RequestBody ReceivingNotice vo) {
		ResultVo resultVo = new ResultVo();
		Map<String, String> map = new HashMap<>();
		map.put("ybId", vo.getYbId()+"");
		List<ReceivingNotice> list = rnMapper.selectAll(map);
		List<String> fileList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			fileList.add(list.get(i).getFilePath());
		}
		String date = DateUtils.getStringCurrentDate();
		String dir = "接收安置通知书word/"  ;
		if(fileList.size()>0){
			try {
				FileUtils.createFilePath(rootPath+dir+"zip/");
				FileOutputStream fos1 = new FileOutputStream(new File(rootPath+dir+"zip/"+date+".zip"));
				WordUtils.toZip(rootPath+dir+date ,fos1,true);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resultVo.setStatusCode(200);
			resultVo.setMessage("开具接收安置通知书成功,共开具【"+list.size()+"】张");
			resultVo.setList("\"file/"+dir+"zip/"+date+".zip\"");
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导出失败：当前批次未查询到已开具的通知书");
		}
		
//		if(fileList.size()>0){
//			long start = System.currentTimeMillis();
//			String path="接收安置通知书word/zip/";
//			FileUtils.createFilePath(rootPath+path);
//			
//			String zipFile = path+DateUtils.getStringCurrentDate()+".zip";
//			WordUtils.fileToZip(fileList,zipFile,rootPath);
//			
//			long end = System.currentTimeMillis();
//			System.out.println("压缩完成，耗时：" + (end - start) + " ms");
//			
//			resultVo.setStatusCode(200);
//			resultVo.setMessage("导出成功");
//			resultVo.setList("\"file/"+zipFile+"\"");
//		}else{
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("导出失败：当前批次未查询到已开具的通知书");
//		}
		return resultVo.toString();
	}
	
	//==========================================
//	
//	/**
//	 * 查询数据
//	 * @param vo
//	 * @return
//	 */
//	@RequestMapping(value = "queryData", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public PageInfo<SoldierBasicInfo> queryData(@RequestBody SoldierBasicInfo vo) {
//		String areaId=request.getHeader("areaId");
//		String regionalCode = vo.getRegionalCode();
//		
//		if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
//			if(areaId.endsWith("00")){//机构编号00结尾的 为地市用户
//				if(StringUtils.isEmpty(regionalCode)){
//					vo.setRegionalCode(areaId.substring(0,4));
//				}else{
//					vo.setRegionalCode(regionalCode);
//				}
//			}else{
//				vo.setRegionalCode(areaId);
//			}
//			vo.setFlag(2);// 查询标识 地市或区县用户 查询省级已审核的数据
//		}else{
//			vo.setFlag(1);// 查询标识 省级用户查询所有数据
//			if(!StringUtils.isEmpty(regionalCode)){
//				vo.setRegionalCode(regionalCode.substring(0,4));
//			}
//		}
//		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
//		List<SoldierBasicInfo> list = mapper.queryAuditData(vo);
//		PageInfo<SoldierBasicInfo> pageInfo=new PageInfo<>(list);
//		return pageInfo;
//	}
//	
//	
//	
//	
//	
//	
//	
//	/**
//	 * 上传士兵相关材料文件
//	 * @param file
//	 * @param sbiId
//	 * @return
//	 */
//	@RequestMapping(value = "uploadFile", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
//	public String uploadFile(@RequestParam("file") MultipartFile file,int sbiId) {
//		
//		FileUtils ft = new FileUtils();
//		ResultVo resultVo = new ResultVo();
//		
//		//存储上传的文件，文件根据士兵ID新建文件夹存放
//		String archivesFilePath="archives/"+sbiId+"/";
//		String filePath=rootPath+archivesFilePath;
//		
//		String filename="";
//		try {
//			String ip = InetAddressInUse.getIp();
//			filename = ft.upload(file, filePath);
//			resultVo.setStatusCode(200);
//			resultVo.setMessage("文件上传成功");
//			resultVo.setList("\""+ip+"/file/"+archivesFilePath+filename+"\"");
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("文件上传处理失败");
//		} catch (IOException e) {
//			e.printStackTrace();
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("文件上传处理失败");
//		}
//		return resultVo.toString();
//	}
//	
//	
//	
//	/**
//	 * 省级审核档案保存量化评分表
//	 * @param vo
//	 * @return
//	 */
//	@RequestMapping(value = "saveScore",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
//    public String saveScore(@RequestBody ArchivesScoringForm vo){
//		ResultVo resultVo = new ResultVo();
//		try {
//			int resultFlag=0;
//			List<ArchivesScoringForm> asfList = asfMapper.selectAll(vo);
//			vo.setCreateTime(DateUtils.parseDate(new Date(), DateUtils.dateTime));
//			if(asfList.size()>0){
//				resultFlag=asfMapper.updateByPrimaryKey(vo);
//			}else{
//				resultFlag=asfMapper.insert(vo);
//			}
//			if(resultFlag==1){
//				resultVo.setStatusCode(200);
//				resultVo.setMessage("保存评分表成功");
//			}else{
//				resultVo.setStatusCode(-1);
//				resultVo.setMessage("保存评分表失败：保存出现异常");
//			}
//		} catch (Exception e) {
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("保存评分表失败：保存出现异常");
//		}
//    	return resultVo.toString();
//    }
//	
//	
//	/**
//	 * 根据士兵ID查询量化评分表数据
//	 * @param vo
//	 * @return
//	 */
//	@RequestMapping(value = "selectScore", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public String selectScore(@RequestBody ArchivesScoringForm vo) {
//		ResultVo resultVo = new ResultVo();
//		List<ArchivesScoringForm> asfList = asfMapper.selectAll(vo);
//		if(asfList.size()>0){
//			resultVo.setStatusCode(200);
//			resultVo.setMessage("查询成功");
//			resultVo.setList(JavaBeanUtils.beanToJsonStr(asfList));
//		}else{
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("暂无数据");
//		}
//		return resultVo.toString();
//	}
//	
//	
//	
//	
//	
//	
//	/**
//	 * 查询非集中审核数据
//	 * @param vo
//	 * @return
//	 */
//	@RequestMapping(value = "querySporadicData", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public PageInfo<SoldierBasicInfo> querySporadicData(@RequestBody SoldierBasicInfo vo) {
//		String areaId=request.getHeader("areaId");
//		String regionalCode = vo.getRegionalCode();
//		vo.setDataType(2);
//		
//		List<SoldierBasicInfo> list =null;
//		if(!areaId.equals("330000")){
//			if(areaId.endsWith("00")){
//				if(StringUtils.isEmpty(regionalCode)){
//					vo.setRegionalCode(areaId.substring(0,4));
//				}else{
//					vo.setRegionalCode(regionalCode);
//				}
//				vo.setFlag(2);//设置查询标志 2表示地市查询 可查询出区县已审核通过和省级审核过的的数据
//			}else{
//				vo.setFlag(1);// 可查询所有数据
//				vo.setRegionalCode(areaId);
//			}
//		}else{
//			vo.setFlag(3);//设置查询标志 3表示省级查询  可查询出地市已审核通过的数据
//		}
//		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
//		list = mapper.querySporadicAuditData(vo);
//		PageInfo<SoldierBasicInfo> pageInfo=new PageInfo<>(list);
//		return pageInfo;
//	}
//	
//	/**
//	 * 安排工作非集中档案审核
//	 * 10、初审通过待复审；11、复审通过待终审；12、复审不通过；13、终审通过；14、终审不通过
//	 * @param vo
//	 * @return
//	 */
//	@RequestMapping(value = "sporadicUpdateData",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
//    public String sporadicUpdateData(@RequestBody SoldierBasicInfo vo){
//		ResultVo resultVo = new ResultVo();
//		
//		String areaId=request.getHeader("areaId");
//		if(StringUtils.isEmpty(areaId)){
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("审核失败：机构ID不能为空");
//			return resultVo.toString();
//		}
//		
//		try {
//			if(vo.getSbiId()== null){
//				vo.setRegionalCode(areaId);
//				vo.setCheckStatus(1);
////				vo.setStatus(0);
//				vo.setDataType(2);
//				vo.setResetType(1);
//				vo.setCheckTime(DateUtils.parseDate(new Date(),DateUtils.dateTime));
//				mapper.insert(vo);
////				vo.getSbiId();
//				vo.setCreateTime("");
//			}
//			int resultFlag=mapper.updateByPrimaryKey(vo);
//			if(resultFlag==1){
//				String displayName="";
//				try {
//					displayName= URLDecoder.decode(request.getHeader("displayName"), "UTF-8");
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
//				
//				//根据审核状态获取中文描述
//				Map<String, String> map = new HashMap<>();
//				map.put("groupKey", "archivesStatus");
//				map.put("paramKey", vo.getStatus()+"");
//				List<SysParamInfo> list= paramMapper.getSysParamInfo(map);
//				
//				String auditDept="省级审核";
//				if(!areaId.equals("330000")){
//					if(areaId.endsWith("00")){
//						auditDept="地市审核";
//					}else{
//						auditDept="区县审核";
//					}
//				}
//				
//				//保存审核日志
//				AuditLog al = new AuditLog();
//				al.setSbiId(vo.getSbiId());
//				al.setAuditDept(auditDept);
//				al.setAuditResult(list.get(0).getParamValue());
//				al.setOperator(displayName);
//				al.setAuditTime(DateUtils.parseDate(new Date(),DateUtils.dateTime));
//				al.setRemark(vo.getRemark());
//				logMapper.insert(al);
//				
//				resultVo.setStatusCode(200);
//				resultVo.setMessage("审核成功");
//				logger.info("========审核成功========");
//			}else{
//				resultVo.setStatusCode(-1);
//				resultVo.setMessage("审核失败：审核出现异常");
//				logger.info("========审核失败：审核更新出现异常========");
//			}
//		} catch (Exception e) {
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("审核失败");
//			logger.info("========审核失败：审核出现异常========");
//		}
//    	return resultVo.toString();
//    }
//	
//	
//	/**
//	 * 档案库查询审核通过的数据
//	 * @param vo
//	 * @return
//	 */
//	@RequestMapping(value = "queryArchivesDetailed", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public PageInfo<SoldierBasicInfo> queryArchivesDetailed(@RequestBody SoldierBasicInfo vo) {
//		String areaId=request.getHeader("areaId");
//		String regionalCode = vo.getRegionalCode();
//		
//		List<SoldierBasicInfo> list =null;
//		if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
//			if(areaId.endsWith("00")){//机构编号00结尾的 为地市用户
//				if(StringUtils.isEmpty(regionalCode)){
//					vo.setRegionalCode(areaId.substring(0,4));
//				}else{
//					vo.setRegionalCode(regionalCode);
//				}
//			}else{
//				vo.setRegionalCode(areaId);
//			}
//			vo.setFlag(2);// 查询标识 地市或区县用户 查询省级已审核的数据
//		}else{
//			vo.setFlag(1);// 查询标识 省级用户查询所有数据
//		}
//		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
//		list = mapper.queryArchivesDetailed(vo);
//		PageInfo<SoldierBasicInfo> pageInfo=new PageInfo<>(list);
//		return pageInfo;
//	}
//	
//	
//	/**
//	 * 新增自主就业退役士兵档案
//	 * @param vo
//	 * @return
//	 */
//	@RequestMapping(value = "addAOESoldierBasicInfo1",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
//    public String addAOESoldierBasicInfo(@RequestBody SoldierBasicInfo vo){
//		ResultVo resultVo = new ResultVo();
//		try {
//			int resultFlag=0;
//			int sbiId = 0;
//			SoldierBasicInfo pvo = new SoldierBasicInfo();
//			pvo.setPhone(vo.getIdcard());
//			//根据手机号查询数据。判断是否已存在数据
//			List<SoldierBasicInfo> tmpvo = mapper.querySoldierBasicInfo(pvo);
//			if(tmpvo.size()>0){
//				
//				resultVo.setStatusCode(-1);
//				resultVo.setMessage("保存档案失败：已存在当前人员信息，请勿重复保存!");
//				
//				return resultVo.toString();
//			}else{
//				String domicile="浙江省"+vo.getDomicile();
//				String resetPlace = "浙江省"+vo.getResetPlace();
//				
//				SysRegional record = new SysRegional();
//				record.setRegionalName(resetPlace);
//				List<SysRegional> srList = srsMapper.selectAllName(record);
//				
//				vo.setDomicile(domicile);
//				vo.setResetPlace(resetPlace);
//				vo.setRegionalCode(srList.get(0).getRegionalCode());
//				vo.setCheckStatus(1);
//				vo.setStatus(0);
//				vo.setDataType(2);
//				vo.setResetType(2);
//				vo.setCheckTime(DateUtils.parseDate(new Date(),DateUtils.dateTime));
//				resultFlag=mapper.insert(vo);
//				sbiId = vo.getSbiId();
//			}
//			if(resultFlag==1){
//				resultVo.setStatusCode(200);
//				resultVo.setMessage("保存档案成功");
//				resultVo.setList("[{\"sbiId\":\""+sbiId+"\"}]");
//			}else{
//				resultVo.setStatusCode(-1);
//				resultVo.setMessage("保存档案失败：保存出现异常");
//			}
//		} catch (Exception e) {
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("保存档案失败：保存出现异常");
//		}
//    	return resultVo.toString();
//    }
//	
//	
//	
//	
//	//----------------------------------单机版-----------------------------------
//	
//	
//	
//	
//	/**
//	 * 地市审档账号导入档案待审核数据
//	 * @param filePath 上传的excel
//	 * @param ybId 批次ID
//	 * @return
//	 */
//	@RequestMapping(value = "impArchivesAuditData1", method = RequestMethod.GET,produces="application/json; charset=utf-8")
//	public String impArchivesAuditData(@RequestParam("filePath") MultipartFile filePath,String year,String batchName){
//		ResultVo resultVo = new ResultVo();
//		String displayName=CharUtils.getDisplayName(request);
//
//		if(StringUtils.isEmpty(year)){
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("导入失败:年度不能为空");
//			return resultVo.toString();
//		}
//		if(StringUtils.isEmpty(batchName)){
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("导入失败:所属批次不能为空");
//			return resultVo.toString();
//		}
//		
//		//获取文件名称，用于校验失败导出excel文件命名
//		String fileName= "";
//		if(filePath.getOriginalFilename().matches("^.+\\.(?i)(xls)$")){
//			fileName= filePath.getOriginalFilename().substring(0,filePath.getOriginalFilename().length()-4);
//		}else{
//			fileName= filePath.getOriginalFilename().substring(0,filePath.getOriginalFilename().length()-5);
//		}
//		ExcelUtils uet = new ExcelUtils();
//		List<String[][]> list  = uet.getExcelData(filePath,5);
//		if (list == null || list.isEmpty()) {
//			resultVo.setStatusCode(-1);
//			resultVo.setMessage("导入失败：数据为空");
//			return resultVo.toString();
//		}
//		int successCount=0;//成功数量
//		int errorCount=0;//失败数量
//		int number = 0;//第几条数据
//		int upCount=0;//数据变更数量
//		//查询所有参数配置表所有参数信息
//		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
//		List<SysParamInfo> rlist= null;
//		
//		//一次查询出所有行政区划数据
//		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
//		List<SysRegional> regional= null;
//		//校验失败数据		
//		List<SoldierBasicInfoStatic> errList = new ArrayList<>();
//		//批量新增士兵信息
//		List<SoldierBasicInfo> insertList = new ArrayList<>();
//		//存储操作日志
//		List<PersonnelArchivesLog> logList = new ArrayList<>();
//		//存储基础信息变更数据，用于返回导入页面提示
//		List<Map<String, String>> arrList = new ArrayList<>();
//		
//		List<AuditLog> auditLogList = new ArrayList<>();
//		
//		//批量更新士兵信息
//		List<SoldierBasicInfo> updateList = new ArrayList<>();
//		
//		List<SoldierBasicInfoStatic> tmpList = new ArrayList<>();
//		
//		BatchManagement bm = new BatchManagement();
//		bm.setOperator(displayName);
//		bm.setYear(Integer.parseInt(year));
//		bm.setBatchName(year+"年"+batchName);
//		bm.setReceivingFilePath("部平台基础信息");
//		List<BatchManagement> bmlist = bmMapper.selectAll(bm);
//		int ybId = 0;
//		//判断批次是否已存在，存在则取已有批次ID，不存在则新建批次并获取新的批次ID
//		if(bmlist.size()>0){
//			ybId=bmlist.get(0).getYbId();
//		}else{
//			bmMapper.insert(bm);
//			ybId=bm.getYbId();
//		}
//		
//		String[][] Sheets = list.get(0);
//		int colNum=59;//定义表格多少列
//		for (String[] rows : Sheets) { // 遍历行
//			if (rows != null && rows.length > 1) {
//				
//				if("".equals(rows[1])&&"".equals(rows[2])&&"".equals(rows[3])&&"".equals(rows[4])){
//					break;
//				}
//				//最后一列未读取到会报错，找不到数据。new一个新的数组存放空值。
//				String[] newRows = new String[colNum];
//				if(rows.length<colNum){
//					for (int i = 0; i < rows.length; i++) {
//						newRows[i]=rows[i];
//					}
//					rows = newRows;
//				}
//				SoldierBasicInfoStatic tmpvo = new SoldierBasicInfoStatic();
//				tmpvo.setRowno(Integer.parseInt(rows[0]));
//				tmpvo.setSbiId(Integer.parseInt(CharUtils.replaceAllNull(rows[1])));
//				tmpvo.setBigUnitName(CharUtils.replaceAllNull(rows[2]));
//				tmpvo.setRegionalName(CharUtils.replaceAllNull(rows[3]));
//				tmpvo.setName(CharUtils.replaceAllNull(rows[4]));
//				tmpvo.setEnlistedMaterials1(CharUtils.replaceAllNull(rows[5]));
//				tmpvo.setEnlistedMaterials2(CharUtils.replaceAllNull(rows[6]));
//				tmpvo.setEnlistedMaterials3(CharUtils.replaceAllNull(rows[7]));
//				tmpvo.setRetiredMaterials1(CharUtils.replaceAllNull(rows[8]));
//				tmpvo.setRetiredMaterials2(CharUtils.replaceAllNull(rows[9]));
//				tmpvo.setRetiredMaterials3(CharUtils.replaceAllNull(rows[10]));
//				tmpvo.setArchivesScore(CharUtils.replaceAllNull(rows[11]));
//				tmpvo.setOfficersMaterials1(CharUtils.replaceAllNull(rows[12]));
//				tmpvo.setOfficersMaterials2(CharUtils.replaceAllNull(rows[13]));
//				tmpvo.setOfficersMaterials3(CharUtils.replaceAllNull(rows[14]));
//				tmpvo.setOfficersMaterials4(CharUtils.replaceAllNull(rows[15]));
//				tmpvo.setOfficersMaterials5(CharUtils.replaceAllNull(rows[16]));
//				tmpvo.setOfficersMaterials6(CharUtils.replaceAllNull(rows[17]));
//				tmpvo.setIsHardArea(CharUtils.replaceAllNull(rows[18]));
//				tmpvo.setHardAreaRemark(CharUtils.replaceAllNull(rows[19]));
//				tmpvo.setRewardMaterials(CharUtils.replaceAllNull(rows[20]));
//				tmpvo.setRewardRemark(CharUtils.replaceAllNull(rows[21]));
//				tmpvo.setPunishmentMaterials(CharUtils.replaceAllNull(rows[22]));
//				tmpvo.setPunishmentRemark(CharUtils.replaceAllNull(rows[23]));
//				tmpvo.setRelocateReasonMaterials1(CharUtils.replaceAllNull(rows[24]));
//				tmpvo.setRelocateReasonMaterials2(CharUtils.replaceAllNull(rows[25]));
//				tmpvo.setRelocateReasonMaterials3(CharUtils.replaceAllNull(rows[26]));
//				tmpvo.setRelocateReasonCode(CharUtils.replaceAllNull(rows[27]));
//				tmpvo.setRetiredType(CharUtils.replaceAllNull(rows[28]));
//				tmpvo.setIdcardMaterials(CharUtils.replaceAllNull(rows[29]));
//				tmpvo.setIdcard(CharUtils.replaceAllNull(rows[30]));
//				tmpvo.setMarriageMaterials(CharUtils.replaceAllNull(rows[31]));
//				tmpvo.setSpouseName(CharUtils.replaceAllNull(rows[32]));
//				tmpvo.setMartyrChildrenMaterials(CharUtils.replaceAllNull(rows[33]));
//				tmpvo.setIsMartyrChildren(CharUtils.replaceAllNull(rows[34]));
//				tmpvo.setDisabilityCategoriesMaterials(CharUtils.replaceAllNull(rows[35]));
//				tmpvo.setDisabilityCategoriesCode(CharUtils.replaceAllNull(rows[36]));
//				tmpvo.setDisableGradeCode(CharUtils.replaceAllNull(rows[37]));
//				tmpvo.setArchivesAuditRemark(CharUtils.replaceAllNull(rows[38]));
//				tmpvo.setSex(CharUtils.replaceAllNull(rows[39]));
//				tmpvo.setSoldiersMilitaryRank(CharUtils.replaceAllNull(rows[40]));
//				tmpvo.setBirthday(CharUtils.replaceAllNull(rows[41]));
//				tmpvo.setEnlistedDate(CharUtils.replaceAllNull(rows[42]));
//				tmpvo.setRetiredDate(CharUtils.replaceAllNull(rows[43]));
//				tmpvo.setMarriageStatus(CharUtils.replaceAllNull(rows[44]));
//				tmpvo.setDomicile(CharUtils.replaceAllNull(rows[45]));
//				tmpvo.setResetPlace(CharUtils.replaceAllNull(rows[46]));
//				tmpvo.setPhone(CharUtils.replaceAllNull(rows[47]));
//				tmpvo.setResetCategory(CharUtils.replaceAllNull(rows[48]));
//				tmpvo.setNation(CharUtils.replaceAllNull(rows[49]));
//				tmpvo.setFormerUnit(CharUtils.replaceAllNull(rows[50]));
//				tmpvo.setServiceDuration(CharUtils.replaceAllNull(rows[51]));
//				tmpvo.setPoliAffiCode(CharUtils.replaceAllNull(rows[52]));
//				tmpvo.setTimJoiParty(CharUtils.replaceAllNull(rows[53]));
//				tmpvo.setEduLevCode(CharUtils.replaceAllNull(rows[54]));
//				tmpvo.setStatus(CharUtils.replaceAllNull(rows[55]));
//				tmpvo.setReturnRemark(CharUtils.replaceAllNull(rows[56]));
//				tmpvo.setOperator(CharUtils.replaceAllNull(rows[57]));
//				tmpvo.setRemark(CharUtils.replaceAllNull(rows[58]));
//				tmpList.add(tmpvo);
//			}
//		}
//		number=tmpList.size();
//		for (int i = 0; i < tmpList.size(); i++) {
//			String errorMsg="";//失败原因
//			boolean flag=true;
//			SoldierBasicInfoStatic tmpvo = tmpList.get(i);
//			SoldierBasicInfo vo = new SoldierBasicInfo();
//			BeanUtils.copyProperties(tmpvo,vo);
//			vo.setYbId(ybId);
//			if(StringUtils.isEmpty(tmpvo.getSbiId()+"")){
//				flag=false;
//				errorMsg="ID不能为空,";
//			}
//			
//			if(StringUtils.isEmpty(tmpvo.getName())){
//				flag=false;
//				errorMsg="姓名不能为空,";
//			}
//			if(!StringUtils.isEmpty(tmpvo.getBigUnitName())){
//				rlist=  ParamUtils.checkList(plist, "bigUnitName", "", tmpvo.getBigUnitName());
//				if(rlist.size()>0){
//					vo.setBigUnitName(rlist.get(0).getParamKey());
//				}else{
//					vo.setBigUnitName(null);
//				}
//			}else{
//				vo.setBigUnitName(null);
//			}
//			//入伍批准书、入伍审查表、公民体检表
//			if(CharUtils.checkMaterialsStatus(tmpvo.getEnlistedMaterials1())){
//				vo.setEnlistedMaterials1("1");
//			}else{
//				vo.setEnlistedMaterials1("0");
//			}
//			//士兵登记表
//			if(CharUtils.checkMaterialsStatus(tmpvo.getEnlistedMaterials2())){
//				vo.setEnlistedMaterials2("1");
//			}else{
//				vo.setEnlistedMaterials2("0");
//			}
//			//入党（团）志愿书
//			if(CharUtils.checkMaterialsStatus(tmpvo.getEnlistedMaterials3())){
//				vo.setEnlistedMaterials3("1");
//			}else{
//				vo.setEnlistedMaterials3("0");
//			}
//			//《义务兵退出现役登记表》（一式三份）
//			if(CharUtils.checkMaterialsStatus(tmpvo.getRetiredMaterials1())){
//				vo.setRetiredMaterials1("1");
//			}else{
//				vo.setRetiredMaterials1("0");
//			}
//			//《士官退出现役登记表》（一式三份）
//			if(CharUtils.checkMaterialsStatus(tmpvo.getRetiredMaterials2())){
//				vo.setRetiredMaterials2("1");
//			}else{
//				vo.setRetiredMaterials2("0");
//			}
//			//《符合政府安排工作条件退役士兵服役表现量化评分表》（一式两份）
//			if(CharUtils.checkMaterialsStatus(tmpvo.getRetiredMaterials3())){
//				vo.setRetiredMaterials3("1");
//			}else{
//				vo.setRetiredMaterials3("0");
//			}
//			//各期士官选取表
//			if(CharUtils.checkMaterialsStatus(tmpvo.getOfficersMaterials1())){
//				vo.setOfficersMaterials1("1");
//			}else{
//				vo.setOfficersMaterials1("0");
//			}
//			//士官学校毕业的入学批准书
//			if(CharUtils.checkMaterialsStatus(tmpvo.getOfficersMaterials2())){
//				vo.setOfficersMaterials2("1");
//			}else{
//				vo.setOfficersMaterials2("0");
//			}
//			//非军事部门直接招收的《招收士官学历专业审定表》
//			if(CharUtils.checkMaterialsStatus(tmpvo.getOfficersMaterials3())){
//				vo.setOfficersMaterials3("1");
//			}else{
//				vo.setOfficersMaterials3("0");
//			}
//			//非军事部门直接招收的《招收士官专业技能考核评定表》
//			if(CharUtils.checkMaterialsStatus(tmpvo.getOfficersMaterials4())){
//				vo.setOfficersMaterials4("1");
//			}else{
//				vo.setOfficersMaterials4("0");
//			}
//			//普通高等学校毕业生中直接招收士官协议书
//			if(CharUtils.checkMaterialsStatus(tmpvo.getOfficersMaterials5())){
//				vo.setOfficersMaterials5("1");
//			}else{
//				vo.setOfficersMaterials5("0");
//			}
//			//普通高等学校毕业生中直接招收士官学历专业审定表
//			if(CharUtils.checkMaterialsStatus(tmpvo.getOfficersMaterials6())){
//				vo.setOfficersMaterials6("1");
//			}else{
//				vo.setOfficersMaterials6("0");
//			}
//			//是否属于艰苦地区
//			if(!StringUtils.isEmpty(tmpvo.getIsHardArea())){
//				rlist=  ParamUtils.checkList(plist, "isOrNo", "", tmpvo.getIsHardArea());
//				if(rlist.size()>0){
//					vo.setIsHardArea(rlist.get(0).getParamKey());
//				}else{
//					vo.setIsHardArea(null);
//				}
//			}
//			//奖励及表彰(核验材料)
//			if(CharUtils.checkMaterialsStatus(tmpvo.getRewardMaterials())){
//				vo.setRewardMaterials("1");
//			}else{
//				vo.setRewardMaterials("0");
//			}
//			//处分登记表
//			if(CharUtils.checkMaterialsStatus(tmpvo.getPunishmentMaterials())){
//				vo.setPunishmentMaterials("1");
//			}else{
//				vo.setPunishmentMaterials("0");
//			}
//			//随配偶材料
//			if(CharUtils.checkMaterialsStatus(tmpvo.getRelocateReasonMaterials1())){
//				vo.setRelocateReasonMaterials1("1");
//			}else{
//				vo.setRelocateReasonMaterials1("0");
//			}
//			//随父母材料
//			if(CharUtils.checkMaterialsStatus(tmpvo.getRelocateReasonMaterials2())){
//				vo.setRelocateReasonMaterials2("1");
//			}else{
//				vo.setRelocateReasonMaterials2("0");
//			}
//			//随配偶父母材料
//			if(CharUtils.checkMaterialsStatus(tmpvo.getRelocateReasonMaterials3())){
//				vo.setRelocateReasonMaterials3("1");
//			}else{
//				vo.setRelocateReasonMaterials3("0");
//			}
//			//异地安置原因
//			if(!StringUtils.isEmpty(tmpvo.getRelocateReasonCode())){
//				rlist=  ParamUtils.checkList(plist, "relocateReasonCode", "", tmpvo.getRelocateReasonCode());
//				if(rlist.size()>0){
//					vo.setRelocateReasonCode(rlist.get(0).getParamKey());
//				}else{
//					vo.setRelocateReasonCode(null);
//				}
//			}
//			//军人公民身份号码表或身份证复印件
//			if(CharUtils.checkMaterialsStatus(tmpvo.getIdcardMaterials())){
//				vo.setIdcardMaterials("1");
//			}else{
//				vo.setIdcardMaterials("0");
//			}
//			
//			//婚姻材料
//			if(CharUtils.checkMaterialsStatus(tmpvo.getMarriageMaterials())){
//				vo.setMarriageMaterials("1");
//			}else{
//				vo.setMarriageMaterials("0");
//			}
//			//烈士子女证明材料
//			if(CharUtils.checkMaterialsStatus(tmpvo.getMartyrChildrenMaterials())){
//				vo.setMartyrChildrenMaterials("1");
//			}else{
//				vo.setMartyrChildrenMaterials("0");
//			}
//			//是否是烈士子女
//			if(!StringUtils.isEmpty(tmpvo.getIsMartyrChildren())){
//				rlist=  ParamUtils.checkList(plist, "isOrNo", "", tmpvo.getIsMartyrChildren());
//				if(rlist.size()>0){
//					vo.setIsMartyrChildren(rlist.get(0).getParamKey());
//				}else{
//					vo.setIsMartyrChildren(null);
//				}
//			}
//			//伤病残评定材料
//			if(CharUtils.checkMaterialsStatus(tmpvo.getDisabilityCategoriesMaterials())){
//				vo.setDisabilityCategoriesMaterials("1");
//			}else{
//				vo.setDisabilityCategoriesMaterials("0");
//			}
//			//伤残性质
//			if(!StringUtils.isEmpty(tmpvo.getDisabilityCategoriesCode())){
//				rlist=  ParamUtils.checkList(plist, "disabilityCategoriesCode", "", tmpvo.getDisabilityCategoriesCode());
//				if(rlist.size()>0){
//					vo.setDisabilityCategoriesCode(rlist.get(0).getParamKey());
//				}else{
//					vo.setDisabilityCategoriesCode(null);
//				}
//			}
//			//伤残等级
//			if(!StringUtils.isEmpty(tmpvo.getDisableGradeCode())){
//				rlist=  ParamUtils.checkList(plist, "disableGradeCode", "", tmpvo.getDisableGradeCode());
//				if(rlist.size()>0){
//					vo.setDisableGradeCode(rlist.get(0).getParamKey());
//				}else{
//					vo.setDisableGradeCode(null);
//				}
//			}
//			
//			if(StringUtils.isEmpty(tmpvo.getArchivesAuditRemark())){
//				vo.setArchivesAuditRemark("");
//			}
//			
//			//性别
//			if(!StringUtils.isEmpty(tmpvo.getSex())){
//				rlist=  ParamUtils.checkList(plist, "sex", "", tmpvo.getSex());
//				if(rlist.size()>0){
//					vo.setSex(rlist.get(0).getParamKey());
//				}else{
//					vo.setSex(null);
//				}
//			}else{
//				vo.setSex(null);
//			}
//			//军(警)衔
//			if(!StringUtils.isEmpty(tmpvo.getSoldiersMilitaryRank())){
//				rlist=  ParamUtils.checkList(plist, "soldMilitRankCode", "", tmpvo.getSoldiersMilitaryRank());
//				if(rlist.size()>0){
//					vo.setSoldiersMilitaryRank(rlist.get(0).getParamKey());
//					int smrTmp=Integer.parseInt(rlist.get(0).getParamKey());
//					//设置人员类型 1、退役士兵；2、退出消防兵
//					if(smrTmp>=20 && smrTmp<30){
//						vo.setPersonnelType("2");
//					}else{
//						vo.setPersonnelType("1");
//					}
//				}else{
//					vo.setSoldiersMilitaryRank(null);
//				}
//			}else{
//				vo.setSoldiersMilitaryRank(null);
//			}
//			//退伍时间
//			if(!StringUtils.isEmpty(tmpvo.getEnlistedDate()) && !StringUtils.isEmpty(tmpvo.getRetiredDate())){
//				//根据入伍时间、退伍时间计算出服役总月数
//				String enlistedDate =tmpvo.getEnlistedDate().replaceAll("\\.", "-")+"-01";
//				String retiredDateTmp =tmpvo.getRetiredDate().replaceAll("\\.", "-");
//				int serviceDuration = DateUtils.getMonthDiff(retiredDateTmp, enlistedDate);
//				//现服役时间
//				if(!StringUtils.isEmpty(tmpvo.getServiceDuration())){
//					if(!tmpvo.getServiceDuration().endsWith("月")){
//						vo.setServiceDuration(serviceDuration+"个月");
//					}
//				}
//			}
//			//婚姻状况
//			if(!StringUtils.isEmpty(tmpvo.getMarriageStatus())){
//				rlist=  ParamUtils.checkList(plist, "marriageStatus", "", tmpvo.getMarriageStatus());
//				if(rlist.size()>0){
//					vo.setMarriageStatus(rlist.get(0).getParamKey());
//				}else{
//					vo.setMarriageStatus(null);
//				}
//			}else{
//				vo.setMarriageStatus(null);
//			}
//			//入伍所在地
//			if(!StringUtils.isEmpty(tmpvo.getDomicile())){
////				flag=false;
////				errorMsg+="入伍前户籍所在地不能为空,";
////			}else{
//				regional=ParamUtils.getRegional(srList, tmpvo.getDomicile(), "");
//				if(regional.size()<1){
//					flag=false;
//					errorMsg+="入伍前户籍所在地填写有误,";
//				}else{
//					vo.setDomicile(regional.get(0).getRegionalCode());
//				}
//			}
//			//拟安置地
//			if("".equals(tmpvo.getResetPlace())){
//				flag=false;
//				errorMsg+="拟安置地不能为空,";
//			}else{
//				regional=ParamUtils.getRegional(srList, tmpvo.getResetPlace(), "");
//				if(regional.size()<1){
//					flag=false;
//					errorMsg+="拟安置地填写有误,";
//				}else{
//					vo.setResetPlace(regional.get(0).getRegionalCode());
//					String[] regionalCode=regional.get(0).getRegionalCode().split("\\,");
//					vo.setRegionalCode(regionalCode[2]);
//					
//					//判断是否易地安置 1省内易地安置 2省外易地安置
//					if(!StringUtils.isEmpty(vo.getDomicile())){
//						if(!vo.getDomicile().substring(0,6).equals("330000")){
//							vo.setIsRelocateReason("2");
//						}else{
//							if(!vo.getDomicile().substring(0,13).equals(vo.getResetPlace().substring(0,13))){
//								vo.setIsRelocateReason("1");
//							}
//						}
//					}
//				}
//			}
//			//安排工作条件
//			if(!StringUtils.isEmpty(tmpvo.getResetCategory())){
//				rlist=  ParamUtils.checkList(plist, "resetCategory", "", tmpvo.getResetCategory());
//				if(rlist.size()>0){
//					vo.setResetCategory(rlist.get(0).getParamKey());
//				}else{
//					vo.setResetCategory(null);
//				}
//			}else{
//				vo.setResetCategory(null);
//			}
//			//民族
//			if(!StringUtils.isEmpty(tmpvo.getNation())){
//				rlist=  ParamUtils.checkList(plist, "nation", "", tmpvo.getNation());
//				if(rlist.size()>0){
//					vo.setNation(rlist.get(0).getParamKey());
//				}else{
//					vo.setNation(null);
//				}
//			}else{
//				vo.setNation(null);
//			}
//			//政治面貌
//			if(!StringUtils.isEmpty(tmpvo.getPoliAffiCode())){
//				rlist=  ParamUtils.checkList(plist, "poliAffiCode", "", tmpvo.getPoliAffiCode());
//				if(rlist.size()>0){
//					vo.setPoliAffiCode(rlist.get(0).getParamKey());
//				}else{
//					vo.setPoliAffiCode(null);
//				}
//			}else{
//				vo.setPoliAffiCode(null);
//			}
//			//文化程度
//			if(!StringUtils.isEmpty(tmpvo.getEduLevCode())){
//				rlist=  ParamUtils.checkList(plist, "eduLevCode", "", tmpvo.getEduLevCode());
//				if(rlist.size()>0){
//					vo.setEduLevCode(rlist.get(0).getParamKey());
//				}else{
//					vo.setEduLevCode(null);
//				}
//			}else{
//				vo.setEduLevCode(null);
//			}
//			//档案审核结果
//			if(StringUtils.isEmpty(tmpvo.getStatus())){
//				flag=false;
//				errorMsg+="档案审核结果不能为空,";
//			}else{
//				rlist=  ParamUtils.checkList(plist, "archivesStatus", "", tmpvo.getStatus());
//				vo.setStatus(Integer.parseInt(rlist.get(0).getParamKey()));
//				if(vo.getStatus()>0){
//					//档案审核人
//					if(StringUtils.isEmpty(tmpvo.getOperator())){
//						flag=false;
//						errorMsg+="档案审核人不能为空,";
//					}
//				}
//			}
//			
//			String returnRemark="";
//			//退档原因(如档案审核结果为退档可填此项内容)
//			if(vo.getStatus()==4){
//				if(StringUtils.isEmpty(tmpvo.getReturnRemark())){
//					flag=false;
//					errorMsg+="退档原因不能为空,";
//				}else{
//					rlist=  ParamUtils.checkList(plist, "returnRemark", "", tmpvo.getReturnRemark());
//					returnRemark=rlist.get(0).getParamKey();
//				}
//			}
//			
//			if(flag){
//				//根据身份证号查询数据，判断数据是否已存在
//				SoldierBasicInfo sbivo = mapper.selectByPrimaryKey(tmpvo.getSbiId());
//				if(sbivo==null){
//					vo.setCheckStatus(1);
//					vo.setDataType(1);
//					vo.setResetType(1);
//					insertList.add(vo);
//					successCount++;
//				}else{
//					updateList.add(vo);
//					
//					//保存审核日志
//					AuditLog al = new AuditLog();
//					al.setSbiId(vo.getSbiId());
//					al.setAuditDept("审核结果导入");
//					al.setAuditResult(tmpvo.getStatus());
//					al.setOperator(vo.getOperator());
//					al.setReturnRemark(returnRemark);
//					auditLogList.add(al);
//					
//					//保存操作日志
//					PersonnelArchivesLog log = new PersonnelArchivesLog();
//					log.setSbiId(vo.getSbiId());
//					log.setOperator(displayName);
//					log.setContext(context+"审核结果导入-更新数据");
//					logList.add(log);
//					
//					successCount++;
//				}
//			}else{
//				tmpvo.setErrRemark(errorMsg.substring(0,errorMsg.length()-1));
//				errList.add(tmpvo);
//				errorCount++;
//			}
//		}
//		//批量新增数据
//		if(insertList.size()>0){
//			mapper.insertList(insertList);
//			for (int i = 0; i < insertList.size(); i++) {
//				//保存操作日志
//				PersonnelArchivesLog log = new PersonnelArchivesLog();
//				log.setSbiId(insertList.get(i).getSbiId());
//				log.setOperator(displayName);
//				log.setContext(context+"待审核档案导入-新增数据");
//				logList.add(log);
//			}
//			
//		}
//		//批量更新数据
//		if(updateList.size()>0){
//			mapper.updateArchivesList(updateList);
//		}
//		//新增操作日志
//		if(logList.size()>0){
//			palMapper.insertList(logList);
//		}
//		//新增审核日志
//		if(auditLogList.size()>0){
//			logMapper.insertList(auditLogList);
//		}
//		
//		//错误数据大于0则生成错误excel表
//		if(errList.size()>0){
//			try {
//				String date=DateUtils.getStringCurrentDate();
//				Map<String, Object> beans = new HashMap<String, Object>();
//				XLSTransformer transformer = new XLSTransformer();
//				
//				
//				InputStream is = this.getClass().getResourceAsStream("/templates/error/档案审核信息汇总表导入失败模板.xls");
//				beans.put("datalist", errList);
//				beans.put("title1", "浙江省"+DateUtils.getStringYearDate()+"年由政府安排工作退役士兵、退出消防员档案审核汇总表");
//				Workbook workbook = transformer.transformXLS(is, beans);
//				workbook.setForceFormulaRecalculation(true);
//				is.close();
//				String newFileName =  fileName+"-失败数据.xls";
//				String subdirectory = "地市待审核档案数据导入失败/"+date+"/";
//				String path = rootPath+subdirectory;
//				boolean isSuccess = ExcelUtils.createExcelFile(path,newFileName, workbook);
//				
//				if(isSuccess){
//					resultVo.setList("\"file/"+subdirectory+newFileName+"\"");
//				}
//			} catch (ParsePropertyException e) {
//				e.printStackTrace();
//			} catch (InvalidFormatException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}	
//		if(errList.size()>0 || arrList.size()>0){
//			resultVo.setStatusCode(-1);
//		}else{
//			resultVo.setStatusCode(200);
//		}
//		resultVo.setMessage("共导入【"+number+"】条数据,成功【"+successCount+"】条,失败【"+errorCount+"】条,数据变更【"+upCount+"】条");
////		resultVo.setArrList(JSON.toJSON(arrList).toString());
//    	return resultVo.toString();
//    }
//	
//	/**
//	 * 查询审核数据列表
//	 * @param vo
//	 * @return
//	 */
//	@RequestMapping(value = "queryDataStatic", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public PageInfo<SoldierBasicInfo> queryDataStatic(@RequestBody SoldierBasicInfo vo) {
//		String areaId=request.getHeader("areaId");
//		
//		//查询所有参数配置表所有参数信息
//		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
//		
//		//一次查询出所有行政区划数据
//		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
//		
//		String regionalCode = vo.getRegionalCode();
//		//判断用户归属地 330000为省级用户
//		if(!areaId.equals("330000")){
//			if(areaId.endsWith("00")){//行政区划编码00结尾的 为地市用户
//				if(StringUtils.isEmpty(regionalCode)){
//					vo.setRegionalCode(areaId.substring(0,4));
//				}else{
//					if(regionalCode.endsWith("00")){
//						vo.setRegionalCode(areaId.substring(0,4));
//					}else{
//						vo.setRegionalCode(regionalCode);
//					}	
//				}
//			}else{
//				vo.setRegionalCode(areaId);
//			}
//		}else{
//			if(!StringUtils.isEmpty(regionalCode)){
//				if(!regionalCode.equals("330000")){
//					if(regionalCode.endsWith("00")){
//						vo.setRegionalCode(regionalCode.substring(0,4));
//					}else{
//						vo.setRegionalCode(regionalCode);
//					}
//				}else{
//					vo.setRegionalCode("");
//				}
//			}
//		}
//		vo.setFlag(1);
//		
//		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
//		List<SoldierBasicInfo> list = mapper.selectAll(vo);
//		CharUtils.codeToName(plist, srList, list);
//		PageInfo<SoldierBasicInfo> pageInfo=new PageInfo<>(list);
//		return pageInfo;
//	}
//	
	
	
	/**
	 * 查询移交情况列表
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryTransferList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageInfo<SoldierBasicInfo> queryTransferList(@RequestBody SoldierBasicInfo vo) {
		String areaId=request.getHeader("areaId");
		
		//查询所有参数配置表所有参数信息
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		List<SysParamInfo> rlist= null;
		
		//一次查询出所有行政区划数据
		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
		List<SysRegional> regional= null;
		
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
		
		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		List<SoldierBasicInfo> list = mapper.querySoldierList(vo);
		CharUtils.codeToName(plist, srList, list);
		
		for (int i = 0; i < list.size(); i++) {
//			list.get(i).setIdcard(list.get(i).getIdcard().replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
			if(!StringUtils.isEmpty(list.get(i).getSex())){
				rlist=  ParamUtils.checkList(plist, "sex", list.get(i).getSex() , "");
				if(rlist.size()>0){
					list.get(i).setSex(rlist.get(0).getParamValue());
				}
			}
			if(!StringUtils.isEmpty(list.get(i).getSoldiersMilitaryRank())){
				rlist=  ParamUtils.checkList(plist, "soldMilitRankCode", list.get(i).getSoldiersMilitaryRank() , "");
				if(rlist.size()>0){
					list.get(i).setSoldiersMilitaryRank(rlist.get(0).getParamValue());
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
			if(StringUtils.isEmpty(list.get(i).getNoticeStatus()+"")){
				list.get(i).setNoticeStatus(0);
			}
		}
		PageInfo<SoldierBasicInfo> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 档案管理-查询移交情况地区级联数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryTransferTreeNodes",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public JSONArray queryTransferTreeNodes(@RequestBody JSONObject json){
		String areaId=request.getHeader("areaId");
		
		Map<String, String> map=new HashMap<>();
		map.put("ybId", json.get("ybId").toString());
		if(!StringUtils.isEmpty(json.get("noticeStatus").toString())){
			map.put("noticeStatus", json.get("noticeStatus").toString());
		}
		map.put("wheTransfer", json.get("wheTransfer").toString());
		
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
}
