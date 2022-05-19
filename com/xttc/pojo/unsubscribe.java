package com.xttc.pojo;



public class unsubscribe {
    private Integer id;
    private String usersid;
    private String bookISBN;
    private Integer countnum;
    private Integer sumprice;
    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getSumprice() {
        return sumprice;
    }

    public void setSumprice(Integer sumprice) {
        this.sumprice = sumprice;
    }

    public Integer getCountnum() {
        return countnum;
    }

    public void setCountnum(Integer countnum) {
        this.countnum = countnum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsersid() {
        return usersid;
    }

    public void setUsersid(String usersid) {
        this.usersid = usersid;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }
}

