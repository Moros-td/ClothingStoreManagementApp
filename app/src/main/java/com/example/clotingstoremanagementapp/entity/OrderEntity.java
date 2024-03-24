package com.example.clotingstoremanagementapp.entity;

import java.util.List;

public class OrderEntity {
    private String orderCode;
    private String orderDate;
    private Float totalPrice;
    private String state;
    private List<OrderItemEntity> listOrder;
    public OrderEntity(String orderCode, String orderDate, Float totalPrice, String state, List<OrderItemEntity> listOrder) {
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.state = state;
        this.listOrder = listOrder;
    }
    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<OrderItemEntity> getListOrder() {
        return listOrder;
    }

    public void setListOrder(List<OrderItemEntity> listOrder) {
        this.listOrder = listOrder;
    }
}


