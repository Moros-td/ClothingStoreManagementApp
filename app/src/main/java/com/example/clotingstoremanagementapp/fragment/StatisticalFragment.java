package com.example.clotingstoremanagementapp.fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.clotingstoremanagementapp.activity.BaseActivity;
import com.example.clotingstoremanagementapp.adapter.StatisticalAdapter;
import com.example.clotingstoremanagementapp.api.ApiService;
import com.example.clotingstoremanagementapp.entity.Statistical;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.fragment.app.Fragment;

import com.example.clotingstoremanagementapp.R;

public class StatisticalFragment extends Fragment {
    private EditText startDateEditText;
    private EditText endDateEditText;
    private ImageView searchImageView;
    private ListView orderListView;
    private List<Statistical> statisticalList = new ArrayList<>();
    private Button exportButton;
    private BaseActivity baseActivity;
    private SessionManager sessionManager;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistical_fragemnt, container, false);
        startDateEditText = view.findViewById(R.id.startDateEditText);
        endDateEditText = view.findViewById(R.id.endDateEditText);
        searchImageView = view.findViewById(R.id.searchImageView);
        orderListView = view.findViewById(R.id.orderListView);
        exportButton = view.findViewById(R.id.exportButton);
        baseActivity = (BaseActivity) getActivity();
        sessionManager = new SessionManager(baseActivity);

        if (sessionManager.isLoggedIn()) {
            String token = sessionManager.getJwt();
            dialog = BaseActivity.openLoadingDialog(baseActivity);

            ApiService.apiService.getOrderDetails(token).enqueue(new Callback<List<Statistical>>() {
                @Override
                public void onResponse(Call<List<Statistical>> call, Response<List<Statistical>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        statisticalList.clear();
                        statisticalList.addAll(response.body());
                        StatisticalAdapter adapter = new StatisticalAdapter(getContext(), statisticalList);
                        orderListView.setAdapter(adapter);
                    } else {
                        Log.e("API Error", "Failed to fetch order details");
                    }
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<Statistical>> call, Throwable throwable) {
                    Log.e("API Error", "Failed to fetch order details", throwable);
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
        }
        StatisticalAdapter adapter = new StatisticalAdapter(getContext(), statisticalList);
        orderListView.setAdapter(adapter);
        startDateEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (datePickerview, year1, month1, dayOfMonth) -> startDateEditText.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1), year, month, day);
                datePickerDialog.show();
            }
            return true;
        });

        endDateEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (datePickerview, year1, month1, dayOfMonth) -> {
                            String startDateStr = startDateEditText.getText().toString();
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                Date startDate = format.parse(startDateStr);
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(year1, month1, dayOfMonth);
                                if (selectedDate.getTime().compareTo(startDate) > 0) {
                                    endDateEditText.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                                } else {
                                    Toast.makeText(getContext(), "Ngày kết thúc phải lớn hơn ngày bắt đầu", Toast.LENGTH_SHORT).show();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
            return true;
        });
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDateStr = startDateEditText.getText().toString();
                String endDateStr = endDateEditText.getText().toString();
                if (!startDateStr.isEmpty() && endDateStr.isEmpty()) {
                    endDateStr = startDateStr;
                    endDateEditText.setText(endDateStr);
                } else if (startDateStr.isEmpty() && !endDateStr.isEmpty()) {
                    startDateStr = endDateStr;
                    startDateEditText.setText(startDateStr);
                }

                // Check if both fields are empty
                if (startDateStr.isEmpty() && endDateStr.isEmpty()) {
                    // Display the entire list
                    StatisticalAdapter adapter = new StatisticalAdapter(getContext(), statisticalList);
                    orderListView.setAdapter(adapter);
                } else {
                    // Existing code for filtering the list
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date startDate = format.parse(startDateStr);
                        Date endDate = format.parse(endDateStr);

                        List<Statistical> filteredList = new ArrayList<>();
                        for (Statistical order : statisticalList) {
                            Date orderDate = order.getOrderDate();
                            if (orderDate.compareTo(startDate) >= 0 && orderDate.compareTo(endDate) <= 0) {
                                filteredList.add(order);
                            }
                        }

                        StatisticalAdapter adapter = new StatisticalAdapter(getContext(), filteredList);
                        orderListView.setAdapter(adapter);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getActivity().getSharedPreferences("com.example.myapplication", Context.MODE_PRIVATE);
                int currentCount = prefs.getInt("exportCount", 0);

                // Increment the count
                currentCount++;

                // Save the new count to SharedPreferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("exportCount", currentCount);
                editor.apply();
                String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                File file = new File(pdfPath, "Thongke" + currentCount + ".pdf");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("PDF Creation", "Error creating file", e);
                        return;
                    }
                }

                try {
                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream(file));
                    document.open();

                    // Create a large bold font for the title
                    Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);

                    // Add title
                    Paragraph title = new Paragraph("THONG KE DOANH THU", titleFont);
                    title.setAlignment(Element.ALIGN_CENTER);
                    document.add(title);

                    // Add date range
                    String startDateStr = startDateEditText.getText().toString();
                    String endDateStr = endDateEditText.getText().toString();
                    Paragraph dateRange = new Paragraph(startDateStr + " -> " + endDateStr);
                    dateRange.setAlignment(Element.ALIGN_CENTER);
                    document.add(dateRange);

                    // Create a table with 4 columns
                    PdfPTable table = new PdfPTable(4);
                    table.addCell("STT");
                    table.addCell("Order Code");
                    table.addCell("Order Date");
                    table.addCell("Total Price");

                    // Get the current list from the ListView
                    StatisticalAdapter adapter = (StatisticalAdapter) orderListView.getAdapter();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    for (int i = 0; i < adapter.getCount(); i++) {
                        Statistical order = adapter.getItem(i);
                        table.addCell(String.valueOf(i + 1));
                        table.addCell(order.getOrderCode());
                        String orderDateStr = format.format(order.getOrderDate());
                        table.addCell(orderDateStr);
                        table.addCell(order.getTotalPrice().toString());
                    }

                    // Add the table to the document
                    document.add(table);

                    document.close();
                    Toast.makeText(getContext(), "Exported to " + file.getName() + " successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("PDF Creation", "Error writing to file", e);
                }
            }
        });
        return view;
    }
}