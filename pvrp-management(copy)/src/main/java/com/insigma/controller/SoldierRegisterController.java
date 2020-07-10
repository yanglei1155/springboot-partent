package com.insigma.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.insigma.po.ResultVo;
import com.insigma.po.SysParamInfo;
import com.insigma.po.soldier.FlexibleEmploymentInfo;
import com.insigma.po.soldier.PersonnelArchivesLog;
import com.insigma.po.soldier.PostSelectionManagement;
import com.insigma.po.soldier.ReceivingNotice;
import com.insigma.po.soldier.RenounceResettlementNotice;
import com.insigma.po.soldier.ResetUnitInfo;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SoldierBasicInfoOse;
import com.insigma.po.soldier.SoldierBasicInfoStatic;
import com.insigma.po.soldier.SoldierRegister;
import com.insigma.po.soldier.SoldierScore;
import com.insigma.po.soldier.SysRegional;
import com.insigma.po.soldier.TreeNodesParam;
import com.insigma.service.FlexibleEmploymentInfoService;
import com.insigma.service.PersonnelArchivesLogService;
import com.insigma.service.PostSelectionManagementService;
import com.insigma.service.ReceivingNoticeService;
import com.insigma.service.RenounceResettlementNoticeService;
import com.insigma.service.ResetUnitInfoService;
import com.insigma.service.SoldierBasicInfoOseService;
import com.insigma.service.SoldierBasicInfoService;
import com.insigma.service.SoldierRegisterService;
import com.insigma.service.SysParamInfoService;
import com.insigma.service.SysRegionalService;
import com.insigma.util.CharUtils;
import com.insigma.util.DateUtils;
import com.insigma.util.FileUtils;
import com.insigma.util.ParamUtils;
import com.insigma.util.WordUtils;

import net.minidev.json.JSONObject;

/**
 * 
 * 类描述：   士兵地市报到管理
 * 创建人：刘伟民   
 * 创建时间：2020年3月20日 上午9:15:29   
 * @version
 */
@RestController
@RequestMapping("/pvrpm/register")
public class SoldierRegisterController {

	@Autowired
    HttpServletRequest request;
	
	@Autowired
	private SoldierRegisterService srsMapper;
	
	@Autowired
	private SoldierBasicInfoService sbsMapper;
	
	@Autowired
	private RenounceResettlementNoticeService rrnMapper;
	
	@Autowired
	private FlexibleEmploymentInfoService fesMapper;
	
	@Autowired
	private SysParamInfoService paramMapper;
	
	@Autowired
	private SysRegionalService srMapper;
	
	@Autowired
	private ResetUnitInfoService ruiMapper;
	
	@Autowired
	private PostSelectionManagementService psmMapper;
	
	@Autowired
	private ReceivingNoticeService rnMapper;
	
	@Autowired
	private SoldierBasicInfoOseService sbioMapper;
	
	@Autowired
	private PersonnelArchivesLogService palMapper;
	
	@Value("${rootpath}")
    private String rootPath;//文件存放根目录
	
