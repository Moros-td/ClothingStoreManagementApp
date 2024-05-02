package com.example.clotingstoremanagementapp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.activity.BaseActivity;
import com.example.clotingstoremanagementapp.adapter.OrderHistoryItemFullInfoAdapter;
import com.example.clotingstoremanagementapp.adapter.OrderItemFullInfoAdapter;
import com.example.clotingstoremanagementapp.entity.OrderEntity;
import com.example.clotingstoremanagementapp.entity.OrderHistoryEntity;
import com.example.clotingstoremanagementapp.entity.OrderHistoryItemEntity;
import com.example.clotingstoremanagementapp.entity.OrderItemEntity;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;

import java.util.List;

public class OrderHistoryDetailFragment extends Fragment {
    private RecyclerView recyclerView;

    private List<OrderHistoryItemEntity> list;

    TextView fullNameTextView, phoneTextView, addressTextView,
            totalPriceTextView, paymentDateTextView, paymentCodeTextView;

    SessionManager sessionManager;
    Dialog dialog;
    View mView;
    private BaseActivity baseActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_order_history_detail, container, false);
        fullNameTextView = mView.findViewById(R.id.fullNameTextView);
        phoneTextView = mView.findViewById(R.id.phoneTextView);
        addressTextView = mView.findViewById(R.id.addressTextView);
        totalPriceTextView = mView.findViewById(R.id.totalPriceTextView);
        paymentCodeTextView = mView.findViewById(R.id.paymentCodeTextView);
        paymentDateTextView = mView.findViewById(R.id.paymentDateTextView);
        sessionManager = new SessionManager(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            OrderHistoryEntity orderHistoryEntity = (OrderHistoryEntity) bundle.get("order_entity");
            list = orderHistoryEntity.getListOrderItem();

            recyclerView = mView.findViewById(R.id.rcv_order_item);
            baseActivity = (BaseActivity) getContext();

            fullNameTextView.setText(orderHistoryEntity.getCustomer().getFullName());
            phoneTextView.setText(orderHistoryEntity.getCustomer().getPhone());
            addressTextView.setText(orderHistoryEntity.getAddress());
            totalPriceTextView.setText(String.valueOf(orderHistoryEntity.getTotalPrice()));

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
            recyclerView.setLayoutManager(linearLayoutManager);

            OrderHistoryItemFullInfoAdapter orderHistoryItemFullInfoAdapter = new OrderHistoryItemFullInfoAdapter(list);
            recyclerView.setAdapter(orderHistoryItemFullInfoAdapter);

        }
        return mView;
    }
}
