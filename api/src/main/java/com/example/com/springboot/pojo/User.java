package com.example.com.springboot.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "user")
@Data
public class User extends BasePojo{
    @ApiModelProperty("用户id")
    private String uid;


    @ApiModelProperty("用户权限")
    private String power;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("用户头像")
    private String head_img;

    @Transient
    @ApiModelProperty("用户地址")
    private String address;

    @Transient
    @ApiModelProperty("地址id")
    private String addressId;

    @Transient
    @ApiModelProperty("用户电话")
    private String phone;

    @Transient
    @ApiModelProperty("地址状态")
    private String status;
}
