package com.example.com.springboot.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "seckill_order")
@Data
public class Order extends BasePojo{


    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("秒杀商品id")
    private String seckill_id;

    @ApiModelProperty("价格")
    private String money;

    @ApiModelProperty("用户id")
    private String user_id;

    @ApiModelProperty("创建时间")
    private String create_time;

    @ApiModelProperty("支付时间")
    private String pay_time;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("接收地址")
    private String receiver_address;

    @ApiModelProperty("电话")
    private String receiver_phone;

    @ApiModelProperty("流水号")
    private String transaction_id;

    @ApiModelProperty("支付方式")
    private String pd_way;

    @Transient
    @ApiModelProperty("商品名称")
    private String name;

    @Transient
    @ApiModelProperty("商品图片")
    private String small_pice;

    @Transient
    @ApiModelProperty("用户名称")
    private String username;

    @Transient
    @ApiModelProperty("当前页数")
    private Integer pageNum;
}
