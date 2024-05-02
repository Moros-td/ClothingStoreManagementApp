package com.example.clotingstoremanagementapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.activity.BaseActivity;
import com.example.clotingstoremanagementapp.activity.ProductInfoActitvity;
import com.example.clotingstoremanagementapp.adapter.ProductAdapter;
import com.example.clotingstoremanagementapp.api.ApiService;
import com.example.clotingstoremanagementapp.custom_interface.IClickItemProductListener;
import com.example.clotingstoremanagementapp.entity.ProductEntity;
import com.example.clotingstoremanagementapp.entity.ResponseEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private BaseActivity baseActivity;
    private View mView;
    private Dialog dialog;

    private FloatingActionButton fab_product;
    private List<ProductEntity> listProduct;
    SessionManager sessionManager;
    ActivityResultLauncher<Intent> activityLauncher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_product, container, false);
        searchView = mView.findViewById(R.id.searchViewProduct);
        baseActivity = (BaseActivity) getActivity();
        recyclerView = mView.findViewById(R.id.rcv_product);
        fab_product = mView.findViewById(R.id.fab_product);

        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Reload dữ liệu từ API và cập nhật RecyclerView
                        callApiGetProducts();
                    }
                });
        //set layout manager cho rcv
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        dialog = BaseActivity.openLoadingDialog(getContext());
        sessionManager = new SessionManager(baseActivity);
        listProduct = new ArrayList<>();
        callApiGetProducts();
        // set adapter cho rcv


        fab_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductInfoActitvity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("hideProductId", true);
                intent.putExtras(bundle); // Thêm dữ liệu để ẩn TextView
                activityLauncher.launch(intent);
            }
        });

        return mView;
    }

    //    private List<ProductEntity> getListProduct() {
//        List<ProductEntity> listP = new ArrayList<>();
//        for (int i = 0; i< 10; i++){
//            Map<String, Integer> sizes = new HashMap<>();
//            sizes.put("S", 10);
//            List<String> images = new ArrayList<>();
//            images.add("./public/products/2023/12/07/img1701935174.jpeg");
//            listP.add(new ProductEntity("SP123", "ÁO THUN TRƠN CỔ ĐỨC KHUY NGỌC TRAI " + i, "red", 10,200.000,"Test",images,sizes,"test","2023-12-07 14:46:19"));
//        }
//        return listP;
//    }
    public void replaceActivityAndMoveData(ProductEntity product){
        Intent intent = new Intent(getActivity(), ProductInfoActitvity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product_entity",product);
        intent.putExtras(bundle);
        activityLauncher.launch(intent);
    }
    private void openConfirmDialog(ProductEntity product) {
        final Dialog dialog = new Dialog(getContext());
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

        confirmTextView.setText("Bạn có chắn rằng muốn xóa sản phẩm này không?");

        //set event
        deleteConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //BaseActivity.openErrorDialog(getContext(), "Xóa sản phẩm thành công!");
                //openErrorDialog();
                callApiDeleteProduct(product.getProductCode());
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
    private void callApiGetProducts() {

        if(sessionManager.isLoggedIn()) {
            String token = sessionManager.getJwt();
            ApiService.apiService.getAllProducts(token).enqueue(new Callback<List<ProductEntity>>() {
                @Override
                public void onResponse(Call<List<ProductEntity>> call, Response<List<ProductEntity>> response) {
                    if (response.isSuccessful()) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        listProduct = response.body();
                        ProductAdapter productAdapter = new ProductAdapter(listProduct, new IClickItemProductListener() {

                            @Override
                            public void onClickEditProduct(ProductEntity product) {
                                replaceActivityAndMoveData(product);
                            }

                            @Override
                            public void onClickDeleteProduct(ProductEntity product) {
                                openConfirmDialog(product);

                                //callApiDeleteProduct(product.getProductCode());

                            }
                        });
                        recyclerView.setAdapter(productAdapter);

                        // sự kiện cho search view
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                productAdapter.getFilter().filter(query);
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                productAdapter.getFilter().filter(newText);
                                return false;
                            }
                        });
                        Log.d("List", "test");
                    } else {
                        BaseActivity.openErrorDialog(getContext(), "Không thể lấy danh sách danh mục từ API.");
                    }
                }

                @Override
                public void onFailure(Call<List<ProductEntity>> call, Throwable throwable) {
                    // Xử lý lỗi
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    BaseActivity.openErrorDialog(getContext(), "Hiện không thể truy cập API, vui lòng thử lại sau!");
                }
            });
        }
    }
    void callApiDeleteProduct(String productCode){
        if(sessionManager.isLoggedIn()) {
            String token = sessionManager.getJwt();
            ApiService.apiService.deleteProduct(token, productCode)
                    .enqueue(new Callback<ResponseEntity>() {
                        @Override
                        public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if (response.isSuccessful() && response.body().isSuccess()) {
                                dialog = BaseActivity.openLoadingDialog(getContext());
                                BaseActivity.openSuccessDialog(getContext(), "Xóa sản phẩm thành công!");

                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        // set result để bên fragment reload
                                        callApiGetProducts();
                                    }
                                }, 3000); // 3000 milliseconds = 3 second
                            } else {
                                BaseActivity.openErrorDialog(getContext(), "Không thể xóa sản phẩm");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseEntity> call, Throwable throwable) {
                            // Xử lý lỗi
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            dialog = BaseActivity.openLoadingDialog(getContext());
                            BaseActivity.openErrorDialog(getContext(), "Không thể thêm sản phẩm từ API 2." + throwable.getMessage());

                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    // set result để bên fragment reload
                                    callApiGetProducts();
                                }
                            }, 3000); // 3000 milliseconds = 3 second
                        }
                    });
        }
    }

}