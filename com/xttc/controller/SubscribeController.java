package com.xttc.controller;

import com.xttc.dao.BookMapper;
import com.xttc.dao.SubscribeMapper;
import com.xttc.dao.UnSubscribeMapper;
import com.xttc.dao.UserMapper;
import com.xttc.pojo.book;
import com.xttc.pojo.person;
import com.xttc.pojo.subscribe;
import com.xttc.pojo.unsubscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SubscribeController {
    @Autowired(required = false)
    private SubscribeMapper subscribeMapper;
    @Autowired(required = false)
    private BookMapper bookMapper;
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private UnSubscribeMapper unSubscribeMapper;
    @RequestMapping("/mngSub")
    public String mngSubscribe(Model model,book book,person user){
        try{
            List<unsubscribe> unsubscribes =unSubscribeMapper.selectUnSubscribeAll();
            model.addAttribute("num",unsubscribes.size());
        }catch (NullPointerException e){
            System.out.println("无");
        }
        List<subscribe> subscribes=null;
        try{
            subscribes= subscribeMapper.selectSubscribeAll();
            for (subscribe subscribe:subscribes){
                book = bookMapper.findBookByISBN(subscribe.getBookISBN());
                model.addAttribute("book",book);
            }
            for (subscribe subscribe:subscribes){
                user =userMapper.findUserBySid(subscribe.getUsersid());
                model.addAttribute("user",user);
            }
            model.addAttribute("subscribes",subscribes);
        }catch (NullPointerException e){
            model.addAttribute("msg","暂无订单");
        }
        return "mngsub";
    }
    @RequestMapping("quexiao")
    public String quxiaodingdan(Model model, HttpServletRequest request){
        Integer dingdanId =Integer.parseInt( request.getParameter("id"));
        try{
            subscribe subscribe = subscribeMapper.findSubscribeById(dingdanId);
            unSubscribeMapper.insertUnSubscribe(subscribe);
            subscribeMapper.deleteSubscribe(dingdanId);
            return "redirect:selectsub";
        }catch (Exception e){
            System.out.println(e);
        }
        return "usersub";
    }
    @Autowired
    JavaMailSenderImpl mailSender;
    @RequestMapping("/deleteSub")
    public String DeleteSub(HttpServletRequest request){
        String reason=request.getParameter("reason");
        String username=request.getParameter("username");
        String useremail=request.getParameter("useremail");
        Integer SubID =Integer.parseInt(request.getParameter("SubID"));
        try{
            subscribeMapper.deleteSubscribe(SubID);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            //内容
            helper.setText("您好！" + username + "。您的退订单号为:" + "<h2>" + SubID + "</h2>" + "已经管理员删除！，原因是："+"<h2>" +reason + "</h2>" +"如有疑问请联系管理员", true);
            //邮件接收者
            helper.setTo(useremail);
            //邮件发送者，必须和配置文件里的一样，不然授权码匹配不上
            helper.setFrom("473238524@qq.com");
            mailSender.send(mimeMessage);
            System.out.println("邮件发送成功！");
            return "redirect:mngsub";
        }
        catch (Exception e){
            System.out.println("删除失败"+e);
        }
        return "mngsub";
    }
}
