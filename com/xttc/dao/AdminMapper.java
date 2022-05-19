package com.xttc.dao;


import com.xttc.pojo.person;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    public person findAdminById(String adminname);
    public person findAdminByEmail(String email);
    public int insertAdmin(person admin);
}
