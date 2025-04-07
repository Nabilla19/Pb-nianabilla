package com.example.pb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MemoActivity extends AppCompatActivity {
    private EditText inputMemo;
    private TextView memoTersimpan;
    private Button tombolSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        inputMemo = findViewById(R.id.inputMemo);
        memoTersimpan = findViewById(R.id.savedMemo);
        tombolSimpan = findViewById(R.id.saveMemoBtn);

        tombolSimpan.setOnClickListener(v -> {
            String memo = inputMemo.getText().toString();
            memoTersimpan.setText("Memo Tersimpan:\n" + memo);
        });
    }
}
