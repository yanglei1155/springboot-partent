package com.insigma.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.insigma.po.ResultVo;
import com.insigma.po.soldier.AreaCascadeData;
import com.insigma.po.soldier.SysRegional;
import com.insigma.service.SysRegionalService;
import com.insigma.util.DateUtils;
import com.insigma.util.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insigma.mapper.PostSelectionManagementMapper;
import com.insigma.po.soldier.PostSelectionManagement;
import com.insigma.service.PostSelectionManagementService;

/**
 * 类描述：   选岗管理
 * 创建人：刘伟民
 * 创建时间：2020年5月28日 上午9:54:07
 */
@Service
public class PostSelectionManagementServiceImpl implements PostSelectionManagementService {
    @Autowired
    private PostSelectionManagementMapper mapper;

    @Autowired
    private SysRegionalService regionalService;

    @Autowired
    private SysRegionalService sysRegionalService;
    @Override
    public int deleteByPrimaryKey(Integer psmId) {
        // TODO Auto-generated method stub
        return mapper.deleteByPrimaryKey(psmId);
    }

    @Override
    public int insert(PostSelectionManagement record) {
        // TODO Auto-generated method stub
        return mapper.insert(record);
    }

    @Override
    public PostSelectionManagement selectByPrimaryKey(Integer psmId) {
        // TODO Auto-generated method stub
        return mapper.selectByPrimaryKey(psmId);
    }

    @Override
    public List<PostSelectionManagement> selectAll(Map<String, String> map) {
        // TODO Auto-generated method stub
        return mapper.selectAll(map);
    }

