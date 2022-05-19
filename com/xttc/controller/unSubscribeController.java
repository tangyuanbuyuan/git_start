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
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class unSubscribeController {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private UnSubscribeMapper unSubscribeMapper;
    @Autowired(required = false)
    private SubscribeMapper subscribeMapper;
    @Autowired(required = false)
    private BookMapper bookMapper;
    @RequestMapping("/userunsub")
    public String UnSubscribe(Model model, HttpSession session){
        person user = (person) session.getAttribute("loginUser");
        String email =user.getEmail();
        String sid=null;
        try{
            person user1 = userMapper.findUserByEmail(email);
            sid= user1.getSid();
        }catch (NullPointerException e){
            model.addAttribute("错误","错误");
        }
        if (sid!=null){
            try{
                List<unsubscribe> unsubscribes= unSubscribeMapper.selectUnSubscribe(sid);
                model.addAttribute("unsubscribes",unsubscribes);
            }
            catch(NullPointerException e){
                //model.addAttribute("msg","您没有订单");
            }
        }
        else{
            model.addAttribute("msg","请先完善个人信息");
        }
        return "userUnSub";
    }
    @RequestMapping("/quxiaoUnSub")
    public String quxiaoUnSub(Model model, HttpServletRequest request){
        Integer daidingdanId =Integer.parseInt( request.getParameter("id"));
        try{
            unsubscribe unsubscribe = unSubscribeMapper.findUnSubscribeById(daidingdanId);
            subscribeMapper.insertSubscribe1(unsubscribe);
            unSubscribeMapper.deleteUnSubscribe(daidingdanId);
            return "redirect:userunsub";
        }catch (Exception e){
            System.out.println(e);
        }
        return "userUnSub";
    }
    @RequestMapping("/mngUnSub")
    public String AllUnSubscribe(Model model, book book, person user){
        List<unsubscribe> unsubscribes=null;
        try{
            unsubscribes=unSubscribeMapper.selectUnSubscribeAll();
            for (unsubscribe unsubscribe:unsubscribes){
                book = bookMapper.findBookByISBN(unsubscribe.getBookISBN());
                model.addAttribute("book",book);
            }
            for (unsubscribe unsubscribe:unsubscribes){
                user =userMapper.findUserBySid(unsubscribe.getUsersid());
                model.addAttribute("user",user);
            }
            model.addAttribute("num",unsubscribes.size());
            model.addAttribute("unsubscribes",unsubscribes);
        }catch (NullPointerException e){
            model.addAttribute("msg","仓库没有书籍");
        }
        return "mngUnSub";
    }
    @Autowired
    JavaMailSenderImpl mailSender;
    @RequestMapping("/deleteUnSub")
    private String DeleteUnSub(HttpServletRequest request,Model model){
        Integer unsubID = Integer.parseInt(request.getParameter("unsubID"));
        String username = request.getParameter("username");
        String useremail = request.getParameter("useremail");
        try{
            unSubscribeMapper.deleteUnSubscribe(unsubID);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            //内容
            helper.setText("您好！，" + username + "。您的退订单号为:" + "<h2>" + unsubID + "</h2>" + "已经管理员删除！，如有疑问请联系管理员", true);
            //邮件接收者
            helper.setTo(useremail);
            //邮件发送者，必须和配置文件里的一样，不然授权码匹配不上
            helper.setFrom("473238524@qq.com");
            mailSender.send(mimeMessage);
            System.out.println("邮件发送成功！");
            return "redirect:mngUnSub";
        }catch (Exception e){
            System.out.println("删除失败"+e);
        }
        return "mngUnSub";
    }
}
