package com.xttc.controller;

import com.xttc.dao.AdminMapper;


import com.xttc.dao.UserMapper;
import com.xttc.pojo.person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;

@Controller
public class LoginController {
    private String codeNum = "";
    @Autowired(required = false)
    private AdminMapper adminMapper;
    @Autowired(required = false)
    private UserMapper userMapper;

    @RequestMapping("/toLoginpage")
    public String toLoginPage(person person, Model model, HttpSession session) {

        String email = person.getEmail();
        String reg = "[\\w]+@[\\w]+.[\\w]+";
        String password = person.getPassword();
        if (email != null && password != null&&email!=""&&password!="") {
            if (email.matches(reg)) {
                boolean flog=false;
                //去管理员库中查询邮箱是否存在
                try {
                    person admin1 = adminMapper.findAdminByEmail(email);
                    admin1.getPassword();
                    flog=true;
                    if (password.equals(admin1.getPassword())) {
                        session.setAttribute("loginAdmin", admin1);
                        return "redirect:main";
                    } else {
                        model.addAttribute("msg2", "密码错误，请重新登录");
                        return "login";
                    }
                } catch (NullPointerException e) {
                    System.out.println("管理员库内没找到");
                }
                //去用户库里查询邮箱是否存在
                try {
                    person user1 = userMapper.findUserByEmail(email);
                    user1.getPassword();
                    flog=true;
                    if (password.equals(user1.getPassword())) {
                        session.setAttribute("loginUser",user1);
                        return "redirect:main1";
                    } else {
                        model.addAttribute("msg2", "密码错误，请重新登录");
                        return "login";
                    }
                } catch (NullPointerException e) {
                    System.out.println("用户库内没找到");
                }
                //如果都没找到，则提示邮箱不存在
                if (flog==false){
                    model.addAttribute("msg1","邮箱地址不存在");
                }
            }
            else {
                model.addAttribute("msg1", "邮箱格式不正确");
            }
        }
        else{
            model.addAttribute("msg1","请输入邮箱账号和密码");
        }
        return "login";
    }

    @Autowired
    JavaMailSenderImpl mailSender;

    @RequestMapping("/register")
    public String register(person user, Model model, HttpSession session) throws MessagingException {
        String username = user.getPname();
        String useremail = user.getEmail();
        String reg = "[\\w]+@[\\w]+.[\\w]+";
        String userpassword = user.getPassword();
        model.addAttribute("anniu", "发送邮箱");
        if (username != null && useremail != null && userpassword != null) {
            if (useremail.matches(reg)) {
                boolean flog = true;
                try {
                    person user1 = adminMapper.findAdminByEmail(useremail);
                    user1.getPassword();//如果查找失败，数据库里不存在，flog=true
                    flog = false;
                } catch (NullPointerException e) {
                    System.out.println("管理员库里不存在");
                }
                try{
                    person user2=userMapper.findUserByEmail(useremail);
                    user2.getPassword();
                    flog=false;
                }catch (NullPointerException e){
                    System.out.println("用户库里不存在");
                }
                if (flog == true) {
                    int count = 1;//默认发送一次
                    MimeMessage mimeMessage = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                    while (count-- != 0) {
                        String codeNum = "";
                        int[] code = new int[3];
                        Random random = new Random();
                        //自动生成验证码
                        for (int i = 0; i < 6; i++) {
                            int num = random.nextInt(10) + 48;
                            int uppercase = random.nextInt(26) + 65;
                            int lowercase = random.nextInt(26) + 97;
                            code[0] = num;
                            code[1] = uppercase;
                            code[2] = lowercase;
                            codeNum += (char) code[random.nextInt(3)];
                        }
                        System.out.println(codeNum);
                        //标题
                        helper.setSubject("您的验证码为：" + codeNum);
                        //内容
                        helper.setText("您好！，欢迎注册本校教材系统。您的验证码为：" + "<h2>" + codeNum + "</h2>" + "千万不能告诉别人哦！", true);
                        //邮件接收者
                        helper.setTo(useremail);
                        //邮件发送者，必须和配置文件里的一样，不然授权码匹配不上
                        helper.setFrom("473238524@qq.com");
                        mailSender.send(mimeMessage);
                        System.out.println("邮件发送成功！" + (count + 1));
                        session.setAttribute("registerUser", user);
                        session.setAttribute("codeNum", codeNum);
                        System.out.println(user);
                        return "redirect:register1";
                    }
                } else {
                    /*System.out.println(admin1.getPassword());*/
                    model.addAttribute("msg1", "邮箱已注册");
                }
            }
            else {
                model.addAttribute("msg1", "邮箱格式不正确");
            }
        }
        return "register";
    }

    @RequestMapping("register1")
    public String register1(person user1, person user, Model model, HttpServletRequest request) throws InterruptedException {
        user = (person) request.getSession().getAttribute("registerUser");
        codeNum = (String) request.getSession().getAttribute("codeNum");
        String userpassword = user.getPassword();
        String username = user.getPname();
        String useremail = user.getEmail();
        String codenum = user1.getCodeNum();
        user.setCodeNum(codenum);
        System.out.println(codenum + "," + username + "," + userpassword + "," + useremail);
        if (codenum != null) {
            if (codenum.equals(codeNum)) {
                userMapper.insertUser(user);
                model.addAttribute("msg","注册成功");
            } else {
                model.addAttribute("msg", "验证码错误");
            }
        }
        return "register1";
    }

   /* @RequestMapping("/main")
    public String main() {
        return "main";
    }
*/
    @RequestMapping("/logout")
    public String logout(HttpSession session) {//用户退出
        session.removeAttribute("loginAdmin"); //清除Session
        return "redirect:toLoginPage"; //退出登录后重定向到登录页面
    }
    @RequestMapping("/logout1")
    public String logout1(HttpSession session) {//用户退出
        session.getAttribute("loginUser"); //清除Session
        return "redirect:toLoginPage"; //退出登录后重定向到登录页面
    }
}
