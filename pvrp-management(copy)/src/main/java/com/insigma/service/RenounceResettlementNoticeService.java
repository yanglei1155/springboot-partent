package com.insigma.service;

import com.insigma.po.soldier.RenounceResettlementNotice;
import java.util.List;

public interface RenounceResettlementNoticeService {
    int deleteByPrimaryKey(Integer rrnId);

    int insert(RenounceResettlementNotice record);

    RenounceResettlementNotice selectByPrimaryKey(String idcard);

    List<RenounceResettlementNotice> selectAll();

    int updateByPrimaryKey(RenounceResettlementNotice record);
}