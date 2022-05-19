package com.xttc.dao;

import com.xttc.pojo.subscribe;
import com.xttc.pojo.unsubscribe;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubscribeMapper {
    public List<subscribe> selectSubscribe(String sid);
    public List<subscribe> selectSubscribeAll();
    public subscribe findSubscribeById(Integer id);
    public int insertSubscribe(subscribe subscribe);
    public int insertSubscribe1(unsubscribe unsubscribe);
    //public int updateSubscribeBySidAndISBN(subscribe subscribe);
    public int deleteSubscribe(Integer id);
}
