package com.insigma.mapper;

import com.insigma.po.soldier.PersonnelArchivesLog;
import java.util.List;

public interface PersonnelArchivesLogMapper {
    int deleteByPrimaryKey(Integer palId);

    int insert(PersonnelArchivesLog record);

    PersonnelArchivesLog selectByPrimaryKey(Integer palId);

    List<PersonnelArchivesLog> selectAll(PersonnelArchivesLog record);

    int updateByPrimaryKey(PersonnelArchivesLog record);
    
    void insertList(List<PersonnelArchivesLog> list);
}