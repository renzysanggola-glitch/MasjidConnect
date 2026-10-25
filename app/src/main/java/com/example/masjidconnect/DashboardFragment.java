package com.example.masjidconnect;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private TextView tvSaldo, tvPemasukan, tvLihatSemua;
    private BarChart barChartKas;
    private RecyclerView rvAgenda;
    private DatabaseHelper dbHelper;

    public DashboardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());

        tvSaldo = view.findViewById(R.id.tv_dash_saldo);
        tvPemasukan = view.findViewById(R.id.tv_dash_pemasukan);
        tvLihatSemua = view.findViewById(R.id.tv_lihat_semua);
        barChartKas = view.findViewById(R.id.bar_chart_kas);
        rvAgenda = view.findViewById(R.id.rv_dash_agenda);

        if (rvAgenda != null) {
            rvAgenda.setLayoutManager(new LinearLayoutManager(getContext()));
            // Matikan nested scrolling agar RecyclerView menyatu dengan ScrollView utama
            rvAgenda.setNestedScrollingEnabled(false);
        }

        if (tvLihatSemua != null) {
            tvLihatSemua.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new AgendaFragment())
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        refreshDashboardData();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshDashboardData();
    }

    private void refreshDashboardData() {
        loadDataKas();
        setupBarChartRealData();
        loadAgendaData();
    }

    private void loadDataKas() {
        double totalMasuk = dbHelper.getTotalPemasukan();
        double totalKeluar = dbHelper.getTotalPengeluaran();
        double totalSaldo = totalMasuk - totalKeluar;

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        if (tvSaldo != null) tvSaldo.setText(formatRupiah.format(totalSaldo));
        if (tvPemasukan != null) tvPemasukan.setText(formatRupiah.format(totalMasuk));
    }

    private void loadAgendaData() {
        if (rvAgenda != null) {
            List<HashMap<String, String>> listAgenda = dbHelper.getAllAgenda();
            AgendaAdapter adapter = new AgendaAdapter(listAgenda);
            rvAgenda.setAdapter(adapter);
        }
    }

    private void setupBarChartRealData() {
        if (barChartKas == null) return;

        // MATIKAN TOUCH DARI BARCHART AGAR SCROLLVIEW BISA DI-SCROLL DENGAN LANCAR
        barChartKas.setTouchEnabled(false);
        barChartKas.setDragEnabled(false);
        barChartKas.setScaleEnabled(false);
        barChartKas.setPinchZoom(false);

        List<HashMap<String, Object>> dataKasBulan = dbHelper.getMonthlyCashFlow();

        List<BarEntry> entriesPemasukan = new ArrayList<>();
        List<BarEntry> entriesPengeluaran = new ArrayList<>();
        List<String> labelsBulan = new ArrayList<>();

        if (dataKasBulan == null || dataKasBulan.isEmpty()) {
            // Sampel data visual default jika transaksi belum diisi di database
            String[] defaultBulan = new String[]{"Apr", "Mei", "Jun", "Jul", "Agu", "Sep"};
            float[] masuk = {4.0f, 5.5f, 3.8f, 6.2f, 4.8f, 5.2f};
            float[] keluar = {2.0f, 2.5f, 3.2f, 2.8f, 2.0f, 2.2f};

            for (int i = 0; i < 6; i++) {
                labelsBulan.add(defaultBulan[i]);
                entriesPemasukan.add(new BarEntry(i, masuk[i]));
                entriesPengeluaran.add(new BarEntry(i, keluar[i]));
            }
        } else {
            for (int i = 0; i < dataKasBulan.size(); i++) {
                HashMap<String, Object> row = dataKasBulan.get(i);
                labelsBulan.add((String) row.get("bulan"));

                // Mengubah nominal rupiah ke bentuk skala (Juta)
                float pMasuk = (float) (((Double) row.get("pemasukan")) / 1_000_000.0);
                float pKeluar = (float) (((Double) row.get("pengeluaran")) / 1_000_000.0);

                entriesPemasukan.add(new BarEntry(i, pMasuk));
                entriesPengeluaran.add(new BarEntry(i, pKeluar));
            }
        }

        BarDataSet setPemasukan = new BarDataSet(entriesPemasukan, "Pemasukan");
        setPemasukan.setColor(Color.parseColor("#0F4C3A")); // Hijau tua

        BarDataSet setPengeluaran = new BarDataSet(entriesPengeluaran, "Pengeluaran");
        setPengeluaran.setColor(Color.parseColor("#FFA000")); // Oranye/Kuning

        BarData data = new BarData(setPemasukan, setPengeluaran);
        data.setDrawValues(false);

        float groupSpace = 0.3f;
        float barSpace = 0.05f;
        float barWidth = 0.3f;

        data.setBarWidth(barWidth);
        barChartKas.setData(data);

        XAxis xAxis = barChartKas.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsBulan));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);

        YAxis axisLeft = barChartKas.getAxisLeft();
        axisLeft.setAxisMinimum(0f);
        barChartKas.getAxisRight().setEnabled(false);

        barChartKas.getXAxis().setAxisMinimum(0);
        barChartKas.getXAxis().setAxisMaximum(labelsBulan.size());
        barChartKas.groupBars(0, groupSpace, barSpace);

        Legend legend = barChartKas.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        barChartKas.getDescription().setEnabled(false);
        barChartKas.animateY(800);
        barChartKas.invalidate();
    }
}