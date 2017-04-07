package com.example.arekr.dumptrace;

import java.io.Serializable;

/**
 * Created by Krishna Teja Are on 4/2/2017.
 */

public class Details implements Serializable {
    private String name;
    private String address;
    private String phonenumber;
    private String email;
    private String date;
    private String time;

    public Details(String name, String address, String phonenumber, String email, String date, String time) {
        this.setName(name);
        this.setAddress(address);
        this.setPhonenumber(phonenumber);
        this.setEmail(email);
        this.setDate(date);
        this.setTime(time);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
