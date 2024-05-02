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
import com.example.clotingstoremanagementapp.custom_interface.IClickItemOrderListener;
import com.example.clotingstoremanagementapp.entity.OrderEntity;
import com.example.clotingstoremanagementapp.entity.OrderItemEntity;
import com.example.clotingstoremanagementapp.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> implements Filterable {

    private BaseActivity baseActivity;
    private IClickItemOrderListener iClickItemOrderListener;
    private List<OrderEntity> list;

    @Override
    public Filter getFilter() {
        return null;
    }

    public OrderAdapter(List<OrderEntity> list, IClickItemOrderListener listener) {
        this.list = list;
        iClickItemOrderListener = listener;
    }



    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderEntity orderEntity = list.get(position);

        if (orderEntity == null) {
            return;
        } else {
            holder.orderCodeTextView.setText(orderEntity.getOrderCode());
            holder.orderDateTextView.setText(String.valueOf(orderEntity.getOrderDate()));

            if(orderEntity.getOrderState().equals("pending")){
                holder.orderStateTextView.setText("Đang xử lý");
                holder.orderStateTextView.setTextColor(Color.parseColor("#f5e642"));
            }
            else if(orderEntity.getOrderState().equals("delivering")){
                holder.orderStateTextView.setText("Đang vận chuyển");
                holder.orderStateTextView.setTextColor(Color.parseColor("#2933f0"));
            }

            holder.totalOrderPriceTextView.setText(String.valueOf(orderEntity.getTotalPrice()));

            List<OrderItemEntity> listOrderItem = orderEntity.getListOrderItem();

            OrderItemLiteAdapter orderItemLiteAdapter = new OrderItemLiteAdapter(listOrderItem);
            holder.productRecyclerView.setLayoutManager(new LinearLayoutManager(baseActivity));
            holder.productRecyclerView.setAdapter(orderItemLiteAdapter);

            if(iClickItemOrderListener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iClickItemOrderListener.onClickOrder(orderEntity);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderCodeTextView, orderStateTextView, orderDateTextView, totalOrderPriceTextView;
        private RecyclerView productRecyclerView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderCodeTextView = itemView.findViewById(R.id.orderCodeTextView);
            orderStateTextView = itemView.findViewById(R.id.orderStateTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            totalOrderPriceTextView = itemView.findViewById(R.id.totalOrderPriceTextView);
            productRecyclerView = itemView.findViewById(R.id.rcv_product_in_order);
        }
    }
}
