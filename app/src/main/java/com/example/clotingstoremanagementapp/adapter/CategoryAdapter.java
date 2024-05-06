package com.example.clotingstoremanagementapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.custom_interface.IClickItemCategoryListener;
import com.example.clotingstoremanagementapp.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> implements Filterable {

    private List<CategoryEntity> listCategory;
    private List<CategoryEntity> listCategoryOld;

    private IClickItemCategoryListener iClickItemCategoryListener;
    private String role;

    public CategoryAdapter(List<CategoryEntity> listCategory, String role, IClickItemCategoryListener listener) {
        this.iClickItemCategoryListener = listener;
        this.listCategory = listCategory;
        this.listCategoryOld = listCategory;
        this.role = role;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        return new CategoryAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        CategoryEntity categoryEntity = listCategory.get(position);
        if(categoryEntity == null){
            return;
        }

        holder.textView_categoryName.setText(categoryEntity.getCategoryName());

        String parentName = "None";
        if(categoryEntity.getCategoryParent() != null){
            if(categoryEntity.getCategoryParent().getCategoryName() != null){
                parentName = categoryEntity.getCategoryParent().getCategoryName();
            }
        }

        holder.textView_categoryParen.setText(parentName);
        holder.textView_categoryId.setText(String.valueOf(categoryEntity.getId()));
        // sự kiện click nút

        if("staff".equals(this.role)){
            holder.imageView_categoryEdit.setVisibility(View.GONE);
            holder.imageView_categoryDelete.setVisibility(View.GONE);
        }
        if(iClickItemCategoryListener != null){
            holder.imageView_categoryEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("oke", "onClick: ");
                    iClickItemCategoryListener.onClickEditCategory(categoryEntity);
                }
            });

            holder.imageView_categoryDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickItemCategoryListener.onClickDeleteCategory(categoryEntity);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(listCategory != null)
            return listCategory.size();
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
                    listCategory = listCategoryOld;
                }
                else{

                    // tìm các phần tử cùng tên category add vào list
                    List<CategoryEntity> list = new ArrayList<>();
                    for (CategoryEntity p: listCategoryOld) {
                        if(p.getCategoryName().toLowerCase().contains(searchStr.toLowerCase())){
                            list.add(p);
                        }
                    }

                    listCategory = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listCategory;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listCategory = (List<CategoryEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_categoryName, textView_categoryParen, textView_categoryId;
        private ImageView imageView_categoryEdit, imageView_categoryDelete;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_categoryName = itemView.findViewById(R.id.textView_categoryName);
            textView_categoryParen = itemView.findViewById(R.id.textView_categoryParen);
            textView_categoryId = itemView.findViewById(R.id.textView_categoryId);
            imageView_categoryEdit = itemView.findViewById(R.id.imageView_edit_category);
            imageView_categoryDelete = itemView.findViewById(R.id.imageView_delete_category);
        }

    }
}
