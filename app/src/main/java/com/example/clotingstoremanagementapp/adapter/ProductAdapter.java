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
import com.example.clotingstoremanagementapp.custom_interface.IClickItemCategoryListener;
import com.example.clotingstoremanagementapp.custom_interface.IClickItemProductListener;
import com.example.clotingstoremanagementapp.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {

    private List<ProductEntity> listProduct;
    private List<ProductEntity> listProductOld;
    private IClickItemProductListener iClickItemProductListener;
    public ProductAdapter(List<ProductEntity> listProduct, IClickItemProductListener listener ) {
        this.iClickItemProductListener = listener;
        this.listProduct = listProduct;
        this.listProductOld = listProduct;
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
        String path = productEntity.getImages().get(0);

        String pathImage = "";
        if (path != null && path.length() > 1) {
            String newPath = path.substring(1);
            pathImage = "http://10.0.2.2:8097"+newPath;
        }
        Glide.with(holder.imageViewProduct).load(pathImage).into(holder.imageViewProduct);

        // set màu
        if(productEntity.getProductColor().equals("red")){
            holder.viewProductColor.setBackgroundResource(R.drawable.circle_background_red);
        }
        else if(productEntity.getProductColor().equals("pink")){
            holder.viewProductColor.setBackgroundResource(R.drawable.circle_background_pink);
        }
        else if(productEntity.getProductColor().equals("yellow")){
            holder.viewProductColor.setBackgroundResource(R.drawable.circle_background_yellow);
        }
        else if(productEntity.getProductColor().equals("green")){
            holder.viewProductColor.setBackgroundResource(R.drawable.circle_backgound_green);
        }
        else if(productEntity.getProductColor().equals("blue")){
            holder.viewProductColor.setBackgroundResource(R.drawable.circle_background_blue);
        }
        else if(productEntity.getProductColor().equals("beige")){
            holder.viewProductColor.setBackgroundResource(R.drawable.cirlce_background_beige);
        }
        else if(productEntity.getProductColor().equals("white")){
            holder.viewProductColor.setBackgroundResource(R.drawable.circle_background_white);
        }
        else if(productEntity.getProductColor().equals("black")){
            holder.viewProductColor.setBackgroundResource(R.drawable.circle_backgound_black);
        }
        else if(productEntity.getProductColor().equals("brown")){
            holder.viewProductColor.setBackgroundResource(R.drawable.circle_background_brown);
        }
        else if(productEntity.getProductColor().equals("gray")){
            holder.viewProductColor.setBackgroundResource(R.drawable.circle_background_gray);
        }

        if(iClickItemProductListener != null){
            holder.imageView_productEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("oke", "onClick: ");
                    iClickItemProductListener.onClickEditProduct(productEntity);
                }
            });

            holder.imageView_productDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickItemProductListener.onClickDeleteProduct(productEntity);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(listProduct != null)
            return listProduct.size();
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
                    listProduct = listProductOld;
                }
                else{

                    // tìm các phần tử cùng tên sản phẩm add vào list
                    List<ProductEntity> list = new ArrayList<>();
                    for (ProductEntity p: listProductOld) {
                        if(p.getProductName().toLowerCase().contains(searchStr.toLowerCase())){
                            list.add(p);
                        }
                    }

                    listProduct = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listProduct;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listProduct = (List<ProductEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageViewProduct,imageView_productEdit,imageView_productDelete;
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
            imageView_productEdit = itemView.findViewById(R.id.imageView_edit_product);
            imageView_productDelete= itemView.findViewById(R.id.imageView_delete_product);

        }

    }
}
