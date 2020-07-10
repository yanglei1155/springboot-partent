package com.example.com.springboot.controller;

import com.example.com.springboot.pojo.Address;
import com.example.com.springboot.pojo.User;
import com.example.com.springboot.service.AddressService;
import com.example.com.springboot.service.UserService;
import com.example.com.springboot.utiles.UploadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
@Api("用户")
@RequestMapping("/store/skill")
@Controller
public class UserController extends BaseController<UserService> {
    @Autowired
    private AddressService addressService;
    @ApiModelProperty("用户注册")
    @RequestMapping(value = "register",method = RequestMethod.POST)
    public String  userRegister(User user, MultipartFile upload){
        if (upload.isEmpty()) {
            return "上传失败，请选择文件";
        }
        String fileName = upload.getOriginalFilename();
        String head_img="img\\head\\"+fileName;
        // 这需要填写一个本地路径
        String filePath ="D:\\IDEA_WORKBEACH\\springboot-partent\\service\\src\\main\\webapp\\img\\head\\";
        File dest = new File(filePath + fileName);
        try {
            upload.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
         user.setHead_img(fileName);
         User U = service.saveUser(user);
         user.setUid(U.getUid());
         addressService.saveAddress(user);
        return "redirect:/jsp/login.jsp";
    }
    @ApiModelProperty("是否存在此用户名")
    @RequestMapping("checkUser")
    @ResponseBody
    public String checkUser(User user){
        User u = service.checkUser(user);
        if(u!=null){
            return "exist";
        }
        return  "notExist";
    }
    @ApiModelProperty("用户登录")
    @RequestMapping(value = "login",method = RequestMethod.POST)
    public String userLogin(User user, HttpServletRequest request, Model model){
        User U = service.checkUser(user);
        if(U==null){
            model.addAttribute("msg","账号或密码错误，请从新登录！");
            return "msg";
        }
        HttpSession session = request.getSession();
        session.setAttribute("user",U);
        return "redirect:product/getListForIndex?pageNum=1";
    }
    @ApiModelProperty("注销登录")
    @RequestMapping(value = "destorySession")
    public String userOff(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return "redirect:/jsp/login.jsp";
    }

    @ApiModelProperty("登陆后再次注册")
    @RequestMapping(value = "removeSession")
    public String register(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return "register";
    }

    @ApiModelProperty("输入用户名获得头像")
    @RequestMapping(value = "getHead")
    @ResponseBody
    public String getHeadImg(User user){
        User U= service.getUserHead(user);
        return  U.getHead_img();
    }
    @ApiModelProperty("后台登录")
    @RequestMapping(value = "loginBack",method = RequestMethod.POST)
    public String userLoginBack(User user, HttpServletRequest request, Model model){
        User U = service.checkUser(user);
        if(U==null){
            model.addAttribute("msg","账号或密码错误，请从新登录！");
            return "admin/index";
        }
        if("1".equals(U.getPower())){//普通用户且没权限进入后台
            model.addAttribute("msg","您没权限进入后台管理");
            return "admin/index";
        }
        HttpSession session = request.getSession();
        session.setAttribute("admin",U);
        return "admin/user/menu";
    }

    @ApiModelProperty("新添地址")
    @RequestMapping("saveAddress")
    public String saveAddress(User user){
        addressService.saveAddress(user);
        return "redirect:product/getListForIndex?pageNum=1";
    }
}
