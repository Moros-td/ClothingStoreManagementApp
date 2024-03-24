package com.example.clotingstoremanagementapp.entity;

public class OrderItemEntity {
    private String productName;
    private int quanity;

    public OrderItemEntity(String productName, int quanity) {
        this.productName = productName;
        this.quanity = quanity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuanity() {
        return quanity;
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }
}
