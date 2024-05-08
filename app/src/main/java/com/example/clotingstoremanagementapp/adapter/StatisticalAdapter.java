package com.example.clotingstoremanagementapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.entity.Statistical;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class StatisticalAdapter extends ArrayAdapter<Statistical> {
    public StatisticalAdapter(Context context, List<Statistical> orders) {
        super(context, 0, orders);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Statistical order = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView orderNumberTextView = convertView.findViewById(R.id.orderNumberTextView);
        TextView orderCodeTextView = convertView.findViewById(R.id.orderCodeTextView);
        TextView orderDateTextView = convertView.findViewById(R.id.orderDateTextView);
        TextView orderPaymentTextView = convertView.findViewById(R.id.orderPaymentTextView);

        orderNumberTextView.setText(String.valueOf(position + 1));
        orderCodeTextView.setText(order.getOrderCode());
        orderCodeTextView.setText(order.getOrderCode());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String orderDateStr = format.format(order.getOrderDate());
        orderDateTextView.setText(orderDateStr);
        orderPaymentTextView.setText(order.getTotalPrice().toString());

        return convertView;
    }
}
