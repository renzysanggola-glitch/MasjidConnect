package com.example.masjidconnect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {

    private ImageButton btnBack;
    private TextView tvUserName, tvUserRole, tvAppVersion;
    private CardView cardProfilMasjid, cardNotifikasi, cardJadwalSalat;
    private CardView cardManajemenUser, cardKeamanan, cardDigitalSignage, cardTentang;
    private LinearLayout btnKeluarAkun;

    private SharedPreferences sessionPref, profilPref;

    public SettingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        profilPref = requireContext().getSharedPreferences("ProfilMasjidPref", Context.MODE_PRIVATE);

        btnBack = view.findViewById(R.id.btn_back);
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvUserRole = view.findViewById(R.id.tv_user_role);
        tvAppVersion = view.findViewById(R.id.tv_app_version);

        cardProfilMasjid = view.findViewById(R.id.card_profil_masjid);
        cardNotifikasi = view.findViewById(R.id.card_notifikasi);
        cardJadwalSalat = view.findViewById(R.id.card_jadwal_salat);
        cardManajemenUser = view.findViewById(R.id.card_manajemen_user);
        cardKeamanan = view.findViewById(R.id.card_keamanan);
        cardDigitalSignage = view.findViewById(R.id.card_digital_signage);
        cardTentang = view.findViewById(R.id.card_tentang);
        btnKeluarAkun = view.findViewById(R.id.btn_keluar_akun);

        loadUserData();
        setupClickListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        String userName = sessionPref.getString("userName", "Zaidan Ahmad");
        String userRole = sessionPref.getString("userRole", "ADMINISTRATOR");
        String masjidName = profilPref.getString("nama_masjid", "MASJID AL-IKHLAS");

        tvUserName.setText(userName);
        String fullRole = userRole.toUpperCase() + " • " + masjidName.toUpperCase();
        tvUserRole.setText(fullRole);

        // Dynamic app version string from BuildConfig
        String versionName = BuildConfig.VERSION_NAME;
        tvAppVersion.setText("Masjid Connect v" + versionName);

        // Access control check based on role
        boolean isAdmin = "ADMINISTRATOR".equalsIgnoreCase(userRole);
        if (cardManajemenUser != null) {
            cardManajemenUser.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        }
        if (cardKeamanan != null) {
            cardKeamanan.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        }
        if (cardDigitalSignage != null) {
            cardDigitalSignage.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        }
    }

    private void setupClickListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getActivity() != null && getActivity() instanceof MainActivity) {
                    // Navigate back to Dashboard or finish
                    ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new DashboardFragment())
                            .commit();
                }
            });
        }

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
            cardJadwalSalat.setOnClickListener(v -> showInfoDialog("Konfigurasi Jadwal Salat",
                    "Lokasi Otomatis: Bumi Tamalanrea Permai, Makassar (WITA)\nMetode Perhitungan: Kemenag RI"));
        }

        if (cardManajemenUser != null) {
            cardManajemenUser.setOnClickListener(v -> showInfoDialog("Manajemen User",
                    "Status: Administrator Utama\nAkses: Full Access Control (Keuangan, Agenda & Digital Signage)"));
        }

        if (cardKeamanan != null) {
            cardKeamanan.setOnClickListener(v -> showInfoDialog("Keamanan & Password",
                    "Sistem autentikasi menggunakan enkripsi SharedPreferences dan SQLite terproteksi."));
        }

        if (cardDigitalSignage != null) {
            cardDigitalSignage.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), DigitalSignageActivity.class);
                startActivity(intent);
            });
        }

        if (cardTentang != null) {
            cardTentang.setOnClickListener(v -> showInfoDialog("Tentang Aplikasi",
                    "Masjid Connect v" + BuildConfig.VERSION_NAME + "\nAplikasi Manajemen Operasional & Keuangan Masjid Terpadu.\n© 2024 Karya Digital Muslim."));
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
                .setTitle(getString(R.string.dialog_logout_title))
                .setMessage(getString(R.string.dialog_logout_message))
                .setPositiveButton(getString(R.string.btn_yes_logout), (dialog, which) -> performLogout())
                .setNegativeButton(getString(R.string.btn_cancel), (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void performLogout() {
        if (getActivity() == null) return;

        SharedPreferences pref = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(getContext(), "Berhasil keluar dari akun", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}