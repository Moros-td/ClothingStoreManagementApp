package com.example.clotingstoremanagementapp.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.activity.BaseActivity;
import com.example.clotingstoremanagementapp.adapter.ProductAdapter;
import com.example.clotingstoremanagementapp.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private BaseActivity baseActivity;
    private View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_product, container, false);
        searchView = mView.findViewById(R.id.searchViewProduct);
        baseActivity = (BaseActivity) getActivity();
        recyclerView = mView.findViewById(R.id.rcv_product);

        //set layout manager cho rcv
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);

        // set adapter cho rcv
        ProductAdapter productAdapter = new ProductAdapter(getListProduct());
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

        return mView;
    }

    private List<ProductEntity> getListProduct() {
        List<ProductEntity> listP = new ArrayList<>();
        for (int i = 0; i< 10; i++){
            listP.add(new ProductEntity("SP123", "ÁO THUN TRƠN CỔ ĐỨC KHUY NGỌC TRAI " + i, 50, 500.00));
        }
        return listP;
    }
}