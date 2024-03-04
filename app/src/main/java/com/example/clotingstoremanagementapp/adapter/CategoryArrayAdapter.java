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

import java.util.List;

public class CategoryArrayAdapter extends ArrayAdapter<CategoryEntity> {

    Context context;
    int res;
    List<CategoryEntity> data;
    public CategoryArrayAdapter(@NonNull Context context, int resource, @NonNull List<CategoryEntity> data) {
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
        CategoryEntity category = data.get(position);
        if(category != null){
            parentCategoryNameTxtView.setText(category.getCategoryName());
            parentCategoryIdTxtView.setText(String .valueOf(category.getId()));
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_parent_category, parent, false);
        TextView parentCategoryNameTxtView = convertView.findViewById(R.id.parentCategoryNameTxtView);
        TextView parentCategoryIdTxtView = convertView.findViewById(R.id.parentCategoryIdTxtView);
        CategoryEntity category = data.get(position);
        if(category != null){
            parentCategoryNameTxtView.setText(category.getCategoryName());
            parentCategoryIdTxtView.setText(String .valueOf(category.getId()));
        }
        return convertView;
    }
}
