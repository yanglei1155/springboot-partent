package com.example.com.springboot.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;

@Table(name = "seckill_address")
@Data
public class Address  extends BasePojo{

    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("用户id")
    private String uid;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("电话号码")
    private String phone;
}
