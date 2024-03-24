package com.example.clotingstoremanagementapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.entity.OrderEntity;
import com.example.clotingstoremanagementapp.entity.OrderHistoryEntity;
import com.example.clotingstoremanagementapp.entity.OrderHistoryItemEntity;
import com.example.clotingstoremanagementapp.entity.OrderItemEntity;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> implements Filterable {
    private List<OrderHistoryEntity> listOrder;
    private List<OrderHistoryEntity> listOrderOld;

    public OrderHistoryAdapter(List<OrderHistoryEntity> listOrder) {

        this.listOrder = listOrder;
        this.listOrderOld = listOrder;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);

        return new OrderHistoryAdapter.OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.OrderHistoryViewHolder holder, int position) {
        OrderHistoryEntity orderHistoryEntity = listOrder.get(position);
        if(orderHistoryEntity == null){
            return;
        }

        holder.orderCode.setText(orderHistoryEntity.getOrderCode());
        List<OrderHistoryItemEntity> list = orderHistoryEntity.getListOrder();
        StringBuilder productsText = new StringBuilder();
        for (OrderHistoryItemEntity item : list) {
            productsText.append(item.getQuantity()).append(" x ").append(item.getProductName()).append("\n");
        }
        holder.productDetail.setText(productsText.toString());
        holder.sate.setText(orderHistoryEntity.getState());
        holder.orderDate.setText(orderHistoryEntity.getOrderDate());
        holder.totalPrice.setText(String.valueOf(orderHistoryEntity.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        if(listOrder != null)
            return listOrder.size();
        return 0;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchStr = constraint.toString();

                // nếu chuỗi tim kiếm trống
                if(searchStr.isEmpty()){
                    listOrder = listOrderOld;
                }
                else{

                    // tìm các phần tử cùng tên sản phẩm add vào list
                    List<OrderHistoryEntity> list = new ArrayList<>();
                    for (OrderHistoryEntity p: listOrderOld) {
                        if(p.getOrderCode().toLowerCase().contains(searchStr.toLowerCase())){
                            list.add(p);
                        }
                    }

                    listOrder = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listOrder;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listOrder = (List<OrderHistoryEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageViewProduct;
        private TextView orderCode, sate, orderDate, productDetail, totalPrice;

        private View viewProductColor;
        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            orderCode = itemView.findViewById(R.id.textView_orderCode);
            sate = itemView.findViewById(R.id.textView_state);
            productDetail = itemView.findViewById(R.id.textView_product_detail);
            orderDate = itemView.findViewById(R.id.textView_orderDate);
            totalPrice = itemView.findViewById(R.id.textView_totalPrice);
        }

    }
}

