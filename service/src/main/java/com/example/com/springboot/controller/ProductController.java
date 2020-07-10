package com.example.com.springboot.controller;

import com.example.com.springboot.pojo.PageModel;
import com.example.com.springboot.pojo.Product;
import com.example.com.springboot.pojo.Tour;
import com.example.com.springboot.service.ProductService;
import com.example.com.springboot.service.TourService;
import com.example.com.springboot.utiles.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Api("产品")
@Controller
@RequestMapping("store/skill/product")
public class ProductController extends BaseController<ProductService> {
    @Autowired
    public  TourService tourService;

    @ApiModelProperty("上传商品")
    @RequestMapping( value = "saveProduct",method = RequestMethod.POST)
    public String saveProduct(Product product ,MultipartFile filePath){
        if (filePath.isEmpty()) {
            return "上传失败，请选择文件";
        }
        String fileName = filePath.getOriginalFilename();
        String product_img=fileName;
        // 这需要填写一个本地路径
        String filePh ="D:\\IDEA_WORKBEACH\\springboot-partent\\service\\src\\main\\webapp\\img\\products\\";
        File dest = new File(filePh + fileName);
        try {
            filePath.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        product.setSmall_pice(product_img);
        product.setStock_count(product.getNum());
        product.setCreate_time(DateUtils.getNowTime());
        product.setStart_time(DateUtils.getMillTimeDelT(product.getStart_time()));
        product.setEnd_time(DateUtils.getMillTimeDelT(product.getEnd_time()));
        service.saveProduct(product);
        return "redirect:getList?pageNum=1";
    }
    @ApiModelProperty("获得前六个秒杀商品（后台）")
    @RequestMapping("getList")
    public String getProductList(Model model, Product product){
        PageModel<Product>pageModel=service.getListProduct(product);
        model.addAttribute("pageModel",pageModel);
        return  "admin/product/list";
    }
    @ApiModelProperty("获得秒杀商品列表")
    @RequestMapping("getProductList")
    public String getProductListForPage(Model model, Product product){
        product.setNowTime(DateUtils.getNowTime());
        product.setUrl("store/skill/product/getProductList");
        PageModel<Product>pageModel=service.getListProduct(product);
        model.addAttribute("pageModel",pageModel);
        return  "productList";
    }
    @ApiModelProperty("根据商品id获取商品")
    @RequestMapping("getProduct")
    public String getProductById(Model model, Product product){
        Product pd=service.getProductById(product);
        pd.setEnd_time(DateUtils.getMillTimeToT(pd.getEnd_time()));
        pd.setStart_time(DateUtils.getMillTimeToT(pd.getStart_time()));
        model.addAttribute("product",pd);
        return  "admin/product/edit";
    }

    @ApiModelProperty("根据商品id更新品")
    @RequestMapping(value = "editProduct",method = RequestMethod.POST)
    public String getProductById( Product product,MultipartFile filePath){
        if (filePath.isEmpty()) {
           product.setSmall_pice(product.getSmall_pice());
        }
        if(!filePath.isEmpty()){
            String fileName = filePath.getOriginalFilename();
            String product_img=fileName;
            // 这需要填写一个本地路径
            String filePh ="D:\\IDEA_WORKBEACH\\springboot-partent\\service\\src\\main\\webapp\\img\\products\\";
            File dest = new File(filePh + fileName);
            try {
                filePath.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            product.setSmall_pice(product_img);
        }
        product.setCreate_time(DateUtils.getNowTime());
        product.setStart_time(DateUtils.getMillTimeDelT(product.getStart_time()));
        product.setEnd_time(DateUtils.getMillTimeDelT(product.getEnd_time()));
        service.editProductById(product);
        return "redirect:getList?pageNum=1";
    }

    @ApiModelProperty("获得前六个秒杀商品（前台）")
    @RequestMapping("getListForIndex")
    public String getProductListToIndex(Model model, Product product){
        product.setNowTime(DateUtils.getNowTime());
        service.saveAllProductToRedis(product);
        PageModel<Product>pageModel=service.getListProduct(product);
        List<Tour>list=tourService.getTourList();
        model.addAttribute("pageModel",pageModel);
        model.addAttribute("tour",list);
        return  "index";
    }

    @ApiModelProperty("商品详情")
    @RequestMapping("getProductForInfo")
    public String getProductForInfo(Model model, Product product){
        Product pd = service.getProductById(product);
        String startSecond=DateUtils.getMillTime(pd.getStart_time());
        String endSecond=DateUtils.getMillTime(pd.getEnd_time());
        pd.setStartSecond(startSecond);
        pd.setEndSecond(endSecond);
        model.addAttribute("product",pd);
        return  "product_info";
    }
}
