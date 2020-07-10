package com.example.com.springboot.service.impl;

import com.example.com.springboot.mapper.OrderMapper;
import com.example.com.springboot.pojo.Order;
import com.example.com.springboot.pojo.PageModel;
import com.example.com.springboot.pojo.Product;
import com.example.com.springboot.pojo.User;
import com.example.com.springboot.service.OrderService;
import com.example.com.springboot.service.ProductService;
import com.example.com.springboot.service.UserService;
import com.example.com.springboot.utiles.UUIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class OrderServiceImpl extends BaseServcieImpl<OrderMapper> implements OrderService {
    @Autowired
    private ProductService productService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void saveOrder(Order order) {
        Product product = new Product();
        product.setId(order.getSeckill_id());
        order.setTransaction_id(UUIdUtils.getUUid());
        while (true) {
            try {
                System.out.println("入参："+order.getSeckill_id()+"是否存在此key:"+redisTemplate.hasKey(order.getSeckill_id()));
                redisTemplate.watch(order.getSeckill_id());
                Integer stcokCount = (Integer) redisTemplate.opsForValue().get(order.getSeckill_id());
                if (stcokCount > 0) {//库存大于0就进来先扣库存在记录购买者
                    //开启事务支持
                    redisTemplate.setEnableTransactionSupport(true);
                    redisTemplate.multi();
                    redisTemplate.opsForValue().increment(order.getSeckill_id(), -1);
                    List exec = redisTemplate.exec();
                    if (exec != null && !exec.isEmpty()) {//减库，同步保存订单

                        saveProduct("allProducts", order.getSeckill_id(), (Integer) redisTemplate.opsForValue().get(order.getSeckill_id()));
                        mapper.saveOrder(order);
                        break;
                    } else {
                        System.out.println("悲剧了" + "顾客:" + order.getUser_id() + "与商品插肩而过");
                        break;
                    }
                } else {
                    System.out.println("商品库存为0 顾客:" + order.getUser_id() + "没抢到商品");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                redisTemplate.unwatch();
            }
        }
    }

    @Override
    public List<Order> selectOrderByUid(User user) {
        return mapper.selectOrderByUid(user);
    }

    @Override
    public Order selectOrderByWhere(Order order) {
        return mapper.selectOrderByWhere(order);
    }

    @Override
    public PageModel<Order> selectAllOrder(Order order) {
        PageModel<Order>pageModel=new PageModel<Order>(order.getPageNum());
        Integer count = mapper.selectAllCount();
        pageModel.setTotalCount(count);
        Integer pageCount=pageModel.getTotalCount()%pageModel.getPageSize()==0?pageModel.getTotalCount()/pageModel.getPageSize():pageModel.getTotalCount()/pageModel.getPageSize()+1;
        pageModel.setPageCount(pageCount);
        pageModel.setList(mapper.selectAllOrder(pageModel));
        pageModel.setUrl("store/skill/order/getAllOrders");
        return  pageModel;
    }

    @Override
    public Order selectOrderByTransactionId(Order order) {
        return mapper.selectOrderByTransactionId(order);
    }

    public void saveProduct(String key, String pid, Integer count) {
        Map<String, Integer> map = (Map<String, Integer>) redisTemplate.opsForValue().get(key);
        map.put(pid, count);
        redisTemplate.opsForValue().set(key, map);
    }
}
