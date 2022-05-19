package com.xttc.controller;


import com.xttc.dao.UserMapper;
import com.xttc.pojo.person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class InformationController {
    @Autowired(required = false)
    private UserMapper userMapper;
    @RequestMapping("/information")
    public String updatainformation(person user1, Model model, HttpSession session){
        person user = (person) session.getAttribute("loginUser");

        try{
            user1=userMapper.findUserByEmail(user.getEmail());
        }catch(NullPointerException e){
            System.out.println("没找到");
        }
        model.addAttribute("user",user1);
        return "information";
    }
    @RequestMapping("/safe")
    public String setSafe(){
        return "safe";
    }
    @RequestMapping("/help")
    public String help(){
        return "help";
    }
    @RequestMapping("/complete")
    public String completeInformation(person user,Model model,HttpSession session){
        person user1 = (person) session.getAttribute("loginUser");//从session中获取user
        person user3=null;
        model.addAttribute("user",user1);//传给html页面
        String username=user.getPname();
        String sid = user.getSid();
        String sclass = user.getSclass();
        if(username!=null && sid!=null && sclass !=null && username!="" && sid!="" && sclass !=""){
            person user2 =null;
           try{
               user2 = userMapper.findUserBySid(sid);
               user2.getSid();
           }
            catch (NullPointerException e){
               model.addAttribute("正确","数据库没有这个学号 ");
            }
           if (user2 != null){
               if (user2.getSid().equals(user1.getSid())){
                   try{
                       user1.setPname(username);
                       user1.setSid(sid);
                       user1.setSclass(sclass);
                       userMapper.updateUser(user1);
                       model.addAttribute("msg1","修改成功");
                   }catch(Exception e){
                       model.addAttribute("msg1","修改失败1");
               }
               }else {
                   model.addAttribute("msg", "学号已存在，请检查输入的学号是否正确");
               }
           }else{
               try{
                   user3.setPname(username);
                   user3.setSid(sid);
                   user3.setSclass(sclass);
                   userMapper.updateUser(user3);
                   model.addAttribute("msg1","修改成功");
               }catch(Exception e){
                   model.addAttribute("msg1","修改失败,该学号下有订单,如想修改，请联系管理员");
               }
           }
        }
        return "information";
    }
}
