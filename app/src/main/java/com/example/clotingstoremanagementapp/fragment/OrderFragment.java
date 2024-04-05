package com.example.clotingstoremanagementapp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.activity.BaseActivity;
import com.example.clotingstoremanagementapp.adapter.OrderAdapter;
import com.example.clotingstoremanagementapp.api.ApiService;
import com.example.clotingstoremanagementapp.entity.OrderEntity;
import com.example.clotingstoremanagementapp.entity.OrderItemEntity;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {

    private View mView;
    private RecyclerView recyclerView;
    private BaseActivity baseActivity;
    private SessionManager sessionManager;
    private OrderAdapter orderAdapter;
    private SearchView searchView;
    private Dialog dialog;
    private List<OrderEntity> orderList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_order, container, false);
        searchView = mView.findViewById(R.id.searchViewOrder);
        recyclerView = mView.findViewById(R.id.rcv_order);
        baseActivity = (BaseActivity) getActivity();
        sessionManager = new SessionManager(baseActivity);
        dialog = BaseActivity.openLoadingDialog(baseActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        callApiGetOrdersByState();
        return mView;
    }

    private void callApiGetOrdersByState() {
        if (sessionManager.isLoggedIn()) {
            String token = sessionManager.getJwt();
            ApiService.apiService.getOrders(token).enqueue(new Callback<List<OrderEntity>>() {
                @Override
                public void onResponse(Call<List<OrderEntity>> call, Response<List<OrderEntity>> response) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    orderList = response.body();
                    updateAdapter();
                    setupSearchViewForOrders();
                }
                @Override
                public void onFailure(Call<List<OrderEntity>> call, Throwable t) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    BaseActivity.openErrorDialog(baseActivity, "Không thể kết nối api");
                }
            });
        }
    }
    private void setupSearchViewForOrders() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (orderAdapter != null) {
                    orderAdapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (orderAdapter != null) {
                    orderAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }
    private void updateAdapter() {
        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);
    }
}
