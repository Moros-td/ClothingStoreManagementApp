package com.example.clotingstoremanagementapp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerEntity implements Serializable {
    @SerializedName("full_name")
    private String fullName;
    private String email;
    private String phone;
    private String address;

    public CustomerEntity(String fullName, String email, String phone, String address) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
