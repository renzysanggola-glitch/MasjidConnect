package com.example.masjidconnect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {

    private CardView cardProfilMasjid, cardNotifikasi, cardJadwalSalat, cardManajemenUser, cardKeamanan, cardTentang;
    private Button btnKeluarAkun;

    public SettingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardProfilMasjid = view.findViewById(R.id.card_profil_masjid);
        cardNotifikasi = view.findViewById(R.id.card_notifikasi);
        cardJadwalSalat = view.findViewById(R.id.card_jadwal_salat);
        cardManajemenUser = view.findViewById(R.id.card_manajemen_user);
        cardKeamanan = view.findViewById(R.id.card_keamanan);
        cardTentang = view.findViewById(R.id.card_tentang);
        btnKeluarAkun = view.findViewById(R.id.btn_keluar_akun);

        setupClickListeners();
    }

    private void setupClickListeners() {
        if (cardProfilMasjid != null) {
            cardProfilMasjid.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ProfilMasjidActivity.class);
                startActivity(intent);
            });
        }

        if (cardNotifikasi != null) {
            cardNotifikasi.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
            });
        }

        if (cardJadwalSalat != null) {
            cardJadwalSalat.setOnClickListener(v -> showInfoDialog("Konfigurasi Jadwal Salat", "Lokasi Otomatis: Bumi Tamalanrea Permai, Makassar (WITA)\nMetode Perhitungan: Kemenag RI"));
        }

        if (cardManajemenUser != null) {
            cardManajemenUser.setOnClickListener(v -> showInfoDialog("Manajemen User", "Status: Administrator Utama\nAkses: Full Access Control (Keuangan & Agenda)"));
        }

        if (cardKeamanan != null) {
            cardKeamanan.setOnClickListener(v -> showInfoDialog("Keamanan & Password", "Sistem autentikasi menggunakan enkripsi SharedPreferences dan SQLite terproteksi."));
        }

        if (cardTentang != null) {
            cardTentang.setOnClickListener(v -> showInfoDialog("Tentang Aplikasi", "Masjid Connect v2.4.0\nAplikasi Manajemen Operasional & Keuangan Masjid Terpadu.\n© 2026 Karya Digital Muslim."));
        }

        if (btnKeluarAkun != null) {
            btnKeluarAkun.setOnClickListener(v -> showLogoutConfirmation());
        }
    }

    private void showInfoDialog(String title, String message) {
        if (getContext() == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showLogoutConfirmation() {
        if (getContext() == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle("Keluar Akun")
                .setMessage("Apakah Anda yakin ingin keluar dari akun Administrator?")
                .setPositiveButton("Ya, Keluar", (dialog, which) -> performLogout())
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void performLogout() {
        if (getActivity() == null) return;

        // Hapus status session login dari SharedPreferences
        SharedPreferences pref = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(getContext(), "Berhasil keluar dari akun", Toast.LENGTH_SHORT).show();

        // Pindah ke LoginActivity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}