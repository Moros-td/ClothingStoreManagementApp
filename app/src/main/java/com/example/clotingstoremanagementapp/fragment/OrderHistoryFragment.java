package com.example.clotingstoremanagementapp.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.activity.BaseActivity;
import com.example.clotingstoremanagementapp.adapter.OrderAdapter;
import com.example.clotingstoremanagementapp.adapter.OrderHistoryAdapter;
import com.example.clotingstoremanagementapp.api.ApiService;
import com.example.clotingstoremanagementapp.custom_interface.IClickItemOrderHistoryListener;
import com.example.clotingstoremanagementapp.custom_interface.IClickItemOrderListener;
import com.example.clotingstoremanagementapp.entity.OrderEntity;
import com.example.clotingstoremanagementapp.entity.OrderHistoryEntity;
import com.example.clotingstoremanagementapp.entity.OrderHistoryItemEntity;
import com.example.clotingstoremanagementapp.entity.OrderItemEntity;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryFragment extends Fragment {

    private View mView;
    private RecyclerView recyclerView;
    private BaseActivity baseActivity;
    private SessionManager sessionManager;
    private OrderAdapter orderAdapter;
    private SearchView searchView;
    private Dialog dialog;
    private List<OrderHistoryEntity> list;

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

    public void replaceFragmentAndMoveData(OrderHistoryEntity orderEntity){
        Bundle bundle = new Bundle();
        bundle.putSerializable("order_entity", orderEntity);
    }
    private void callApiGetAllOrders() {

        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
//            String email = sessionManager.getCustom("email");
//            dialog = BaseActivity.openLoadingDialog(getContext());

            ApiService.apiService.getAllOrdersHistory(token)
                    .enqueue(new Callback<List<OrderHistoryEntity>>() {
                        @Override
                        public void onResponse(Call<List<OrderHistoryEntity>> call, Response<List<OrderHistoryEntity>> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            list = response.body();
                            OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(list, new IClickItemOrderHistoryListener() {
                                @Override
                                public void onClickOrderHistory(OrderHistoryEntity orderHistoryItemEntity) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("order_entity", orderHistoryItemEntity);
                                    Fragment fragment = new OrderHistoryDetailFragment();
                                    fragment.setArguments(bundle);

                                    FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.container, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            });
                            recyclerView.setAdapter(orderHistoryAdapter);
                        }

                        @Override
                        public void onFailure(Call<List<OrderHistoryEntity>> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(getContext(), throwable.getMessage());
                        }
                    });
        }
    }
}