package com.insigma.service;

import com.insigma.po.soldier.PersonnelArchivesLog;
import java.util.List;

public interface PersonnelArchivesLogService {
    int deleteByPrimaryKey(Integer palId);

    /**
     * 保存操作日志
     * @param record
     * @return
     */
    int insert(PersonnelArchivesLog record);

    PersonnelArchivesLog selectByPrimaryKey(Integer palId);

    List<PersonnelArchivesLog> selectAll(PersonnelArchivesLog record);

    int updateByPrimaryKey(PersonnelArchivesLog record);
    /**
     * 保存多条日志信息
     * @param list
     * @return
     */
    void insertList(List<PersonnelArchivesLog> list);
}