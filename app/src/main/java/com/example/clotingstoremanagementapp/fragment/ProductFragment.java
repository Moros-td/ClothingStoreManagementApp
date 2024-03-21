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
import com.example.clotingstoremanagementapp.activity.CategoryInfoActivity;
import com.example.clotingstoremanagementapp.activity.ProductInfoActitvity;
import com.example.clotingstoremanagementapp.adapter.ProductAdapter;
import com.example.clotingstoremanagementapp.custom_interface.IClickItemProductListener;
import com.example.clotingstoremanagementapp.entity.ProductEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
public class ProductFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private BaseActivity baseActivity;
    private View mView;

    private FloatingActionButton fab_product;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_product, container, false);
        searchView = mView.findViewById(R.id.searchViewProduct);
        baseActivity = (BaseActivity) getActivity();
        recyclerView = mView.findViewById(R.id.rcv_product);
        fab_product = mView.findViewById(R.id.fab_product);

        //set layout manager cho rcv
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);

        // set adapter cho rcv
        ProductAdapter productAdapter = new ProductAdapter(getListProduct(), new IClickItemProductListener() {

            @Override
            public void onClickEditProduct(ProductEntity product) {
                replaceActivityAndMoveData(product);
            }

            @Override
            public void onClickDeleteProduct(ProductEntity product) {
                openConfirmDialog(product);
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
        fab_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductInfoActitvity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("hideProductId", true);
                intent.putExtras(bundle); // Thêm dữ liệu để ẩn TextView
                startActivity(intent);
            }
        });
        return mView;
    }

    private List<ProductEntity> getListProduct() {
        List<ProductEntity> listP = new ArrayList<>();
        for (int i = 0; i< 10; i++){
            listP.add(new ProductEntity(i,"SP123", "ÁO THUN TRƠN CỔ ĐỨC KHUY NGỌC TRAI " + i, 50, 500.00,"Mô tả rỗng"));
        }
        return listP;
    }
    public void replaceActivityAndMoveData(ProductEntity product){
        Intent intent = new Intent(getActivity(), ProductInfoActitvity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product_entity",product);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void openConfirmDialog(ProductEntity product) {
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

        confirmTextView.setText("Bạn có chắn rằng muốn xóa sản phẩm này không?");

        //set event
        deleteConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                BaseActivity.openErrorDialog(getContext(), "Xóa sản phẩm thành công!");
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

}