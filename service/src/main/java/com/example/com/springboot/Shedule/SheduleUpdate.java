package com.example.com.springboot.Shedule;

import com.example.com.springboot.pojo.Tour;
import com.example.com.springboot.service.ProductService;
import com.example.com.springboot.service.TourService;
import com.example.com.springboot.utiles.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Configuration
@EnableScheduling
public class SheduleUpdate {
    @Autowired
    private TourService tourService;
    @Autowired
    private ProductService productService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Scheduled(cron = "0 00 06 * * *")
    public void updateTour(){
        Map<String, Tour> tourMap = (Map<String, Tour>) redisTemplate.opsForValue().get("tourList");
        tourService.timeToUpdate(CollectionUtils.mapToList(tourMap));
    }
    @Scheduled(cron = "0 24 16 * * *")
    public void updateProduct(){
         productService.timeToUpdateStockCount();
    }
}