	/**
	 * 地市报到管理-查询安排工作级联数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "querySRTreeNodes",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public JSONArray querySRTreeNodes(@RequestBody JSONObject json){
		String areaId=request.getHeader("areaId");
		
		Map<String, String> map=new HashMap<>();
		map.put("ybId", json.get("ybId").toString());
		if(!StringUtils.isEmpty(json.get("registerStatus").toString())){
			map.put("registerStatus", json.get("registerStatus").toString());
		}
		map.put("personnelType", json.get("personnelType").toString());
		map.put("queryType", "1");
		map.put("noticeStatus", "1");
		map.put("wheTransfer", "1");
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
		arr1 = JSONArray.parseArray(JSONArray.toJSONString(sbsMapper.queryAreaCascadeData(map)));//查询出总人数
		
		map.put("flag", "2");
		JSONArray arr2 = JSONArray.parseArray(JSONArray.toJSONString(sbsMapper.queryAreaCascadeData(map)));
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
			JSONArray arr3 = JSONArray.parseArray(JSONArray.toJSONString(sbsMapper.queryAreaCascadeData(map)));
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
	 * 地市报到管理-查询安排工作士兵报到数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryRegisterData", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageInfo<SoldierRegister> queryRegisterData(@RequestBody SoldierRegister vo) {
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
		List<SysRegional> srList = srMapper.queryRegionalTotal(null);
		List<SysRegional> regional= null;
		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		List<SoldierRegister> list = srsMapper.selectAll(vo);
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
	 * 保存士兵报到信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "saveSoldierRegister",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public String saveSoldierRegister(@RequestBody SoldierRegister vo){
		String displayName=CharUtils.getDisplayName(request);
		
		ResultVo resultVo = new ResultVo();
		
		try {
			//报到变更士兵基础信息表中的报到状态
			SoldierBasicInfo record = new SoldierBasicInfo();
			record.setSbiId(vo.getSbiId());
			record.setRegisterStatus(2);
			sbsMapper.updateByPrimaryKey(record);
			
			SoldierRegister sr = new SoldierRegister();
			sr.setIdcard(vo.getIdcard());
			sr.setOperator(displayName);
			sr.setRegisterTime(vo.getRegisterTime());
			SoldierRegister check = srsMapper.querySR(vo.getIdcard());
			//判断是否存在报到记录，存在则更新，不存在则新增
			if(check!=null){
				srsMapper.updateSR(sr);
			}else{
				srsMapper.insertSR(sr);
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
	
	/**
	 * 开具安置待遇放弃告知书
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "createRenounceResettlementNotice",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public String renounceResettlementNotice(@RequestBody RenounceResettlementNotice vo){
		ResultVo resultVo = new ResultVo();
		String path=rootPath+"放弃安置待遇告知书/";
		
		String renounceFlag1 = "";
		String renounceFlag2 = "";
		if("1".equals(vo.getRenounceType())){
			renounceFlag1 = "☑";
			renounceFlag2 = "□";
		}else{
			renounceFlag1 = "□";
			renounceFlag2 = "☑";
		}
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String newregisterDate="";
//		try {
//			Date tmpDate=sdf.parse(vo.getRegisterTimeSlot().replaceAll("[^(0-9)]", "-"));
//			Calendar calendar = new GregorianCalendar(); 
//			calendar.setTime(tmpDate); 
//			calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
//			newregisterDate=sdf.format(calendar.getTime());  
//		} catch (ParseException e1) {
//			e1.printStackTrace();
//		}
		//应报到日期
		String[] registerDate=vo.getRegisterTimeSlot().replaceAll("[^(0-9)]", "-").split("-");
		//档案接收日期
		String[] receiveDate=vo.getReceiveDate().replaceAll("[^(0-9)]", "-").split("-");
		//截止日期
		String[] endDate=vo.getEndDate().replaceAll("[^(0-9)]", "-").split("-");
		
		Map<String,String> dataMap = new HashMap<String, String>();
		dataMap.put("param1", vo.getName());
		dataMap.put("param2", registerDate[0]);
		dataMap.put("param3", registerDate[1]);
		dataMap.put("param4", registerDate[2]);
		dataMap.put("param5", receiveDate[0]);
		dataMap.put("param6", receiveDate[1]);
		dataMap.put("param7", receiveDate[2]);
		dataMap.put("param8", endDate[0]);
		dataMap.put("param9", endDate[1]);
		dataMap.put("param10", endDate[2]);
		dataMap.put("param11", vo.getRegionalName());
		dataMap.put("param12", DateUtils.getCnCurrentDate());
		dataMap.put("param13", renounceFlag1);
		dataMap.put("param14", renounceFlag2);
		WordUtils wu = new WordUtils();
	    wu.createWord(path,vo.getIdcard()+".doc","放弃安置待遇告知书模板.ftl",dataMap);
	    String filePath="file/放弃安置待遇告知书/"+vo.getIdcard()+".doc";
	    vo.setFilePath(filePath);
		try {
			
			RenounceResettlementNotice checkvo = rrnMapper.selectByPrimaryKey(vo.getIdcard());
			if(checkvo!=null){
				vo.setRrnId(checkvo.getRrnId());
				rrnMapper.updateByPrimaryKey(vo);
			}else{
				rrnMapper.insert(vo);
			}
			
			SoldierBasicInfo sbi= new SoldierBasicInfo();
			sbi.setSbiId(vo.getSbiId());
			sbi.setNewResetType(2);
			sbsMapper.updateByPrimaryKey(sbi);
			
			SoldierBasicInfo sbivo = sbsMapper.selectByPrimaryKey(vo.getSbiId());
			
			SoldierBasicInfoOse osevo = new SoldierBasicInfoOse();
			BeanUtils.copyProperties(sbivo,osevo);
			osevo.setYear(DateUtils.getStringYearDate());
			osevo.setNewResetType("2");
			osevo.setSbiId(vo.getSbiId());
			osevo.setRegisterStatus("1");
			
			SoldierBasicInfoOse record = new SoldierBasicInfoOse();
			record.setIdcard(vo.getIdcard());
			//根据身份证号查询数据，判断数据是否已存在
			List<SoldierBasicInfoOse> sbioList = sbioMapper.selectAll(record);
			if(sbioList.size()>0){
				sbioMapper.updateByPrimaryKey(osevo);
			}else{
				sbioMapper.insert(osevo);
			}
			
			resultVo.setStatusCode(200);
			resultVo.setMessage("开具放弃安置待遇告知书成功");
			resultVo.setList("\""+filePath+"\"");
		} catch (Exception e) {
			e.printStackTrace();
			resultVo.setStatusCode(-1);
			resultVo.setMessage("开具失败：开具放弃安置待遇告知书出现异常");
		}
    	return resultVo.toString();
    }
	
	/**
	 * 根据身份证号查询放弃安置待遇通知书详细信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryRRNotice", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public RenounceResettlementNotice queryReceivingNotice(@RequestBody RenounceResettlementNotice vo) {
		RenounceResettlementNotice rnvo = rrnMapper.selectByPrimaryKey(vo.getIdcard());
		return rnvo;
	}
	
	
	/**
	 * 安置方式管理-查询政府安排、灵活就业级联数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryResetTreeNodes",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public JSONArray queryResetTreeNodes(@RequestBody JSONObject json){
		String areaId=request.getHeader("areaId");
		
		Map<String, String> map=new HashMap<>();
		map.put("ybId", json.get("ybId").toString());
		map.put("queryType", json.get("queryType").toString());
		map.put("noticeStatus", "1");
		map.put("wheTransfer", "1");//是否移交 1已移交 2未移交
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
		arr1 = JSONArray.parseArray(JSONArray.toJSONString(sbsMapper.queryAreaCascadeData(map)));//查询出总人数
		
		map.put("flag", "2");
		JSONArray arr2 = JSONArray.parseArray(JSONArray.toJSONString(sbsMapper.queryAreaCascadeData(map)));
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
			JSONArray arr3 = JSONArray.parseArray(JSONArray.toJSONString(sbsMapper.queryAreaCascadeData(map)));
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
	 * 安置方式管理-查询政府安排、灵活就业数据列表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryResetData", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageInfo<SoldierBasicInfoStatic> queryResetData(@RequestBody JSONObject json) {
		String areaId=request.getHeader("areaId");
		//查询类型 1符合政府安排 2灵活就业 3自主就业
		String queryType = json.get("queryType").toString();
		
		Map<String, String> map=new HashMap<>();
		map.put("ybId", json.get("ybId").toString());
		map.put("name", json.get("name").toString());
//		map.put("personnelType", json.get("personnelType").toString());
		map.put("queryType", queryType);
		
		String regionalCode=json.get("regionalCode").toString();
		
		if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
			if(areaId.endsWith("00")){//机构编号00结尾的 为地市用户
				if(StringUtils.isEmpty(regionalCode)){
					map.put("regionalCode", areaId.substring(0,4));
				}else{
					if(regionalCode.equals("330000")){
						map.put("regionalCode", areaId.substring(0,4));
					}else if(regionalCode.endsWith("00")){
						map.put("regionalCode", regionalCode.substring(0,4));
					}else{
						map.put("regionalCode", regionalCode);
					}
				}
			}else{
				map.put("regionalCode", areaId);
			}
		}else{
			if(!StringUtils.isEmpty(regionalCode) && !regionalCode.equals("330000")){
				if(regionalCode.endsWith("00")){
					map.put("regionalCode", regionalCode.substring(0,4));
				}else{
					map.put("regionalCode", regionalCode);
				}
			}
		}
		//查询所有参数配置表所有参数信息
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		List<SysParamInfo> rlist= null;
		//一次查询出所有行政区划数据
		List<SysRegional> srList = srMapper.queryRegionalTotal(null);
		List<SysRegional> regional= null;
		
		Integer pageNum=Integer.parseInt(json.get("pageNum").toString());
		Integer pageSize=Integer.parseInt(json.get("pageSize").toString());
		
		PageHelper.startPage(pageNum, pageSize);
		List<SoldierBasicInfoStatic> list = srsMapper.queryRegisterData(map);
		
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setIdcardStr(list.get(i).getIdcard().replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
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
			}else{
				list.get(i).setNewResetType("安排工作");
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
		
		PageInfo<SoldierBasicInfoStatic> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 根据身份证号查询待放弃安置待遇人员
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "querySoldierBasicInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String querySoldierBasicInfo(@RequestBody SoldierBasicInfo vo) {
		ResultVo resultVo = new ResultVo();
		String areaId=request.getHeader("areaId");
		Map<String, String> map=new HashMap<>();
		map.put("ybId", vo.getYbId()+"");
		map.put("name", vo.getName());
		map.put("regionalCode", areaId.substring(0,4));
		List<SoldierBasicInfo> list = srsMapper.querySoldierBasicInfo(map);
		if(list.size()>0){
			resultVo.setStatusCode(200);
			resultVo.setMessage("查询成功");
			resultVo.setList(JSON.toJSON(list).toString());
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("根据姓名未查询到相关人员信息");
		}
		return resultVo.toString();
	}
	
	/**
	 * 开具灵活就业申请表
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "saveFlexibleEmploymentInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String saveFlexibleEmploymentInfo(@RequestBody FlexibleEmploymentInfo vo) {
		String displayName=CharUtils.getDisplayName(request);
		ResultVo resultVo = new ResultVo();
		String areaId=request.getHeader("areaId");
		try {
			SoldierBasicInfo sbivo = sbsMapper.checkSoldierBasicInfo(vo.getIdcard());
			if(sbivo!=null){
				//一次查询出所有参数数据
				List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
				List<SysParamInfo> newlist=null;
				
				String sex ="";
				String dcCode = "";
				String dgCode = "";
				String ismc ="";
				if(!StringUtils.isEmpty(sbivo.getSex())){
					newlist=ParamUtils.checkList(plist, "sex", sbivo.getSex(),"");
					if(newlist.size()>0){
						sex=newlist.get(0).getParamValue();
					}
				}
				if(!StringUtils.isEmpty(sbivo.getDisabilityCategoriesCode())){
					newlist=ParamUtils.checkList(plist, "disabilityCategoriesCode", sbivo.getDisabilityCategoriesCode(),"");
					if(newlist.size()>0){
						dcCode=newlist.get(0).getParamValue();
					}
				}
				if(!StringUtils.isEmpty(sbivo.getDisableGradeCode())){
					newlist=ParamUtils.checkList(plist, "disableGradeCode", sbivo.getDisableGradeCode(),"");
					if(newlist.size()>0){
						dgCode=newlist.get(0).getParamValue();
					}
				}
				if(!StringUtils.isEmpty(sbivo.getIsMartyrChildren())){
					newlist=ParamUtils.checkList(plist, "isOrNo", sbivo.getIsMartyrChildren(),"");
					if(newlist.size()>0){
						ismc=newlist.get(0).getParamValue();
					}
				}
				String regionalName="";
				//一次查询出所有行政区划数据
				List<SysRegional> srList = srMapper.queryRegionalTotal(null);
				List<SysRegional> regional= null;
				if(!StringUtils.isEmpty(sbivo.getResetPlace())){
					regional=ParamUtils.getRegional(srList, "", sbivo.getResetPlace());
					if(regional.size()>0){
						regionalName=regional.get(0).getRegionalName();
					}
				}
				
				
				String date = DateUtils.getCnCurrentDate();
				String personnelType="退役士兵";
				String param19="入伍时间";
				String param20="退役时间";
				if(sbivo.getPersonnelType().equals("2")){
					personnelType="退出消防员";
					param19="参加工作时间";
					param20="退出消防员时间";
				}
				String path="灵活就业申请表/"+DateUtils.getStringCurrentDate()+"/";
				FileUtils.createFilePath(rootPath+path);
				Map<String, String> map = new HashMap<String, String>();
				map.put("param1", sbivo.getName());
				map.put("param2", sbivo.getFormerUnit()==null?"":sbivo.getFormerUnit());
				map.put("param3", sex);
				map.put("param4", sbivo.getEnlistedDate()==null?"":sbivo.getEnlistedDate()+".1");
				map.put("param5", sbivo.getRetiredDate()==null?"":sbivo.getRetiredDate());
				map.put("param6", sbivo.getIdcard());
				map.put("param7", dcCode+dgCode);
				map.put("param8", sbivo.getRewardRemark()==null?"":sbivo.getRewardRemark());
				map.put("param9", ismc);
				map.put("param10", vo.getBankAccount());
				map.put("param11", vo.getName()+"_"+vo.getBankName());
				map.put("param12", vo.getDomicile());
				map.put("param13", date);
				map.put("param14", vo.getSelfSubsAmount()==null?"0":vo.getSelfSubsAmount()+"");
				map.put("param15", vo.getSelfSubsAmount()==null?"零":CharUtils.digitUppercase(vo.getSelfSubsAmount()+""));
				map.put("param16", regionalName.substring(3));
				map.put("param17", date);
				map.put("param18", personnelType);
				map.put("param19", param19);
				map.put("param20", param20);
				WordUtils wu = new WordUtils();
				wu.createWord(rootPath+path,sbivo.getIdcard()+".doc","灵活就业申请表模板.ftl",map);
				
				vo.setFilePath("file/"+path+sbivo.getIdcard()+".doc");
				
				FlexibleEmploymentInfo checkvo = fesMapper.selectByPrimaryKey(sbivo.getSbiId());
				vo.setSbiId(sbivo.getSbiId());
				if(checkvo!=null){
					vo.setFeiId(checkvo.getFeiId());
					fesMapper.updateByPrimaryKey(vo);
				}else{
					fesMapper.insert(vo);
				}
				
				SoldierBasicInfo sbi= new SoldierBasicInfo();
				sbi.setSbiId(sbivo.getSbiId());
				sbi.setNewResetType(2);
				sbsMapper.updateByPrimaryKey(sbi);
				
				SoldierBasicInfoOse sbiovo = new SoldierBasicInfoOse();
				BeanUtils.copyProperties(sbivo,sbiovo);
				sbiovo.setRegisterStatus(sbivo.getRegisterStatus()+"");
				sbiovo.setYear(DateUtils.getStringYearDate());
				sbiovo.setNewResetType("2");
//				sbiovo.setRegisterStatus("1");
				
				SoldierBasicInfoOse po = new SoldierBasicInfoOse();
				po.setIdcard(sbiovo.getIdcard());
				List<SoldierBasicInfoOse> oselist = sbioMapper.selectAll(po);
				if(oselist.size()>0){
					sbiovo.setSbiId(oselist.get(0).getSbiId());
					sbioMapper.updateByPrimaryKey(sbiovo);
				}else{
					sbioMapper.insert(sbiovo);
				}
				
				//保存操作日志
				PersonnelArchivesLog log = new PersonnelArchivesLog();
				log.setSbiId(sbivo.getSbiId());
				log.setOperator(displayName);
				log.setContext("安置管理-开具灵活就业申请表");
				//保存操作日志
				palMapper.insert(log);
				
				resultVo.setStatusCode(200);
				resultVo.setMessage("保存成功");
				resultVo.setList("\""+vo.getFilePath()+"\"");
			}
		} catch (Exception e) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("保存出现异常");
			e.printStackTrace();
		}
		
		return resultVo.toString();
	}
	
	/**
	 * 查询士兵成绩数据级联数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryScoreTreeNodes",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
	public JSONArray queryScoreTreeNodes(@RequestBody JSONObject json){
		String areaId=request.getHeader("areaId");
		
		Map<String, String> map=new HashMap<>();
		map.put("ybId", json.get("ybId").toString());
		map.put("wheTransfer", "1");
		map.put("noticeStatus", "1");
		map.put("queryType", "1");
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
		arr1 = JSONArray.parseArray(JSONArray.toJSONString(sbsMapper.queryAreaCascadeData(map)));//查询出总人数
		
		map.put("flag", "2");
		JSONArray arr2 = JSONArray.parseArray(JSONArray.toJSONString(sbsMapper.queryAreaCascadeData(map)));
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
			JSONArray arr3 = JSONArray.parseArray(JSONArray.toJSONString(sbsMapper.queryAreaCascadeData(map)));
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
	 * 查询士兵成绩数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryScoreList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageInfo<SoldierScore> queryScoreList(@RequestBody SoldierScore vo) {
		String areaId=request.getHeader("areaId");
		String regionalCode = vo.getRegionalCode();
		if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
			if(areaId.endsWith("00")){//机构编号00结尾的 为地市用户
				if(StringUtils.isEmpty(regionalCode)){
					vo.setRegionalCode(areaId.substring(0,4));
				}else{
					if(regionalCode.equals("330000")){
						vo.setRegionalCode("");
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
		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		List<SoldierScore> list = srsMapper.queryScoreList(vo);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setIdcard(list.get(i).getIdcard().replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
		}
		PageInfo<SoldierScore> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 更新档案分、考试分
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "updateSoldierScore",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public String updateSoldierScore(@RequestBody SoldierScore vo){
//		String displayName=CharUtils.getDisplayName(request);
		
		ResultVo resultVo = new ResultVo();
		
		try {
			BigDecimal xs1 = new BigDecimal("0.6");
			BigDecimal xs2 = new BigDecimal("0.4");
			BigDecimal t1 = new BigDecimal(0.0);
			BigDecimal t2 = new BigDecimal(0.0);
			if(!StringUtils.isEmpty(vo.getArchivesScore()) && !StringUtils.isEmpty(vo.getExamScore())){
				BigDecimal s1 = new BigDecimal(vo.getArchivesScore());
				t1=s1.multiply(xs1);
				BigDecimal s2 = new BigDecimal(vo.getExamScore());
				t2=s2.multiply(xs2);
				t1.add(t2);
				vo.setTotalScore(t1.add(t2).toString());
			}
			if(!StringUtils.isEmpty(vo.getArchivesScore()) && StringUtils.isEmpty(vo.getExamScore())){
				BigDecimal s1 = new BigDecimal(vo.getArchivesScore());
				t1=s1.multiply(xs1);
				vo.setTotalScore(t1.toString());
				vo.setExamScore(null);
			}
			if(StringUtils.isEmpty(vo.getArchivesScore()) && !StringUtils.isEmpty(vo.getExamScore())){
				BigDecimal s2 = new BigDecimal(vo.getExamScore());
				t2=s2.multiply(xs2);
				vo.setTotalScore(t2.toString());
				vo.setArchivesScore(null);
			}
			srsMapper.updateSoldierScore(vo);
			
			resultVo.setStatusCode(200);
			resultVo.setMessage("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultVo.setStatusCode(-1);
			resultVo.setMessage("保存出现异常");
		}
    	return resultVo.toString();
    }
	
	
	/**
	 * 查询士兵选岗数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryPSMSoldierList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageInfo<PostSelectionManagement> queryPSMSoldierList(@RequestBody JSONObject json) {
		String areaId=request.getHeader("areaId");
		
		Map<String, String> map=new HashMap<>();
		map.put("ybId", json.get("ybId").toString());
		map.put("name", json.get("name").toString());
		map.put("wheLetOfIntro", json.get("wheLetOfIntro").toString());
		map.put("wheRegistrForm", json.get("wheRegistrForm").toString());
		
		String regionalCode=json.get("regionalCode").toString();
		if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
			if(areaId.endsWith("00")){//机构编号00结尾的 为地市用户
				if(StringUtils.isEmpty(regionalCode)){
					map.put("regionalCode", areaId.substring(0,4));
				}else{
					if(regionalCode.equals("330000")){
						map.put("regionalCode", areaId.substring(0,4));
					}else if(regionalCode.endsWith("00")){
						map.put("regionalCode", regionalCode.substring(0,4));
					}else{
						map.put("regionalCode", regionalCode);
					}
				}
			}else{
				map.put("regionalCode", areaId);
			}
		}else{
			if(!StringUtils.isEmpty(regionalCode) && !regionalCode.equals("330000")){
				if(regionalCode.endsWith("00")){
					map.put("regionalCode", regionalCode.substring(0,4));
				}else{
					map.put("regionalCode", regionalCode);
				}
			}
		}
		Integer pageNum=Integer.parseInt(json.get("pageNum").toString());
		Integer pageSize=Integer.parseInt(json.get("pageSize").toString());
		
		PageHelper.startPage(pageNum, pageSize);
		List<PostSelectionManagement> list = srsMapper.queryPSMSoldierData(map);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setIdcard(list.get(i).getIdcard().replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
		}
		PageInfo<PostSelectionManagement> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 人员综合表根据行政区划查询级联数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryPSMTreeNodes",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public JSONArray queryPSMTreeNodes(@RequestBody JSONObject json){
		String areaId=request.getHeader("areaId");
		JSONArray arr1 = new JSONArray();
		JSONArray arr2tmp = new JSONArray();
		
		Map<String, String> map=new HashMap<>();
		map.put("ybId", json.get("ybId").toString());
		
		String regionalCode=json.get("regionalCode").toString();
		if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
			if(areaId.endsWith("00")){//机构编号00结尾的 为地市用户
				if(StringUtils.isEmpty(regionalCode)){
					map.put("regionalCode", areaId.substring(0,4));
				}else{
					if(regionalCode.equals("330000")){
					}else if(regionalCode.endsWith("00")){
						map.put("regionalCode", regionalCode.substring(0,4));
					}else{
						map.put("regionalCode", regionalCode);
					}
				}
			}else{
				map.put("regionalCode", areaId);
			}
		}else{
			if(!StringUtils.isEmpty(regionalCode) && !regionalCode.equals("330000")){
				if(regionalCode.endsWith("00")){
					map.put("regionalCode", regionalCode.substring(0,4));
				}else{
					map.put("regionalCode", regionalCode);
				}
			}
		}
		
		map.put("flag", "1");
		arr1 = JSONArray.parseArray(JSONArray.toJSONString(srsMapper.queryPSMSoldierDataTreeNodes(map)));//查询出总人数
		
		map.put("flag", "2");
		JSONArray arr2 = JSONArray.parseArray(JSONArray.toJSONString(srsMapper.queryPSMSoldierDataTreeNodes(map)));
		for (int i = 0; i < arr2.size(); i++) {
			com.alibaba.fastjson.JSONObject json2 = (com.alibaba.fastjson.JSONObject) arr2.get(i);
			
			map.put("flag", "3");
			if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
				if(!areaId.endsWith("00")){//机构编号00结尾的 为地市用户
					map.put("regionalCode", areaId);
				}
			}else{
				map.put("regionalCode", json2.getString("regionalCode").substring(0,4));
			}
			JSONArray arr3 = JSONArray.parseArray(JSONArray.toJSONString(srsMapper.queryPSMSoldierDataTreeNodes(map)));
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
	 * 选岗查询单位信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryAllUnit", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String queryAllUnit(@RequestBody JSONObject json) {
		ResultVo resultVo = new ResultVo();
		
		String areaId=request.getHeader("areaId");
		Map<String, String> map=new HashMap<>();
		map.put("unitName", json.get("unitName").toString());
		map.put("regionalCode", areaId.substring(0,4));
		
		
		List<ResetUnitInfo> list = ruiMapper.queryAllUnit(map);
		if(list.size()>0){
			resultVo.setStatusCode(200);
			resultVo.setMessage("查询成功");
			resultVo.setList(JSON.toJSON(list).toString());
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("根据单位名称未查询到相关单位信息");
		}
		return resultVo.toString();
	}
	
	/**
	 * 选岗查询单位信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "savePostSelection", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String savePostSelection(@RequestBody PostSelectionManagement vo) {
		ResultVo resultVo = new ResultVo();
		vo.setYear(DateUtils.getStringYearDate());
		vo.setWheLetOfIntro("1");
		vo.setWheRegistrForm("1");
		PostSelectionManagement psm = psmMapper.selectByPrimaryKey(vo.getSbiId());
		if(psm!=null){
			psm.setSbiId(vo.getSbiId());
			psm.setRuiId(vo.getRuiId());
			psm.setUnitName(vo.getUnitName());
			psm.setName(vo.getName());
			psmMapper.updateByPrimaryKey(psm);
		}else{
			psmMapper.insert(vo);
		}
		resultVo.setStatusCode(200);
		resultVo.setMessage("选岗成功");
		return resultVo.toString();
	}
	
	
	/**
	 * 开具安排工作介绍信
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "saveLetOfIntro", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String saveLetOfIntro(@RequestBody JSONObject json) {
		ResultVo resultVo = new ResultVo();
		String areaId=request.getHeader("areaId");
		try {
			String[] registerTime=json.get("registerTime").toString().split("\\.");
			
			String[] sbiIdArr;
			List<String> fileList = new ArrayList<>();
			List<PostSelectionManagement> list=null;
			if(StringUtils.isEmpty(json.get("sbiId").toString())){
				Map<String, String> map=new HashMap<>();
				map.put("ybId", json.get("ybId").toString());
				String regionalCode=json.get("regionalCode").toString();
				if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
					if(areaId.endsWith("00")){//机构编号00结尾的 为地市用户
						if(StringUtils.isEmpty(regionalCode)){
							map.put("regionalCode", areaId.substring(0,4));
						}else{
							if(regionalCode.equals("330000")){
								map.put("regionalCode", areaId.substring(0,4));
							}else if(regionalCode.endsWith("00")){
								map.put("regionalCode", regionalCode.substring(0,4));
							}else{
								map.put("regionalCode", regionalCode);
							}
						}
					}else{
						map.put("regionalCode", areaId);
					}
				}else{
					resultVo.setStatusCode(-1);
					resultVo.setMessage("当前账号无开具登记卡权限");
					return resultVo.toString();
				}
				
				list = psmMapper.selectAll(map);
//				sbiIdArr = new String[list.size()];
//				sbiIdArr=list.stream()
//                        .map(PostSelectionManagement::getSbiId)
//                        .toArray(String[]::new);
			}else{
				list = new ArrayList<>();
				sbiIdArr = json.get("sbiId").toString().split("\\,");
				for (int i = 0; i < sbiIdArr.length; i++) {
					int sbiId= Integer.parseInt(sbiIdArr[i]);
					PostSelectionManagement psmvo = psmMapper.selectByPrimaryKey(sbiId);
					list.add(psmvo);
				}
			}
			//一次查询出所有行政区划数据
			List<SysRegional> srList = srMapper.queryRegionalTotal(null);
			List<SysRegional> regional= null;
			for (int i = 0; i < list.size(); i++) {
				SoldierBasicInfo sbivo = sbsMapper.selectByPrimaryKey(list.get(i).getSbiId());
				if(sbivo!=null){
					String regionalName="";
					
					if(!StringUtils.isEmpty(sbivo.getResetPlace())){
						regional=ParamUtils.getRegional(srList, "", sbivo.getResetPlace());
						if(regional.size()>0){
							regionalName=regional.get(0).getRegionalName();
						}
					}
					
					
					String date = DateUtils.getCnCurrentDate();
					String personnelType="退役士兵";
					String param13="服役部队";
					if(sbivo.getPersonnelType().equals("2")){
						personnelType="退出消防员";
						param13="工作单位";
					}
					
					String path="安排工作介绍信/"+DateUtils.getStringCurrentDate()+"/";
					FileUtils.createFilePath(rootPath+path);
					String year = DateUtils.getStringYearDate();
					Map<String, String> seqmap = new HashMap<String, String>();
					seqmap.put("name", "SEQ_LOI_NUM");
					seqmap.put("year", year);
					int num = rnMapper.getMaxNum(seqmap);
					//判断序列是否存在，不存在则新增
					if(num<1){
						num=1;
						rnMapper.insertNextval(seqmap);
					}
					String param1="("+year+")安介字第"+num+"号";
					Map<String, String> map = new HashMap<String, String>();
					map.put("param1", param1);
					map.put("param2", list.get(i).getUnitName());
					map.put("param3", sbivo.getName());
					map.put("param5", registerTime[0]);
					map.put("param6", registerTime[1]);
					map.put("param7", registerTime[2]);
					map.put("param8", sbivo.getIdcard());
					map.put("param9", sbivo.getFormerUnit());
					map.put("param10", regionalName.substring(3));
					map.put("param11", date);
					map.put("param12", personnelType);
					map.put("param13", param13);
					WordUtils wu = new WordUtils();
					wu.createWord(rootPath+path,sbivo.getIdcard()+".doc","安排工作介绍信模板.ftl",map);
					//开具介绍信更新选岗记录表信息
					list.get(i).setWheLetOfIntro("2");
					list.get(i).setLetOfIntroFilePath("file/"+path+sbivo.getIdcard()+".doc");
					String createLetterOfIntroTime=DateUtils.getStringCurrentDate(DateUtils.date6);
					list.get(i).setCreateIntroletterTime(createLetterOfIntroTime);
					psmMapper.updateByPrimaryKey(list.get(i));
					
					fileList.add(path+sbivo.getIdcard()+".doc");
				}
			}
			
			if(fileList.size()>1){
				//根据生成的word接收安置通知书，达成zip压缩包
				long start = System.currentTimeMillis();
				String path="安排工作介绍信/zip/";
				FileUtils.createFilePath(rootPath+path);
				
				String zipFile = path+"安排工作介绍信"+DateUtils.getStringCurrentDate()+".zip";
				WordUtils.fileToZip(fileList,zipFile ,rootPath);
				
				long end = System.currentTimeMillis();
				System.out.println("压缩完成，耗时：" + (end - start) + " ms");
				resultVo.setStatusCode(200);
				resultVo.setMessage("开具安排工作介绍信成功,共开具【"+fileList.size()+"】张");
				resultVo.setList("\"file/"+zipFile+"\"");
			}else if(fileList.size()== 1){
				resultVo.setStatusCode(200);
				resultVo.setMessage("开具安排工作介绍信成功");
				resultVo.setList("\"file/"+fileList.get(0)+"\"");
			}else{
				resultVo.setStatusCode(-1);
				resultVo.setMessage("未查询到可开具接收安置通知书的数据");
			}
		} catch (Exception e) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("保存出现异常");
			e.printStackTrace();
		}
		
		return resultVo.toString();
	}
	
	/**
	 * 开具安置登记卡
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "saveRegistrForm", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String saveRegistrForm(@RequestBody JSONObject json) {
		ResultVo resultVo = new ResultVo();
		String areaId=request.getHeader("areaId");
		try {
			
			String[] sbiIdArr;
			List<String> fileList = new ArrayList<>();
			List<PostSelectionManagement> list=null;
			if(StringUtils.isEmpty(json.get("sbiId").toString())){
				Map<String, String> map=new HashMap<>();
				map.put("ybId", json.get("ybId").toString());
				String regionalCode=json.get("regionalCode").toString();
				if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
					if(areaId.endsWith("00")){//机构编号00结尾的 为地市用户
						if(StringUtils.isEmpty(regionalCode)){
							map.put("regionalCode", areaId.substring(0,4));
						}else{
							if(regionalCode.equals("330000")){
								map.put("regionalCode", areaId.substring(0,4));
							}else if(regionalCode.endsWith("00")){
								map.put("regionalCode", regionalCode.substring(0,4));
							}else{
								map.put("regionalCode", regionalCode);
							}
						}
					}else{
						map.put("regionalCode", areaId);
					}
				}else{
					resultVo.setStatusCode(-1);
					resultVo.setMessage("当前账号无开具登记卡权限");
					return resultVo.toString();
				}
				list = psmMapper.selectAll(map);
			}else{
				list = new ArrayList<>();
				sbiIdArr = json.get("sbiId").toString().split("\\,");
				for (int i = 0; i < sbiIdArr.length; i++) {
					int sbiId= Integer.parseInt(sbiIdArr[i]);
					PostSelectionManagement psmvo = psmMapper.selectByPrimaryKey(sbiId);
					list.add(psmvo);
				}
			}
			//一次查询出所有参数数据
			List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
			List<SysParamInfo> newlist=null;
			//一次查询出所有行政区划数据
			List<SysRegional> srList = srMapper.queryRegionalTotal(null);
			List<SysRegional> regional= null;
			
			for (int i = 0; i < list.size(); i++) {
				int sbiId = list.get(i).getSbiId();
				int ruiId = list.get(i).getRuiId();
				SoldierBasicInfo sbivo = sbsMapper.selectByPrimaryKey(sbiId);
				if(sbivo!=null){
					
					String sex ="";
					String eduLevCode = "";
					String soldiersMilitaryRank = "";
					if(!StringUtils.isEmpty(sbivo.getSex())){
						newlist=ParamUtils.checkList(plist, "sex", sbivo.getSex(),"");
						if(newlist.size()>0){
							sex=newlist.get(0).getParamValue();
						}
					}
					if(!StringUtils.isEmpty(sbivo.getEduLevCode())){
						newlist=ParamUtils.checkList(plist, "eduLevCode", sbivo.getEduLevCode(),"");
						if(newlist.size()>0){
							eduLevCode=newlist.get(0).getParamValue();
						}
					}
					if(!StringUtils.isEmpty(sbivo.getSoldiersMilitaryRank())){
						newlist=ParamUtils.checkList(plist, "soldMilitRankCode", sbivo.getSoldiersMilitaryRank(),"");
						if(newlist.size()>0){
							soldiersMilitaryRank=newlist.get(0).getParamValue();
						}
					}
					
					String regionalName="";
					if(!StringUtils.isEmpty(sbivo.getResetPlace())){
						regional=ParamUtils.getRegional(srList, "", sbivo.getResetPlace());
						if(regional.size()>0){
							regionalName=regional.get(0).getRegionalName();
						}
					}
					
					
					String personnelType="退役士兵";
					String param18="入伍时间";
					String param19="退役时间";
					String param20="退役";
					if(sbivo.getPersonnelType().equals("2")){
						personnelType="退出消防员";
						param18="参加工作时间";
						param19="退出消防员时间";
						param20="退出";
					}
					
					String path="安置登记卡/"+DateUtils.getStringCurrentDate()+"/";
					FileUtils.createFilePath(rootPath+path);
					
					Map<String, String> map = new HashMap<String, String>();
					map.put("param1", sbivo.getName());
					map.put("param2", sex);
					map.put("param3", sbivo.getBirthday());
					map.put("param4", eduLevCode);
					map.put("param5", sbivo.getEnlistedDate()+".1");
					map.put("param6", sbivo.getRetiredDate());
					map.put("param7", soldiersMilitaryRank);
					map.put("param8", sbivo.getFormerUnit());
					map.put("param9", sbivo.getIdcard());
					map.put("param10", regionalName.substring(3)+"退役军人事务局");
					
					ResetUnitInfo ruvo = ruiMapper.selectByPrimaryKey(ruiId);
					map.put("param11", ruvo.getUnitName()==null?"": ruvo.getUnitName());
					map.put("param12", "1");
					String unitCategory="";
					if(!StringUtils.isEmpty(ruvo.getUnitCategory())){
						newlist=ParamUtils.checkList(plist, "unitCategory", ruvo.getUnitCategory(),"");
						if(newlist.size()>0){
							unitCategory=newlist.get(0).getParamValue();
						}
					}
					String p13="□";
					String p14="□";
					String p15="□";
					String p16="□";
					if(!StringUtils.isEmpty(unitCategory)){
						if(unitCategory.indexOf("机关")!=-1){
							p13="☑";
						}else if(unitCategory.indexOf("事业")!=-1){
							p14="☑";
						}else if(unitCategory.indexOf("企")!=-1){
							p15="☑";
						}else{
							p16="☑";
						}
					}
					map.put("param13", p13);
					map.put("param14", p14);
					map.put("param15", p15);
					map.put("param16", p16);
					map.put("param17", personnelType);
					map.put("param18", param18);
					map.put("param19", param19);
					map.put("param20", param20);
					WordUtils wu = new WordUtils();
					wu.createWord(rootPath+path,sbivo.getIdcard()+".doc","安置登记卡模板.ftl",map);
					
					list.get(i).setWheRegistrForm("2");
					list.get(i).setRegistrFormFilePath("file/"+path+sbivo.getIdcard()+".doc");
					psmMapper.updateByPrimaryKey(list.get(i));
					
					fileList.add(path+sbivo.getIdcard()+".doc");
				}
			}
			
			if(fileList.size()>1){
				//根据生成的word接收安置通知书，达成zip压缩包
				long start = System.currentTimeMillis();
				String path="安置登记卡/zip/";
				FileUtils.createFilePath(rootPath+path);
				
				String zipFile = path+"安置登记卡"+DateUtils.getStringCurrentDate()+".zip";
				WordUtils.fileToZip(fileList,zipFile ,rootPath);
				
				long end = System.currentTimeMillis();
				System.out.println("压缩完成，耗时：" + (end - start) + " ms");
				resultVo.setStatusCode(200);
				resultVo.setMessage("开具安置登记卡成功,共开具【"+fileList.size()+"】张");
				resultVo.setList("\"file/"+zipFile+"\"");
			}else if(fileList.size()== 1){
				resultVo.setStatusCode(200);
				resultVo.setMessage("开具安置登记卡成功");
				resultVo.setList("\"file/"+fileList.get(0)+"\"");
			}else{
				resultVo.setStatusCode(-1);
				resultVo.setMessage("未查询到可开具安置登记卡的数据");
			}
		} catch (Exception e) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("开具出现异常");
			e.printStackTrace();
		}
		
		return resultVo.toString();
	}
}
