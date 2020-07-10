package com.example.com.springboot.service.impl;

import com.example.com.springboot.mapper.CollectMapper;
import com.example.com.springboot.pojo.Collect;
import com.example.com.springboot.pojo.Product;
import com.example.com.springboot.service.CollectService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CollectServiceImpl extends BaseServcieImpl<CollectMapper>implements CollectService {
    @Override
    public List<Product> getCollectList(Collect collect) {
        return  mapper.getCollectList(collect);
    }

    @Override
    public Collect selectCollectByWhere(Collect collect) {
        return mapper.selectCollectByWhere(collect);
    }

    @Override
    public void collectProduct(Collect collect) {
       mapper.collectProduct(collect);
    }
}
