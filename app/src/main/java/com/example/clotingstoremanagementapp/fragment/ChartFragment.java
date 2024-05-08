package com.example.clotingstoremanagementapp.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.activity.BaseActivity;
import com.example.clotingstoremanagementapp.api.ApiService;
import com.example.clotingstoremanagementapp.entity.RevenueEntity;
import com.example.clotingstoremanagementapp.entity.TopProductEntity;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartFragment extends Fragment {
    boolean doanhThu = true;
    String[] items = new String[]{"Tháng", "Năm"};
    private static final List<String> PRODUCT = Arrays.asList("Áo", "Quần", "Giày", "Áo khoác");
    private static final int CHART_BAR_COLOR = Color.rgb(45, 190, 236);
    private Button btnDoanhThu;
    private Button btnSanPham;
    private TextView txtTitle;
    private FrameLayout chartContainer;
    private BaseActivity baseActivity;
    private Spinner spinnerThongKe;
    private SessionManager sessionManager;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        btnDoanhThu = view.findViewById(R.id.btn_doanhThu);
        btnSanPham = view.findViewById(R.id.btn_sanPham);
        txtTitle = view.findViewById(R.id.txt_title);
        chartContainer = view.findViewById(R.id.chart);
        spinnerThongKe = view.findViewById(R.id.spinner_thongKe);
        baseActivity = (BaseActivity) getActivity();
        sessionManager = new SessionManager(baseActivity);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        spinnerThongKe.setAdapter(adapter);
        spinnerThongKe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = items[position];
                updateChartData(selectedOption);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        btnDoanhThu.setOnClickListener(v -> switchChart(true));
        btnSanPham.setOnClickListener(v -> switchChart(false));
        switchChart(true);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        return view;
    }

    private void updateChartData(String selectedOption) {
        if (sessionManager.isLoggedIn()) {
            String token = sessionManager.getJwt();
            dialog = BaseActivity.openLoadingDialog(baseActivity); // Show loading dialog

            if (doanhThu) {
                if ("Tháng".equals(selectedOption)) {
                    ApiService.apiService.getRevenueByMonth(token).enqueue(new Callback<List<RevenueEntity>>() {
                        @Override
                        public void onResponse(Call<List<RevenueEntity>> call, Response<List<RevenueEntity>> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss(); // Dismiss loading dialog
                            }
                            if (response.isSuccessful() && response.body() != null) {
                                List<String> labels = new ArrayList<>();
                                List<Float> values = new ArrayList<>();
                                for (RevenueEntity entity : response.body()) {
                                    labels.add("Tháng" + entity.getMonth() + "/" + entity.getYear());
                                    values.add((float) entity.getRevenue());
                                }
                                updateBarChart(labels, values);
                            } else {
                                Log.e("API Error", "Failed to fetch revenue data by month");
                            }
                        }
                        @Override
                        public void onFailure(Call<List<RevenueEntity>> call, Throwable t) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss(); // Dismiss loading dialog
                            }
                            Log.e("API Error", "Failed to fetch revenue data by month: " + t.getMessage());
                        }
                    });
                } else if ("Năm".equals(selectedOption)) {
                    ApiService.apiService.getRevenueByYear(token).enqueue(new Callback<List<RevenueEntity>>() {
                        @Override
                        public void onResponse(Call<List<RevenueEntity>> call, Response<List<RevenueEntity>> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss(); // Dismiss loading dialog
                            }
                            if (response.isSuccessful() && response.body() != null) {
                                List<String> labels = new ArrayList<>();
                                List<Float> values = new ArrayList<>();
                                for (RevenueEntity entity : response.body()) {
                                    labels.add("Năm" + entity.getYear());
                                    values.add((float) entity.getRevenue());
                                }
                                updateBarChart(labels, values);
                            } else {
                                Log.e("API Error", "Failed to fetch revenue data by year");
                            }
                        }
                        @Override
                        public void onFailure(Call<List<RevenueEntity>> call, Throwable t) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss(); // Dismiss loading dialog
                            }
                            Log.e("API Error", "Failed to fetch revenue data by year: " + t.getMessage());
                        }
                    });
                }
            } else {
                ApiService.apiService.getTopSellingProducts(token).enqueue(new Callback<List<TopProductEntity>>() {
                    @Override
                    public void onResponse(Call<List<TopProductEntity>> call, Response<List<TopProductEntity>> response) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss(); // Dismiss loading dialog
                        }
                        if (response.isSuccessful() && response.body() != null) {
                            List<String> labels = new ArrayList<>();
                            List<Float> values = new ArrayList<>();
                            for (TopProductEntity entity : response.body()) {
                                labels.add(entity.getProductName());
                                values.add((float) entity.getTotalQuantity());
                            }
                            updatePieChart(labels, values);
                        } else {
                            Log.e("API Error", "Failed to fetch top selling products");
                        }
                    }
                    @Override
                    public void onFailure(Call<List<TopProductEntity>> call, Throwable t) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss(); // Dismiss loading dialog
                        }
                        Log.e("API Error", "Failed to fetch top selling products: " + t.getMessage());
                    }
                });
            }
        }
    }
    // Phương thức chuyển đổi giữa biểu đồ doanh thu và biểu đồ sản phẩm
    private void switchChart(boolean isDoanhThu) {
        doanhThu = isDoanhThu;
        if (doanhThu) {
            // Hiển thị biểu đồ doanh thu
            btnDoanhThu.setEnabled(false);
            btnSanPham.setEnabled(true);
            spinnerThongKe.setEnabled(true);
            spinnerThongKe.setClickable(true);
            txtTitle.setText("Thống kê doanh thu");
            View barChartLayout = LayoutInflater.from(requireContext()).inflate(R.layout.bar_chart, chartContainer, false);
            chartContainer.removeAllViews();
            chartContainer.addView(barChartLayout);
            updateChartData((String) spinnerThongKe.getSelectedItem());
        } else {
            // Hiển thị biểu đồ sản phẩm
            btnDoanhThu.setEnabled(true);
            btnSanPham.setEnabled(false);
            spinnerThongKe.setEnabled(false);
            spinnerThongKe.setClickable(false);
            txtTitle.setText("Sản phẩm bán chạy");
            View pieChartLayout = LayoutInflater.from(requireContext()).inflate(R.layout.pie_chart, chartContainer, false);
            chartContainer.removeAllViews();
            chartContainer.addView(pieChartLayout);
            updateChartData("");
        }
    }

    // Cập nhật dữ liệu cho biểu đồ Bar Chart
    private void updateBarChart(List<String> labels, List<Float> values) {
        BarChart barChart = chartContainer.findViewById(R.id.barchart);
        if (barChart == null) return;

        // Xác định dữ liệu
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            entries.add(new BarEntry(i, values.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu");
        dataSet.setColor(CHART_BAR_COLOR);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        // Thiết lập dữ liệu cho biểu đồ
        barChart.setData(barData);
        barChart.getAxisLeft().setAxisLineWidth(2f);
        barChart.getAxisLeft().setTextSize(12f);
        barChart.getBarData().setBarWidth(0.5f);
        barChart.setExtraOffsets(5f, 5f, 5f, 15f);

        // Cập nhật các thuộc tính của biểu đồ
        barChart.getXAxis().setLabelCount(labels.size());
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setLabelRotationAngle(45f);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setTextSize(12f);
        barChart.getXAxis().setAxisLineWidth(2f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisLeft().setAxisMaximum(Collections.max(values) * 1.2f); // Tăng giá trị max một chút để tránh chạm vào biên
        barChart.invalidate(); // Cập nhật biểu đồ
        barChart.getAxisRight().setDrawLabels(false); //ko vẽ cột bên phải
        barChart.getAxisRight().setDrawGridLines(false);
    }

    // Cập nhật dữ liệu cho biểu đồ Pie Chart
    private void updatePieChart(List<String> labels, List<Float> values) {
        PieChart pieChart = chartContainer.findViewById(R.id.piechart);
        if (pieChart == null) return;

        // Xác định dữ liệu
        List<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            entries.add(new PieEntry(values.get(i), labels.get(i)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(12f);

        // Thiết lập dữ liệu cho biểu đồ
        pieChart.setData(pieData);

        // Cập nhật các thuộc tính của biểu đồ
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate(); // Cập nhật biểu đồ
    }

}