package com.example.com.springboot.service;

import com.example.com.springboot.pojo.PageModel;
import com.example.com.springboot.pojo.Product;

import java.util.List;

public interface ProductService {

    void saveProduct(Product product);

    PageModel<Product> getListProduct(Product product);

    Integer getTotalCount(Product product);

    Product getProductById(Product product);

    void editProductById(Product product);

    void deCreament(Product product);

    void saveAllProductToRedis(Product product);

    void timeToUpdateStockCount();
}
