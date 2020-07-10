package com.example.com.springboot.service;

import com.example.com.springboot.pojo.Order;
import com.example.com.springboot.pojo.PageModel;
import com.example.com.springboot.pojo.User;

import java.util.List;

public interface OrderService {

    void saveOrder(Order order);

    List<Order>selectOrderByUid(User user);

    Order selectOrderByWhere(Order order);

    PageModel<Order>selectAllOrder(Order order);

    Order selectOrderByTransactionId(Order order);
}
