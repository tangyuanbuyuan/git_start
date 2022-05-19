package com.xttc.dao;

import com.xttc.pojo.book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {
    public List<book> selectBookAll();
    public int insertBook(book book);
    //根据ISBN查找书以及订单，删除要用
    public book findBookandSubByISBN(String ISBN);
    public book findBookByISBN(String ISBN);
    //根据ISBN查找书
    public int deleteBook(String ISBN);
    //修改
    public int updateBook(book book);
    //模糊查找
    public List<book> selectBook(String str);
}
