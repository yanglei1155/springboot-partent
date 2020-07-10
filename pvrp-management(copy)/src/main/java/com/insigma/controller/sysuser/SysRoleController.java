package com.insigma.controller.sysuser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.insigma.dto.SysFunctionDTO;
import com.insigma.dto.SysRoleDTO;
import com.insigma.dto.SysRoleFunctionDTO;
import com.insigma.dto.SysUserDTO;
import com.insigma.po.RoleVo;
import com.insigma.service.sysuser.UserInfoService;
import com.insigma.service.sysuser.facade.SysFunctionFacade;
import com.insigma.service.sysuser.facade.SysOrgFacade;
import com.insigma.service.sysuser.facade.SysRoleFacade;
import com.insigma.util.JavaBeanUtils;
import com.insigma.util.TreeUtil;

import star.bizbase.exception.BizRuleException;
import star.bizbase.vo.result.Results;
import star.vo.result.ResultVo;


/**
 * 系统角色 管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("sys/role")
public class SysRoleController extends BasicController {
	@Autowired
	private SysRoleFacade sysRoleFacade;
	@Autowired
	private SysFunctionFacade sysFunctionFacade;
	@Autowired
	private SysOrgFacade sysOrgFacade;
	@Autowired
	private UserInfoService userInfoService;

	/**
	 * 查询角色详情
	 * @param request
	 * @param roleId
	 * @return
	 * @throws BizRuleException
	 */
	@ResponseBody
	@RequestMapping(value = { "/detail" })
	public ResultVo<SysRoleDTO> detail(HttpServletRequest request, String roleId) {
		ResultVo<SysRoleDTO> result = Results.newResultVo();
		SysRoleDTO sysRole = sysRoleFacade.getByPrimaryKey(roleId);
//		SysOrgDTO org = sysOrgFacade.getByPrimaryKey(sysRole.getOrgId());
//		sysRole.setIdpath(org.getIdpath());
		result.setSuccess(true);
		result.setCode("0");
		result.setResult(sysRole);
		return result;

	}

	/**
	 * 分页查询数据
	 * 
	 * @param request
	 * @param roleName
	 * @param roleDesc
	 * @param roleType
	 * @param page
	 * @param size
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/queryTable" })
	public ResultVo<RoleVo> queryTable(HttpServletRequest request,
			@RequestParam(name = "roleName") String roleName,
			@RequestParam(name = "roleDesc") String roleDesc, 
			@RequestParam(name = "roleType") String roleType,
			@RequestParam(name = "page") Integer page, 
			@RequestParam(name = "size") Integer size) {
		ResultVo<RoleVo> result = Results.newResultVo();
		HashMap<String, Object> searchMap = new HashMap<>();
		searchMap.put("roleName", roleName);
		searchMap.put("roleDesc", roleDesc);
		searchMap.put("active", "1");
		searchMap.put("roleType", roleType);
		//从缓存获取用户信息
		SysUserDTO user = userInfoService.getUserInfo(request);
		String userType = user.getUserType();
		if (null != userType && !"".equals(userType)) {
//			if ("1".equals(userType)) {// 超级管理员
//				searchMap.put("roleType", roleType);
//			}
//			if ("2".equals(userType)) {// 行政区管理员
//				searchMap.put("areaId", user.getAreaId());
//			}
//			if ("3".equals(userType)) {// 机构管理员
//				searchMap.put("orgId", user.getOrgId());
//				if(null != roleType && !"".equals(roleType)){
//					searchMap.put("roleType", roleType);
//				}else{
//					searchMap.put("roleType", "4");
//				}
//			}
			// 计算当前页
			page = (page - 1) * size;
			List<SysRoleDTO> list = sysRoleFacade.getListByWhere(searchMap, page, size);
			int count = sysRoleFacade.getCountByWhere(searchMap);
			RoleVo roleVo = new RoleVo();
			roleVo.setCount(count);
			roleVo.setRoleList(list);
			result.setCode("0");
			result.setSuccess(true);
			result.setResult(roleVo);
		} else {
			result.setResultDes("非管理员角色，不能看到任何角色");
			result.setCode("-1");
		}
		return result;
	}

	/**
	 * 修改角色信息
	 * 
	 * @param sysRole
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/updateRole" },method = RequestMethod.POST)
	public ResultVo<SysRoleDTO> updateRole(@RequestBody SysRoleDTO sysRole) {
		ResultVo<SysRoleDTO> result = Results.newResultVo();
		boolean bool = true;// 校验是否通过
		bool = sysRoleFacade.checkRoleName(sysRole.getRoleName(), sysRole.getId());
		if (bool) {
			result.setResultDes("角色名已存在");
			result.setSuccess(false);
		} else {
			sysRoleFacade.updatepo(sysRole);
			result.setCode("0");
			result.setSuccess(true);
			result.setResultDes("修改成功");
			result.setResult(sysRole);
		}
		return result;

	}

	/**
	 * 新增角色信息
	 * 
	 * @param sysRole
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/addSysRole" },method = RequestMethod.POST)
	public ResultVo<Map<String, Object>> addSysRole(HttpServletRequest request,@RequestBody SysRoleDTO sysRole) {
		ResultVo<Map<String, Object>> result = Results.newResultVo();
		result.setSuccess(false);
		//生成主键ID
		String uuid = UUID.randomUUID().toString();
		String id = uuid.replace("-", "");
		sysRole.setId(id);

		//从缓存获取用户信息
		SysUserDTO sysUser = userInfoService.getUserInfo(request);

		//机构管理员获取自身区域和机构
		if(!"1".equals(sysUser.getUserType())){
			long areaId = sysUser.getAreaId();
			long orgId = sysUser.getOrgId();
			sysRole.setAreaId(areaId);
			sysRole.setOrgId(orgId);
		}
		sysRole.setCreatorId(sysUser.getId().toString());
		sysRole.setActive("1");

		try {
			boolean bool = true;// 校验是否通过
			bool = sysRoleFacade.checkRoleName(sysRole.getRoleName(), null);
			if (bool) {
				result.setResultDes("角色名已存在");
				result.setSuccess(false);
			} else {
				String msg ="";
				sysRoleFacade.addSysRole(sysRole);
				msg = "新增成功";
				result.setResultDes(msg);
				result.setCode("0");
				result.setSuccess(true);
			}
		} catch (Exception e) {
			result.setResultDes("新增失败，失败原因：" + e.getMessage());
		}
		return result;

	}

	/**
	 * 角色删除
	 * 
	 * @param request
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/deleteSysRole" })
	public ResultVo<SysRoleDTO> addSysRole(HttpServletRequest request, String roleId) {
		ResultVo<SysRoleDTO> result = Results.newResultVo();
		SysRoleDTO sysRole = sysRoleFacade.getByPrimaryKey(roleId);
		if (null == sysRole) {
			result.setResultDes("角色不存在");
			result.setSuccess(false);
		} else {
			try {
				sysRoleFacade.deleteByPrimaryKey(roleId);
				result.setResultDes("删除成功");
				result.setCode("0");
				result.setSuccess(true);
			} catch (Exception e) {
				result.setSuccess(false);
				result.setResultDes("删除失败，失败原因："+e.getMessage());
			}
			
		}
		return result;
	}

	/**
	 * 查询菜单树和已授权菜单
	 * 
	 * @param roleType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/queryTree" })
	public ResultVo<RoleVo> queryTree(Model model,String roleType,String roleId) {
		ResultVo<RoleVo> result = Results.newResultVo();
		List<SysFunctionDTO> functionList = new ArrayList<SysFunctionDTO>();
		Map<Integer,JSONArray> mapArray = new HashMap<>();
		for (int i = 1; i <= 2; i++) {
			functionList = sysFunctionFacade.findByFunTypeList(String.valueOf(i));
			JSONArray jsonArray = TreeUtil.listToTree(JSONArray.parseArray(JSONArray.toJSONString(functionList)), "id",
					"parentId", "children");
			mapArray.put(i, jsonArray);
		}
		
		//查询角色拥有的功能列表
		List<SysRoleFunctionDTO> rfList = sysFunctionFacade.findByRoleId(roleId);
		
		RoleVo roleVo = new RoleVo();
		roleVo.setMapArray(mapArray);
		roleVo.setRfList(rfList);
		
		result.setCode("0");
		result.setSuccess(true);
		result.setResult(roleVo);
		return result;
	}

	/**
	 * 授权
	 * 
	 * @param jsonObject
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/addRoleRef" },method = RequestMethod.POST)
	public ResultVo<String> addRoleRef(@RequestBody JSONObject jsonObject) {
		ResultVo<String> result = Results.newResultVo();
		String roleId = (String) jsonObject.get("roleId");
		// 判断角色有效性
		SysRoleDTO role = sysRoleFacade.getByPrimaryKey(roleId);
		JSONArray jsonArray = null;
		try {
			if (null != role) {
				jsonArray = jsonObject.getJSONArray("treeInfo");
				JSONObject jsonObject1 = null;
				List<SysRoleFunctionDTO> list = new ArrayList<>();
				for (int i = 0; i < jsonArray.size(); i++) {
					SysRoleFunctionDTO sysRoleFunction = JavaBeanUtils.pageElementToBean((JSONObject)jsonArray.get(i),SysRoleFunctionDTO.class);
//					jsonObject1 = (JSONObject) JSONObject.parse(jsonArray.get(i).toString());
//					Long functionId = jsonObject1.getLong("functionId");
//					list.add(functionId);
					list.add(sysRoleFunction);
				}
				sysRoleFacade.deleteRoleFunctionRefAndAddNewRef(roleId, list);
				result.setSuccess(true);
				result.setResultDes("授权成功");
			} else {
				result.setSuccess(false);
				result.setResultDes("角色不存在");
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResultDes("授权失败，失败原因：" + e.getMessage());
		}

		return result;
	}
}
