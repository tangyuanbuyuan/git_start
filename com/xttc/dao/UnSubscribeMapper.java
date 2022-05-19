package com.xttc.dao;

import com.xttc.pojo.subscribe;
import com.xttc.pojo.unsubscribe;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UnSubscribeMapper {
    public int insertUnSubscribe(subscribe unsub);
    public List<unsubscribe> selectUnSubscribeAll();
    public List<unsubscribe> selectUnSubscribe(String sid);
    public unsubscribe findUnSubscribeById(Integer id);
    public int deleteUnSubscribe(Integer id);
}
