package com.insigma.controller.sysuser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.insigma.dto.SysAreaDTO;
import com.insigma.dto.SysOrgDTO;
import com.insigma.dto.SysRoleDTO;
import com.insigma.dto.SysUserAreaDTO;
import com.insigma.dto.SysUserDTO;
import com.insigma.dto.SysUserRoleDTO;
import com.insigma.po.UserVo;
import com.insigma.service.sysuser.UserInfoService;
import com.insigma.service.sysuser.facade.SysAreaFacade;
import com.insigma.service.sysuser.facade.SysOrgFacade;
import com.insigma.service.sysuser.facade.SysRoleFacade;
import com.insigma.service.sysuser.facade.SysUserFacade;
import com.insigma.util.JavaBeanUtils;
import com.insigma.util.TreeUtil;

import star.bizbase.vo.result.Results;
import star.util.StringUtil;
import star.vo.result.ResultVo;

/**
 * 系统用户 管理
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("sys/user")
public class SysUserController extends BasicController {

	@Autowired
	private SysUserFacade sysUserFacade;
	@Autowired
	private SysOrgFacade sysOrgFacade;
	@Autowired
	private SysAreaFacade sysAreaFacade;
	@Autowired
	private SysRoleFacade sysRoleFacade;
	@Autowired
	private UserInfoService userInfoService;
	/**
	 * 分页查询数据
	 * @param logonName
	 * @param displayName
	 * @param orgId
	 * @param userState
	 * @param page
	 * @param size
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/queryTable" })
	public ResultVo<UserVo> queryTable(HttpServletRequest request,
												@RequestParam(name = "logonName") String logonName,
									            @RequestParam(name = "displayName") String displayName,
									            @RequestParam(name = "orgId") String orgId,
									            @RequestParam(name = "userState") String userState,
									            @RequestParam(name = "page") Integer page,
									            @RequestParam(name = "size") Integer size) {
		ResultVo<UserVo> result = Results.newResultVo();
		HashMap<String, Object> searchMap = new HashMap<>();
		searchMap.put("logonName", logonName);
		searchMap.put("displayName", displayName);
		searchMap.put("orgId", orgId);
		searchMap.put("userState", userState);
		//计算当前页
//		page = (page -1)*size;
		//从缓存获取用户信息
		SysUserDTO user = userInfoService.getUserInfo(request);
		searchMap.put("user", user);
//		List<SysUserDTO> list = sysUserFacade.getListByWhere(searchMap,page,size);
		List<SysUserDTO> list = sysUserFacade.getListByWhere(searchMap);
		int count = sysUserFacade.getCountByWhere(searchMap);
		
		int pageCount=count/page;
        int fromIndex = size * (page - 1);
        int toIndex = fromIndex + size;
        if (toIndex >= count) {
            toIndex = count;
        }
        if(page>pageCount+1){
            fromIndex=0;
            toIndex=0;
        }
        list = list.subList(fromIndex, toIndex);
		
		UserVo userVo = new UserVo();
		userVo.setCount(count);
		userVo.setSysUserDTO(user);
		userVo.setUserList(list);
		
		result.setSuccess(true);
		result.setResultDes("success");
		result.setCode("0");
		result.setResult(userVo);
		return result;
	}
	
	/**
	 * 保存用户
	 * @param pageData
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/doSave" },method = RequestMethod.POST)
	public ResultVo<SysUserDTO> save(HttpServletRequest request,@RequestBody JSONObject pageData){
		ResultVo<SysUserDTO> result = Results.newResultVo();
		result.setSuccess(false);
		try {
			SysUserDTO sysUser = JavaBeanUtils.pageElementToBean(pageData,SysUserDTO.class);
			//记录回退日志

			SysUserDTO user = userInfoService.getUserInfo(request);

			boolean bool = true;// 校验是否通过
			bool = sysUserFacade.checkLogonName(sysUser.getLogonName(), sysUser.getId());
			if (bool) {
				result.setResultDes("存在相同登录名");
				result.setSuccess(false);
				return result;
			}
			JSONArray roleIds = pageData.getJSONArray("roleIds");
            List<SysUserRoleDTO> list = new ArrayList<>();
            for(int i=0;i<roleIds.size();i++){
   			 	JSONObject job = roleIds.getJSONObject(i);
   			 	SysUserRoleDTO sysUserRole = new SysUserRoleDTO();
                sysUserRole.setRoleId(job.get("id").toString());
                list.add(sysUserRole);
            }
            sysUser.setSysUserRoleList(list);
            //区域树值
            JSONArray areaIds = pageData.getJSONArray("areaIds");
            List<String> addAreaIds = new ArrayList<>();
            for (int i = 0; i < areaIds.size(); i++) {
                JSONObject obj = (JSONObject) areaIds.get(i);
                addAreaIds.add((String) obj.get("id"));
            }
            Map<String, List<String>> map = new ConcurrentHashMap<>();
            map.put("addAreaIds", addAreaIds);
            sysUserFacade.saveUser(sysUser, map);
            result.setCode("0");
            result.setSuccess(true);
            result.setResultDes("保存成功");
            result.setResult(user);
		} catch (Exception e) {
			result.setResultDes("保存失败，失败原因："+e.getMessage());
		}
		
		return result;
	}


	/**
	 * 修改密码
	 * 
	 * @param model
	 * @param oldPasswd
	 * @param newPasswd
	 * @return
	 */
	@RequestMapping("/updatePasswd")
	@ResponseBody
	public Model updatePasswd(HttpServletRequest request, Model model,String oldPasswd,String newPasswd) {
		ResultVo<SysUserDTO> result = Results.newResultVo();
		//从缓存获取用户信息
		SysUserDTO user = userInfoService.getUserInfo(request);
		result.setSuccess(false);
		if (StringUtil.isNotEmpty(oldPasswd) && StringUtil.isNotEmpty(newPasswd)) {
			if (StringUtil.getMD5(oldPasswd).equals(user.getPasswd())) {
				user.setPasswd(StringUtil.getMD5(newPasswd));
				int ret = sysUserFacade.updatepo(user);
				if (ret > 0) {
					result.setCode("0");
					result.setResultDes("密码修改成功");
					result.setSuccess(true);
				}
			} else {
				result.setResultDes("原密码不正确");
				result.setSuccess(false);
			}
		} else {
			result.setResultDes("信息不全");
			result.setSuccess(false);
		}
		model.addAttribute("result", result);
		return model;
	}

	/**
	 * 用户注销
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("/logoutUser")
	@ResponseBody
	public ResultVo<String> logoutUser(String userId) {
		ResultVo<String> result = Results.newResultVo();
		result.setSuccess(false);
		try {
			//从缓存获取用户信息
			Long loninUserId = 0L;
			if(loninUserId.equals(userId)){
				result.setSuccess(false);
				result.setResultDes("当前用户不可注销");
				return result;
			}
			sysUserFacade.logoutUser(Long.parseLong(userId));
			result.setResultDes("注销成功");
			result.setCode("0");
			result.setSuccess(true);
		} catch (Exception e) {
			result.setResultDes("注销失败，失败原因：" + e.getMessage());
		}
		return result;
	}

	/**
	 * 用户解锁
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("/unlockUser")
	@ResponseBody
	public ResultVo<String> unlockUser(String userId) {
		ResultVo<String> result = Results.newResultVo();
		result.setSuccess(false);
		try {
			//从缓存获取用户信息
			Long loninUserId = 0L;
			
			if(loninUserId.equals(userId)){
				result.setSuccess(false);
				result.setResultDes("当前用户不可操作");
				return result;
			}
			sysUserFacade.unlockUser(Long.parseLong(userId));
			result.setResultDes("解锁成功");
			result.setCode("0");
			result.setSuccess(true);
		} catch (Exception e) {
			result.setResultDes("解锁失败，失败原因：" + e.getMessage());
		}
		return result;
	}

	/**
	 * 锁定用户
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("/lockUser")
	@ResponseBody
	public ResultVo<String> lockUser(String userId) {
		ResultVo<String> result = Results.newResultVo();
		result.setSuccess(false);
		try {
			//从缓存获取用户信息
			Long loninUserId = 0L;
			if(loninUserId.equals(userId)){
				result.setSuccess(false);
				result.setResultDes("当前用户不可锁定");
				return result;
			}
			sysUserFacade.lockUser(Long.parseLong(userId));
			result.setResultDes("锁定成功");
			result.setCode("0");
			result.setSuccess(true);
		} catch (Exception e) {
			result.setResultDes("锁定失败，失败原因：" + e.getMessage());
		}
		return result;
	}

	/**
	 * 初始化密码
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("/initPasswd")
	@ResponseBody
	public ResultVo<String> initPasswd(String userId) {
		ResultVo<String> result = Results.newResultVo();
		result.setSuccess(false);
		int ret;
		try {
			ret = sysUserFacade.resetPassWD(Long.parseLong(userId));
			if (ret > 0) {
				result.setCode("0");
				result.setSuccess(true);
				result.setResultDes("初始化密码成功，默认密码为：000000");
			}
		} catch (Exception e) {
			result.setResultDes("初始化密码失败，失败原因：" + e.getMessage());
		}
		return result;
	}

	/**
	 * 校验用户登录名是否重复
	 * @param userId
	 * @param logonName
	 * @return
	 */
	@RequestMapping("/doCheck")
	@ResponseBody
	public ResultVo<String> doCheck(String userId, String logonName) {
		boolean bool = true;// 校验是否通过
		ResultVo<String> result = Results.newResultVo();
		result.setSuccess(false);
		bool = sysUserFacade.checkLogonName(logonName, Long.parseLong(userId));
		if (bool) {
			result.setResultDes("存在相同登录名");
			result.setSuccess(false);
		} else {
			result.setResultDes("登录名可用");
			result.setSuccess(true);
		}
		return result;
	}
	
	/**
	 *根据用户ID查询用户所有信息
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = { "/queryOneUser" })
	@ResponseBody
    public ResultVo<Map<String, Object>> queryOneUser(HttpServletRequest request,String userId){
		ResultVo<Map<String, Object>> result = Results.newResultVo();
		result.setSuccess(false);
		SysUserDTO sysUser = sysUserFacade.getByPrimaryKey(Long.parseLong(userId));
        List<SysUserRoleDTO> roleList;
        List<SysUserAreaDTO> areaList;
        List<SysUserRoleDTO> userRoleList = new ArrayList<SysUserRoleDTO>();
		try {
			roleList = sysUserFacade.queryUserRole(Long.parseLong(userId));
			for(SysUserRoleDTO ur :roleList){
				ur.setId(ur.getRoleId());
				SysRoleDTO role = sysRoleFacade.getByPrimaryKey(ur.getRoleId());
				ur.setRoleName(role.getRoleName());
				userRoleList.add(ur);
			}
			areaList = sysUserFacade.queryUserArea(Long.parseLong(userId));
			SysOrgDTO sysOrg = sysOrgFacade.getByPrimaryKey(sysUser.getOrgId());
	        SysAreaDTO area = sysAreaFacade.getByPrimaryKey(sysUser.getAreaId());
	        Map<String, Object> map = new ConcurrentHashMap<>();
	        map.put("sysUser", sysUser);
	        map.put("roleList", userRoleList);
	        map.put("areaList", areaList);

	        if (sysOrg != null) {
	            map.put("orgId", sysUser.getOrgId());
	            map.put("sysOrgs",sysOrg);
	        }
	        List<String> areaArray =new ArrayList<String>();
	        if (area != null){
	        	String pid = area.getParentId();
	        	if(null != pid && !"".equals(pid)){
	        		SysAreaDTO area2 = sysAreaFacade.getByPrimaryKey(Long.parseLong(pid));
	        		if(null !=area2.getParentId() && !"".equals(area2.getParentId())){
	        			areaArray.add(area2.getParentId());
	        		}
	        		areaArray.add(area.getParentId());
	        	}
	        	areaArray.add(area.getId().toString());
	        }
	        map.put("areaArray", areaArray);
	        result.setResult(map);
	        result.setCode("0");
	        result.setSuccess(true);    
		} catch (Exception e) {
			result.setResultDes("信息查询失败，失败原因："+e.getMessage());
		}
        return result;
    }
	
	/**
	 * 获取组织机构列表
	 * @param areaId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/getOrgTreeNodes" })
	public ResultVo<JSONArray> getOrgTreeNodes(Long userid,String areaId) {
		//从缓存获取用户信息
//		Long userId = loginComonent.getLoginUserId();
//		SysUserDTO sysUser = sysUserFacade.getCacheByPrimaryKey(userId);
		SysUserDTO sysUser = sysUserFacade.getByPrimaryKey(userid);
		ResultVo<JSONArray> result = Results.newResultVo();
		JSONArray orgNodes = TreeUtil.listToTree(JSONArray.parseArray(JSONArray.toJSONString(sysUserFacade.queryOrgNodes(areaId,sysUser))), "id", "parentId", "children");
		result.setCode("0");
		result.setSuccess(true);
		result.setResult(orgNodes);
		return result;
	}
	
	/**
	 * 获取区域列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/getAreaTreeNodes" })
	public ResultVo<JSONArray> getAreaTreeNodes(Long userid) {
		ResultVo<JSONArray> result = Results.newResultVo();
//		//从缓存获取用户信息
//		Long userId = loginComonent.getLoginUserId();
//		SysUserDTO sysUser = sysUserFacade.getCacheByPrimaryKey(userId);
		//从缓存获取用户信息
		SysUserDTO sysUser = sysUserFacade.getByPrimaryKey(userid);
		
		JSONArray orgNodes = TreeUtil.listToTree(JSONArray.parseArray(JSONArray.toJSONString(sysUserFacade.queryAreaNodes(sysUser))), "id", "parentId", "children");
		result.setCode("0");
		result.setSuccess(true);
		result.setResult(orgNodes);
		return result;
	}
	
	/**
	 * 获取管辖行政区域
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/getAreaList" })
	public ResultVo<JSONArray> getAreaList(String areaId) {
		ResultVo<JSONArray> result = Results.newResultVo();
		JSONArray orgNodes = TreeUtil.listToTree(JSONArray.parseArray(JSONArray.toJSONString(sysAreaFacade.findByParentId(Long.parseLong(areaId)))), "id", "parentId", "children");
		result.setCode("0");
		result.setSuccess(true);
		result.setResult(orgNodes);
		return result;
	}
	
	/**
	 *  根据机构获取角色列表
	 * @param userType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/getRoleTreeNodes" })
	public ResultVo<JSONArray> getRoleTreeNodes(String orgId,String userType) {
		ResultVo<JSONArray> result = Results.newResultVo();
		HashMap<String, Object> map = new HashMap<>();
        map.put("orgId", orgId);
        map.put("roleType", userType);
        map.put("active", "1");
        List<SysRoleDTO> roleList = sysRoleFacade.getListByWhere(map);
		
		JSONArray orgNodes = TreeUtil.listToTree(JSONArray.parseArray(JSONArray.toJSONString(roleList)), "id", "parentId", "children");
		result.setCode("0");
		result.setSuccess(true);
		result.setResult(orgNodes);
		return result;
	}
	
}
