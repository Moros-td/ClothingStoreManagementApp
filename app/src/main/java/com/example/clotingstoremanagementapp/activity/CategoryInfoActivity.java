package com.example.clotingstoremanagementapp.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.adapter.CategoryArrayAdapter;
import com.example.clotingstoremanagementapp.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryInfoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView categoryIdTextView, srceenName;
    private EditText categoryNameEditText;

    private Spinner spinnerParentCategory;

    private CategoryArrayAdapter categoryArrayAdapter;

    private Button saveCategoryBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_info);
        setView();

        // nhận data
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        // Kiểm tra xem dữ liệu đã được truyền qua không
        boolean shouldHideCategoryId = bundle.getBoolean("hideCategoryId", false);
        if (shouldHideCategoryId) {
            // Ẩn TextView đi
            categoryIdTextView.setVisibility(View.GONE);
        }
        else{
            categoryIdTextView.setVisibility(View.VISIBLE);
        }

        CategoryEntity category = (CategoryEntity) bundle.get("category_entity");

        if(category != null){
            setData(category);
        }

        setEvent();
    }

    private void setData(CategoryEntity category) {
        categoryNameEditText.setText(category.getCategoryName());
        categoryIdTextView.setText(String.valueOf(category.getId()));

        // set category parent
        List<CategoryEntity> tmp = getListParentCategory();
        int i = 0;
        for (CategoryEntity x : tmp) {
            if(x.getId() == category.getCategoryParent().getId()){
                spinnerParentCategory.setSelection(i);
                break;
            }
                i += 1;
        }
    }

    public void setView() {
        //setup toolbar
        toolbar = findViewById(R.id.toolbarInfo);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            // set hiện nút back
             getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        categoryIdTextView = findViewById(R.id.categoryId);
        categoryNameEditText = findViewById(R.id.categoryName);
        spinnerParentCategory = findViewById(R.id.spinnerParentCategory);

        // set data for spinner
        categoryArrayAdapter = new CategoryArrayAdapter(this, android.R.layout.simple_list_item_1, getListParentCategory());
        spinnerParentCategory.setAdapter(categoryArrayAdapter);

        saveCategoryBtn = findViewById(R.id.saveCategoryBtn);
        srceenName = findViewById(R.id.srceenName);
        srceenName.setText("Thông tin danh mục");
    }

    public void setEvent() {
        saveCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSuccessDialog();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Xử lý khi bấm nút back trên toolbar
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<CategoryEntity> getListParentCategory() {
        List<CategoryEntity> list = new ArrayList<>();
        list.add(new CategoryEntity(1, "Áo nữ", null));
        list.add(new CategoryEntity(2,"Áo nam", null));
        return list;
    }

    private void openSuccessDialog() {
        final Dialog dialog = new Dialog(this);
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

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish(); // Đóng activity hiện tại sau khi dialog đóng
            }
        });

        // click ra ngoài thì tắt dialog
        dialog.setCancelable(false);

        dialog.show();
    }

    private void openErrorDialog() {
        final Dialog dialog = new Dialog(this);
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
}