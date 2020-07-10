package com.insigma.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.insigma.po.ResultVo;
import com.insigma.po.SysParamInfo;
import com.insigma.po.soldier.ResetUnitInfo;
import com.insigma.po.soldier.SoldierBasicInfo;
import com.insigma.po.soldier.SoldierResetInfo;
import com.insigma.po.soldier.TreeNodesParam;
import com.insigma.service.ResetUnitInfoService;
import com.insigma.service.SysParamInfoService;
import com.insigma.util.CharUtils;
import com.insigma.util.ExcelUtils;
import com.insigma.util.ParamUtils;

/**
 * 暂不启用
 * 安置管理（录入年度指标）
 * @author chenkb
 *
 */
@RestController
@RequestMapping("/pvrpm/ruic")
public class ResetUnitInfoController {
	@Autowired
	private ResetUnitInfoService ruService;
	@Autowired
	private SysParamInfoService paramMapper;
	@Autowired
    HttpServletRequest request;
	/**
	 * 保存导入数据
	 * @param filePath 上传的excel
	 * @return
	 */
	@RequestMapping(value = "impResetUnitData", method = RequestMethod.GET,produces="application/json; charset=utf-8")
	public String impSoldierData(@RequestParam("filePath") MultipartFile filePath){
		ResultVo resultVo = new ResultVo();
		
		ExcelUtils uet = new ExcelUtils();
		List<String[][]> list  = uet.getExcelData(filePath,1);
		if (list == null || list.isEmpty()) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败：数据为空");
			return resultVo.toString();
		}
		int successCount=0;//成功数量
		int errorCount=0;//失败数量
		int number = 0;//第几条数据
		
