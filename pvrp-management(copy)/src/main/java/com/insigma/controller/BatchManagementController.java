package com.insigma.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.insigma.po.ResultVo;
import com.insigma.po.soldier.BatchManagement;
import com.insigma.service.BatchManagementService;
import com.insigma.util.DateUtils;

/**
 *       
 * 类描述：批次管理   
 * 创建人：liuwm   
 * 创建时间：2020年3月2日 下午1:59:43   
 * @version
 */

@RestController
@RequestMapping("/pvrpm/batch")
public class BatchManagementController {
	
	@Autowired
    HttpServletRequest request;
	
	@Autowired
	private BatchManagementService mapper;
	
	/**
	 * 新建批次信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "saveBatch",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public String saveBatch(@RequestBody BatchManagement vo){
		ResultVo resultVo = new ResultVo();
		
		String displayName="";
		try {
			displayName= URLDecoder.decode(request.getHeader("displayName"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String year=DateUtils.getStringYearDate();
		vo.setOperator(displayName);
		vo.setYear(Integer.parseInt(year));
		String batchName = vo.getBatchName();
		vo.setBatchName(year+"年"+batchName+"退役士兵移交安置");
		
		List<BatchManagement> list = mapper.selectAll(vo);
		if(list.size()>0){
			resultVo.setStatusCode(-1);
			resultVo.setMessage("新建失败:当前批次已存在");
			return resultVo.toString();
		}
		mapper.insert(vo);
		if(vo.getYbId()>0){
			resultVo.setStatusCode(200);
			resultVo.setMessage("新建成功");
		}else{
			resultVo.setStatusCode(-1);
			resultVo.setMessage("新建失败");
		}
    	return resultVo.toString();
    }
	
	/**
	 * 更新批次
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "updateBatch",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public String updateBatch(@RequestBody BatchManagement vo){
		ResultVo resultVo = new ResultVo();
		try {
			String year=DateUtils.getStringYearDate();
			vo.setYear(Integer.parseInt(year));
			String batchName = vo.getBatchName();
			vo.setBatchName(year+"年"+batchName+"退役士兵移交安置");
			mapper.updateByPrimaryKey(vo);
			resultVo.setStatusCode(200);
			resultVo.setMessage("修改成功");
		} catch (Exception e) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("修改失败");
		}
    	return resultVo.toString();
    }
	/**
	 * 删除批次
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "deleteBatch",method = RequestMethod.POST ,produces="application/json; charset=utf-8")
    public String deleteBatch(@RequestBody BatchManagement vo){
		ResultVo resultVo = new ResultVo();
		
		try {
			mapper.deleteByPrimaryKey(vo.getYbId());
			resultVo.setStatusCode(200);
			resultVo.setMessage("删除成功");
		} catch (Exception e) {
			resultVo.setStatusCode(-1);
			resultVo.setMessage("删除失败");
		}
    	return resultVo.toString();
    }
	
	/**
	 * 查询批次
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryBatch", method = RequestMethod.POST, produces = "application/json")
	public PageInfo<BatchManagement> queryBatch(@RequestBody BatchManagement vo) {
		
		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		List<BatchManagement> list = mapper.selectAll(vo);
		PageInfo<BatchManagement> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}
}
