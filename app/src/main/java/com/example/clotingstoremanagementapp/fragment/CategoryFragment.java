package com.example.clotingstoremanagementapp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.activity.BaseActivity;
import com.example.clotingstoremanagementapp.activity.CategoryInfoActivity;
import com.example.clotingstoremanagementapp.adapter.CategoryAdapter;
import com.example.clotingstoremanagementapp.adapter.CategoryArrayAdapter;
import com.example.clotingstoremanagementapp.custom_interface.IClickItemCategoryListener;
import com.example.clotingstoremanagementapp.entity.CategoryEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private BaseActivity baseActivity;
    private View mView;

    private CategoryInfoActivity categoryInfoActivity;

    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_category, container, false);
        searchView = mView.findViewById(R.id.searchViewCategory);
        baseActivity = (BaseActivity) getActivity();
        recyclerView = mView.findViewById(R.id.rcv_category);
        fab = mView.findViewById(R.id.fab_category);

        //set layout manager cho rcv
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);

        // set adapter cho rcv
        // và set event cho 2 nút
        CategoryAdapter categoryAdapter = new CategoryAdapter(getListCategory(), new IClickItemCategoryListener() {
            @Override
            public void onClickEditCategory(CategoryEntity category) {
                replaceActivityAndMoveData(category);
                //Log.d("oke", "onClickEditCategory: ");
            }

            @Override
            public void onClickDeleteCategory(CategoryEntity category) {
                openConfirmDialog(category);
            }
        });
        recyclerView.setAdapter(categoryAdapter);

        // sự kiện cho search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                categoryAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                categoryAdapter.getFilter().filter(newText);
                return false;
            }
        });

        // sự kiện cho nút thêm
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("hideCategoryId", true);
                intent.putExtras(bundle); // Thêm dữ liệu để ẩn TextView
                startActivity(intent);
            }
        });

        return mView;
    }

    private void openConfirmDialog(CategoryEntity category) {
        final Dialog dialog = new Dialog(baseActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_dialog);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttr = window.getAttributes();
        windowAttr.gravity = Gravity.CENTER;
        window.setAttributes(windowAttr);

        // click ra ngoài thì tắt dialog
        dialog.setCancelable(true);

        // setView trên dialog
        TextView confirmTextView = dialog.findViewById(R.id.confirmTextView);
        Button deleteConfirmBtn = dialog.findViewById(R.id.deleteConfirmBtn);
        Button cancelConfirmBtn = dialog.findViewById(R.id.cancleConfirmBtn);

        confirmTextView.setText("Bạn có chắn rằng muốn xóa danh mục này không?");

        //set event
        deleteConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openSuccessDialog();
                //openErrorDialog();
            }
        });

        cancelConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openSuccessDialog() {
        final Dialog dialog = new Dialog(baseActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.success_dialog);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttr = window.getAttributes();
        windowAttr.gravity = Gravity.CENTER;
        window.setAttributes(windowAttr);

        // setView trên dialog
        TextView successTextView = dialog.findViewById(R.id.successTextView);

        successTextView.setText("Xóa danh mục thành công!");

        // Khởi tạo Handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Đóng dialog sau 3 giây
                dialog.dismiss();
            }
        }, 3000); // Thời gian đợi 3 giây trước khi đóng dialog

        // click ra ngoài thì tắt dialog
        dialog.setCancelable(false);

        dialog.show();
    }

    private void openErrorDialog() {
        final Dialog dialog = new Dialog(baseActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.error_dialog);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttr = window.getAttributes();
        windowAttr.gravity = Gravity.CENTER;
        window.setAttributes(windowAttr);

        // click ra ngoài thì tắt dialog
        dialog.setCancelable(true);

        // setView trên dialog
        TextView errorTextView = dialog.findViewById(R.id.errorTextView);
        Button oKConfirmBtn = dialog.findViewById(R.id.oKConfirmBtn);

        errorTextView.setText("Đã có lỗi xảy ra!");

        //set event
        oKConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void replaceActivityAndMoveData(CategoryEntity category){
        Intent intent = new Intent(getActivity(), CategoryInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("category_entity", category);
        //bundle.putBoolean("hideCategoryId", false);
        intent.putExtras(bundle); // Thêm dữ liệu để ẩn TextView
        startActivity(intent);
    }

    private List<CategoryEntity> getListCategory() {
        List<CategoryEntity> list = new ArrayList<>();
        list.add(new CategoryEntity(3,"Áo sơ mi nữ", new CategoryEntity(1, "Áo nữ", null)));
        list.add(new CategoryEntity(4, "Áo thun nam", new CategoryEntity(2, "Áo nam", null)));
        return list;
    }

}