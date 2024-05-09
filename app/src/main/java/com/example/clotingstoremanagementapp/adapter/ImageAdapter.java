package com.example.clotingstoremanagementapp.adapter;

import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<Uri> listImage;
    private List<String> listImageString;
    public ImageAdapter(List<Uri> listImage, List<String> listImageString) {

        //this.listProduct = listProduct;
        this.listImage = listImage;
        this.listImageString = listImageString;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if(listImage == null && listImageString == null){
            return;
        }
        if(listImage!= null){
            Uri uri = listImage.get(position);

            //set ảnh
            Glide.with(holder.imageViewAddProduct).load(uri).into(holder.imageViewAddProduct);
        }
        if(listImageString != null){
            String path = listImageString.get(position);

            String pathImage = "";
            if (path != null && path.length() > 1) {
                String newPath = path.substring(1);
                pathImage = "http://10.0.2.2:8097"+newPath;
            }
            Glide.with(holder.imageViewAddProduct).load(pathImage).into(holder.imageViewAddProduct);
        }

    }

    public int getItemCount() {
        if(listImage != null)
            return listImage.size();
        if(listImageString != null)
            return listImageString.size();
        return 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageViewAddProduct;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewAddProduct = itemView.findViewById(R.id.imageView_Add_Product);
        }

    }
}
