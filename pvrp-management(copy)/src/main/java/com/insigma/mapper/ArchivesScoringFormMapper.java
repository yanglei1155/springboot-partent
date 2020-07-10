package com.insigma.mapper;

import com.insigma.po.soldier.ArchivesScoringForm;
import java.util.List;

public interface ArchivesScoringFormMapper {
    int deleteByPrimaryKey(Integer asfId);

    int insert(ArchivesScoringForm record);

    ArchivesScoringForm selectByPrimaryKey(Integer asfId);

    List<ArchivesScoringForm> selectAll(ArchivesScoringForm record);

    int updateByPrimaryKey(ArchivesScoringForm record);
}