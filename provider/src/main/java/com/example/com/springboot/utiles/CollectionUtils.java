package com.example.com.springboot.utiles;

import com.example.com.springboot.pojo.Tour;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtils {
    public static Map<String,Tour>  listToMap(List<Tour> list){
        Map<String, Tour>map=new HashMap<>();
        for(Tour o:list){
            map.put(o.getId(),o);
        }
        return  map;
    }

    public static List<Tour> mapToList(Map<String,Tour> map){
        List<Tour>list=new ArrayList<>();
        for(Tour tour:map.values()){
            list.add(tour);
        }
        return  list;
    }

}
