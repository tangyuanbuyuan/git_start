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
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    @Autowired(required = false)
    private BookMapper bookMapper;
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private SubscribeMapper subscribeMapper;
    @Autowired(required = false)
    private UnSubscribeMapper unSubscribeMapper;
    @RequestMapping("/user")
    public String ManageUser(Model model){
        //传递待办事务数量
        try{
            List<unsubscribe> unsubscribes =unSubscribeMapper.selectUnSubscribeAll();
            model.addAttribute("num",unsubscribes.size());
        }catch (NullPointerException e){
            System.out.println("无");
        }
        List<person> users=null;
        try{
            users=userMapper.selectAllUser();
            for(person user:users){
                if (user.getPhone()==null){
                    user.setPhone("无");
                }
                if (user.getSid()==null){
                    user.setSid("无");
                }
                if (user.getSclass()==null){
                    user.setSclass("无");
                }
            }
            model.addAttribute("users",users);
        }catch (NullPointerException e){
            model.addAttribute("msg","仓库没有书籍");
        }
        return "user";
    }
    @RequestMapping("/selectuser")
    public String selectUser(Model model, HttpServletRequest request){
        //传递待办事务数量
        try{
            List<unsubscribe> unsubscribes =unSubscribeMapper.selectUnSubscribeAll();
            model.addAttribute("num",unsubscribes.size());
        }catch (NullPointerException e){
            System.out.println("无");
        }
        //正题
        String str = request.getParameter("str");
        if (str!=null &&str!=""){
            try{
                List<person> users = userMapper.selectUser(str);
                for(person user:users){
                    if (user.getPhone()==null){
                        user.setPhone("无");
                    }
                    if (user.getSid()==null){
                        user.setSid("无");
                    }
                    if (user.getSclass()==null){
                        user.setSclass("无");
                    }
                }
                model.addAttribute("users",users);
            }catch (NullPointerException e){
                model.addAttribute("msg","仓库没有书籍");
            }
        }else{
            List<person> users=null;
            try{
                users=userMapper.selectAllUser();
                for(person user:users){
                    if (user.getPhone()==null){
                        user.setPhone("无");
                    }
                    if (user.getSid()==null){
                        user.setSid("无");
                    }
                    if (user.getSclass()==null){
                        user.setSclass("无");
                    }
                }
                model.addAttribute("users",users);
            }catch (NullPointerException e){
                model.addAttribute("msg","仓库没有书籍");
            }
        }
        return "user";
    }
    @RequestMapping("/selectsub")
    public String selectSubscribe(Model model, HttpSession session){
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
                List<subscribe> subscribes= subscribeMapper.selectSubscribe(sid);
                model.addAttribute("subscribes",subscribes);
            }
           catch(NullPointerException e){
                //model.addAttribute("msg","您没有订单");
           }
        }
        else{
            model.addAttribute("msg","请先完善个人信息");
        }
        return "usersub";
    }
    @RequestMapping("/dingyue")
    public String dingyueBook(book book,subscribe subscribe,Model model,HttpServletRequest request,HttpSession session){
        Integer book_count=0;//所要订阅的书的数量
        person user = (person)session.getAttribute("loginUser");
        String usersid = user.getSid();//获取当前账号的sid
        Integer count =  Integer.parseInt(request.getParameter("bcount"));//输入的数量
        String ISBN = request.getParameter("ISBN");//获取所要订阅的教材的ISBN码
        System.out.println(ISBN);
        //获取当前下订单时间
        Date dateTime =new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String time = sdf.format(dateTime);
        System.out.println(time);
        //初始话总价格以及书的单价
        Integer sumprice=0;
        Integer price=0;
        if (usersid!=null){//判断账号的个人信息是否完善
            if(count!=0){//判断输入的数是否为零或未输入
                try{
                   book= bookMapper.findBookByISBN(ISBN);
                   book_count = book.getBcount();//获取库存数量
                   price = book.getPrice();//获取书的单价
                   sumprice = count*price;//计算总价格
                }
                catch(NullPointerException e){
                    model.addAttribute("msg","没找到这本书");
                }
                if (count<=book_count){//判断数量是否超出库存
                    subscribe.setBookISBN(ISBN);
                    subscribe.setCountnum(count);
                    subscribe.setUsersid(usersid);
                    subscribe.setSumprice(sumprice);
                    subscribe.setDateTime(time);
                    System.out.println(subscribe.getDateTime());
                    try{
                        subscribeMapper.insertSubscribe(subscribe);
                        System.out.println("订阅成功");
                        return "redirect:main1";
                    }catch(Exception e){
                        System.out.println(e);
                    }
                }else{
                    model.addAttribute("msg","库存不足，当前剩余数量"+book.getBcount());
                }
            }else{
                model.addAttribute("msg","请输入大于0的数");
            }
        }else{
            model.addAttribute("msg","请完善个人信息");
        }
        return "main1";
    }
    @Autowired
    JavaMailSenderImpl mailSender;
    @RequestMapping("/deleteuser")
    private String DeleteUnSub(HttpServletRequest request, RedirectAttributesModelMap model){
        String username = request.getParameter("username");
        String useremail = request.getParameter("useremail");
        try{
            userMapper.deleteUser(useremail);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            //内容
            helper.setText("您好！，" + username + "。您的账号为:" + "<h2>" + useremail + "</h2>" + "已经管理员删除！，如有疑问请联系管理员", true);
            //邮件接收者
            helper.setTo(useremail);
            //邮件发送者，必须和配置文件里的一样，不然授权码匹配不上
            helper.setFrom("473238524@qq.com");
            mailSender.send(mimeMessage);
            System.out.println("邮件发送成功！");
            return "redirect:mngUnSub";
        }catch (Exception e){
            model.addFlashAttribute("msg","此账号下有订单，请先处理订单");
            System.out.println("删除失败"+e);
        }
        return "redirect:user";
    }
}
