package com.example.pb;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextInputEditText emailPengguna, password;
    CheckBox ingatSaya;
    Button btnLogin;
    TextView lupaPassword, signUp;

    FirebaseAuth mAuth;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        emailPengguna = findViewById(R.id.emailPengguna);
        password = findViewById(R.id.password);
        ingatSaya = findViewById(R.id.ingatSaya);
        btnLogin = findViewById(R.id.btnLogin);
        lupaPassword = findViewById(R.id.lupaPassword);
        signUp = findViewById(R.id.signUp);

        btnLogin.setOnClickListener(view -> {
            try {
                String email = emailPengguna.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailPengguna.setError("Masukkan email");
                    emailPengguna.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError("Masukkan password");
                    password.requestFocus();
                    return;
                }

                loginUser(email, pass);
            } catch (Exception e) {
                Log.e(TAG, "Login error: ", e);
                Toast.makeText(MainActivity.this, "Terjadi kesalahan saat login.", Toast.LENGTH_SHORT).show();
            }
        });

        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
        });

        lupaPassword.setOnClickListener(view -> {
            // (Opsional) pindah ke halaman lupa password kalau kamu sudah buat
            Toast.makeText(this, "Fitur lupa password belum diimplementasi.", Toast.LENGTH_SHORT).show();
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(MainActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e(TAG, "Login berhasil tapi user null");
                            Toast.makeText(MainActivity.this, "Terjadi kesalahan. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Login gagal: " + task.getException());
                        Toast.makeText(MainActivity.this, "Login gagal. Periksa email dan password!", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
