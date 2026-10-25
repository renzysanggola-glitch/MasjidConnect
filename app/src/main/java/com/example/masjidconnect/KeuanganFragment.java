package com.example.masjidconnect;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class KeuanganFragment extends Fragment {

    private TextView tvSaldo, tvPemasukan, tvPengeluaran;
    private Button btnTambahTransaksi;
    private BarChart barChartKas;
    private RecyclerView rvRiwayat;
    private DatabaseHelper dbHelper;

    public KeuanganFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keuangan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());

        tvSaldo = view.findViewById(R.id.tv_keu_saldo);
        tvPemasukan = view.findViewById(R.id.tv_keu_pemasukan);
        tvPengeluaran = view.findViewById(R.id.tv_keu_pengeluaran);
        btnTambahTransaksi = view.findViewById(R.id.btn_tambah_transaksi);
        barChartKas = view.findViewById(R.id.bar_chart_keuangan);
        rvRiwayat = view.findViewById(R.id.rv_riwayat_keuangan);

        if (rvRiwayat != null) {
            rvRiwayat.setLayoutManager(new LinearLayoutManager(getContext()));
            rvRiwayat.setNestedScrollingEnabled(false);
        }

        if (btnTambahTransaksi != null) {
            btnTambahTransaksi.setOnClickListener(v -> showDialogTambahTransaksi());
        }

        loadDataKeuangan();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataKeuangan();
    }

    private void loadDataKeuangan() {
        double totalMasuk = dbHelper.getTotalPemasukan();
        double totalKeluar = dbHelper.getTotalPengeluaran();
        double totalSaldo = totalMasuk - totalKeluar;

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        if (tvSaldo != null) tvSaldo.setText(formatRupiah.format(totalSaldo));
        if (tvPemasukan != null) tvPemasukan.setText(formatRupiah.format(totalMasuk));
        if (tvPengeluaran != null) tvPengeluaran.setText(formatRupiah.format(totalKeluar));

        // Load Riwayat Transaksi
        if (rvRiwayat != null) {
            List<HashMap<String, String>> list = dbHelper.getAllKeuangan();
            KeuanganAdapter adapter = new KeuanganAdapter(list);
            rvRiwayat.setAdapter(adapter);
        }

        // Load Chart Arus Kas
        setupBarChartRealData();
    }

    private void showDialogTambahTransaksi() {
        if (getContext() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Tambah Transaksi Kas");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30);

        final EditText etJudul = new EditText(getContext());
        etJudul.setHint("Keterangan Transaksi (misal: Infaq Jumat)");
        layout.addView(etJudul);

        final EditText etJumlah = new EditText(getContext());
        etJumlah.setHint("Jumlah (Rp)");
        etJumlah.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layout.addView(etJumlah);

        // Label Tipe Transaksi
        TextView tvLabelTipe = new TextView(getContext());
        tvLabelTipe.setText("Jenis Transaksi:");
        tvLabelTipe.setPadding(0, 24, 0, 8);
        tvLabelTipe.setTextColor(Color.parseColor("#424242"));
        layout.addView(tvLabelTipe);

        final RadioGroup rgTipe = new RadioGroup(getContext());
        rgTipe.setOrientation(RadioGroup.HORIZONTAL);
        rgTipe.setPadding(0, 10, 0, 20);

        final RadioButton rbMasuk = new RadioButton(getContext());
        rbMasuk.setText("Pemasukan");
        // Berikan ID unik agar RadioGroup bisa mengenali dan mengontrolnya secara eksklusif
        rbMasuk.setId(View.generateViewId());
        rbMasuk.setPadding(0, 0, 30, 0); // Jarak antar tombol

        final RadioButton rbKeluar = new RadioButton(getContext());
        rbKeluar.setText("Pengeluaran");
        rbKeluar.setId(View.generateViewId());
        rbKeluar.setPadding(0, 0, 0, 0); // Jarak antar tombol

        rgTipe.addView(rbMasuk);
        rgTipe.addView(rbKeluar);
        layout.addView(rgTipe);

        builder.setView(layout);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String judul = etJudul.getText().toString().trim();
            String jumlahStr = etJumlah.getText().toString().trim();

            // Pengecekan pilihan RadioButton secara eksklusif
            String tipe = rbMasuk.isChecked() ? "pemasukan" : "pengeluaran";
            String tgl = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            if (!judul.isEmpty() && !jumlahStr.isEmpty()) {
                double jumlah = Double.parseDouble(jumlahStr);
                boolean success = dbHelper.insertKeuangan(judul, jumlah, tipe, tgl);
                if (success) {
                    Toast.makeText(getContext(), "Transaksi Berhasil Disimpan!", Toast.LENGTH_SHORT).show();
                    loadDataKeuangan();
                } else {
                    Toast.makeText(getContext(), "Gagal Menyimpan Data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void setupBarChartRealData() {
        if (barChartKas == null) return;

        barChartKas.setTouchEnabled(false);

        List<HashMap<String, Object>> dataKasBulan = dbHelper.getMonthlyCashFlow();

        List<BarEntry> entriesPemasukan = new ArrayList<>();
        List<BarEntry> entriesPengeluaran = new ArrayList<>();
        List<String> labelsBulan = new ArrayList<>();

        if (dataKasBulan == null || dataKasBulan.isEmpty()) {
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

                float pMasuk = (float) (((Double) row.get("pemasukan")) / 1_000_000.0);
                float pKeluar = (float) (((Double) row.get("pengeluaran")) / 1_000_000.0);

                entriesPemasukan.add(new BarEntry(i, pMasuk));
                entriesPengeluaran.add(new BarEntry(i, pKeluar));
            }
        }

        BarDataSet setPemasukan = new BarDataSet(entriesPemasukan, "Pemasukan");
        setPemasukan.setColor(Color.parseColor("#0F4C3A"));

        BarDataSet setPengeluaran = new BarDataSet(entriesPengeluaran, "Pengeluaran");
        setPengeluaran.setColor(Color.parseColor("#FFA000"));

        BarData data = new BarData(setPemasukan, setPengeluaran);
        data.setDrawValues(false);

        data.setBarWidth(0.3f);
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
        barChartKas.groupBars(0, 0.3f, 0.05f);

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