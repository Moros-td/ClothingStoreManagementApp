package com.example.clotingstoremanagementapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.clotingstoremanagementapp.R;

public class HomeFragment extends Fragment {

    private Button btnThongKe;
    private Button btnBieuDo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnThongKe = view.findViewById(R.id.btn_thongKe);
        btnBieuDo = view.findViewById(R.id.btn_bieuDo);

        btnThongKe.setOnClickListener(v -> {
            Fragment fragment = new StatisticalFragment();
            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        btnBieuDo.setOnClickListener(v -> {
            Fragment fragment = new ChartFragment();
            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }
}