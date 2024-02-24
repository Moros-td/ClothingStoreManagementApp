package com.example.clotingstoremanagementapp.entity;

public class CategoryEntity {
    private String categoryName;
    private String categoryParenName;

    public CategoryEntity(String categoryName, String categoryParenName) {
        this.categoryName = categoryName;
        this.categoryParenName = categoryParenName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryParenName() {
        return categoryParenName;
    }

    public void setCategoryParenName(String categoryParenName) {
        this.categoryParenName = categoryParenName;
    }
}
