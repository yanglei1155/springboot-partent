package com.example.com.springboot.mapper;

import com.example.com.springboot.pojo.Tour;
import com.example.com.springboot.pojo.TourImg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface TourMapper {

    List<Tour> getTourList();

    Tour getTourById(Tour tour);

    List<TourImg>getTourImgByTid(Tour tour);

    void zanCount(Tour tour);

    void changeAttention(Tour tour);
}
