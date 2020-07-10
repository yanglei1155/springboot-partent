package com.insigma.controller.sysuser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.insigma.dto.SysOrgDTO;
import com.insigma.dto.SysUserDTO;
import com.insigma.po.OrgVo;
import com.insigma.service.sysuser.UserInfoService;
import com.insigma.service.sysuser.facade.SysOrgFacade;
import com.insigma.service.sysuser.facade.SysUserFacade;
import com.insigma.util.TreeUtil;

import star.bizbase.exception.BizRuleException;
import star.bizbase.vo.result.Results;
import star.vo.result.ResultVo;

/**
 * 组织机构管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("sys/org")
public class SysOrgController extends BasicController {

	@Autowired
	private SysOrgFacade sysOrgFacade;
	@Autowired
	private SysUserFacade sysUserFacade;
	@Autowired
	private UserInfoService userInfoService;

	/**
	 * 查询树
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/queryTree" })
	public ResultVo<JSONArray> queryTree(HttpServletRequest request) {
		//获取当前登录用户的所属区域
		//从缓存获取用户信息
		SysUserDTO user = userInfoService.getUserInfo(request);
		ResultVo<JSONArray> result = Results.newResultVo();
		List<SysOrgDTO> orgList = null ;
		if(user.getUserType().equals("1")){//管理员获取所有机构
			HashMap<String, Object> searchMap = new HashMap<>();
			searchMap.put("orgState", "1");
			orgList = sysOrgFacade.getListByWhere(searchMap);
		}else{
			orgList = sysOrgFacade.queryOrgNodes(user.getAreaId().toString());
		}
		JSONArray jsonArray = TreeUtil.listToTree(JSONArray.parseArray(JSONArray.toJSONString(orgList)), "id",
				"parentId", "children");
		result.setCode("0");
		result.setSuccess(true);
		result.setResult(jsonArray);
		return result;

	}
	
	/**
	 * 保存组织机构
	 * @param sysOrg
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/saveOrg" },method = RequestMethod.POST)
	public ResultVo<JSONArray> saveOrg(@RequestBody OrgVo orgVo) {
		ResultVo<JSONArray> result = Results.newResultVo();
		result.setSuccess(false);
		SysOrgDTO sysOrg = orgVo.getSysOrgDTO();
		Boolean flag = sysOrgFacade.checkOrgNameOrgenterCode(sysOrg);
		try {
			if(flag == false){//没有重复
				if(null ==sysOrg.getId() || "".equals(sysOrg.getId())){
					sysOrg.setOrgState("1");
					Long id = sysOrgFacade.addSysOrg(sysOrg);
					sysOrg.setId(id);
					SysOrgDTO org = sysOrgFacade.findByName(sysOrg.getOrgName());
					if(null != org.getParentId() && !"".equals(org.getParentId())){
						//填充idpath
						SysOrgDTO org1 = sysOrgFacade.getByPrimaryKey(org.getParentId());
						org.setIdpath(org1.getIdpath()+"/"+org.getId());
					}else{
						org.setIdpath(org.getId().toString());
					}
					sysOrgFacade.updatepo(org);
				}else{
					sysOrgFacade.updatepo(sysOrg);
				}
				
				result.setCode("0");
				result.setSuccess(true);
				result.setResultDes("保存成功");
			}else{
				result.setResultDes("机构名称或机构代码重复");
			}
		} catch (Exception e) {
			result.setResultDes("组织机构保存失败，失败原因："+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 根据id查询详情
	 * @param orgId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/findOrgById" })
	public ResultVo<Map<String, Object>> findOrgById(Model model,String orgId) {
		ResultVo<Map<String, Object>> result = Results.newResultVo();
		result.setSuccess(false);
		Map<String, Object> map = new ConcurrentHashMap<>();
		if(null !=orgId && !"".equals(orgId)){
			SysOrgDTO sysOrg = sysOrgFacade.getByPrimaryKey(Long.parseLong(orgId));
			map.put("sysOrg", sysOrg);
			result.setResult(map);
			result.setCode("0");
			result.setSuccess(true);
		}
		return result;
	}
	
	/**
	 * 组织机构删除
	 * @param orgId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/deleteOrg" })
	public ResultVo<SysOrgDTO> deleteOrg(String orgId) {
		ResultVo<SysOrgDTO> result = Results.newResultVo();
		result.setSuccess(false);
		if(null !=orgId && !"".equals(orgId)){
			SysOrgDTO sysOrg = sysOrgFacade.getByPrimaryKey(Long.parseLong(orgId));
			if(null == sysOrg){
				result.setResultDes("组织机构不存在");
			}
			else{
				try {
					//查找是否有用户关联
					HashMap<String, Object> searchMap = new HashMap<String, Object>();
					searchMap.put("orgId", orgId);
					List<SysUserDTO> userList = sysUserFacade.getListByWhere(searchMap);
					if(userList.size()>0){
						result.setResultDes("有关联用户不能删除");
						return result;
					}
					
					sysOrgFacade.deleteByPrimaryKey(Long.parseLong(orgId));
					result.setResultDes("删除成功");
					result.setCode("0");
					result.setSuccess(true);
				} catch (BizRuleException e) {
					result.setResultDes("删除失败，失败原因："+e.getMessage());
				}
			}
		}
		return result;
	}
	
	
	/**
	 * 获取区域列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/getAreaTreeNodes" })
	public ResultVo<JSONArray> getAreaTreeNodes(HttpServletRequest request) {
		ResultVo<JSONArray> result = Results.newResultVo();
		//从缓存获取用户信息
		SysUserDTO sysUser = userInfoService.getUserInfo(request);
		JSONArray orgNodes = TreeUtil.listToTree(JSONArray.parseArray(JSONArray.toJSONString(sysUserFacade.queryAreaNodes(sysUser))), "id", "parentId", "children");
		result.setCode("0");
		result.setSuccess(true);
		result.setResult(orgNodes);
		return result;
	}
	
	
}
