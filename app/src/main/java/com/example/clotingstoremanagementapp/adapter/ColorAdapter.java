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

public class ColorAdapter extends ArrayAdapter<String> {

    Context context;

    int resource;
    int res;
    List<String> data;
    public ColorAdapter(@NonNull Context context, int resource, @NonNull List<String> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(this.resource, parent, false);
        TextView roleTextView = convertView.findViewById(R.id.roleTextView);
        String color = data.get(position);
        if(color != null){
            roleTextView.setText(color);
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_role, parent, false);
        TextView colorTextView = convertView.findViewById(R.id.roleTextView);
        String color = data.get(position);
        if(color != null){
            colorTextView.setText(color);
        }
        return convertView;
    }
}
