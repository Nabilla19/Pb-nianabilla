package com.example.pb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FinanceActivity extends AppCompatActivity {
    private EditText inputPemasukan, inputPengeluaran;
    private TextView hasil;
    private Button tombolHitung;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);

        // Inisialisasi UI
        inputPemasukan = findViewById(R.id.inputIncome);
        inputPengeluaran = findViewById(R.id.inputExpense);
        hasil = findViewById(R.id.resultFinance);
        tombolHitung = findViewById(R.id.calculateBtn);

        // Inisialisasi Firebase Realtime Database
        database = FirebaseDatabase.getInstance().getReference("keuangan");

        // Cek koneksi Firebase
        FirebaseDatabase.getInstance().getReference(".info/connected")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean connected = snapshot.getValue(Boolean.class);
                        if (Boolean.TRUE.equals(connected)) {
                            Toast.makeText(FinanceActivity.this, "Firebase Terkoneksi!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FinanceActivity.this, "Firebase Tidak Terkoneksi!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(FinanceActivity.this, "Gagal Mengecek Koneksi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Tombol hitung & simpan
        tombolHitung.setOnClickListener(v -> {
            String pemasukanStr = inputPemasukan.getText().toString().trim();
            String pengeluaranStr = inputPengeluaran.getText().toString().trim();

            if (pemasukanStr.isEmpty() || pengeluaranStr.isEmpty()) {
                Toast.makeText(this, "Isi semua field dulu ya!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double pemasukan = Double.parseDouble(pemasukanStr);
                double pengeluaran = Double.parseDouble(pengeluaranStr);
                double saldo = pemasukan - pengeluaran;

                hasil.setText("Sisa Saldo: Rp " + saldo);

                // Simpan ke Firebase pakai HashMap
                String id = database.push().getKey();
                if (id != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("pemasukan", pemasukan);
                    data.put("pengeluaran", pengeluaran);
                    data.put("saldo", saldo);

                    database.child(id).setValue(data)
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Gagal menyimpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Masukkan angka yang valid!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
