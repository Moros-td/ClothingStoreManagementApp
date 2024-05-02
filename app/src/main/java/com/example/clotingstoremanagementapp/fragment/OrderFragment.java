package com.example.clotingstoremanagementapp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.activity.BaseActivity;
import com.example.clotingstoremanagementapp.adapter.OrderAdapter;
import com.example.clotingstoremanagementapp.api.ApiService;
import com.example.clotingstoremanagementapp.custom_interface.IClickItemOrderListener;
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
    private List<OrderEntity> list;

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
        callApiGetAllOrders();
        return mView;
    }

    public void replaceFragmentAndMoveData(OrderEntity orderEntity){
        Bundle bundle = new Bundle();
        bundle.putSerializable("order_entity", orderEntity);
    }
    private void callApiGetAllOrders() {

        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
//            String email = sessionManager.getCustom("email");
//            dialog = BaseActivity.openLoadingDialog(getContext());

            ApiService.apiService.getAllOrders(token)
                    .enqueue(new Callback<List<OrderEntity>>() {
                        @Override
                        public void onResponse(Call<List<OrderEntity>> call, Response<List<OrderEntity>> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            list = response.body();
                            OrderAdapter orderAdapter = new OrderAdapter(list, new IClickItemOrderListener() {
                                @Override
                                public void onClickOrder(OrderEntity orderEntity) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("order_entity", orderEntity);
                                    Fragment fragment = new OrderDetailFragment();
                                    fragment.setArguments(bundle);

                                    FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.container, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            });
                            recyclerView.setAdapter(orderAdapter);
                        }

                        @Override
                        public void onFailure(Call<List<OrderEntity>> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(getContext(), throwable.getMessage());
                        }
                    });
        }
    }
}
