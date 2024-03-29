package com.example.clotingstoremanagementapp.entity;

import com.google.gson.annotations.SerializedName;

public class RevenueEntity {
    @SerializedName("year")
    private int year;

    @SerializedName("month")
    private int month;

    @SerializedName("revenue")
    private double revenue;

    public RevenueEntity(int year, int month, double revenue) {
        this.year = year;
        this.month = month;
        this.revenue = revenue;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}
