package com.example.com.springboot.service;

import com.example.com.springboot.pojo.Tour;
import com.example.com.springboot.pojo.TourImg;

import java.util.List;

public interface TourService {

    void timeToUpdate(List<Tour> tours);

    List<Tour> getTourList();

    Tour getTourById(Tour tour);

    List<TourImg>getTourImgByTid(Tour tour);

    void zanCount(Tour tour);

    void changeAttention(Tour tour);
}
