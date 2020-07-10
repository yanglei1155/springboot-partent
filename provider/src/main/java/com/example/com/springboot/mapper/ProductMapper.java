package com.example.com.springboot.mapper;

import com.example.com.springboot.pojo.PageModel;
import com.example.com.springboot.pojo.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
     void saveProduct(Product product);

     List<Product> getListProduct(PageModel<Product> pageModel);

     Integer getTotalCount(Product product);

     Product getProductById(Product product);

     void editProductById(Product product);

     void deCreament(Product product);

     List<Product>getAllProduct(Product product);
}
