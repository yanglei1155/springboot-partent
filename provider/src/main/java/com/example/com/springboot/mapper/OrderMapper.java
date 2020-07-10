package com.example.com.springboot.mapper;

import com.example.com.springboot.pojo.Order;
import com.example.com.springboot.pojo.PageModel;
import com.example.com.springboot.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    void saveOrder(Order order);

    List<Order> selectOrderByUid(User user);

    Order selectOrderByWhere(Order order);

    List<Order>selectAllOrder(PageModel<Order>pageModel);

    Order selectOrderByTransactionId(Order order);

    Integer selectAllCount();
}
