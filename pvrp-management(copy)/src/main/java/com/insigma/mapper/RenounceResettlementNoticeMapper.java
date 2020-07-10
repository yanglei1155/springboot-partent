package com.insigma.mapper;

import com.insigma.po.soldier.RenounceResettlementNotice;
import java.util.List;

public interface RenounceResettlementNoticeMapper {
    int deleteByPrimaryKey(Integer rrnId);

    int insert(RenounceResettlementNotice record);

    RenounceResettlementNotice selectByPrimaryKey(String idcard);

    List<RenounceResettlementNotice> selectAll();

    int updateByPrimaryKey(RenounceResettlementNotice record);
}