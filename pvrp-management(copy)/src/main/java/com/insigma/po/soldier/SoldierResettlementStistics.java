package com.insigma.po.soldier;

import lombok.Data;

/**
 * @author 杨雷
 * @createTime 2020.7.2
 */
@Data
public class SoldierResettlementStistics {
    /**
     * 主键id
     */
     private String id;
    /**
     * 接收单位
     */
    private String receiveUnit;
    /**
     * 上级单位（公司）
     */
    private String parentUnit;
    /**
     * 下达计划数
     */
    private String releasePlanNum;
    /**
     * 安置地区
     */
    private String resettlementPlace;
    /**
     * 实际接收数
     */
    private String actualReceiveNum;
    /**
     * 类型 1央企 2省属事业 3省属企业
     */
    private String type;
    /**
     * 省份
     */
    private String province;

}
