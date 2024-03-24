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
import com.example.clotingstoremanagementapp.adapter.OrderAdapter;
import com.example.clotingstoremanagementapp.adapter.OrderHistoryAdapter;
import com.example.clotingstoremanagementapp.entity.OrderEntity;
import com.example.clotingstoremanagementapp.entity.OrderHistoryEntity;
import com.example.clotingstoremanagementapp.entity.OrderHistoryItemEntity;
import com.example.clotingstoremanagementapp.entity.OrderItemEntity;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryFragment extends Fragment {

    private View hView;
    private RecyclerView recyclerView;
    private BaseActivity baseActivity;
    private SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        hView = inflater.inflate(R.layout.fragment_order, container, false);
        searchView = hView.findViewById(R.id.searchViewOrder);
        recyclerView = hView.findViewById(R.id.rcv_order);
        baseActivity = (BaseActivity) getActivity();
        //set layout manager cho rcv
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        // set adapter cho rcv
        OrderHistoryAdapter OrderHistoryAdapter = new OrderHistoryAdapter(getListOrder());
        recyclerView.setAdapter(OrderHistoryAdapter);
        return hView;
    }

    private List<OrderHistoryEntity> getListOrder() {
        List<OrderHistoryEntity> listOrder = new ArrayList<>();
        List<OrderHistoryItemEntity> listItem = new ArrayList<>();
        listItem.add(new OrderHistoryItemEntity("Áo thun nữ",1));
        listItem.add(new OrderHistoryItemEntity("Áo thun nam",3));
        listOrder.add(new OrderHistoryEntity("DH001", "27/03/2024", 145000.00F, "Đã giao", listItem));
        listOrder.add(new OrderHistoryEntity("DH002", "27/03/2024", 186000.00F, "Đang hủy", listItem));
        return listOrder;
    }
}