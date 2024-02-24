package com.example.clotingstoremanagementapp.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.activity.BaseActivity;
import com.example.clotingstoremanagementapp.adapter.CategoryAdapter;
import com.example.clotingstoremanagementapp.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private BaseActivity baseActivity;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_category, container, false);
        searchView = mView.findViewById(R.id.searchViewCategory);
        baseActivity = (BaseActivity) getActivity();
        recyclerView = mView.findViewById(R.id.rcv_category);

        //set layout manager cho rcv
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);

        // set adapter cho rcv
        CategoryAdapter CategoryAdapter = new CategoryAdapter(getListCategory());
        recyclerView.setAdapter(CategoryAdapter);

        // sự kiện cho search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CategoryAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                CategoryAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return mView;
    }

    private List<CategoryEntity> getListCategory() {
        List<CategoryEntity> list = new ArrayList<>();
        list.add(new CategoryEntity("Áo sơ mi nữ", "Áo nữ"));
        list.add(new CategoryEntity("Áo thun nam", "Áo nam"));
        return list;
    }
}