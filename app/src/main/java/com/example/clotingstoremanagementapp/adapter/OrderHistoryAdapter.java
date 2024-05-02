package com.example.clotingstoremanagementapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.activity.BaseActivity;
import com.example.clotingstoremanagementapp.custom_interface.IClickItemOrderHistoryListener;
import com.example.clotingstoremanagementapp.entity.OrderEntity;
import com.example.clotingstoremanagementapp.entity.OrderHistoryEntity;
import com.example.clotingstoremanagementapp.entity.OrderHistoryItemEntity;
import com.example.clotingstoremanagementapp.entity.OrderItemEntity;
import com.example.clotingstoremanagementapp.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> implements Filterable {

    private BaseActivity baseActivity;
    private IClickItemOrderHistoryListener IClickItemOrderHistoryListener;
    private List<OrderHistoryEntity> list;

    @Override
    public Filter getFilter() {
        return null;
    }

    public OrderHistoryAdapter(List<OrderHistoryEntity> list, IClickItemOrderHistoryListener listener) {
        this.list = list;
        IClickItemOrderHistoryListener = listener;
    }



    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderHistoryAdapter.OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        OrderHistoryEntity orderEntity = list.get(position);

        if (orderEntity == null) {
            return;
        } else {
            holder.orderCodeTextView.setText(orderEntity.getOrderCode());
            holder.orderDateTextView.setText(String.valueOf(orderEntity.getOrderDate()));

            if(orderEntity.getOrderState().equals("cancelled")){
                holder.orderStateTextView.setText("Đã hủy");
                holder.orderStateTextView.setTextColor(Color.parseColor("#ab1a13"));
            }
            else{
                holder.orderStateTextView.setText("Giao hàng thành công");
                holder.orderStateTextView.setTextColor(Color.parseColor("#42ad2a"));
            }

            holder.totalOrderPriceTextView.setText(String.valueOf(orderEntity.getTotalPrice()));

            List<OrderHistoryItemEntity> listOrderItem = orderEntity.getListOrderItem();

            OrderHistoryItemLiteAdapter orderItemLiteAdapter = new OrderHistoryItemLiteAdapter(listOrderItem);
            holder.productRecyclerView.setLayoutManager(new LinearLayoutManager(baseActivity));
            holder.productRecyclerView.setAdapter(orderItemLiteAdapter);

            if(IClickItemOrderHistoryListener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IClickItemOrderHistoryListener.onClickOrderHistory(orderEntity);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView orderCodeTextView, orderStateTextView, orderDateTextView, totalOrderPriceTextView;
        private RecyclerView productRecyclerView;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            orderCodeTextView = itemView.findViewById(R.id.orderCodeTextView);
            orderStateTextView = itemView.findViewById(R.id.orderStateTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            totalOrderPriceTextView = itemView.findViewById(R.id.totalOrderPriceTextView);
            productRecyclerView = itemView.findViewById(R.id.rcv_product_in_order);
        }
    }
}

