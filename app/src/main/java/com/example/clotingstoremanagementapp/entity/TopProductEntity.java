package com.example.clotingstoremanagementapp.entity;

import com.google.gson.annotations.SerializedName;

public class TopProductEntity {
    @SerializedName("product_name")
    private String productName;

    @SerializedName("total_quantity")
    private int totalQuantity;

    public TopProductEntity(String productName, int totalQuantity) {
        this.productName = productName;
        this.totalQuantity = totalQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
