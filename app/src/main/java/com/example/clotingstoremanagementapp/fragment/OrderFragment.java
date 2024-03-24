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
import com.example.clotingstoremanagementapp.adapter.CategoryAdapter;
import com.example.clotingstoremanagementapp.adapter.OrderAdapter;
import com.example.clotingstoremanagementapp.entity.OrderEntity;
import com.example.clotingstoremanagementapp.entity.OrderItemEntity;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private View mView;
    private RecyclerView recyclerView;
    private BaseActivity baseActivity;
    private SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_order, container, false);
        searchView = mView.findViewById(R.id.searchViewOrder);
        recyclerView = mView.findViewById(R.id.rcv_order);
        baseActivity = (BaseActivity) getActivity();
        //set layout manager cho rcv
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        // set adapter cho rcv
        OrderAdapter OrderAdapter = new OrderAdapter(getListOrder());
        recyclerView.setAdapter(OrderAdapter);
        return mView;
    }

    private List<OrderEntity> getListOrder() {
        List<OrderEntity> listOrder = new ArrayList<>();
        List<OrderItemEntity> listItem = new ArrayList<>();
        listItem.add(new OrderItemEntity("Áo sơ mi nữ",2));
        listItem.add(new OrderItemEntity("Áo sơ mi nam",3));
        listOrder.add(new OrderEntity("DH001", "27/03/2024", 135000.00F, "Đang vận chuyển", listItem));
        listOrder.add(new OrderEntity("DH002", "27/03/2024", 138000.00F, "Đang xử lý", listItem));
        return listOrder;
    }
}