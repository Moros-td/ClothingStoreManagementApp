package com.example.clotingstoremanagementapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.clotingstoremanagementapp.adapter.ProductArrayAdapter;
import com.example.clotingstoremanagementapp.entity.CategoryEntity;
import com.example.clotingstoremanagementapp.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class ProductInfoActitvity extends InterceptorActivity {
    private Toolbar toolbar;
    private EditText namePD,pricePD,sizeS, sizeM,sizeXL,sizeXXL,sizeL,describePD;
    private Spinner categoryPD,colorPD;
    private TextView productID,srceenName;
    private Button uploadFile,btnSave;
    private ProductArrayAdapter productArrayAdapter;
    private CategoryArrayAdapter categoryArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info_actitvity);
        setView();

        // nhận data
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        // Kiểm tra xem dữ liệu đã được truyền qua không
        boolean shouldHideProductId = bundle.getBoolean("hideProductId", false);
        if (shouldHideProductId) {
            // Ẩn TextView đi
            productID.setVisibility(View.GONE);
        }
        else{
            productID.setVisibility(View.VISIBLE);
        }

        ProductEntity product = (ProductEntity) bundle.get("product_entity");

        if(product != null){
            setData(product);
        }
//
//        setEvent();
    }

    private void setEvent() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSuccessDialog();
            }
        });
    }

    private void setData(ProductEntity product) {
        namePD.setText(product.getProductName());
        productID.setText(String.valueOf(product.getId()));
        pricePD.setText(String.valueOf(product.getProductPrice()));
        sizeS.setText("4");
        sizeM.setText("5");
        sizeL.setText("6");
        sizeXL.setText("7");
        sizeXXL.setText("8");
        describePD.setText("Mô tả ví dụ");

    }

    private void setView() {
        //setup toolbar
        toolbar = findViewById(R.id.toolbarInfo);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            // set hiện nút back
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        productID = findViewById(R.id.productId);
        namePD = findViewById(R.id.productName);
        categoryPD = findViewById(R.id.productCategory);
        sizeS =findViewById(R.id.sizeS);
        sizeM =findViewById(R.id.sizeM);
        sizeL =findViewById(R.id.sizeL);
        sizeXL =findViewById(R.id.sizeXL);
        sizeXXL =findViewById(R.id.sizeXXL);
        colorPD = findViewById(R.id.productColor);
        pricePD=findViewById(R.id.productPrice);
        btnSave=findViewById(R.id.saveProductBtn);
        describePD=findViewById(R.id.prductDescribe);
        // set data for spinner
        categoryArrayAdapter = new CategoryArrayAdapter(this, android.R.layout.simple_list_item_1, getListParentCategory());
        categoryPD.setAdapter(categoryArrayAdapter);

        //saveCategoryBtn = findViewById(R.id.saveCategoryBtn);
        srceenName = findViewById(R.id.srceenName);
        srceenName.setText("Thông tin sản phẩm");
    }
    private List<CategoryEntity> getListParentCategory() {
        List<CategoryEntity> list = new ArrayList<>();
        list.add(new CategoryEntity(1, "Áo nữ", null));
        list.add(new CategoryEntity(2,"Áo nam", null));
        return list;
    }
    private List<ProductEntity> getListProduct() {
        List<ProductEntity> listP = new ArrayList<>();
        for (int i = 0; i< 5; i++){
            listP.add(new ProductEntity(i,"SP123", "ÁO THUN TRƠN CỔ ĐỨC KHUY NGỌC TRAI " + i, 50, 500.00,"Mô tả rỗng"));
        }
        return listP;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Xử lý khi bấm nút back trên toolbar
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}