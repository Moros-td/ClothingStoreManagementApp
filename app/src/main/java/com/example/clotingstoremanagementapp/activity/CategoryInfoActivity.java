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

public class CategoryInfoActivity extends InterceptorActivity {
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
                BaseActivity.openSuccessDialog(CategoryInfoActivity.this, "Thêm danh mục thành công!");
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

}