package com.example.arekr.dumptrace;

/**
 * Created by Krishna Teja Are on 4/2/2017.
 */

public class Price {
    private String count;
    private String price;
    //private String discount;
    private String finalprice;

    public Price(String count, String price, String finalprice) {
        this.setCount(count);
        this.setPrice(price);
        this.setFinalprice(finalprice);
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public String getFinalprice() {
        return finalprice;
    }

    public void setFinalprice(String finalprice) {
        this.finalprice = finalprice;
    }
}
