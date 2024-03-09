package com.example.clotingstoremanagementapp.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

public class HomeFragment extends Fragment {

    boolean doanhThu = true;
    String[] items = new String[]{"Tháng", "Năm"};
    private static final List<String> MONTHS = Arrays.asList("Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4");
    private static final List<String> YEARS = Arrays.asList("2020", "2021", "2022", "2023");
    private static final List<String> PRODUCT = Arrays.asList("Áo", "Quần", "Giày", "Áo khoác");
    private static final int CHART_BAR_COLOR = Color.rgb(45, 190, 236);

    // Khai báo các thành phần giao diện
    private Button btnDoanhThu;
    private Button btnSanPham;
    private TextView txtTitle;
    private FrameLayout chartContainer;
    private Spinner spinnerThongKe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ các thành phần giao diện
        btnDoanhThu = view.findViewById(R.id.btn_doanhThu);
        btnSanPham = view.findViewById(R.id.btn_sanPham);
        txtTitle = view.findViewById(R.id.txt_title);
        chartContainer = view.findViewById(R.id.chart);
        spinnerThongKe = view.findViewById(R.id.spinner_thongKe);

        // Khởi tạo ArrayAdapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        spinnerThongKe.setAdapter(adapter);

        // Thiết lập sự kiện cho Spinner
        spinnerThongKe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = items[position];
                updateChartData(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không cần xử lý gì khi không có lựa chọn được chọn
            }
        });

        // Thiết lập sự kiện cho Button doanh thu
        btnDoanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchChart(true);
            }
        });

        // Thiết lập sự kiện cho Button sản phẩm
        btnSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchChart(false);
            }
        });

        // Mặc định hiển thị biểu đồ doanh thu khi Fragment được tạo
        switchChart(true);

        return view;
    }

    // Phương thức cập nhật dữ liệu cho biểu đồ
    private void updateChartData(String selectedOption) {
        if (doanhThu) {
            // Nếu đang hiển thị biểu đồ doanh thu
            if ("Tháng".equals(selectedOption)) {
                updateBarChart(MONTHS, Arrays.asList(500f, 700f, 600f, 800f));
            } else if ("Năm".equals(selectedOption)) {
                updateBarChart(YEARS, Arrays.asList(1500f, 1700f, 1600f, 1800f));
            }
        } else {
            // Nếu đang hiển thị biểu đồ sản phẩm
            updatePieChart(PRODUCT, Arrays.asList(500f, 700f, 600f, 800f));
        }
    }

    // Phương thức chuyển đổi giữa biểu đồ doanh thu và biểu đồ sản phẩm
    private void switchChart(boolean isDoanhThu) {
        doanhThu = isDoanhThu;
        if (doanhThu) {
            // Hiển thị biểu đồ doanh thu
            btnDoanhThu.setEnabled(false);
            btnSanPham.setEnabled(true);
            txtTitle.setText("Thống kê doanh thu");
            View barChartLayout = LayoutInflater.from(requireContext()).inflate(R.layout.bar_chart, chartContainer, false);
            chartContainer.removeAllViews();
            chartContainer.addView(barChartLayout);
            updateChartData((String) spinnerThongKe.getSelectedItem());
        } else {
            // Hiển thị biểu đồ sản phẩm
            btnDoanhThu.setEnabled(true);
            btnSanPham.setEnabled(false);
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

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        // Thiết lập dữ liệu cho biểu đồ
        barChart.setData(barData);

        // Cập nhật các thuộc tính của biểu đồ
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setLabelRotationAngle(45f);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisLeft().setAxisMaximum(Collections.max(values) * 1.2f); // Tăng giá trị max một chút để tránh chạm vào biên
        barChart.invalidate(); // Cập nhật biểu đồ
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
