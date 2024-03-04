package com.example.clotingstoremanagementapp.custom_interface;

import com.example.clotingstoremanagementapp.entity.CategoryEntity;

public interface IClickItemCategoryListener {
    void onClickEditCategory(CategoryEntity category);

    void onClickDeleteCategory(CategoryEntity category);
}
