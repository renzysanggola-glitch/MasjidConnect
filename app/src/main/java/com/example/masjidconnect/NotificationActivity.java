package com.example.masjidconnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class NotificationActivity extends AppCompatActivity {

    private SwitchCompat switchAdzan, switchIqomah, switchPengumuman, switchLaporan, switchKajian, switchBaterai;
    private Button btnSimpan;
    private ImageView btnBack;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);

        pref = getSharedPreferences("NotificationSettings", Context.MODE_PRIVATE);

        switchAdzan = findViewById(R.id.switch_adzan);
        switchIqomah = findViewById(R.id.switch_iqomah);
        switchPengumuman = findViewById(R.id.switch_pengumuman);
        switchLaporan = findViewById(R.id.switch_laporan);
        switchKajian = findViewById(R.id.switch_kajian);
        switchBaterai = findViewById(R.id.switch_baterai);
        btnSimpan = findViewById(R.id.btn_simpan_notif);
        btnBack = findViewById(R.id.btn_back_notif);

        loadSavedSettings();

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnSimpan != null) {
            btnSimpan.setOnClickListener(v -> saveSettings());
        }
    }

    private void loadSavedSettings() {
        switchAdzan.setChecked(pref.getBoolean("notif_adzan", true));
        switchIqomah.setChecked(pref.getBoolean("notif_iqomah", true));
        switchPengumuman.setChecked(pref.getBoolean("notif_pengumuman", true));
        switchLaporan.setChecked(pref.getBoolean("notif_laporan", false));
        switchKajian.setChecked(pref.getBoolean("notif_kajian", true));
        switchBaterai.setChecked(pref.getBoolean("notif_baterai", false));
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("notif_adzan", switchAdzan.isChecked());
        editor.putBoolean("notif_iqomah", switchIqomah.isChecked());
        editor.putBoolean("notif_pengumuman", switchPengumuman.isChecked());
        editor.putBoolean("notif_laporan", switchLaporan.isChecked());
        editor.putBoolean("notif_kajian", switchKajian.isChecked());
        editor.putBoolean("notif_baterai", switchBaterai.isChecked());
        editor.apply();

        Toast.makeText(this, "Pengaturan notifikasi berhasil disimpan!", Toast.LENGTH_SHORT).show();
        finish();
    }
}