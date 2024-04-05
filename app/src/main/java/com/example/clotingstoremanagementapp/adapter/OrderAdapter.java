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
import com.example.clotingstoremanagementapp.entity.OrderItemEntity;
import com.example.clotingstoremanagementapp.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> implements Filterable {
    private List<OrderEntity> listOrder;
    private List<OrderEntity> listOrderOld;

    public OrderAdapter(List<OrderEntity> listOrder) {

        this.listOrder = listOrder;
        this.listOrderOld = listOrder;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);

        return new OrderAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        OrderEntity orderEntity = listOrder.get(position);
        if(orderEntity == null){
            return;
        }

        holder.orderCode.setText(orderEntity.getOrderCode());
        List<OrderItemEntity> list = orderEntity.getListOrder();
        StringBuilder productsText = new StringBuilder();
        for (OrderItemEntity item : list) {
            productsText.append(item.getQuantity()).append(" x ").append(item.getProductName()).append("\n");
        }
        holder.productDetail.setText(productsText.toString());
        holder.sate.setText(orderEntity.getState());
        holder.orderDate.setText(orderEntity.getOrderDate());
        holder.totalPrice.setText(String.valueOf(orderEntity.getTotalPrice()));
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
                    List<OrderEntity> list = new ArrayList<>();
                    for (OrderEntity p: listOrderOld) {
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
                listOrder = (List<OrderEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageViewProduct;
        private TextView orderCode, sate, orderDate, productDetail, totalPrice;

        private View viewProductColor;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderCode = itemView.findViewById(R.id.textView_orderCode);
            sate = itemView.findViewById(R.id.textView_state);
            productDetail = itemView.findViewById(R.id.textView_product_detail);
            orderDate = itemView.findViewById(R.id.textView_orderDate);
            totalPrice = itemView.findViewById(R.id.textView_totalPrice);
        }

    }
}
