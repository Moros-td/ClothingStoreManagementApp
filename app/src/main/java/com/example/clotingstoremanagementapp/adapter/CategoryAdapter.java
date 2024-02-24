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
import com.example.clotingstoremanagementapp.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> implements Filterable {

    private List<CategoryEntity> listCategory;
    private List<CategoryEntity> listCategoryOld;

    public CategoryAdapter(List<CategoryEntity> listCategory) {

        this.listCategory = listCategory;
        this.listCategoryOld = listCategory;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        return new CategoryAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        CategoryEntity CategoryEntity = listCategory.get(position);
        if(CategoryEntity == null){
            return;
        }

        holder.textView_categoryName.setText(CategoryEntity.getCategoryName());
        holder.textView_categoryParen.setText(CategoryEntity.getCategoryParenName());
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

        private TextView textView_categoryName, textView_categoryParen;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_categoryName = itemView.findViewById(R.id.textView_categoryName);
            textView_categoryParen = itemView.findViewById(R.id.textView_categoryParen);

        }

    }
}
