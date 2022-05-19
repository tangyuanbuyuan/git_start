package com.xttc.controller;

import com.xttc.dao.BookMapper;
import com.xttc.dao.UnSubscribeMapper;
import com.xttc.pojo.book;
import com.xttc.pojo.subscribe;
import com.xttc.pojo.unsubscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
public class BookController {
    @Autowired(required =false)
    private BookMapper bookMapper;
    @Autowired(required =false)
    private UnSubscribeMapper unSubscribeMapper;
    @RequestMapping("/main")
    public String ManageBook(book book,book book1, Model model,RedirectAttributesModelMap model1){
        try{
            List<unsubscribe> unsubscribes =unSubscribeMapper.selectUnSubscribeAll();
            model.addAttribute("num",unsubscribes.size());
        }catch (NullPointerException e){
            System.out.println("无");
        }
        String bookname = book.getBookname();
        String ISBN = book.getISBN();
        String press = book.getPress();
        Integer price = book.getPrice();
        Integer bcount = book.getBcount();
        String author = book.getAuthor();
        if(bookname != null && ISBN != null && press !=null && price !=null && bcount!=null && author !=null
                && bookname !="" && ISBN != "" && press != "" && author !=""
        ){
                try{
                    book1=bookMapper.findBookByISBN(ISBN);
                }catch(NullPointerException e){
                    System.out.println("书库没有，可以添加");
                }

                if (book1 == null){
                    bookMapper.insertBook(book);
                    model1.addFlashAttribute("biaozhi","1");
                    return "redirect:main";
                }
                else{
                    model1.addFlashAttribute("biaozhi","0");
                    return "redirect:main";
                }
        }
        List<book> books=null;
        try{
            books=bookMapper.selectBookAll();
            model.addAttribute("books",books);
        }catch (NullPointerException e){
            model.addAttribute("msg","仓库没有书籍");
        }
        return "main";
    }
    @RequestMapping("/main1")
    public String getBook1(Model model, HttpSession session){
        List<book> books=null;
        try{
            books=bookMapper.selectBookAll();
            model.addAttribute("books",books);
        }catch (NullPointerException e){
            model.addAttribute("msg","仓库没有书籍");
        }
        return "main1";
    }
    @RequestMapping("/delete")
    public String deleteBook(book book,Model model , HttpServletRequest request, HttpServletResponse response) throws IOException {
        //传递待办事务数量
        try{
            List<unsubscribe> unsubscribes =unSubscribeMapper.selectUnSubscribeAll();
            model.addAttribute("num",unsubscribes.size());
        }catch (NullPointerException e){
            System.out.println("无");
        }
        response.setCharacterEncoding("GBK");
        String ISBN=request.getParameter("ISBN");
        System.out.println("ISBN:"+ISBN);
        if(ISBN != null && ISBN != ""){
            try{
                book =bookMapper.findBookandSubByISBN(ISBN);
            }catch (NullPointerException e){
                System.out.println("删除了个寂寞，不存在的 ");
            }
            if (book==null){
                try{
                    bookMapper.deleteBook(ISBN);
                    System.out.println("删除成功");
                    return "redirect:main";
                }
                catch(NullPointerException e){
                    System.out.println("删除失败");
                }
            }
            else{
                PrintWriter out = response.getWriter();
                out.print("<script>alert(\"已经被订阅，不能删除\")</script>");
            }
        }
        List<book> books=null;
        try{
            books=bookMapper.selectBookAll();
            model.addAttribute("books",books);
        }catch (NullPointerException e){
            model.addAttribute("msg","");
        }
        return "main";
    }
    @RequestMapping("/update")
    public String updateBook(book book,Model model){
        //传递待办事务数量
        try{
            List<unsubscribe> unsubscribes =unSubscribeMapper.selectUnSubscribeAll();
            model.addAttribute("num",unsubscribes.size());
        }catch (NullPointerException e){
            System.out.println("无");
        }
        String bookname = book.getBookname();
        String press = book.getPress();
        String author = book.getAuthor();
        String ISBN = book.getISBN();
        Integer price = book.getPrice();
        Integer bcount = book.getBcount();
        System.out.println(book);
        book book1 = null;
        if(bookname != null && ISBN != null && press !=null && price !=null && bcount!=null && author !=null
                && bookname !="" && ISBN != "" && press != "" && author !="")
        {
            try{
                book1 = bookMapper.findBookByISBN(ISBN);
            }catch(NullPointerException e){
                System.out.println(e);
            }
            if (book1.getISBN().equals(ISBN) || book1==null){
                if (bcount>=0){
                    if (price>0){
                        try{
                            bookMapper.updateBook(book);
                            return "redirect:main";
                        }catch (Exception e){
                            model.addAttribute("msg1","已被订购,暂不能修改");
                        }
                    }else{
                        model.addAttribute("msg1","价格不能小于零或等于0");
                    }
                }else{
                    model.addAttribute("msg1","数量不能设置为负");
                }
            }else{
                model.addAttribute("msg1","ISBN码重复，请检查输入是否正确");
            }
        }
        List<book> books=null;
        try{
            books=bookMapper.selectBookAll();
            model.addAttribute("books",books);
        }catch (NullPointerException e){
            model.addAttribute("msg","仓库没有书籍");
        }
        return "main";
    }
    @RequestMapping("/selectbook")
    public String selectBook(book book,Model model,HttpServletRequest request){
        //传递待办事务数量
        try{
            List<unsubscribe> unsubscribes =unSubscribeMapper.selectUnSubscribeAll();
            model.addAttribute("num",unsubscribes.size());
        }catch (NullPointerException e){
            System.out.println("无");
        }
        String str =request.getParameter("str");
        if (str!=null && str!=""){
            try{
                List<book> books=bookMapper.selectBook(str);
                model.addAttribute("books",books);
            }
            catch(NullPointerException e){
                model.addAttribute("msg","仓库没有这本书书籍");
            }
        }else{
            List<book> books=null;
            try{
                books=bookMapper.selectBookAll();
                model.addAttribute("books",books);
            }catch (NullPointerException e){
                model.addAttribute("msg","仓库没有书籍");
            }
        }
        return "main";
    }
    @RequestMapping("/selectbook1")
    public String selectBook1(book book,Model model,HttpServletRequest request){
        //传递待办事务数量
        try{
            List<unsubscribe> unsubscribes =unSubscribeMapper.selectUnSubscribeAll();
            model.addAttribute("num",unsubscribes.size());
        }catch (NullPointerException e){
            System.out.println("无");
        }
        String str =request.getParameter("str");
        if (str!=null && str!=""){
            try{
                List<book> books=bookMapper.selectBook(str);
                model.addAttribute("books",books);
            }
            catch(NullPointerException e){
                model.addAttribute("msg","仓库没有这本书书籍");
            }
        }else{
            List<book> books=null;
            try{
                books=bookMapper.selectBookAll();
                model.addAttribute("books",books);
            }catch (NullPointerException e){
                model.addAttribute("msg","仓库没有书籍");
            }
        }
        return "main1";
    }

}
