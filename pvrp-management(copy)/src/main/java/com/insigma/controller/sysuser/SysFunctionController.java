package com.insigma.controller.sysuser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.insigma.service.sysuser.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.insigma.dto.SysFunctionDTO;
import com.insigma.dto.SysUserDTO;
import com.insigma.service.sysuser.facade.SysFunctionFacade;
import com.insigma.util.TreeUtil;

import star.bizbase.vo.result.Results;
import star.util.StringUtil;
import star.vo.result.ResultVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 菜单管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("sys/function")
public class SysFunctionController extends BasicController {

	@Autowired
	private SysFunctionFacade sysFunctionFacade;

	@Autowired
	private UserInfoService userInfoService;
	
	/**
	 * 菜单列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/queryTree" })
	public ResultVo<JSONArray> queryTree(String funType) {
		ResultVo<JSONArray> result = Results.newResultVo();
		List<SysFunctionDTO> functionList = sysFunctionFacade.findByFunTypeList(funType);
		JSONArray jsonArray = TreeUtil.listToTree(JSONArray.parseArray(JSONArray.toJSONString(functionList)), "id",
				"parentId", "children");
		result.setCode("0");
		result.setSuccess(true);
		result.setResult(jsonArray);
		
		return result;

	}

	/**
	 * 首页菜单树
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/queryFunctionList" })
	public ResultVo<JSONArray> queryFunctionList(HttpServletRequest request,String funType) {
		ResultVo<JSONArray> result = Results.newResultVo();
		//从缓存获取用户信息
		SysUserDTO sysUser = userInfoService.getUserInfo(request);
		List<SysFunctionDTO> functionList = new ArrayList<SysFunctionDTO>();
		if(null != funType){
			functionList = sysFunctionFacade.findByFunTypeAndUserList(funType,sysUser);
		}
		JSONArray jsonArray = TreeUtil.listToTree(JSONArray.parseArray(JSONArray.toJSONString(functionList)), "id",
				"parentId", "children");
		result.setCode("0");
		result.setSuccess(true);
		result.setResult(jsonArray);
		return result;

	}
	
	/**
	 * 保存菜单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/saveMenu" },method = RequestMethod.POST)
	public ResultVo<JSONArray> saveMenu(@RequestBody SysFunctionDTO sysFunction) {
		ResultVo<JSONArray> result = Results.newResultVo();
		result.setSuccess(false);
		Boolean flag = false;
		if("1".equals(sysFunction.getNodeType()) || "2".equals(sysFunction.getNodeType())){//父菜单，子节点的节点链接不校验
			flag = false;
		}else{
//			flag = sysFunctionFacade.checkLocation(sysFunction.getId(), sysFunction.getLocation());
		}
		try {
			if(flag == false){
				Map<String,Object> map  = new ConcurrentHashMap<>();
				if(null ==sysFunction.getId() || "".equals(sysFunction.getId())){
					sysFunction.setAuFlag("0");//自动审核
					sysFunction.setSystemType("0");
					//修改排序
					map.put("funOrder", sysFunction.getFunOrder());
					map.put("parentId", sysFunction.getParentId());
					map.put("type", "add");
					sysFunctionFacade.updateFunOrder(map);
					sysFunctionFacade.addSysFunction(sysFunction);
				}else{
					if(sysFunction.getFunType().equals("1")){
						result.setResultDes("此菜单不能修改");
						return result;
					}
					SysFunctionDTO fun = sysFunctionFacade.getByPrimaryKey(sysFunction.getId());
					if(!fun.getFunOrder().equals(sysFunction.getFunOrder())){//排序有改变
						if(fun.getFunOrder()>sysFunction.getFunOrder()){
							map.put("type", "updateSmall");//向小改
						}else{
							map.put("type", "updateBig");
						}
						//修改排序
						map.put("funOrder", sysFunction.getFunOrder());
						map.put("oldOrder", fun.getFunOrder());
						map.put("parentId", sysFunction.getParentId());
						sysFunctionFacade.updateFunOrder(map);
					}
					sysFunctionFacade.updateSysFunction(sysFunction);
				}
				
				result.setCode("0");
				result.setSuccess(true);
				result.setResultDes("保存成功");
			}else{
				result.setResultDes("路径重复");
			}
		} catch (Exception e) {
			result.setResultDes("菜单保存失败，失败原因："+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 查询详情
	 * @param functionId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/nodeClick" })
	public ResultVo<Map<String, Object>> nodeClick(String functionId) {
		ResultVo<Map<String, Object>> result = Results.newResultVo();
		Map<String, Object> map = new ConcurrentHashMap<>();
		result.setSuccess(false);
		if(null !=functionId && !"".equals(functionId)){
			SysFunctionDTO sysFunction = sysFunctionFacade.getByPrimaryKey(Long.parseLong(functionId));
			map.put("sysFunction", sysFunction);
			result.setResult(map);
			result.setCode("0");
			result.setSuccess(true);
		}
		return result;
	}
	
	/**
	 * 菜单删除
	 * @param functionId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/deleteMenu" })
	public ResultVo<SysFunctionDTO> deleteMenu(String functionId) {
		ResultVo<SysFunctionDTO> result = Results.newResultVo();
		result.setSuccess(false);
		if(null !=functionId && !"".equals(functionId)){
			SysFunctionDTO sysFunction = sysFunctionFacade.getByPrimaryKey(Long.parseLong(functionId));
			if(null == sysFunction){
				result.setResultDes("菜单不存在");
			}
			else{
				try {
					if(sysFunction.getFunType().equals("1")){
						result.setResultDes("此菜单不能删除");
						return result;
					}
					List<SysFunctionDTO> funList = sysFunctionFacade.getListByParentId(Long.parseLong(functionId));
					if(!funList.isEmpty()){
						result.setResultDes("存在下级菜单，不能删除");
						return result;
					}
					sysFunctionFacade.deleteMenu(sysFunction);
					result.setResultDes("删除成功");
					result.setCode("0");
					result.setSuccess(true);
				} catch (Exception e) {
					result.setResultDes("删除失败，失败原因："+e.getMessage());
				}
			}
		}
		return result;
	}
	
	/**
	 * 菜单拖拽
	 * @param id
	 * @param parentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/updateFunNode" })
	public ResultVo<Object> updateFunNode(String id,String parentId) {
		ResultVo<Object> result = Results.newResultVo();
		if(StringUtil.isNotEmpty(id) && StringUtil.isNotEmpty(parentId)){
			int funOrder = 0;
			List<SysFunctionDTO> funList = sysFunctionFacade.getListByParentId(Long.parseLong(parentId));
			if(funList.size()>0){
				funOrder = sysFunctionFacade.findMaxFunOrder(Long.parseLong(parentId));
			}
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("parentId", parentId);
			map.put("funOrder", funOrder+1);
			try {
				sysFunctionFacade.updateFunNode(map);
				
				result.setCode("0");
				result.setSuccess(true);
				result.setResultDes("拖拽成功");
			} catch (Exception e) {
				result.setResultDes("拖拽失败,失败原因："+e.getMessage());
				result.setSuccess(false);
			}
		}
		return result;
	}

}
