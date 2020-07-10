package com.example.com.springboot.service.impl;

import com.example.com.springboot.mapper.TourMapper;
import com.example.com.springboot.pojo.Tour;
import com.example.com.springboot.pojo.TourImg;
import com.example.com.springboot.service.TourService;
import com.example.com.springboot.utiles.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TourServiceImpl extends BaseServcieImpl<TourMapper> implements TourService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Tour> getTourList() {
        Map<String, Tour> map = new HashMap<>();
        if (redisTemplate.hasKey("tourList")) {
            Map<String, Tour> tours = (Map<String, Tour>) redisTemplate.opsForValue().get("tourList");
            List<Tour> list = CollectionUtils.mapToList(tours);
            return list;
        }
        List<Tour> list = mapper.getTourList();
        Map<String, Tour> tourMap = CollectionUtils.listToMap(list);
        redisTemplate.opsForValue().set("tourList", tourMap);
        return mapper.getTourList();
    }

    @Override
    public Tour getTourById(Tour tour) {
        Map<String, Tour> tourMap = (Map<String, Tour>) redisTemplate.opsForValue().get("tourList");
        if (tourMap.containsKey(tour.getId())) {
            return tourMap.get(tour.getId());
        }
        return null;
    }

    @Override
    public List<TourImg> getTourImgByTid(Tour tour) {
        return mapper.getTourImgByTid(tour);
    }

    @Override
    public void zanCount(Tour tour) {
        Map<String, Tour> tourMap = (Map<String, Tour>) redisTemplate.opsForValue().get("tourList");
        if (tourMap.containsKey(tour.getId())) {
            Tour to = tourMap.get(tour.getId());
            to.setZan(String.valueOf(Integer.parseInt(tourMap.get(tour.getId()).getZan()) + 1));
            tourMap.put(tour.getId(), to);
        }
        redisTemplate.opsForValue().set("tourList", tourMap);

    }

    @Override
    public void changeAttention(Tour tour) {
        Map<String, Tour> tourMap = (Map<String, Tour>) redisTemplate.opsForValue().get("tourList");
        if (tourMap.containsKey(tour.getId())) {
            Tour to = tourMap.get(tour.getId());
            to.setAttention(tour.getAttention());
            tourMap.put(tour.getId(), to);
            redisTemplate.opsForValue().set("tourList",tourMap);
        }
    }

    //定时更新
    @Override
    public void timeToUpdate(List<Tour> tours) {
        for(Tour tour:tours){
            mapper.zanCount(tour);
        }
    }
}
