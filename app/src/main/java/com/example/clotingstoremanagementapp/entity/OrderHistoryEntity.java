package com.example.clotingstoremanagementapp.entity;

import java.util.List;

public class OrderHistoryEntity {
    private String orderCode;
    private String orderDate;
    private Float totalPrice;
    private String state;
    private List<OrderHistoryItemEntity> listOrder;

    public OrderHistoryEntity(String orderCode, String orderDate, Float totalPrice, String state, List<OrderHistoryItemEntity> listOrder) {
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.state = state;
        this.listOrder = listOrder;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public String getState() {
        return state;
    }

    public List<OrderHistoryItemEntity> getListOrder() {
        return listOrder;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setListOrder(List<OrderHistoryItemEntity> listOrder) {
        this.listOrder = listOrder;
    }
}