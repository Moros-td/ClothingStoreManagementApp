package com.example.clotingstoremanagementapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.entity.ProductEntity;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends  RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    private List<ProductEntity> listProduct;

    public ProductAdapter(List<ProductEntity> listProduct) {
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductEntity productEntity = listProduct.get(position);
        if(productEntity == null){
            return;
        }

        holder.textViewProductCode.setText(productEntity.getProductCode());
        holder.textViewProductName.setText(productEntity.getProductName());
        String priceString = String.format(Locale.getDefault(), "%.2fđ", productEntity.getProductPrice());
        holder.textViewProductPrice.setText(priceString);
        holder.textViewProductQuantity.setText(String.valueOf(productEntity.getProductQuantity()));
    }

    @Override
    public int getItemCount() {
        if(listProduct != null)
            return listProduct.size();
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageViewProduct;
        private TextView textViewProductCode, textViewProductName,
                textViewProductQuantity, textViewProductPrice;

        private View viewProductColor;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewProduct = itemView.findViewById(R.id.imageView_product);
            textViewProductCode = itemView.findViewById(R.id.textView_product_code);
            textViewProductName = itemView.findViewById(R.id.textView_product_name);
            textViewProductQuantity = itemView.findViewById(R.id.textView_product_quantity);
            textViewProductPrice = itemView.findViewById(R.id.textView_product_price);
            viewProductColor = itemView.findViewById(R.id.view_product_color);
        }

    }
}
