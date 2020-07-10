package com.insigma.controller;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.insigma.po.SysOrg;
import com.insigma.service.sysuser.facade.SysOrgFacade;
import com.insigma.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.insigma.po.ResultVo;
import com.insigma.po.SysParamInfo;
import com.insigma.po.soldier.ArchivesReport;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SoldierBasicInfoStatic;
import com.insigma.po.soldier.SoldierBasicInfoTotal;
import com.insigma.po.soldier.SoldierResetInfo;
import com.insigma.po.soldier.SysRegional;
import com.insigma.po.soldier.TreeNodesParam;
import com.insigma.service.ArchivesReportService;
import com.insigma.service.AuditLogService;
import com.insigma.service.SoldierBasicInfoService;
import com.insigma.service.SoldierResetInfoService;
import com.insigma.service.SysParamInfoService;
import com.insigma.service.SysRegionalService;
import com.insigma.util.CharUtils;
import com.insigma.util.JavaBeanUtils;
import com.insigma.util.ParamUtils;

import net.minidev.json.JSONObject;

/**
 * 
 * 类描述：  报表统计查询 
 * 创建人：刘伟民   
 * 创建时间：2020年4月8日 上午9:09:59   
 * @version
 */
@RestController
@RequestMapping("/pvrpm/report")
public class ArchivesReportController {

	@Autowired
    HttpServletRequest request;
	
	@Autowired
	private ArchivesReportService mapper;
	
	@Autowired
	private SysRegionalService srsMapper;
	
	@Autowired
	private SysParamInfoService paramMapper;
	
	@Autowired
	private AuditLogService logMapper;
	
	@Autowired
	private SoldierBasicInfoService sbiMapper;
	
	@Autowired
	private SoldierResetInfoService sriMapper;
	
