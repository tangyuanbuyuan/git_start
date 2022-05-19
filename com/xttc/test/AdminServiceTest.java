package com.xttc.test;


import com.xttc.dao.AdminMapper;


import com.xttc.dao.BookMapper;
import com.xttc.dao.UserMapper;
import com.xttc.pojo.book;
import com.xttc.pojo.person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminServiceTest {
    @Autowired
    private AdminMapper adminMapper;
    @Test
    public void findBookById() {
        person admin =adminMapper.findAdminById("侯爽");
        System. out. println("管理员password:" + admin.getPassword());
    }
    @Test
    public void findAdminByEmail(){
        try {
            person admin=adminMapper.findAdminByEmail("473238524@qq.com");
            System.out.println(admin.getPname());
        }catch (NullPointerException e) {
            System.out.println(e+"不存在");
        }
    }
    @Autowired
    private UserMapper userMapper;
    @Test
    public void find(){
        try {
            person user=userMapper.findUserByEmail("653394808@qq.com");
            System.out.println(user.getPname());
        }catch (NullPointerException e) {
            System.out.println(e+"不存在");
        }
    }
    @Autowired
    private BookMapper bookMapper;
    @Test
    public void findBookByISBN(){
        try{
            book book = bookMapper.findBookByISBN("978-7-302-44607-1");
            System.out.println(book);
        }catch(NullPointerException e){
            System.out.println("不存在");
        }
    }
}