		List<SysParamInfo> plist=paramMapper.getSysParamInfo(null);
		List<SysParamInfo> rlist= null;
		List<Map<String, String>> retList = new ArrayList<>();
		for (String[][] Sheets : list) { // 遍历sheet
			
			for (String[] rows : Sheets) { // 遍历行
				if (rows != null && rows.length > 1) {
					number++;
					String errorMsg="";//失败原因
					boolean flag=true;
					
					ResetUnitInfo ru = new ResetUnitInfo();
					if("".equals(rows[1])){
						flag=false;
						errorMsg="年度不能为空,";
					}
					ru.setYear(CharUtils.numberReplaceAll(rows[1]));
					
					if("".equals(rows[2])){
						flag=false;
						errorMsg+="单位名称不能为空,";
					}
					ru.setUnitName(CharUtils.replaceAllNull(rows[2]));
					
					if("".equals(rows[3])){
						flag=false;
						errorMsg+="单位所在市不能为空,";
					}
					ru.setCityOfUnit(CharUtils.replaceAllNull(rows[3]));
					
					if("".equals(rows[4])){
						flag=false;
						errorMsg+="单位所在区县不能为空,";
					}
					ru.setRegionalCode(CharUtils.replaceAllNull(rows[4]));
					
					if("".equals(rows[5])){
						flag=false;
						errorMsg+="接收单位类别不能为空,";
					}
					rlist=  ParamUtils.checkList(plist, "unitCategory", "", CharUtils.replaceAllNull(rows[5]));
					ru.setUnitCategory(rlist.get(0).getParamKey());
					
					if("".equals(rows[6])){
						flag=false;
						errorMsg+="劳动合同签约单位所在地不能为空,";
					}
					ru.setSigningRegional(CharUtils.replaceAllNull(rows[6]));
					
					if("".equals(rows[7])){
						flag=false;
						errorMsg+="岗位状况不能为空,";
					}
					ru.setPostsName(CharUtils.replaceAllNull(rows[7]));
					
					if("".equals(rows[8])){
						flag=false;
						errorMsg+="需求人数不能为空,";
					}
					ru.setDemandNum(CharUtils.numberReplaceAll(rows[8]));
					
					if("".equals(rows[9])){
						flag=false;
						errorMsg+="联系人不能为空,";
					}
					ru.setContactsName(CharUtils.replaceAllNull(rows[9]));
					//职务
					ru.setPositionName(CharUtils.replaceAllNull(rows[10]));
					
					if("".equals(rows[11]) && "".equals(rows[12])){
						flag=false;
						errorMsg+="手机号和座机不能同时为空,";
					}
					ru.setPhone(CharUtils.replaceAllNull(rows[11]));
					ru.setTelephone(CharUtils.replaceAllNull(rows[12]));
					//备注
					ru.setRemark(CharUtils.replaceAllNull(rows[13]));
					
					String name = ru.getUnitName();
					if(flag){
						ResetUnitInfo ruInfo = ruService.selectByUnitName(ru.getUnitName());
						if(ruInfo == null){
							int ret = ruService.insertResetUnitInfo(ru);
							if(ret == 0){
								errorCount++;
								errorMsg += "导入出现异常，请核对是否有重复数据,";
								Map<String, String> map = new HashMap<String, String>();
								map.put("errorMsg", "第【"+number+"】条数据【"+name+"】"+errorMsg.substring(0,errorMsg.length()-1));
								retList.add(map);
							}else{
								successCount++;
							}
						}else{
							ru.setRuiId(ruInfo.getRuiId());
							ruService.updateResetUnitInfo(ru);
							successCount++;
						}
					}else{
						Map<String, String> map = new HashMap<String, String>();
						map.put("errorMsg", "第【"+number+"】条数据【"+name+"】"+errorMsg.substring(0,errorMsg.length()-1));
						retList.add(map);
						errorCount++;
					}
				}
			}
		}
		resultVo.setStatusCode(200);
		resultVo.setMessage("共导入【"+number+"】条数据,成功【"+successCount+"】条,失败【"+errorCount+"】条");
		resultVo.setList(JSON.toJSON(retList).toString());
    	return resultVo.toString();
    }
	
	/**
	 * 新增保存数据
	 * @param ResetUnitInfo
	 * @return
	 */
	@RequestMapping(value = "saveResetUnitInfo", method = RequestMethod.POST,produces="application/json; charset=utf-8")
	public String saveResetUnitInfo(@RequestBody ResetUnitInfo resetUnitInfo){
		ResultVo resultVo = new ResultVo();
		if(resetUnitInfo.getPhone().length()>11){
			resultVo.setStatusCode(200);
			resultVo.setMessage("保存失败：手机号超过长度限制");
	    	return resultVo.toString();
		}
		resetUnitInfo.setDemandNum(CharUtils.numberReplaceAll(resetUnitInfo.getDemandNum()));
		int count =ruService.queryUnitNameCount(resetUnitInfo);
		if(count>0){
			resultVo.setStatusCode(-1);
			resultVo.setMessage("当前单位已存在");
		}else{
			ruService.insertResetUnitInfo(resetUnitInfo);
			resultVo.setStatusCode(200);
			resultVo.setMessage("保存成功");
		}
    	return resultVo.toString();
    }
	
	/**
	 * 编辑修改数据
	 * @param ResetUnitInfo
	 * @return
	 */
	@RequestMapping(value = "updateResetUnitInfo", method = RequestMethod.POST,produces="application/json; charset=utf-8")
	public String updateResetUnitInfo(@RequestBody ResetUnitInfo resetUnitInfo){
		ResultVo resultVo = new ResultVo();
		if(resetUnitInfo.getPhone().length()>11){
			resultVo.setStatusCode(200);
			resultVo.setMessage("保存失败：手机号超过长度限制");
	    	return resultVo.toString();
		}
		resetUnitInfo.setDemandNum(CharUtils.numberReplaceAll(resetUnitInfo.getDemandNum()));
		ruService.updateResetUnitInfo(resetUnitInfo);
		resultVo.setStatusCode(200);
		resultVo.setMessage("保存成功");
    	return resultVo.toString();
    }
	
	/**
	 * 删除数据
	 * @param ResetUnitInfo
	 * @return
	 */
	@RequestMapping(value = "deleteResetUnitInfo", method = RequestMethod.POST,produces="application/json; charset=utf-8")
	public String deleteResetUnitInfo(@RequestBody ResetUnitInfo resetUnitInfo){
		ResultVo resultVo = new ResultVo();
		ruService.deleteResetUnitInfo(resetUnitInfo.getRuiId());
		resultVo.setStatusCode(200);
		resultVo.setMessage("删除成功");
    	return resultVo.toString();
    }
	
	/**
	 * 根据ID查询数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "selectByPrimaryKey", method = RequestMethod.POST, produces = "application/json")
	public ResetUnitInfo selectByPrimaryKey(@RequestBody ResetUnitInfo vo) {
		ResetUnitInfo sbivo = ruService.selectByPrimaryKey(vo.getRuiId());
		return sbivo;
	}
	
	/**
	 * 查询年度单位指标列表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "getResetUnitList",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public PageInfo<ResetUnitInfo> getResetUnitList(@RequestBody JSONObject json){
		String areaId=request.getHeader("areaId");
		
		Map<String, String> map = new HashMap<>();
		map.put("unitName", json.getString("unitName"));
		map.put("year", json.getString("year"));
		
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
		
		int pageNum=Integer.parseInt(json.getString("pageNum"));
		int pageSize=Integer.parseInt(json.getString("pageSize"));
		PageHelper.startPage(pageNum, pageSize);
		List<ResetUnitInfo> list= ruService.getResetUnitList(map);
		PageInfo<ResetUnitInfo> pageInfo=new PageInfo<>(list);
    	return pageInfo;
    }

	
	/**
	 * 人员综合表根据行政区划查询级联数据
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryResetUnitInfoTreeNodes",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public JSONArray queryResetUnitInfoTreeNodes(@RequestBody JSONObject json){
		
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
		map.put("flag", "1");//查询浙江省安置总人数
		arr1 = JSONArray.parseArray(JSONArray.toJSONString(ruService.queryResetUnitInfoTreeNodes(map)));//查询出总人数
		
		map.put("flag", "2");//查询各地市总人数
		JSONArray arr2 = JSONArray.parseArray(JSONArray.toJSONString(ruService.queryResetUnitInfoTreeNodes(map)));
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
			JSONArray arr3 = JSONArray.parseArray(JSONArray.toJSONString(ruService.queryResetUnitInfoTreeNodes(map)));
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
