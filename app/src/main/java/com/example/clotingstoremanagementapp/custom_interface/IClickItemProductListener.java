package com.example.clotingstoremanagementapp.custom_interface;

import com.example.clotingstoremanagementapp.entity.ProductEntity;
public interface IClickItemProductListener {
    void onClickEditProduct(ProductEntity product);

    void onClickDeleteProduct(ProductEntity product);
}
