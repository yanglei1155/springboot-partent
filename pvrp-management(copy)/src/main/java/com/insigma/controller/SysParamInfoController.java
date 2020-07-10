package com.insigma.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import com.alibaba.fastjson.JSONObject;
import com.insigma.po.ResultVo;
import com.insigma.po.SysParamInfo;
import com.insigma.po.soldier.SysRegional;
import com.insigma.service.SysParamInfoService;
import com.insigma.service.SysRegionalService;
import com.insigma.service.sysuser.SysAreaService;
import com.insigma.util.CharUtils;
import com.insigma.util.ExcelUtils;

import sun.misc.BASE64Encoder;


@RestController
@RequestMapping("/pvrpm/param")
public class SysParamInfoController {
   
	@Autowired
	private SysParamInfoService mapper;
	
	@Autowired
	private SysRegionalService srMapper;
	
	@Autowired
    HttpServletRequest request;
	
	@Autowired
	private SysAreaService saMapper;
	
	/**
	 * 查询系统参数
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "getSysParamInfo",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public List<SysParamInfo> getSysParamInfo(@RequestBody JSONObject json){
		Map<String, String> map = new HashMap<>();
		map.put("groupKey", json.get("groupKey").toString());
		List<SysParamInfo> list= mapper.getSysParamInfo(map);
		return list;
    }
	
	/**
	 * 查询行政区划
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "getSysRegional",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public List<SysRegional> getSysRegional(@RequestBody SysRegional vo){
		String areaId=request.getHeader("areaId");
		String regionalCode = vo.getRegionalCode();
		
		
		if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
			if(areaId.endsWith("00")){//机构编号00结尾的 为地市用户
				vo.setParentId(Integer.parseInt(areaId));
			}else{
				vo.setRegionalCode(areaId);
			}
		}else{
			if(!StringUtils.isEmpty(regionalCode) && !regionalCode.equals("330000")){
				if(regionalCode.endsWith("00")){
					vo.setParentId(Integer.parseInt(areaId));
				}else{
					vo.setRegionalCode(areaId);
				}
			}else{
				vo.setParentId(Integer.parseInt(areaId));
			}
		}
		
//		if(!StringUtils.isEmpty(regionalCode)){
//			if(!areaId.equals("330000")){//判断用户所属机构，330000为省级用户
//				if(areaId.endsWith("00")){//机构编号00结尾的 为地市用户
//					vo.setParentId(Integer.parseInt(areaId));
//				}else{
//					vo.setRegionalCode(areaId);
//				}
//			}else{
//				vo.setParentId(Integer.parseInt(areaId));
//			}
//		}
		List<SysRegional> list= srMapper.selectAll(vo);
		return list;
    } 
	
	/**
	 * 查询行政区划
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "getSysRegionalOne",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public List<SysRegional> getSysRegionalOne(@RequestBody SysRegional vo){
		List<SysRegional> list= srMapper.selectAllName(vo);
		return list;
    }
	
	/**
	 * 查询所有行政区划
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "getSysRegionalAll",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public String getSysRegionalAll(@RequestBody SysRegional po){
		String regionalCode = po.getRegionalCode();
//		if(StringUtils.isEmpty(regionalCode)){
//			
//		}else{
//			
//		}
		List<Map<String, String>> listMap = new ArrayList<Map<String,String>>();
		po.setParentId(0);
		List<SysRegional> list= srMapper.selectAll(po);//查询出所有省
		for (SysRegional vo:list) {
			Map<String, String> map1 = new HashMap<>();
			map1.put("value", vo.getRegionalCode());
			map1.put("label", vo.getRegionalName());
			po.setParentId(Integer.parseInt(vo.getRegionalCode()));
			List<SysRegional> list1= srMapper.selectAll(po);//根据省份编码查询出市
			
			List<Map<String, String>> listMap1 = new ArrayList<Map<String,String>>();
			for(SysRegional vo1:list1){
				Map<String, String> map2 = new HashMap<>();
				map2.put("value", vo1.getRegionalCode());
				map2.put("label", vo1.getRegionalName());
				po.setParentId(Integer.parseInt(vo1.getRegionalCode()));
				List<SysRegional> list2= srMapper.selectAll(po);//根据地市编码查询出区县
				
				List<Map<String, String>> listMap2 = new ArrayList<Map<String,String>>();
				for(SysRegional vo2:list2){
					Map<String, String> map3 = new HashMap<>();
					map3.put("value", vo2.getRegionalCode());
					map3.put("label", vo2.getRegionalName());
					listMap2.add(map3);
				}
				map2.put("children",JSON.toJSON(listMap2).toString());
				listMap1.add(map2);
			}
			map1.put("children",JSON.toJSON(listMap1).toString());
			
			listMap.add(map1);
		}
		
		return JSON.toJSON(listMap).toString();
    } 
	
	@RequestMapping(value = "testFile",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public String testFile(@RequestBody SysRegional vo){
		List<SysRegional> list= srMapper.selectAllName(vo);
		return "";
    }
	
	public String storeFile(){
		byte[] buffer =null;
		try {
			File file = new File("");
			FileInputStream inputFile = new FileInputStream(file);
			buffer = new byte[(int)file.length()];
			inputFile.read(buffer);
			inputFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BASE64Encoder().encode(buffer);
	}
	
	
	@RequestMapping(value = "impNewRegional", method = RequestMethod.GET,produces="application/json; charset=utf-8")
	public String impNewRegional(@RequestParam("filePath") MultipartFile filePath){
		ResultVo resultVo = new ResultVo();
		
		ExcelUtils uet = new ExcelUtils();
		List<String[][]> list  = uet.getExcelData(filePath,1);
		if (list == null || list.isEmpty()) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("导入失败：数据为空");
			return resultVo.toString();
		}
		for (String[][] Sheets : list) { // 遍历sheet
			
			String parentId2="0";
			String parentId3="0";
			for (String[] rows : Sheets) { // 遍历行
				if (rows != null && rows.length > 1) {
					if("".equals(rows[0])&&"".equals(rows[1])){
						break;
					}
					
					SysRegional sr = new SysRegional();
					
					String row1 = rows[0];
					String row2 = rows[1];
					if(row1.endsWith("0000")){
						sr.setParentId(0);
						parentId2=row1;
					}else if(row1.endsWith("00")){
						sr.setParentId(Integer.parseInt(parentId2));
						parentId3=row1;
					}else{
						if(parentId2.equals("110000") || parentId2.equals("120000") 
								|| parentId2.equals("310000") || parentId2.equals("500000") ){
							sr.setParentId(Integer.parseInt(parentId2));
						}else{
							sr.setParentId(Integer.parseInt(parentId3));
						}
					}
					sr.setRegionalCode(row1);
					sr.setRegionalName(row2);
					sr.setStatus(1);
					sr.setCreateTime(new Date());
					sr.setModifyTime(new Date());
					srMapper.insert(sr);
					System.out.println(sr.getParentId()+"-----"+row1+"-----"+row2);
					
					
				}
			}
		}
    	return resultVo.toString();
    }
	
}
