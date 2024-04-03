package com.example.clotingstoremanagementapp.custom_interface;

import com.example.clotingstoremanagementapp.entity.AdminEntity;
public interface IClickItemAdminListener {
    void onClickEditStaff(AdminEntity admin);

    void onClickDeleteStaff(AdminEntity admin);

    void onClickResetPasswordStaff(AdminEntity admin);
}
