package com.example.clotingstoremanagementapp.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Statistical {
    @SerializedName("order_code")
    private String orderCode;
    @SerializedName("DATE(order_date)")
    private Date orderDate;
    @SerializedName("total_price")
    private Double totalPrice;

    public Statistical(String orderCode, Date orderDate, Double totalPrice) {
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}