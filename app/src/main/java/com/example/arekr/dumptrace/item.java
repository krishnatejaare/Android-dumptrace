package com.example.arekr.dumptrace;

import java.io.Serializable;

/**
 * Created by Krishna Teja Are on 3/28/2017.
 */

public class item implements Serializable {
    private String name;
    private String count;
public item(String name, String count){
    this.name=name;
    this.count=count;
}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
