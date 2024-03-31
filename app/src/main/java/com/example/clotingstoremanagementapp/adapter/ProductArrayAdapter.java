package com.example.clotingstoremanagementapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.entity.CategoryEntity;
import com.example.clotingstoremanagementapp.entity.ProductEntity;

import java.util.List;

public class ProductArrayAdapter extends ArrayAdapter<ProductEntity> {
    private Context context;
    private int res;
    private List<ProductEntity> data;
    public ProductArrayAdapter(@NonNull Context context, int resource, @NonNull List<ProductEntity> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
        this.res = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_paren_category_selected, parent, false);
        TextView parentCategoryNameTxtView = convertView.findViewById(R.id.parentCategoryNameTxtView);
        TextView parentCategoryIdTxtView = convertView.findViewById(R.id.parentCategoryIdTxtView);
        ProductEntity product = data.get(position);
        if(product != null){
            parentCategoryNameTxtView.setText(product.getProductName());
            //parentCategoryIdTxtView.setText(String .valueOf(product.getId()));
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_parent_category, parent, false);
        TextView parentCategoryNameTxtView = convertView.findViewById(R.id.parentCategoryNameTxtView);
        TextView parentCategoryIdTxtView = convertView.findViewById(R.id.parentCategoryIdTxtView);
        ProductEntity product = data.get(position);
        if(product != null){
            parentCategoryNameTxtView.setText(product.getProductName());
            //parentCategoryIdTxtView.setText(String .valueOf(product.getId()));
        }
        return convertView;
    }
}
