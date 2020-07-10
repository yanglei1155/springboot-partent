package com.example.com.springboot.mapper;

import com.example.com.springboot.pojo.Collect;
import com.example.com.springboot.pojo.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CollectMapper {

    List<Product> getCollectList(Collect collect);

    Collect selectCollectByWhere(Collect collect);

    void collectProduct(Collect collect);
}