	@Autowired
	private SysOrgFacade orgMapper;
	/**
	 * 安置类别统计表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryReport1", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String queryReport1(@RequestBody JSONObject json) {
		ResultVo resultVo = new ResultVo();
		
		Map<String, String> map=new HashMap<>();
		map.put("year", json.get("year").toString());
		map.put("ybId", json.get("ybId").toString());
		map.put("personnelType", json.get("personnelType").toString());
		map.put("wheTransfer", json.get("wheTransfer").toString());
		List<ArchivesReport> list = mapper.queryReport1(map);
		if(list.size()>0){
			resultVo.setStatusCode(200);
			resultVo.setMessage("查询报表成功");
			resultVo.setList(JavaBeanUtils.beanToJsonStr(list));
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("暂无数据");
		}
		return resultVo.toString();
	}
	
	/**
	 * 易地安置统计表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryReport2", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String queryReport2(@RequestBody JSONObject json) {
		ResultVo resultVo = new ResultVo();
		
		Map<String, String> map=new HashMap<>();
		map.put("year", json.get("year").toString());
		map.put("ybId", json.get("ybId").toString());
		map.put("personnelType", json.get("personnelType").toString());
		List<ArchivesReport> list = mapper.queryReport2(map);
		if(list.size()>0){
			resultVo.setStatusCode(200);
			resultVo.setMessage("查询报表成功");
			resultVo.setList(JavaBeanUtils.beanToJsonStr(list));
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("暂无数据");
		}
		return resultVo.toString();
	}
	
	/**
	 * 档案审核问题汇总表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryReport3", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String queryReport3(@RequestBody JSONObject json) {
		ResultVo resultVo = new ResultVo();
		
		Map<String, String> map=new HashMap<>();
		map.put("year", json.get("year").toString());
		map.put("ybId", json.get("ybId").toString());
		map.put("personnelType", json.get("personnelType").toString());
		List<ArchivesReport> list = mapper.queryReport3(map);
		if(list.size()>0){
			resultVo.setStatusCode(200);
			resultVo.setMessage("查询报表成功");
			resultVo.setList(JavaBeanUtils.beanToJsonStr(list));
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("暂无数据");
		}
		return resultVo.toString();
	}
	
	/**
	 * 档案审核问题明细表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryReport4", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageInfo<ArchivesReport> queryReport4(@RequestBody JSONObject json) {
		
		Map<String, String> map=new HashMap<>();
		map.put("name", json.get("name").toString());
		map.put("year", json.get("year").toString());
		map.put("ybId", json.get("ybId").toString());
		map.put("personnelType", json.get("personnelType").toString());
		String regionalCode=json.get("regionalCode").toString();
		if(!StringUtils.isEmpty(regionalCode)){
			map.put("regionalCode", regionalCode.substring(0,4));
		}
		
		Integer pageNum=Integer.parseInt(json.get("pageNum").toString());
		Integer pageSize=Integer.parseInt(json.get("pageSize").toString());
		
		PageHelper.startPage(pageNum, pageSize);
		List<ArchivesReport> list = mapper.queryReport4(map);
		PageInfo<ArchivesReport> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 人员综合表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryReport5", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageInfo<SoldierBasicInfoStatic> queryReport5(@RequestBody JSONObject json) {
		//查询类型 1符合政府安排 2灵活就业 3自主就业
		String queryType = json.get("queryType").toString();
		String service_duration_begin = String.valueOf((int)json.get("service_duration") * 12);
		String service_duration_end = String.valueOf((int)json.get("service_duration") * 12+11);
		Map<String, String> map=new HashMap<>();
//		map.put("year", json.get("year").toString());
		map.put("ybId", json.get("ybId").toString());
		map.put("name", json.get("name").toString());
		map.put("personnelType", json.get("personnelType").toString());
		map.put("queryType", queryType);
        map.put("sex",json.get("sex").toString());
		map.put("nation",json.get("nation").toString());
		map.put("marriage_status",json.get("marriage_status").toString());
		map.put("soldiers_military_rank",json.get("soldiers_military_rank").toString());
		map.put("poli_affi_code",json.get("poli_affi_code").toString());
		map.put("edu_lev_code",json.get("edu_lev_code").toString());
		map.put("disability_categories_code",json.get("disability_categories_code").toString());
		map.put("disable_grade_code",json.get("disable_grade_code").toString());
		map.put("service_duration_begin",service_duration_begin);//服役区间（精确到月）
		map.put("service_duration_end",service_duration_end);//服役区间（精确到月）
		map.put("minAge",DateUtils.getBirthday((int)json.get("minAge")));//最小年龄
		map.put("maxAge",DateUtils.getBirthday((int)json.get("maxAge")));//最大年龄
		map.put("is_hard_area",json.get("is_hard_area").toString());
		map.put("hard_area_remark",json.get("hard_area_remark").toString());
		map.put("reward_remark",json.get("reward_remark").toString());
		map.put("punishment_remark",json.get("punishment_remark").toString());
		String regionalCode=json.get("regionalCode").toString();
		if(!regionalCode.equals("330000")){
			if(regionalCode.endsWith("00")){//机构编号00结尾的 为地市用户
				map.put("regionalCode", regionalCode.substring(0,4));
			}else{
				map.put("regionalCode", regionalCode);
			}
		}
		//查询所有参数配置表所有参数信息
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		List<SysParamInfo> rlist= null;
		
		//一次查询出所有行政区划数据
		List<SysRegional> srList = srsMapper.queryRegionalTotal(null);
		List<SysRegional> regional= null;
		
		Integer pageNum=Integer.parseInt(json.get("pageNum").toString());
		Integer pageSize=Integer.parseInt(json.get("pageSize").toString());
		
		PageHelper.startPage(pageNum, pageSize);
		List<SoldierBasicInfoStatic> list = mapper.queryReport5(map);
		
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
		PageInfo<SoldierBasicInfoStatic> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}
	
	
	/**
	 * 人员综合信息库-查询地区级联数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "querySoldierTreeNodes",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public JSONArray querySoldierTreeNodes(@RequestBody JSONObject json){
		String areaId=request.getHeader("areaId");
		
		Map<String, String> map=new HashMap<>();
		map.put("ybId", json.get("ybId").toString());
		
		JSONArray arr1 = new JSONArray();
		JSONArray arr2tmp = new JSONArray();
		if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
			if(areaId.endsWith("00")){//机构编号00结尾的 为地市用户
				map.put("regionalCode", areaId.substring(0,4));
			}else{
				map.put("regionalCode", areaId);
			}
		}
		//查询类型 1符合政府安排 2灵活就业
		map.put("queryType", json.get("queryType").toString());
		map.put("wheTransfer", "1");
		
		map.put("flag", "1");
		arr1 = JSONArray.parseArray(JSONArray.toJSONString(sbiMapper.queryAreaCascadeData(map)));//查询出总人数
		
		map.put("flag", "2");
		JSONArray arr2 = JSONArray.parseArray(JSONArray.toJSONString(sbiMapper.queryAreaCascadeData(map)));
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
			JSONArray arr3 = JSONArray.parseArray(JSONArray.toJSONString(sbiMapper.queryAreaCascadeData(map)));
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
	 * 根据士兵ID查询详细信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryReport5Detailed", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String queryReport5Detailed(@RequestBody JSONObject json) {
		ResultVo resultVo = new ResultVo();
		int sbiId = Integer.parseInt(json.get("sbiId").toString());
		
		SoldierResetInfo srivo = sriMapper.selectByPrimaryKey(sbiId);
		
		if(srivo!=null){
			resultVo.setStatusCode(200);
			resultVo.setMessage("查询成功");
			resultVo.setList(JavaBeanUtils.beanToJsonStr(srivo));
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("暂无数据");
		}
		return resultVo.toString();
	}
	
	/**
	 * 艰边/立功/处分统计表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryReport6", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String queryReport6(@RequestBody JSONObject json) {
		ResultVo resultVo = new ResultVo();
		
		Map<String, String> map=new HashMap<>();
		map.put("year", json.get("year").toString());
		map.put("ybId", json.get("ybId").toString());
		map.put("personnelType", json.get("personnelType").toString());
		List<ArchivesReport> list = mapper.queryReport6(map);
		if(list.size()>0){
			resultVo.setStatusCode(200);
			resultVo.setMessage("查询报表成功");
			resultVo.setList(JavaBeanUtils.beanToJsonStr(list));
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("暂无数据");
		}
		return resultVo.toString();
	}
	
	/**
	 * 综合成绩明细表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryReport7", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public PageInfo<ArchivesReport> queryReport7(@RequestBody JSONObject json) {
		
		Map<String, String> map=new HashMap<>();
		map.put("ybId", json.get("ybId").toString());
		map.put("name", json.get("name").toString());
		String regionalCode=json.get("regionalCode").toString();
		if(regionalCode.indexOf("330000")!=-1){//包含省所以查全省得
			map.put("province","33");//浙江省
		}else {
			if(regionalCode.indexOf("330000")==-1){//不包含省级筛选市级
				if(regionalCode.indexOf("330100")!=-1){//包含市级编码就去除只获取其地区编码
					regionalCode= regionalCode.replace("330100,", "");
				}
				if(regionalCode.indexOf("330200")!=-1){//包含市级编码就去除只获取其地区编码
					regionalCode=regionalCode.replace("330200,","");
				}
				if(regionalCode.indexOf("330300")!=-1){//包含市级编码就去除只获取其地区编码
					regionalCode=regionalCode.replace("330300,","");
				}

				if(regionalCode.indexOf("330400")!=-1){//包含市级编码就去除只获取其地区编码
					regionalCode=regionalCode.replace("330400,","");
				}
				if(regionalCode.indexOf("330500")!=-1){//包含市级编码就去除只获取其地区编码
					regionalCode=regionalCode.replace("330500,","");
				}
				if(regionalCode.indexOf("330600")!=-1){//包含市级编码就去除只获取其地区编码
					regionalCode=regionalCode.replace("330600,","");
				}
				if(regionalCode.indexOf("330600")!=-1){//包含市级编码就去除只获取其地区编码
					regionalCode=regionalCode.replace("330600,","");
				}
				if(regionalCode.indexOf("330700")!=-1){//包含市级编码就去除只获取其地区编码
					regionalCode=regionalCode.replace("330700,","");
				}
				if(regionalCode.indexOf("330800")!=-1){//包含市级编码就去除只获取其地区编码
					regionalCode=regionalCode.replace("330800,","");
				}
				if(regionalCode.indexOf("330900")!=-1){//包含市级编码就去除只获取其地区编码
					regionalCode=regionalCode.replace("330900,","");
				}
				if(regionalCode.indexOf("331000")!=-1){//包含市级编码就去除只获取其地区编码
					regionalCode=regionalCode.replace("331000,","");
				}
				if(regionalCode.indexOf("331100")!=-1){//包含市级编码就去除只获取其地区编码
					regionalCode=regionalCode.replace("331100,","");
				}
				map.put("regionalCode",regionalCode);
			}

		}
		
		Integer pageNum=Integer.parseInt(json.get("pageNum").toString());
		Integer pageSize=Integer.parseInt(json.get("pageSize").toString());
		
		PageHelper.startPage(pageNum, pageSize);
		List<ArchivesReport> list = mapper.queryReport7(map);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setIdcard(list.get(i).getIdcard().replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
		}
		PageInfo<ArchivesReport> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 根据行政区划查询级联数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryReport7Total",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
	public JSONArray queryReport7Total(@RequestBody JSONObject json){
		String areaId=request.getHeader("areaId");
		
		Map<String, String> map=new HashMap<>();
		map.put("ybId", json.get("ybId").toString());
		
		JSONArray arr1 = new JSONArray();
		JSONArray arr2tmp = new JSONArray();
		if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
			if(areaId.endsWith("00")){//机构编号00结尾的 为地市用户
				map.put("regionalCode", areaId.substring(0,4));
			}else{
				map.put("regionalCode", areaId);
			}
		}
		map.put("wheTransfer", "1");
		map.put("flag", "1");
		arr1 = JSONArray.parseArray(JSONArray.toJSONString(sbiMapper.queryAreaCascadeData(map)));//查询出总人数
		
		map.put("flag", "2");
		JSONArray arr2 = JSONArray.parseArray(JSONArray.toJSONString(sbiMapper.queryAreaCascadeData(map)));
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
			JSONArray arr3 = JSONArray.parseArray(JSONArray.toJSONString(sbiMapper.queryAreaCascadeData(map)));
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
	 * 易地安置原因统计表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryReport8", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String queryReport8(@RequestBody JSONObject json) {
		ResultVo resultVo = new ResultVo();
		
		Map<String, String> map=new HashMap<>();
		map.put("year", json.get("year").toString());
		map.put("ybId", json.get("ybId").toString());
		map.put("personnelType", json.get("personnelType").toString());
		List<ArchivesReport> list = mapper.queryReport8(map);
		if(list.size()>0){
			resultVo.setStatusCode(200);
			resultVo.setMessage("查询报表成功");
			resultVo.setList(JavaBeanUtils.beanToJsonStr(list));
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("暂无数据");
		}
		return resultVo.toString();
	}
	
	/**
	 * 符合条件退役人员分类表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryReport9", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String queryReport9(@RequestBody JSONObject json) {
		ResultVo resultVo = new ResultVo();
		
		Map<String, String> map=new HashMap<>();
		map.put("year", json.get("year").toString());
		map.put("ybId", json.get("ybId").toString());
		List<ArchivesReport> list = mapper.queryReport9(map);
		if(list.size()>0){
			resultVo.setStatusCode(200);
			resultVo.setMessage("查询报表成功");
			resultVo.setList(JavaBeanUtils.beanToJsonStr(list));
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("暂无数据");
		}
		return resultVo.toString();
	}
	
	/**
	 * 退役人员数量统计表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryReport10", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String queryReport10(@RequestBody JSONObject json) {
		ResultVo resultVo = new ResultVo();
		
		Map<String, String> map=new HashMap<>();
		map.put("year", json.get("year").toString());
		map.put("personnelType", json.get("personnelType").toString());
		if(!StringUtils.isEmpty(json.get("resetType").toString())){
			map.put("resetType", json.get("resetType").toString());
		}else{
			map.put("resetType", "3");
		}
		
		List<ArchivesReport> list = mapper.queryReport10(map);
		if(list.size()>0){
			resultVo.setStatusCode(200);
			resultVo.setMessage("查询报表成功");
			resultVo.setList(JavaBeanUtils.beanToJsonStr(list));
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("暂无数据");
		}
		return resultVo.toString();
	}
	
	
	/**
	 * 根据大单位统计各地市人数
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryReport12", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String queryReport12(@RequestBody JSONObject json) {
		ResultVo resultVo = new ResultVo();
		
		Map<String, String> map=new HashMap<>();
		map.put("year", json.get("year").toString());
		map.put("ybId", json.get("ybId").toString());
		map.put("personnelType", json.get("personnelType").toString());
		List<ArchivesReport> list = mapper.queryReport12(map);
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).getHzcount()<1 && list.get(i).getWzcount()<1 && list.get(i).getNbcount()<1 
						&& list.get(i).getJxcount()<1 && list.get(i).getHuzcount()<1 && list.get(i).getSxcount()<1 &&
						 list.get(i).getJhcount()<1 && list.get(i).getQzcount()<1 && list.get(i).getZscount()<1 &&
						 list.get(i).getTzcount()<1 && list.get(i).getLscount()<1 ){
					list.remove(i);
					i--;
				}
			}
			
			resultVo.setStatusCode(200);
			resultVo.setMessage("查询报表成功");
			resultVo.setList(JavaBeanUtils.beanToJsonStr(list));
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("暂无数据");
		}
		return resultVo.toString();
	}
	
	/**
	 * 符合条件退役人员接收单位类别统计表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryReport13", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String queryReport13(@RequestBody JSONObject json) {
		ResultVo resultVo = new ResultVo();
		
		Map<String, String> map=new HashMap<>();
		map.put("ybId", json.get("ybId").toString());
		map.put("personnelType", json.get("personnelType").toString());
		List<ArchivesReport> list = mapper.queryReport13(map);
		if(list.size()>0){
			NumberFormat nt = NumberFormat.getPercentInstance();
			nt.setMinimumFractionDigits(2);
			for (int i = 0; i < list.size(); i++) {
				Integer total1 = list.get(i).getCount1();
				Integer total2 = list.get(i).getCount2() 
								+ list.get(i).getCount3()
								+ list.get(i).getCount4()
								+ list.get(i).getCount5()
								+ list.get(i).getCount6();
				Integer total3 = list.get(i).getCount7() 
								+ list.get(i).getCount8()
								+ list.get(i).getCount9()
								+ list.get(i).getCount10();
				
				Integer total = total1+total2+total3+list.get(i).getCount11();
				list.get(i).setTotal(total);
				
				if(total>0 && total1>0){
					String proportion1 = nt.format((float) total1 / (float) total );
					list.get(i).setProportion1(proportion1);
				}
				if(total>0 && total2>0){
					String proportion2 = nt.format((float) total2 / (float) total );
					list.get(i).setProportion2(proportion2);
				}
				if(total>0 && total3>0){
					String proportion3 = nt.format((float) total3 / (float) total );
					list.get(i).setProportion3(proportion3);
				}
			}
			
			resultVo.setStatusCode(200);
			resultVo.setMessage("查询报表成功");
			resultVo.setList(JavaBeanUtils.beanToJsonStr(list));
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("暂无数据");
		}
		return resultVo.toString();
	}

	/**
	 * 转业士官接收安置情况统计表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryReport14", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public ArchivesReport queryReport14(@RequestBody JSONObject json){
		ResultVo resultVo=new ResultVo();
		HashMap<String,String>map=new HashMap<>();
		map.put("ybId",json.get("ybId").toString());
		map.put("year",json.get("year").toString());
		map.put("personnelType",json.get("personnelType").toString());
		Map<String, ArchivesReport> stringListMap = mapper.queryReport14(map);
		ArchivesReport archivesReport = stringListMap.get("province");
		return archivesReport;
	}
	/**
	 * 转业士官接收安置情况退档详情统计
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "queryReport15", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String queryReport15(@RequestBody JSONObject json){
		ResultVo resultVo=new ResultVo();
		HashMap<String,String>map=new HashMap<>();
		map.put("ybId",json.get("ybId").toString());
		map.put("year",json.get("year").toString());
		map.put("personnelType",json.get("personnelType").toString());
		map.put("name",json.get("name").toString());
		List<SoldierBasicInfo> list = mapper.queryReport15(map);
		if(list.size()>0){
			resultVo.setStatusCode(200);
			resultVo.setMessage("查询报表成功");
			resultVo.setList(JavaBeanUtils.beanToJsonStr(list));
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("暂无数据");
		}
		return resultVo.toString();
	}
}
