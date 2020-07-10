package com.example.com.springboot.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;

@Table(name = "seckill_collect")
@Data
public class Collect  extends BasePojo{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户id")
    private String uid;

    @ApiModelProperty("商品id")
    private String gid;

}
