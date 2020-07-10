package com.insigma.mapper;

import com.insigma.po.soldier.AreaCascadeData;
import com.insigma.po.soldier.PostSelectionManagement;
import java.util.List;
import java.util.Map;

public interface PostSelectionManagementMapper {
    int deleteByPrimaryKey(Integer psmId);

    int insert(PostSelectionManagement record);

    PostSelectionManagement selectByPrimaryKey(Integer sbiId);

    List<PostSelectionManagement> selectAll(Map<String, String> map);

    int updateByPrimaryKey(PostSelectionManagement record);

    List<PostSelectionManagement> getPostSelectionManageMentList(PostSelectionManagement psm);

    void updateBySubId(PostSelectionManagement psm);

    void updateStatusOfIssueNotification(Map<String,String>map);
    /**
     * 获取浙江及其所有市区所有符合条件的总数
     * @return
     */
   AreaCascadeData getTotalCountOfArea(PostSelectionManagement pos);
}