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

import com.bumptech.glide.Glide;
import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.entity.OrderHistoryEntity;
import com.example.clotingstoremanagementapp.entity.OrderHistoryItemEntity;
import com.example.clotingstoremanagementapp.entity.OrderItemEntity;

import java.util.List;

public class OrderHistoryItemFullInfoAdapter extends RecyclerView.Adapter<OrderHistoryItemFullInfoAdapter.ProductInOrderHistoryViewHolder> implements Filterable {
    private List<OrderHistoryItemEntity> list;
    @Override
    public Filter getFilter() {
        return null;
    }

    public OrderHistoryItemFullInfoAdapter(List<OrderHistoryItemEntity> list) {
        this.list = list;
    }



    @NonNull
    @Override
    public OrderHistoryItemFullInfoAdapter.ProductInOrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_full_info, parent, false);
        return new OrderHistoryItemFullInfoAdapter.ProductInOrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryItemFullInfoAdapter.ProductInOrderHistoryViewHolder holder, int position) {
        OrderHistoryItemEntity orderItemEntity  = list.get(position);
        if (orderItemEntity == null) {
            return;
        } else {
            holder.productNameTextView.setText(orderItemEntity.getProduct().getProductName());
            holder.quantityTextView.setText(String.valueOf(orderItemEntity.getQuantity()));
            holder.sizeTextView.setText(orderItemEntity.getSize());
            holder.totalPriceTextView.setText(String.valueOf(orderItemEntity.getTotalPrice()));

            //set ảnh
            String path = orderItemEntity.getProduct().getImages().get(0);
            String pathImage = "";
            if (path != null && path.length() > 1) {
                String newPath = path.substring(1);
                pathImage = "http://10.0.2.2:8097"+newPath;
            }
            Glide.with(holder.productImageView).load(pathImage).into(holder.productImageView);

            // set màu
            if(orderItemEntity.getProduct().getProductColor().equals("red")){
                holder.colorView.setBackgroundResource(R.drawable.circle_background_red);
            }
            else if(orderItemEntity.getProduct().getProductColor().equals("pink")){
                holder.colorView.setBackgroundResource(R.drawable.circle_background_pink);
            }
            else if(orderItemEntity.getProduct().getProductColor().equals("yellow")){
                holder.colorView.setBackgroundResource(R.drawable.circle_background_yellow);
            }
            else if(orderItemEntity.getProduct().getProductColor().equals("green")){
                holder.colorView.setBackgroundResource(R.drawable.circle_backgound_green);
            }
            else if(orderItemEntity.getProduct().getProductColor().equals("blue")){
                holder.colorView.setBackgroundResource(R.drawable.circle_background_blue);
            }
            else if(orderItemEntity.getProduct().getProductColor().equals("beige")){
                holder.colorView.setBackgroundResource(R.drawable.cirlce_background_beige);
            }
            else if(orderItemEntity.getProduct().getProductColor().equals("white")){
                holder.colorView.setBackgroundResource(R.drawable.circle_background_white);
            }
            else if(orderItemEntity.getProduct().getProductColor().equals("black")){
                holder.colorView.setBackgroundResource(R.drawable.circle_backgound_black);
            }
            else if(orderItemEntity.getProduct().getProductColor().equals("brown")){
                holder.colorView.setBackgroundResource(R.drawable.circle_background_brown);
            }
            else if(orderItemEntity.getProduct().getProductColor().equals("gray")){
                holder.colorView.setBackgroundResource(R.drawable.circle_background_gray);
            }
            // sự kiện các nút
//            if(iClickItemCartListener != null){
//            }
        }
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public class ProductInOrderHistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView
                productNameTextView, quantityTextView, sizeTextView, totalPriceTextView;
        private ImageView productImageView;
        private View colorView;

        public ProductInOrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            sizeTextView = itemView.findViewById(R.id.sizeTextView);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
            productImageView = itemView.findViewById(R.id.productImageView);
            colorView = itemView.findViewById(R.id.colorView);
        }
    }
}
