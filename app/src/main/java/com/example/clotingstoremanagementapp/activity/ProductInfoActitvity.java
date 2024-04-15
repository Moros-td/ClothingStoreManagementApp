package com.example.clotingstoremanagementapp.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.adapter.CategoryAdapter;
import com.example.clotingstoremanagementapp.adapter.CategoryArrayAdapter;
import com.example.clotingstoremanagementapp.adapter.ColorAdapter;
import com.example.clotingstoremanagementapp.adapter.ImageAdapter;
import com.example.clotingstoremanagementapp.adapter.ProductArrayAdapter;
import com.example.clotingstoremanagementapp.api.ApiService;
import com.example.clotingstoremanagementapp.custom_interface.IClickItemCategoryListener;
import com.example.clotingstoremanagementapp.entity.CategoryEntity;
import com.example.clotingstoremanagementapp.entity.ProductEntity;
import com.example.clotingstoremanagementapp.entity.ResponseEntity;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.InputStream;
public class ProductInfoActitvity extends InterceptorActivity {
    private Toolbar toolbar;
    private EditText namePD,pricePD,sizeS, sizeM,sizeXL,sizeXXL,sizeL,describePD;
    private Spinner categoryPD,colorPD;
    private TextView srceenName;
    private Button uploadFile,btnSave;
    private ProductArrayAdapter productArrayAdapter;
    private CategoryArrayAdapter categoryArrayAdapter;
    private SessionManager sessionManager;
    private Dialog dialog;
    private ProductEntity product;
    private List<CategoryEntity> listCategories;
    private List<Uri> selectedFileUris;
    private ActivityResultLauncher<Intent> activityLauncher;
    private BaseActivity baseActivity;
    private RecyclerView recyclerView;
    private View mView;
    private String action;
    private String changeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeImage= "false";
        selectedFileUris = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info_actitvity);

        sessionManager = new SessionManager(this);
        dialog = BaseActivity.openLoadingDialog(this);
        this.callApiGetAllCategories();
        /// set view image
        recyclerView = findViewById(R.id.rcv_image);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductInfoActitvity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        ////
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
            //productID.setVisibility(View.GONE);
        }
        else{
            //productID.setVisibility(View.VISIBLE);
        }

        product = (ProductEntity) bundle.get("product_entity");

        if(product != null){
            setData(product);
            action = "edit";
        }
        else {
            action = "add";
            List<String> color = new ArrayList<>();
            color.add("red");
            color.add("pink");
            color.add("yellow");
            color.add("green");
            color.add("blue");
            color.add("beige");
            color.add("white");
            color.add("black");
            color.add("brown");

            ColorAdapter colorAdapter = new ColorAdapter(this, R.layout.item_role_selected, color);
            //colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            colorPD.setAdapter(colorAdapter);
        }
        setEvent();

    }

    private void setEvent() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(action.equals("edit")){
                        callApiEditProduct();
                    }
                    else if(action.equals("add")){
                        callApiAddProduct();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage= "true";
                selectedFileUris.clear();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, 100);
                //activityLauncher.launch(intent);
            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedFileUris.add(imageUri);
                }
            } else if (data.getData() != null) {
                selectedFileUris.add(data.getData());
            }
            //set image
            ImageAdapter imageAdapter = new ImageAdapter(selectedFileUris, null);
            recyclerView.setAdapter(imageAdapter);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    private void setData(ProductEntity product) {
        namePD.setText(product.getProductName());
        pricePD.setText(String.valueOf(product.getProductPrice()));
        sizeS.setText(product.getSizes().get("S").toString());
        sizeM.setText(product.getSizes().get("M").toString());
        sizeL.setText(product.getSizes().get("L").toString());
        sizeXL.setText(product.getSizes().get("XL").toString());
        sizeXXL.setText(product.getSizes().get("XXL").toString());
        describePD.setText(product.getDescription());


        List<String> color = new ArrayList<>();
        color.add("red");
        color.add("pink");
        color.add("yellow");
        color.add("green");
        color.add("blue");
        color.add("beige");
        color.add("white");
        color.add("black");
        color.add("brown");

        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, color);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorPD.setAdapter(colorAdapter);
        for (int i = 0; i < colorAdapter.getCount(); i++) {
            if (colorAdapter.getItem(i).equals(product.getProductColor())) {
                colorPD.setSelection(i);
                break;
            }
        }
        //set image
        selectedFileUris.clear();
        List<String> images =product.getImages();
        ImageAdapter imageAdapter = new ImageAdapter(null, images);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setVisibility(View.VISIBLE);

    }

    private void setView() {
        //setup toolbar
        toolbar = findViewById(R.id.toolbarInfo);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            // set hiện nút back
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //productID = findViewById(R.id.productId);
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
        uploadFile= findViewById(R.id.uploadButton);

        // set data for spinner
//        categoryArrayAdapter = new CategoryArrayAdapter(this, android.R.layout.simple_list_item_1, getListParentCategory());
//        categoryPD.setAdapter(categoryArrayAdapter);

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
    private void callApiGetAllCategories() {
        List<CategoryEntity> list = new ArrayList<>();

        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            //final CountDownLatch latch = new CountDownLatch(1);
            ApiService.apiService.getAllCategories(token)
                    .enqueue(new Callback<List<CategoryEntity>>() {
                        @Override
                        public void onResponse(Call<List<CategoryEntity>> call, Response<List<CategoryEntity>> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            setListCategories(response.body());
                            categoryArrayAdapter = new CategoryArrayAdapter(ProductInfoActitvity.this, android.R.layout.simple_list_item_1, response.body());
                            categoryPD.setAdapter(categoryArrayAdapter);
                            if(product == null){
                                return;
                            }
                            for (int i = 0; i < categoryArrayAdapter.getCount(); i++) {
                                if (categoryArrayAdapter.getItem(i).getCategoryName().equals(product.getCategoryObj().getCategoryName())) {
                                    categoryPD.setSelection(i);
                                    break;
                                }
                            }
                            //latch.countDown();
                        }

                        @Override
                        public void onFailure(Call<List<CategoryEntity>> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(ProductInfoActitvity.this, "Không thể kết nối api");
                            //latch.countDown();
                        }
                    });
//            try {
//                // Chờ cho đến khi count của latch giảm xuống 0
//                latch.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

    }
    private void callApiAddProduct() throws IOException {
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            RequestBody nameRB =RequestBody.create(namePD.getText().toString(), MediaType.parse("text/plain"));

            CategoryEntity selectedCategory = (CategoryEntity) categoryPD.getSelectedItem();
            RequestBody categoryRB = RequestBody.create(String.valueOf(selectedCategory.getId()), MediaType.parse("text/plain"));

            RequestBody colorRB =RequestBody.create(colorPD.getSelectedItem().toString(), MediaType.parse("text/plain"));
            RequestBody priceRB =RequestBody.create(pricePD.getText().toString(), MediaType.parse("text/plain"));
            RequestBody describeRB =RequestBody.create(describePD.getText().toString(), MediaType.parse("text/plain"));
            RequestBody sizeSRB = RequestBody.create(sizeS.getText().toString(), MediaType.parse("text/plain"));
            RequestBody sizeMRB =RequestBody.create(sizeM.getText().toString(), MediaType.parse("text/plain"));
            RequestBody sizeLRB =RequestBody.create(sizeL.getText().toString(), MediaType.parse("text/plain"));
            RequestBody sizeXLRB =RequestBody.create(sizeXL.getText().toString(), MediaType.parse("text/plain"));
            RequestBody sizeXXLRB =RequestBody.create(sizeXXL.getText().toString(), MediaType.parse("text/plain"));

            MultipartBody.Part[] bodies = new MultipartBody.Part[selectedFileUris.size()];
            for (int i = 0; i < selectedFileUris.size(); i++) {
                Uri fileUri = selectedFileUris.get(i);
                InputStream inputStream = getContentResolver().openInputStream(fileUri);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                RequestBody requestFile = RequestBody.create(bytes, MediaType.parse(getContentResolver().getType(fileUri)));

                // Tạo MultipartBody.Part từ RequestBody
                MultipartBody.Part body = MultipartBody.Part.createFormData("file[]", "image"+i+".jpg", requestFile);
                bodies[i] = body;
            }
            dialog = BaseActivity.openLoadingDialog(this);

            ApiService.apiService.addProduct(token, nameRB, priceRB, categoryRB, colorRB, describeRB, sizeSRB, sizeMRB, sizeLRB, sizeXLRB, sizeXXLRB, bodies)
                    .enqueue(new Callback<ResponseEntity>() {
                        @Override
                        public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if (response.isSuccessful() && response.body().isSuccess()) {
                                BaseActivity.openSuccessDialog(ProductInfoActitvity.this, "Thêm sản phẩm thành công!");
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                    }
                                }, 3000);
                            } else {
                                BaseActivity.openErrorDialog(ProductInfoActitvity.this, "Không thể thêm sản phẩm từ API 1.");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseEntity> call, Throwable throwable) {
                            // Xử lý lỗi
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(ProductInfoActitvity.this, "Không thể thêm sản phẩm từ API 2."+throwable.getMessage());
                        }
                    });
        }
    }
    private void callApiEditProduct() throws IOException {
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            RequestBody productCodeRB = RequestBody.create(String.valueOf(product.getProductCode()), MediaType.parse("text/plain"));
            RequestBody nameRB =RequestBody.create(namePD.getText().toString(), MediaType.parse("text/plain"));
            CategoryEntity selectedCategory = (CategoryEntity) categoryPD.getSelectedItem();
            RequestBody categoryRB = RequestBody.create(String.valueOf(selectedCategory.getId()), MediaType.parse("text/plain"));
            RequestBody colorRB =RequestBody.create(colorPD.getSelectedItem().toString(), MediaType.parse("text/plain"));
            RequestBody priceRB =RequestBody.create(pricePD.getText().toString(), MediaType.parse("text/plain"));
            RequestBody describeRB =RequestBody.create(describePD.getText().toString(), MediaType.parse("text/plain"));
            RequestBody sizeSRB = RequestBody.create(sizeS.getText().toString(), MediaType.parse("text/plain"));
            RequestBody sizeMRB =RequestBody.create(sizeM.getText().toString(), MediaType.parse("text/plain"));
            RequestBody sizeLRB =RequestBody.create(sizeL.getText().toString(), MediaType.parse("text/plain"));
            RequestBody sizeXLRB =RequestBody.create(sizeXL.getText().toString(), MediaType.parse("text/plain"));
            RequestBody sizeXXLRB =RequestBody.create(sizeXXL.getText().toString(), MediaType.parse("text/plain"));
            RequestBody ChangeImage= RequestBody.create(changeImage, MediaType.parse("text/plain"));
            MultipartBody.Part[] bodies;
            if(changeImage.equals("true")){
                bodies= new MultipartBody.Part[selectedFileUris.size()];
                for (int i = 0; i < selectedFileUris.size(); i++) {
                    Uri fileUri = selectedFileUris.get(i);
                    InputStream inputStream = getContentResolver().openInputStream(fileUri);
                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    RequestBody requestFile = RequestBody.create(bytes, MediaType.parse(getContentResolver().getType(fileUri)));

                    // Tạo MultipartBody.Part từ RequestBody
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file[]", "image"+i+".jpg", requestFile);
                    bodies[i] = body;
                }
            }
            else{
                bodies = new MultipartBody.Part[0];
            }
            dialog = BaseActivity.openLoadingDialog(this);

            ApiService.apiService.editProduct(token, productCodeRB, nameRB, priceRB, categoryRB, colorRB, describeRB, sizeSRB, sizeMRB, sizeLRB, sizeXLRB, sizeXXLRB, ChangeImage, bodies)
                    .enqueue(new Callback<ResponseEntity>() {
                        @Override
                        public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if (response.isSuccessful() && response.body().isSuccess()) {
                                BaseActivity.openSuccessDialog(ProductInfoActitvity.this, "Chỉnh sửa sản phẩm thành công!");
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                    }
                                }, 3000);
                            } else {
                                BaseActivity.openErrorDialog(ProductInfoActitvity.this, "Không thể thêm sản phẩm từ API 1.");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseEntity> call, Throwable throwable) {
                            // Xử lý lỗi
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(ProductInfoActitvity.this, "Không thể thêm sản phẩm từ API 2."+throwable.getMessage());
                        }
                    });
        }
    }
    public List<CategoryEntity> getListCategories() {
        return listCategories;
    }

    public void setListCategories(List<CategoryEntity> listCategories) {
        this.listCategories = listCategories;
    }
}