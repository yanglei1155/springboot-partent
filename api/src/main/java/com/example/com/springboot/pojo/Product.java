package com.example.com.springboot.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "seckill_goods")
@Data
public class Product extends BasePojo{
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("商品图片")
    private String small_pice;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品原价")
    private String price;

    @ApiModelProperty("商品现价")
    private String coat_price;

    @ApiModelProperty("创建时间")
    private String create_time;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("起始时间")
    private String start_time;

    @ApiModelProperty("截至时间")
    private String end_time;

    @ApiModelProperty("描述")
    private String introduction;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("库存")
    private Integer stock_count;

    @Transient
    @ApiModelProperty("当前页数")
    private Integer  pageNum;

    @Transient
    @ApiModelProperty("开始时间戳")
    private String startSecond;

    @Transient
    @ApiModelProperty("截至时间戳")
    private String endSecond;

    @Transient
    @ApiModelProperty("当前时间")
    private String nowTime;

    @Transient
    @ApiModelProperty("跳转地址")
    private String url;

    @Transient
    @ApiModelProperty("用户id")
    private String uid;
}
