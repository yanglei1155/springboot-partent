package com.insigma.controller;

import com.github.pagehelper.PageInfo;
import com.insigma.po.ResultVo;
import com.insigma.po.SysParamInfo;
import com.insigma.po.soldier.AreaCascadeData;
import com.insigma.po.soldier.PostSelectionManagement;
import com.insigma.service.PostSelectionManagementService;
import com.insigma.service.SysParamInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 士兵选岗管理
 * 创建人：杨雷
 * 创建时间：2020年6月29日 下午 15：58
 * @version
 */
@RestController
@RequestMapping("/pvrpm/selection")
public class PostSelectionController {
    @Autowired
    PostSelectionManagementService postSelectService;

    @Autowired
    SysParamInfoService sysParamInfoService;

    @Value("${rootpath}")
    private String rootPath;

    /**
     * 获取接收安置管理列表
     * @param postSelectionManagement
     * @return
     */
    @GetMapping("selectPostSelection")
    @ResponseBody
    public PageInfo<PostSelectionManagement> getPostSelectionManageMentList(PostSelectionManagement postSelectionManagement){
        List<PostSelectionManagement> postSelectionManageMentList = postSelectService.getPostSelectionManageMentList(postSelectionManagement);
        PageInfo<PostSelectionManagement>pageInfo=new PageInfo<>(postSelectionManageMentList);
        return pageInfo;
    }

    /**
     * 按士兵id更新未接收原因或接收时间等字段
     * @param postSelectionManagement
     */
    @PostMapping("updatePostSelection")
    @ResponseBody
    public ResultVo updateBySbiId(PostSelectionManagement postSelectionManagement){
         ResultVo resultVo=new ResultVo();
        try {
            postSelectService.updateBySubId(postSelectionManagement);
            resultVo.setStatusCode(200);
            resultVo.setMessage("更新成功");
        }catch (RuntimeException e){
            e.printStackTrace();
            resultVo.setStatusCode(-1);
            resultVo.setMessage("更新失败");
        }
      return  resultVo;
    }

    /**
     * 获取未接收原因集合
     * @return
     */
    @GetMapping("getUnReceiveReason")
    @ResponseBody
    public List<SysParamInfo> getUnReceiveReason(){
        Map<String,String>map=new HashMap<>();
        map.put("groupKey","unreceiveReason");
        List<SysParamInfo> sysParamInfo = sysParamInfoService.getSysParamInfo(map);
        return  sysParamInfo;
    }

    /**
     * 开具工作待遇告知书
     * @param pos
     * @return
     */
    @PostMapping("createIssueNotification")
    @ResponseBody
    public  ResultVo createIssueNotification(PostSelectionManagement pos){
        pos.setRootPath(rootPath);
        ResultVo resultVo = postSelectService.createIssueNotification(pos);
        return resultVo;
    }

    /**
     * 根据地区统计收安置管理数
     * @param pos
     */
    @GetMapping("getPostSelectManagementByRegionalCode")
    @ResponseBody
    public AreaCascadeData getPostSelectManagementByRegionalCode(PostSelectionManagement pos){
        AreaCascadeData totalCountOfArea = postSelectService.getTotalCountOfArea(pos);
        return totalCountOfArea;
    }
}
