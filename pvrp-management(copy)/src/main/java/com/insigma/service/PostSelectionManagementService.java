package com.insigma.service;

import com.insigma.po.ResultVo;
import com.insigma.po.soldier.AreaCascadeData;
import com.insigma.po.soldier.PostSelectionManagement;
import org.omg.CORBA.OBJ_ADAPTER;

import java.util.List;
import java.util.Map;

public interface PostSelectionManagementService {
    int deleteByPrimaryKey(Integer psmId);

    int insert(PostSelectionManagement record);

    PostSelectionManagement selectByPrimaryKey(Integer psmId);

    List<PostSelectionManagement> selectAll(Map<String, String> map);

    int updateByPrimaryKey(PostSelectionManagement record);

    List<PostSelectionManagement>getPostSelectionManageMentList(PostSelectionManagement psm);

    /**
     * 根据士兵id更新接收后的接收时间
     * 安置岗位，是否在编，以及超过时间
     * 开具放弃原因等字段
     * @param psm
     */
    void updateBySubId(PostSelectionManagement psm);

    /**
     * 开具放弃工作待遇
     * @param pos
     * @return
     */
    ResultVo createIssueNotification(PostSelectionManagement pos);

    /**
     * 更新开具状态（报道截止时间大于当前时间为可在规定时间内报道，
     * 当前时间小于报道截止时间为超过报道时间及可开具告知书）
     * @param map
     */
    void updateStatusOfIssueNotification(Map<String,String>map);

    /**
     * 获取浙江及其所有市区所有符合条件的总数
     * @return
     */
    AreaCascadeData getTotalCountOfArea(PostSelectionManagement pos);
}