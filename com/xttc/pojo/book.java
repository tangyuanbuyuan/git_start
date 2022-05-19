package com.xttc.pojo;

import java.util.List;

public class book {
    private Integer id;
    private String bookname;
    private String ISBN;
    private Integer price;
    private String press;
    private Integer bcount;
    private String author;
    private List<subscribe> subscribeList;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public Integer getBcount() {
        return bcount;
    }

    public void setBcount(Integer bcount) {
        this.bcount = bcount;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<subscribe> getSubscribeList() {
        return subscribeList;
    }

    public void setSubscribeList(List<subscribe> subscribeList) {
        this.subscribeList = subscribeList;
    }

    @Override
    public String toString() {
        return "book{" +
                "id=" + id +
                ", bookname='" + bookname + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", price=" + price +
                ", press='" + press + '\'' +
                ", bcount=" + bcount +
                ", author='" + author + '\'' +
                ", subscribeList=" + subscribeList +
                '}';
    }
}
