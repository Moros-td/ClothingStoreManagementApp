package com.example.clotingstoremanagementapp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.activity.BaseActivity;
import com.example.clotingstoremanagementapp.adapter.OrderAdapter;
import com.example.clotingstoremanagementapp.adapter.OrderItemFullInfoAdapter;
import com.example.clotingstoremanagementapp.api.ApiService;
import com.example.clotingstoremanagementapp.entity.OrderEntity;
import com.example.clotingstoremanagementapp.entity.OrderItemEntity;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;
import com.example.clotingstoremanagementapp.response.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailFragment extends Fragment {

    private RecyclerView recyclerView;

    private List<OrderItemEntity> list;

    TextView fullNameTextView, phoneTextView, addressTextView,
            totalPriceTextView, paymentDateTextView, paymentCodeTextView;
    Button cancelButton, deliveringButton, successButton ;
    SessionManager sessionManager;
    Dialog dialog;
    View mView;
private BaseActivity baseActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_order_detail, container, false);
        fullNameTextView = mView.findViewById(R.id.fullNameTextView);
        phoneTextView = mView.findViewById(R.id.phoneTextView);
        addressTextView = mView.findViewById(R.id.addressTextView);
        totalPriceTextView = mView.findViewById(R.id.totalPriceTextView);
        cancelButton = mView.findViewById(R.id.cancelButton);
        deliveringButton = mView.findViewById(R.id.deliveringButton);
        successButton = mView.findViewById(R.id.successButton);
        paymentCodeTextView = mView.findViewById(R.id.paymentCodeTextView);
        paymentDateTextView = mView.findViewById(R.id.paymentDateTextView);
        sessionManager = new SessionManager(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            OrderEntity orderEntity = (OrderEntity) bundle.get("order_entity");
            list = orderEntity.getListOrderItem();

            recyclerView = mView.findViewById(R.id.rcv_order_item);
            baseActivity = (BaseActivity) getContext();

            fullNameTextView.setText(orderEntity.getCustomer().getFullName());
            phoneTextView.setText(orderEntity.getCustomer().getPhone());
            addressTextView.setText(orderEntity.getAddress());
            totalPriceTextView.setText(String.valueOf(orderEntity.getTotalPrice()));

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
            recyclerView.setLayoutManager(linearLayoutManager);

            // Kiểm tra trạng thái của đơn hàng và cập nhật nút tương ứng
            if (orderEntity.getOrderState().equals("pending")) {
                // Nếu đơn hàng đang chờ xử lý, hiển thị cả ba nút
                cancelButton.setVisibility(View.VISIBLE);
                deliveringButton.setVisibility(View.VISIBLE);
                successButton.setVisibility(View.VISIBLE);
            } else if (orderEntity.getOrderState().equals("delivering")) {
                // Nếu đơn hàng đang được giao, chỉ hiển thị nút thanh toán và hủy
                cancelButton.setVisibility(View.VISIBLE);
                deliveringButton.setVisibility(View.GONE);
                successButton.setVisibility(View.VISIBLE);
            }

            OrderItemFullInfoAdapter orderItemFullInfoAdapter = new OrderItemFullInfoAdapter(list);
            recyclerView.setAdapter(orderItemFullInfoAdapter);

            if (orderEntity.getPaymentCode() != null) {
                paymentCodeTextView.setText(orderEntity.getPaymentCode());
                paymentDateTextView.setText(String.valueOf(orderEntity.getPaymentDate()));
            } else {
                paymentCodeTextView.setText("Chưa thanh toán");
                paymentDateTextView.setText("Chưa thanh toán");
            }
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callApiCancelOrder(orderEntity.getOrderCode());
                    Fragment fragment = new OrderFragment();
                    replaceFragment(fragment);
                }
            });
            deliveringButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callApiDeliveringOrder(orderEntity.getOrderCode());
                    Fragment fragment = new OrderFragment();
                    replaceFragment(fragment);
                }
            });
            successButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callApiDeliveredOrder(orderEntity.getOrderCode());
                    Fragment fragment = new OrderFragment();
                    replaceFragment(fragment);
                }
            });
        }
        

        return mView;
    }

    private void callApiDeliveredOrder(String orderCode) {
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            dialog = BaseActivity.openLoadingDialog(getContext());
            ApiService.apiService.deliveredOrder(token, orderCode)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            LoginResponse response1 = response.body();
                            if(response1 != null){
                                if("done".equals(response1.getMessage())){
                                    BaseActivity.openSuccessDialog(getContext(), "Thay đổi trạng thái thanh toán thành công!");
                                }
                                else{
                                    BaseActivity.openErrorDialog(getContext(), response1.getErr());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(getContext(), throwable.getMessage());
                        }
                    });
        }

    }

    private void callApiDeliveringOrder(String orderCode) {
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            dialog = BaseActivity.openLoadingDialog(getContext());
            ApiService.apiService.deliveringOrder(token, orderCode)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            LoginResponse response1 = response.body();
                            if(response1 != null){
                                if("done".equals(response1.getMessage())){
                                    BaseActivity.openSuccessDialog(getContext(), "Thay đổi trạng thái vận chuyển thành công!");
                                }
                                else{
                                    BaseActivity.openErrorDialog(getContext(), response1.getErr());
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(getContext(), throwable.getMessage());
                        }
                    });
        }
    }


    private void callApiCancelOrder(String orderCode) {
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            dialog = BaseActivity.openLoadingDialog(getContext());
            ApiService.apiService.cancelOrder(token, orderCode)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            LoginResponse response1 = response.body();
                            if(response1 != null){
                                if("done".equals(response1.getMessage())){
                                    BaseActivity.openSuccessDialog(getContext(), "Hủy đơn thành công!");
                                }
                                else{
                                    BaseActivity.openErrorDialog(getContext(), response1.getErr());
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(getContext(), throwable.getMessage());
                        }
                    });
        }
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
