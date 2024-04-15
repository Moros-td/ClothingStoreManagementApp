package com.example.clotingstoremanagementapp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseEntity  implements Serializable {
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private boolean success;

    public ResponseEntity(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ResponseEntity() {
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