    @Override
    public int updateByPrimaryKey(PostSelectionManagement record) {
        // TODO Auto-generated method stub
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public List<PostSelectionManagement> getPostSelectionManageMentList(PostSelectionManagement psm) {
        if (psm.getRegionalCode() != null) {
            if (psm.getRegionalCode().equals("330000")) {
                psm.setRegionalCode("330000".substring(0, 2));
            } else {
                if (psm.getRegionalCode().endsWith("00")) {
                    psm.setRegionalCode(psm.getRegionalCode().substring(0, 4));
                }
            }
        }
        //查询前先先以当前时间判断是否超过截止报道时间更新开具状态（报道截止时间小于当前时间就可开具报告）
        Map<String,String>paramMap=new HashMap<>();
        paramMap.put("nowTime",DateUtils.getStringCurrentDate(DateUtils.date6));
        paramMap.put("flag","true");
        updateStatusOfIssueNotification(paramMap);
        PageHelper.startPage(psm.getPageNum(), psm.getPageSize());
        List<PostSelectionManagement> postSelectionManageMentList = mapper.getPostSelectionManageMentList(psm);
        //根据市区编码获取市区名
        for (PostSelectionManagement pos : postSelectionManageMentList) {
            Map<String, String> map = new HashMap<>();
            String[] place = pos.getResetPlace().split(",");
            String area = new String();
            pos.setIdcard(pos.getIdcard().replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
            for (String reginoalCode : place) {
                SysRegional sysRegional = new SysRegional();
                sysRegional.setRegionalCode(reginoalCode);
                List<SysRegional> sysRegionals = regionalService.selectAll(sysRegional);
                area += sysRegionals.get(0).getRegionalName();
            }
            pos.setResettlementDepartment(area + pos.getResettlementDepartment());
        }
        return postSelectionManageMentList;
    }

    @Override
    public void updateBySubId(PostSelectionManagement psm) {
        mapper.updateBySubId(psm);
    }

    @Override
    public ResultVo createIssueNotification(PostSelectionManagement pos) {
        Map<String, String> paramMap = new HashMap<>();
        Map<String, String> letterMap = new HashMap<>();
        Map<String, String> reportEndMap = new HashMap<>();
        Map<String,String>map=new HashMap<>();
        WordUtils wu = new WordUtils();
        ResultVo resultVo = new ResultVo();
        String path = "放弃安排工作待遇告知书/" + DateUtils.getStringCurrentDate() + "/";
        String newPath = pos.getRootPath() + path;
        //介绍信开具时间
        letterMap = DateUtils.getTime(pos.getCreateIntroletterTime(), DateUtils.date6);
        //报道截止时间
        reportEndMap = DateUtils.getTime(pos.getReportEndTime(), DateUtils.date6);
        paramMap.put("personnelType", pos.getPersonnelType());
        paramMap.put("name", pos.getName());
        paramMap.put("createYear", letterMap.get("year"));
        paramMap.put("createMonth", letterMap.get("month"));
        paramMap.put("createDay", letterMap.get("day"));
        paramMap.put("endYear", reportEndMap.get("year"));
        paramMap.put("endMonth", reportEndMap.get("month"));
        paramMap.put("endDay", reportEndMap.get("day"));
        paramMap.put("unitName", pos.getUnitName());
        Boolean createWord = wu.createWord(newPath, pos.getName() + "放弃安排工作待遇告知书" + ".doc", "视为放弃安排工作待遇告知书.ftl", paramMap);
        if (createWord) {
            //word文档创建成功开具成功更新开具状态(根据士兵id)
            map.put("issueNotification","1");
            map.put("sbId",String.valueOf(pos.getSbiId()));
            map.put("issueFilePath",newPath+pos.getName() + "放弃安排工作待遇告知书" + ".doc");
            updateStatusOfIssueNotification(map);
            resultVo.setStatusCode(200);
            resultVo.setMessage("开具成功");
            resultVo.setList(newPath+pos.getName() + "放弃安排工作待遇告知书" + ".doc");
        } else {
            resultVo.setStatusCode(-1);
            resultVo.setMessage("开具失败");
        }
        return resultVo;
    }

    @Override
    public void updateStatusOfIssueNotification(Map<String,String>map) {
         mapper.updateStatusOfIssueNotification(map);
    }

    @Override
    public AreaCascadeData getTotalCountOfArea(PostSelectionManagement pos) {
        Map<String,List<AreaCascadeData>>map=new HashMap<>();
        //市的集合
        List<AreaCascadeData>totalCountOfCityList=new ArrayList<>();
        String regionalCode=pos.getRegionalCode();
        //浙江省的总数
            pos.setRegionalCode(regionalCode.substring(0,2));
            AreaCascadeData totalCountOfProvince = mapper.getTotalCountOfArea(pos);
            totalCountOfProvince.setRegionalCode(regionalCode);
            totalCountOfProvince.setRegionalName("浙江省");
            //浙江省市区的总数
            SysRegional sysRegional=new SysRegional();
            sysRegional.setParentId(Integer.parseInt(regionalCode));
            //所有市区
            List<SysRegional> sysRegionalCitys = sysRegionalService.selectAll(sysRegional);
            for(SysRegional sysRegionalCity:sysRegionalCitys){
                pos.setRegionalCode(sysRegionalCity.getRegionalCode().substring(0,4));
                AreaCascadeData totalCountOfCity = mapper.getTotalCountOfArea(pos);
                totalCountOfCity.setRegionalName(sysRegionalCity.getRegionalName());
                totalCountOfCity.setRegionalCode(sysRegionalCity.getRegionalCode());
                //特定市所有区总数的集合
                SysRegional sysRegiona=new SysRegional();
                sysRegiona.setParentId(Integer.parseInt(sysRegionalCity.getRegionalCode()));
                List<SysRegional> sysRegionalAreas = sysRegionalService.selectAll(sysRegiona);
                //遍历特定市所有区
                //区的集合
                List<AreaCascadeData>totalCountOfAreaList=new ArrayList<>();
                for(SysRegional sysRegionalArea:sysRegionalAreas){
                    pos.setRegionalCode(sysRegionalArea.getRegionalCode());
                    AreaCascadeData totalCountOfArea = mapper.getTotalCountOfArea(pos);
                    totalCountOfArea.setRegionalCode(sysRegionalArea.getRegionalCode());
                    totalCountOfArea.setRegionalName(sysRegionalArea.getRegionalName());
                    totalCountOfAreaList.add(totalCountOfArea);
                }
              totalCountOfCity.setChildren(totalCountOfAreaList);
              totalCountOfCityList.add(totalCountOfCity);
        }
         totalCountOfProvince.setChildren(totalCountOfCityList);
        return totalCountOfProvince;
    }


}
