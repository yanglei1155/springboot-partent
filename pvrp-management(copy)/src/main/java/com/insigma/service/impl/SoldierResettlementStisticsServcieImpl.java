package com.insigma.service.impl;

import com.insigma.mapper.SoldierResettlementStisticsMapper;
import com.insigma.po.soldier.SoldierResettlementStistics;
import com.insigma.service.SoldierResettlementStisticsServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoldierResettlementStisticsServcieImpl implements SoldierResettlementStisticsServcie {
    @Autowired
    private SoldierResettlementStisticsMapper mapper;
    @Override
    public void insertSoldierResettlementStistics(List<SoldierResettlementStistics> resettlementStisticsList) {
       mapper.insertSoldierResettlementStistics(resettlementStisticsList);
    }
}
