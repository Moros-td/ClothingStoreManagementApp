package com.example.clotingstoremanagementapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.activity.AdminInfoActivity;
import com.example.clotingstoremanagementapp.activity.BaseActivity;
import com.example.clotingstoremanagementapp.activity.CategoryInfoActivity;
import com.example.clotingstoremanagementapp.activity.ResetPasswordActivity;
import com.example.clotingstoremanagementapp.adapter.AdminAdapter;
import com.example.clotingstoremanagementapp.api.ApiService;
import com.example.clotingstoremanagementapp.custom_interface.IClickItemAdminListener;
import com.example.clotingstoremanagementapp.entity.AdminEntity;
import com.example.clotingstoremanagementapp.entity.CategoryEntity;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;
import com.example.clotingstoremanagementapp.response.LoginResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffFragment extends Fragment {

    RecyclerView rcv_staff;
    FloatingActionButton fab_staff;
    SearchView searchViewStaff;
    BaseActivity baseActivity;
    SessionManager sessionManager;
    Dialog dialogLoading;
    List<AdminEntity> listStaff;

    ActivityResultLauncher<Intent> activityLauncher;
    public static final int REQUEST_CODE_ADD_PRODUCT = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_staff, container, false);
        rcv_staff = mView.findViewById(R.id.rcv_staff);
        fab_staff = mView.findViewById(R.id.fab_staff);
        searchViewStaff = mView.findViewById(R.id.searchViewStaff);
        baseActivity = (BaseActivity) getContext();

        //set layout manager cho rcv
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        rcv_staff.setLayoutManager(linearLayoutManager);
        sessionManager = new SessionManager(baseActivity);
        dialogLoading = BaseActivity.openLoadingDialog(baseActivity);
        listStaff = new ArrayList<>();

        // tạo một activityLauncher để reload lại fragment nếu kết quả trả về là ok
        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Reload dữ liệu từ API và cập nhật RecyclerView
                        callApiGetAllStaff();
                    }
                });

        callApiGetAllStaff();

        if(sessionManager.getCustom("role").equals("admin")){
            // sự kiện cho nút thêm
            fab_staff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AdminInfoActivity.class);
                    activityLauncher.launch(intent);
                }
            });
        } else{
            fab_staff.setVisibility(View.GONE);
        }

        return mView;
    }

    private void callApiGetAllStaff() {
        if(sessionManager.isLoggedIn()) {
            String token = sessionManager.getJwt();
            String role = sessionManager.getCustom("role");
            ApiService.apiService.getAllStaffs(token)
                    .enqueue(new Callback<List<AdminEntity>>() {
                        @Override
                        public void onResponse(Call<List<AdminEntity>> call, Response<List<AdminEntity>> response) {
                            if (dialogLoading != null && dialogLoading.isShowing()) {
                                dialogLoading.dismiss();
                            }
                            listStaff = response.body();

                            AdminAdapter adapter = new AdminAdapter(listStaff, role, new IClickItemAdminListener() {
                                @Override
                                public void onClickEditStaff(AdminEntity admin) {
                                    replaceActivityAndMoveData(admin, AdminInfoActivity.class);
                                }

                                @Override
                                public void onClickDeleteStaff(AdminEntity admin) {
                                    openConfirmDialog(admin, "delete");
                                }

                                @Override
                                public void onClickResetPasswordStaff(AdminEntity admin) {
                                    openConfirmDialog(admin, "resetPassword");
                                }
                            });
                            rcv_staff.setAdapter(adapter);

                            searchViewStaff.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    adapter.getFilter().filter(query);
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    adapter.getFilter().filter(newText);
                                    return false;
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<List<AdminEntity>> call, Throwable throwable) {
                            if (dialogLoading != null && dialogLoading.isShowing()) {
                                dialogLoading.dismiss();
                            }
                            BaseActivity.openErrorDialog(baseActivity, "Không thể kết nối api");
                        }
                    });
        }
    }

    private void openConfirmDialog(AdminEntity admin, String option) {
        final Dialog dialogConfirm = new Dialog(baseActivity);
        dialogConfirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogConfirm.setContentView(R.layout.confirm_dialog);

        Window window = dialogConfirm.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttr = window.getAttributes();
        windowAttr.gravity = Gravity.CENTER;
        window.setAttributes(windowAttr);

        // click ra ngoài thì tắt dialog
        dialogConfirm.setCancelable(true);

        // setView trên dialog
        TextView confirmTextView = dialogConfirm.findViewById(R.id.confirmTextView);
        Button deleteConfirmBtn = dialogConfirm.findViewById(R.id.deleteConfirmBtn);
        deleteConfirmBtn.setText("Xác nhận");
        Button cancelConfirmBtn = dialogConfirm.findViewById(R.id.cancleConfirmBtn);

        if(option.equals("delete")) {
            confirmTextView.setText("Bạn có chắn rằng muốn xóa nhân viên này không?");
        }
        if(option.equals("resetPassword")){
            confirmTextView.setText("Bạn có chắn rằng muốn reset mật khẩu nhân viên này không?");
        }
            //set event
            deleteConfirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogConfirm.dismiss();

                    if(option.equals("delete")) {
                        callApiDeleteStaff(admin);
                    }
                    if(option.equals("resetPassword")){
                        replaceActivityAndMoveData(admin, ResetPasswordActivity.class);
                    }
                }
            });

        cancelConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirm.dismiss();
            }
        });

        dialogConfirm.show();
    }

    private void callApiDeleteStaff(AdminEntity admin){
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            dialogLoading = BaseActivity.openLoadingDialog(baseActivity);
            ApiService.apiService.deleteStaff(token, admin.getUsername())
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (dialogLoading != null && dialogLoading.isShowing()) {
                                dialogLoading.dismiss();
                            }
                            LoginResponse loginResponse = response.body();
                            if(loginResponse.getErr() != null){
                                BaseActivity.openErrorDialog(baseActivity, loginResponse.getErr());
                            }
                            else if(loginResponse.getMessage() != null){
                                BaseActivity.openSuccessDialog(baseActivity, loginResponse.getMessage());
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // set result để bên fragment reload
                                        callApiGetAllStaff();
                                    }
                                }, 3000); // 3000 milliseconds = 3 second
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                            if (dialogLoading != null && dialogLoading.isShowing()) {
                                dialogLoading.dismiss();
                            }
                            BaseActivity.openErrorDialog(baseActivity, "Không thể kết nối api");
                        }
                    });
        }
    }

    public void replaceActivityAndMoveData(AdminEntity admin, Class<?> cls){
        Intent intent = new Intent(getActivity(), cls);
        Bundle bundle = new Bundle();
        bundle.putSerializable("admin_entity", admin);
        intent.putExtras(bundle); // Thêm dữ liệu để ẩn TextView
        activityLauncher.launch(intent);
    }
}