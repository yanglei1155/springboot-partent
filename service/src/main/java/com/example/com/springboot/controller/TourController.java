package com.example.com.springboot.controller;

import com.example.com.springboot.pojo.Tour;
import com.example.com.springboot.pojo.TourImg;
import com.example.com.springboot.service.TourService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api("旅游故事")
@RequestMapping("store/skill/tour")
@Controller
public class TourController extends BaseController<TourService> {

    @ApiModelProperty("根据id查找tour")
    @RequestMapping("getTourById")
    public String getTourById(Model model,Tour tour){
        Tour tr=service.getTourById(tour);
        List<TourImg> list=service.getTourImgByTid(tour);
        tr.setTourImgs(list);
        model.addAttribute("tour",tr);
        return "tour";
    }

    @ApiModelProperty("点赞")
    @RequestMapping("plusZan")
    @ResponseBody
    public String plusZan(Tour tour){
        service.zanCount(tour);
        Tour tr=service.getTourById(tour);
        return tr.getZan().toString();
    }


    @ApiModelProperty("改变关注")
    @RequestMapping(value = "changeAttention")
    @ResponseBody
    public String changeAttention(Tour tour){
        String attention="关注";
        String alreadyAttention="已关注";
        if(tour.getAttention().equals(attention)){
            tour.setAttention(alreadyAttention);
            service.changeAttention(tour);
            return alreadyAttention;
        }
        else{
            tour.setAttention(attention);
            service.changeAttention(tour);
            return attention;
        }
    }
}
