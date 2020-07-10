package com.example.com.springboot.controller;

import com.example.com.springboot.pojo.Collect;
import com.example.com.springboot.pojo.Product;
import com.example.com.springboot.service.CollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;

import java.util.List;

@Api("收藏商品")
@Controller
@RequestMapping("store/skill/collect")
public class CollectController extends BaseController<CollectService>  {
    @ApiModelProperty("收藏")
    @RequestMapping("collectProduct")
    @ResponseBody
    public String collectProduct(Collect collect){
        if(service.selectCollectByWhere(collect)!=null){
            return  "您已收藏了此商品";
        }
        service.collectProduct(collect);
        return "收藏成功";
    }

    @ApiModelProperty("收藏列表")
    @RequestMapping("collectList")
    public String collectClist(Collect collect, Model model){
        List<Product> collectList = service.getCollectList(collect);
        model.addAttribute("collectList",collectList);
        return "collect";
    }
}
