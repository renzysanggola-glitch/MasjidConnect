package com.example.masjidconnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilMasjidActivity extends AppCompatActivity {

    private EditText etNama, etAlamat, etTelepon, etEmail, etWebsite;
    private TextView tvNamaHeader;
    private Button btnSimpan;
    private ImageView btnBack;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_masjid);

        pref = getSharedPreferences("ProfilMasjidPref", Context.MODE_PRIVATE);

        etNama = findViewById(R.id.et_nama_masjid);
        etAlamat = findViewById(R.id.et_alamat_masjid);
        etTelepon = findViewById(R.id.et_telepon_masjid);
        etEmail = findViewById(R.id.et_email_masjid);
        etWebsite = findViewById(R.id.et_website_masjid);
        tvNamaHeader = findViewById(R.id.tv_nama_header);
        btnSimpan = findViewById(R.id.btn_simpan_profil);
        btnBack = findViewById(R.id.btn_back_profil);

        loadProfilData();

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnSimpan != null) {
            btnSimpan.setOnClickListener(v -> saveProfilData());
        }
    }

    private void loadProfilData() {
        String nama = pref.getString("nama_masjid", "Masjid Al-Ikhlas");
        String alamat = pref.getString("alamat_masjid", "Jl. Kebon Jeruk No. 12, Jakarta Barat, DKI Jakarta, 11530");
        String telepon = pref.getString("telepon_masjid", "+62 812-3456-7890");
        String email = pref.getString("email_masjid", "info@masjidalikhlas.id");
        String website = pref.getString("website_masjid", "www.masjidalikhlas.id");

        etNama.setText(nama);
        etAlamat.setText(alamat);
        etTelepon.setText(telepon);
        etEmail.setText(email);
        etWebsite.setText(website);
        if (tvNamaHeader != null) tvNamaHeader.setText(nama);
    }

    private void saveProfilData() {
        String nama = etNama.getText().toString().trim();
        String alamat = etAlamat.getText().toString().trim();
        String telepon = etTelepon.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String website = etWebsite.getText().toString().trim();

        if (nama.isEmpty()) {
            Toast.makeText(this, "Nama Masjid tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("nama_masjid", nama);
        editor.putString("alamat_masjid", alamat);
        editor.putString("telepon_masjid", telepon);
        editor.putString("email_masjid", email);
        editor.putString("website_masjid", website);
        editor.apply();

        if (tvNamaHeader != null) tvNamaHeader.setText(nama);

        Toast.makeText(this, "Profil Masjid berhasil disimpan!", Toast.LENGTH_SHORT).show();
        finish();
    }
}