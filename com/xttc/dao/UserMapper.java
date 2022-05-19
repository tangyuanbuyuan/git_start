package com.xttc.dao;

import com.xttc.pojo.person;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    public person findUserByEmail(String email);
    public person findUserBySid(String sid);
    public int insertUser(person admin);
    public int updateUser(person user);
    public List<person> selectAllUser();
    public List<person> selectUser(String str);
    public int deleteUser(String email);
}
