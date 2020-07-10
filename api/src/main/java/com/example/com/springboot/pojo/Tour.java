package com.example.com.springboot.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;
import java.util.List;

@Table(name="tour")
@Data
public class Tour extends BasePojo {

    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("图片")
    private String img;

    @ApiModelProperty("点赞数")
    private String zan;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("关注")
    private String attention;

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("简易描述")
    private String desc;

    @ApiModelProperty("详细描述")
    private String minuteDesc;

    @ApiModelProperty("图片")
    private List<TourImg>tourImgs;

}
