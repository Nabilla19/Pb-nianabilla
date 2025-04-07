package com.example.pb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private TextView tvTitle;
    private Button btnGoToFinance, btnGoToMemo, btnGoToTodo;
    private FirebaseAuth mAuth;

    public HomeFragment() {
        // Konstruktor kosong diperlukan
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvTitle = view.findViewById(R.id.tvTitle);
        btnGoToFinance = view.findViewById(R.id.btnGoToFinance);
        btnGoToMemo = view.findViewById(R.id.btnGoToMemo);
        btnGoToTodo = view.findViewById(R.id.btnGoToTodo);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String email = user.getEmail();
            String username = email != null && email.contains("@") ? email.split("@")[0] : "User";
            tvTitle.setText("Halo, " + username + "!");
        } else {
            tvTitle.setText("Halo, Pengguna!");
        }

        btnGoToFinance.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FinanceActivity.class);
            startActivity(intent);
        });

        btnGoToMemo.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MemoActivity.class);
            startActivity(intent);
        });

        btnGoToTodo.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), TodoActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
