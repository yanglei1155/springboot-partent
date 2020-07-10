package com.example.com.springboot.service.impl;

import com.example.com.springboot.mapper.ProductMapper;
import com.example.com.springboot.pojo.PageModel;
import com.example.com.springboot.pojo.Product;
import com.example.com.springboot.service.ProductService;
import com.example.com.springboot.utiles.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductServiceImpl extends BaseServcieImpl<ProductMapper> implements ProductService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void saveProduct(Product product) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        product.setId(id);
        product.setStatus("1");
        mapper.saveProduct(product);
        if (redisTemplate.hasKey("allProducts")) {
            Map<String, Integer> productMap = (Map<String, Integer>) redisTemplate.opsForValue().get("allProducts");
            productMap.put(id, product.getStock_count());
            redisTemplate.opsForValue().set(id,product.getStock_count());
            redisTemplate.opsForValue().set("allProducts",productMap);
        }
    }

    @Override
    public PageModel<Product> getListProduct(Product product) {
        PageModel<Product> pageModel = new PageModel<>(product.getPageNum());
        pageModel.setTotalCount(getTotalCount(product));
        Integer totalPage = pageModel.getTotalCount() % pageModel.getPageSize() == 0 ? pageModel.getTotalCount() / pageModel.getPageSize() : pageModel.getTotalCount() / pageModel.getPageSize() + 1;
        pageModel.setPageCount(totalPage);
        pageModel.setNowTime(product.getNowTime());
        List<Product> list = mapper.getListProduct(pageModel);
        setStockCountFromRedis(list);
        pageModel.setList(list);
        String url;
        if (product.getUrl() == "" || product.getUrl() == null) {
            url = "store/skill/product/getList";
        } else {
            url = product.getUrl();
        }
        pageModel.setUrl(url);
        return pageModel;
    }

    @Override
    public Integer getTotalCount(Product product) {
        return mapper.getTotalCount(product);
    }

    @Override
    public Product getProductById(Product product) {
        Integer count =(Integer) redisTemplate.opsForValue().get(product.getId());
        Product pd = mapper.getProductById(product);
        pd.setStock_count(count);
        return pd;
    }

    @Override
    public void editProductById(Product product) {
        mapper.editProductById(product);
    }

    @Override
    public void deCreament(Product product) {
        mapper.deCreament(product);
    }

    @Override
    public void saveAllProductToRedis(Product product) {
        Map<String, Integer> productMap = new HashMap<>();
        if (!redisTemplate.hasKey("allProducts")) {
            List<Product> allProduct = mapper.getAllProduct(product);
            for (Product p : allProduct) {
                productMap.put(p.getId(), p.getStock_count());
                redisTemplate.opsForValue().set(p.getId(),p.getStock_count());
            }
            redisTemplate.opsForValue().set("allProducts", productMap);
        }
    }

    @Override
    public void timeToUpdateStockCount() {
        Map<String,Integer>orderMap =(Map<String,Integer>)redisTemplate.opsForValue().get("allProducts");
        Product product=new Product();
        for(Map.Entry<String,Integer>map:orderMap.entrySet()){
            product.setStock_count(map.getValue());
            product.setId(map.getKey());
            mapper.deCreament(product);
        }
    }
    void setStockCountFromRedis(List<Product>list){
        for(Product p:list){
            if(redisTemplate.hasKey(p.getId())){
                p.setStock_count(Integer.parseInt(redisTemplate.opsForValue().get(p.getId()).toString()));
            }
        }
    }
}
