package com.example.masjidconnect;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AgendaFragment extends Fragment {

    private RecyclerView rvAgenda;
    private EditText etSearch;
    private Button btnSemua, btnKajian, btnRapat, btnEvent;
    private FloatingActionButton fabTambah;
    private DatabaseHelper dbHelper;

    private List<HashMap<String, String>> masterListAgenda = new ArrayList<>();
    private String selectedFilter = "Semua";

    public AgendaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());

        rvAgenda = view.findViewById(R.id.rv_agenda_full);
        etSearch = view.findViewById(R.id.et_search_agenda);
        btnSemua = view.findViewById(R.id.btn_filter_semua);
        btnKajian = view.findViewById(R.id.btn_filter_kajian);
        btnRapat = view.findViewById(R.id.btn_filter_rapat);
        btnEvent = view.findViewById(R.id.btn_filter_event);
        fabTambah = view.findViewById(R.id.fab_tambah_agenda);

        if (rvAgenda != null) {
            rvAgenda.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        setupFilterListeners();
        setupSearchListener();

        if (fabTambah != null) {
            fabTambah.setOnClickListener(v -> showDialogTambahAgenda());
        }

        loadDataAgenda();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataAgenda();
    }

    private void loadDataAgenda() {
        masterListAgenda = dbHelper.getAllAgenda();
        applyFilterAndSearch();
    }

    private void setupFilterListeners() {
        View.OnClickListener listener = v -> {
            int id = v.getId();
            if (id == R.id.btn_filter_semua) {
                selectedFilter = "Semua";
            } else if (id == R.id.btn_filter_kajian) {
                selectedFilter = "Kajian";
            } else if (id == R.id.btn_filter_rapat) {
                selectedFilter = "Rapat";
            } else if (id == R.id.btn_filter_event) {
                selectedFilter = "Event";
            }
            updateFilterButtonStyles();
            applyFilterAndSearch();
        };

        if (btnSemua != null) btnSemua.setOnClickListener(listener);
        if (btnKajian != null) btnKajian.setOnClickListener(listener);
        if (btnRapat != null) btnRapat.setOnClickListener(listener);
        if (btnEvent != null) btnEvent.setOnClickListener(listener);
    }

    private void updateFilterButtonStyles() {
        Button[] buttons = {btnSemua, btnKajian, btnRapat, btnEvent};
        String[] filters = {"Semua", "Kajian", "Rapat", "Event"};

        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == null) continue;
            if (filters[i].equalsIgnoreCase(selectedFilter)) {
                buttons[i].setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#0F4C3A")));
                buttons[i].setTextColor(Color.WHITE);
            } else {
                buttons[i].setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.WHITE));
                buttons[i].setTextColor(Color.parseColor("#424242"));
            }
        }
    }

    private void setupSearchListener() {
        if (etSearch == null) return;
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilterAndSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void applyFilterAndSearch() {
        String keyword = etSearch != null ? etSearch.getText().toString().toLowerCase().trim() : "";
        List<HashMap<String, String>> filteredList = new ArrayList<>();

        for (HashMap<String, String> item : masterListAgenda) {
            String judul = item.get("judul") != null ? item.get("judul").toLowerCase() : "";
            String pembicara = item.get("pembicara") != null ? item.get("pembicara").toLowerCase() : "";
            String kategori = item.get("kategori") != null ? item.get("kategori") : "";

            boolean matchFilter = selectedFilter.equalsIgnoreCase("Semua") || kategori.equalsIgnoreCase(selectedFilter);
            boolean matchSearch = keyword.isEmpty() || judul.contains(keyword) || pembicara.contains(keyword);

            if (matchFilter && matchSearch) {
                filteredList.add(item);
            }
        }

        if (rvAgenda != null) {
            AgendaAdapter adapter = new AgendaAdapter(filteredList);
            rvAgenda.setAdapter(adapter);
        }
    }

    private void showDialogTambahAgenda() {
        if (getContext() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Tambah Agenda / Kajian");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30);

        final EditText etJudul = new EditText(getContext());
        etJudul.setHint("Judul Agenda (misal: Kajian Fiqih)");
        layout.addView(etJudul);

        final EditText etPembicara = new EditText(getContext());
        etPembicara.setHint("Pembicara / Pengisi");
        layout.addView(etPembicara);

        final EditText etTanggal = new EditText(getContext());
        etTanggal.setHint("Tanggal (misal: 21 SEPT)");
        layout.addView(etTanggal);

        final EditText etWaktu = new EditText(getContext());
        etWaktu.setHint("Waktu (misal: 20:00 - Selesai)");
        layout.addView(etWaktu);

        final EditText etKategori = new EditText(getContext());
        etKategori.setHint("Kategori (Kajian / Rapat / Event)");
        layout.addView(etKategori);

        builder.setView(layout);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String judul = etJudul.getText().toString().trim();
            String pembicara = etPembicara.getText().toString().trim();
            String tanggal = etTanggal.getText().toString().trim();
            String waktu = etWaktu.getText().toString().trim();
            String kategori = etKategori.getText().toString().trim();

            if (!judul.isEmpty() && !tanggal.isEmpty()) {
                if (kategori.isEmpty()) kategori = "Kajian";
                boolean success = dbHelper.insertAgenda(judul, pembicara, tanggal, waktu, kategori);
                if (success) {
                    Toast.makeText(getContext(), "Agenda Berhasil Disimpan!", Toast.LENGTH_SHORT).show();
                    loadDataAgenda();
                } else {
                    Toast.makeText(getContext(), "Gagal Menyimpan Agenda", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Judul dan Tanggal wajib diisi", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}