package com.example.com.springboot.pojo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;

@Table(name = "tour_img")
@Data
public class TourImg extends  BasePojo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("tour的主键")
    private String tid;

    @ApiModelProperty("剩余图片")
    private String other_img;
}
