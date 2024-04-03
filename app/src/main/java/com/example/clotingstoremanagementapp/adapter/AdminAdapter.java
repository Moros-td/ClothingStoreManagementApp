package com.example.clotingstoremanagementapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.custom_interface.IClickItemAdminListener;
import com.example.clotingstoremanagementapp.custom_interface.IClickItemCategoryListener;
import com.example.clotingstoremanagementapp.entity.AdminEntity;
import com.example.clotingstoremanagementapp.entity.CategoryEntity;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> implements Filterable {
    private List<AdminEntity> listAdmin;
    private List<AdminEntity> listAdminOld;
    private IClickItemAdminListener iClickItemAdminListener;

    private String role;

    public AdminAdapter(List<AdminEntity> listAdmin, String role, IClickItemAdminListener listener) {
        this.iClickItemAdminListener = listener;
        this.listAdmin = listAdmin;
        this.listAdminOld = listAdmin;
        this.role = role;
    }
    @NonNull
    @Override
    public AdminAdapter.AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff, parent, false);

        return new AdminAdapter.AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.AdminViewHolder holder, int position) {
        AdminEntity adminEntity = listAdmin.get(position);
        if(adminEntity == null){
            return;
        }

        holder.textViewUsername.setText(adminEntity.getUsername());
        holder.textViewRole.setText(adminEntity.getRole());

        // nếu khác admin thì không hiển thị
        if(!role.equals("admin")){
            holder.imageView_edit_staff.setVisibility(View.GONE);
            holder.imageView_delete_staff.setVisibility(View.GONE);
            holder.buttonResetPassword.setVisibility(View.GONE);
        }

        // nếu login hiện tại là admin thì mới hiển thị
        if(role.equals("admin")){
            if(iClickItemAdminListener != null){

                if(adminEntity.getRole().equals("admin")){
                    holder.imageView_edit_staff.setVisibility(View.GONE);
                    holder.imageView_delete_staff.setVisibility(View.GONE);
                    holder.buttonResetPassword.setVisibility(View.GONE);
                }
                holder.imageView_edit_staff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.d("oke", "onClick: ");
                        iClickItemAdminListener.onClickEditStaff(adminEntity);
                    }
                });


                holder.imageView_delete_staff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iClickItemAdminListener.onClickDeleteStaff(adminEntity);
                    }
                });

                holder.buttonResetPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iClickItemAdminListener.onClickResetPasswordStaff(adminEntity);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        if(listAdmin != null)
            return listAdmin.size();
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
                    listAdmin = listAdminOld;
                }
                else{

                    // tìm các phần tử cùng tên category add vào list
                    List<AdminEntity> list = new ArrayList<>();
                    for (AdminEntity p: listAdminOld) {
                        if(p.getUsername().toLowerCase().contains(searchStr.toLowerCase())){
                            list.add(p);
                        }
                    }

                    listAdmin = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listAdmin;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listAdmin = (List<AdminEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUsername, textViewRole;
        private Button buttonResetPassword;
        private ImageView imageView_edit_staff, imageView_delete_staff;
        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewRole = itemView.findViewById(R.id.textViewRole);
            buttonResetPassword = itemView.findViewById(R.id.buttonResetPassword);
            imageView_edit_staff = itemView.findViewById(R.id.imageView_edit_staff);
            imageView_delete_staff = itemView.findViewById(R.id.imageView_delete_staff);
        }
    }
}
