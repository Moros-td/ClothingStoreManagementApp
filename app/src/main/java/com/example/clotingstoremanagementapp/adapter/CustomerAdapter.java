package com.example.clotingstoremanagementapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.entity.CustomerEntity;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> implements Filterable {

    private List<CustomerEntity> listCustomer;
    private List<CustomerEntity> listCustomerOld;

    public CustomerAdapter(List<CustomerEntity> listCustomer) {

        this.listCustomer = listCustomer;
        this.listCustomerOld = listCustomer;
    }

    @NonNull
    @Override
    public CustomerAdapter.CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);

        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.CustomerViewHolder holder, int position) {
        CustomerEntity customerEntity = listCustomer.get(position);
        if(customerEntity == null){
            return;
        }

        holder.textView_fullName.setText(customerEntity.getFullName());
        holder.textView_phone.setText(customerEntity.getPhone());
        holder.textView_email.setText(customerEntity.getEmail());
        holder.textView_address.setText(customerEntity.getAddress());
    }

    @Override
    public int getItemCount() {
        if(listCustomer != null)
            return listCustomer.size();
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
                    listCustomer = listCustomerOld;
                }
                else{

                    // tìm các phần tử cùng tên khách hàng add vào list
                    List<CustomerEntity> list = new ArrayList<>();
                    for (CustomerEntity p: listCustomerOld) {
                        if(p.getFullName().toLowerCase().contains(searchStr.toLowerCase())){
                            list.add(p);
                        }
                    }

                    listCustomer = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listCustomer;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listCustomer = (List<CustomerEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder{

        private TextView textView_fullName, textView_phone, textView_email, textView_address;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_fullName = itemView.findViewById(R.id.textView_fullName);
            textView_email = itemView.findViewById(R.id.textView_email);
            textView_phone = itemView.findViewById(R.id.textView_phone);
            textView_address = itemView.findViewById(R.id.textView_address);

        }

    }
}
