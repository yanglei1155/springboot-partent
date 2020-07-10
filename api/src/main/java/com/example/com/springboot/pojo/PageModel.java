package com.example.com.springboot.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PageModel<T>{
    private Integer totalCount;
    private List<T>list;
    private Integer startIndex;
    private Integer pageSize=6;
    private Integer nowPage;
    private String  url;
    private Integer nextPage;
    private Integer lastPage;
    private Integer pageCount;
    private String  nowTime;
    public PageModel(Integer pageNum){
        this.nowPage=pageNum;
        this.nextPage=nowPage+1;
        this.lastPage=nowPage-1;
        this.startIndex=(nowPage-1)*pageSize;
    }
}
