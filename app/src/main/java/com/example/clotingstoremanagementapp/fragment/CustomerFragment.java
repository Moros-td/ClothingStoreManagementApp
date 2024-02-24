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
import com.example.clotingstoremanagementapp.adapter.CustomerAdapter;
import com.example.clotingstoremanagementapp.adapter.ProductAdapter;
import com.example.clotingstoremanagementapp.entity.CustomerEntity;
import com.example.clotingstoremanagementapp.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class CustomerFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private BaseActivity baseActivity;
    private View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_customer, container, false);
        searchView = mView.findViewById(R.id.searchViewCustomer);
        baseActivity = (BaseActivity) getActivity();
        recyclerView = mView.findViewById(R.id.rcv_customer);

        //set layout manager cho rcv
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);

        // set adapter cho rcv
        CustomerAdapter customerAdapter = new CustomerAdapter(getListCustomer());
        recyclerView.setAdapter(customerAdapter);

        // sự kiện cho search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                customerAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return mView;
    }
    private List<CustomerEntity> getListCustomer() {
        List<CustomerEntity> list = new ArrayList<>();
        for (int i = 0; i< 10; i++){
            list.add(new CustomerEntity("Nguyễn Văn Tèo" + i, "abc@gmail.com", "012345", "10/15/15, Phước Long B, Thủ Đức, TPHCM"));
        }
        return list;
    }
}