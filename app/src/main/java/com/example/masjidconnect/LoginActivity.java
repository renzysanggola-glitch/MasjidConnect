package com.example.masjidconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmailUsername, etPassword;
    private Button btnMasuk;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        etEmailUsername = findViewById(R.id.et_email_username);
        etPassword = findViewById(R.id.et_password);
        btnMasuk = findViewById(R.id.btn_masuk);

        btnMasuk.setOnClickListener(v -> {
            String inputUser = etEmailUsername.getText().toString().trim();
            String inputPass = etPassword.getText().toString().trim();

            if ((inputUser.equals("admin") || inputUser.equals("admin@alikhlas.com")) && inputPass.equals("admin123")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();

                Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show();

                // Pindah ke MainActivity setelah login sukses
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Username/Email atau Password Salah!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}