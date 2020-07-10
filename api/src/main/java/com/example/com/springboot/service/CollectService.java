package com.example.com.springboot.service;

import com.example.com.springboot.pojo.Collect;
import com.example.com.springboot.pojo.Product;

import java.util.List;

public interface CollectService {
    List<Product> getCollectList(Collect collect);

    Collect selectCollectByWhere(Collect collect);

    void collectProduct(Collect collect);
}
