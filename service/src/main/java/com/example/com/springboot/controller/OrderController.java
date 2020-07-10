package com.example.com.springboot.controller;

import com.example.com.springboot.pojo.*;
import com.example.com.springboot.service.AddressService;
import com.example.com.springboot.service.OrderService;
import com.example.com.springboot.service.ProductService;
import com.example.com.springboot.service.UserService;
import com.example.com.springboot.utiles.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api("订单")
@Controller
@RequestMapping("store/skill/order")
public class OrderController extends BaseController<OrderService> {
    @Autowired
    private ProductService productService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserService userService;

    @ApiModelProperty("点击秒杀跳转订单详情页")
    @RequestMapping("clickSkill")
    public String clickSkillBtn(Order order, Model model) {
        Product product = new Product();
        User user = new User();
        user.setUid(order.getUser_id());
        product.setId(order.getSeckill_id());
        Product productById = productService.getProductById(product);
        productById.setCreate_time(DateUtils.getNowTime());
        List<Address> addresses = addressService.addressList(user);
        model.addAttribute("product", productById);
        model.addAttribute("addressList", addresses);
        return "order_info";
    }

    @ApiModelProperty("根据地址获取号码")
    @RequestMapping("getPhone")
    @ResponseBody
    public String clickSkillBtn(Address address) {
        Address ad = addressService.getAddressById(address);
        return ad.getPhone();
    }

    @ApiModelProperty("保存订单")
    @RequestMapping("saveOrder")
    public String saveOrder(Order order) {
        Address address = new Address();
        address.setId(order.getReceiver_address());
        order.setReceiver_address(addressService.getAddressById(address).getAddress());
        order.setPay_time(DateUtils.getNowTime());
        order.setStatus("1");
        service.saveOrder(order);
        return "redirect:selectOrder?uid=" + order.getUser_id();
    }

    @ApiModelProperty("根据用户查询订单")
    @RequestMapping("selectOrder")
    public String selectOrderByUid(User user, Model model) {
        List<Order> orders = service.selectOrderByUid(user);
        User u = userService.checkUser(user);
        model.addAttribute("orderList", orders);
        model.addAttribute("user",u);
        return "orderList";
    }

    @ApiModelProperty("查询所有订单")
    @RequestMapping("getAllOrders")
    public String selectAllOrders( Model model,Order order) {
        PageModel<Order>orderList = service.selectAllOrder(order);
        model.addAttribute("pageModel",orderList);
        return "admin/order/list";
    }
    @ApiModelProperty("根据流水号查订单详情")
    @RequestMapping("getOrderDetail")
    @ResponseBody
    public Order getOrderDetail(Order order) {
        Order o = service.selectOrderByTransactionId(order);
        return  o;
    }
    @ApiModelProperty("查询用户是否秒杀了此物品")
    @RequestMapping("existOrder")
    @ResponseBody
    public String selectOrderByWhere(Order order) {
        Order or = service.selectOrderByWhere(order);
        if (or != null) {
            return "exist";
        }
        return "notExist";
    }
}
