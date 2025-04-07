package com.example.pb;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TodoActivity extends AppCompatActivity {
    private EditText inputTugas;
    private Button tombolTambahTugas;
    private ListView daftarTugas;
    private ArrayList<String> daftar;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        inputTugas = findViewById(R.id.inputTask);
        tombolTambahTugas = findViewById(R.id.addTaskBtn);
        daftarTugas = findViewById(R.id.taskList);

        daftar = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, daftar);
        daftarTugas.setAdapter(adapter);

        tombolTambahTugas.setOnClickListener(v -> {
            String tugas = inputTugas.getText().toString();
            if (!tugas.isEmpty()) {
                daftar.add(tugas);
                adapter.notifyDataSetChanged();
                inputTugas.setText("");
            }
        });
    }
}
