package com.insigma.service;

import com.insigma.po.soldier.SoldierResettlementStistics;

import java.util.List;


public interface SoldierResettlementStisticsServcie {
    /**
     * 插入接收安置统计表
     * @param resettlementStisticsList
     */
   void  insertSoldierResettlementStistics(List<SoldierResettlementStistics>resettlementStisticsList);
}
