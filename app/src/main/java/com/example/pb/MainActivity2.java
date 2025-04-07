package com.example.pb;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pb.Models.UserDetails;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {
    Button btnSignUp;
    TextInputEditText usernameSignUp, passwordSignUp, nimPengguna, emailPengguna;
    FirebaseAuth mAuth;
    private static final String TAG = "MainActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnSignUp = findViewById(R.id.btnSignUp);
        usernameSignUp = findViewById(R.id.usernameSignUp);
        emailPengguna = findViewById(R.id.emailPengguna);
        passwordSignUp = findViewById(R.id.passwordSignUp);
        nimPengguna = findViewById(R.id.nimPengguna);
        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(view -> {
            String username, email, password, NIM;

            username = String.valueOf(usernameSignUp.getText()).trim();
            email = String.valueOf(emailPengguna.getText()).trim();
            password = String.valueOf(passwordSignUp.getText()).trim();
            NIM = String.valueOf(nimPengguna.getText()).trim();

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(MainActivity2.this, "Enter username", Toast.LENGTH_LONG).show();
                usernameSignUp.requestFocus();
            } else if (TextUtils.isEmpty(email)) {
                Toast.makeText(MainActivity2.this, "Enter email", Toast.LENGTH_LONG).show();
                emailPengguna.requestFocus();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(MainActivity2.this, "Enter password", Toast.LENGTH_LONG).show();
                passwordSignUp.requestFocus();
            } else if (TextUtils.isEmpty(NIM)) {
                Toast.makeText(MainActivity2.this, "Enter NIM", Toast.LENGTH_LONG).show();
                nimPengguna.requestFocus();
            } else {
                registerUser(username, email, password, NIM);
            }
        });
    }

    private void registerUser(String username, String email, String password, String NIM) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity2.this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser fUser = mAuth.getCurrentUser();
                if (fUser == null) {
                    Toast.makeText(MainActivity2.this, "User not created", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "FirebaseUser is null after registration");
                    return;
                }

                String uid = fUser.getUid();
                UserDetails userDetails = new UserDetails(uid, username, email, password, NIM);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.child(uid).setValue(userDetails).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(MainActivity2.this, "Account created successfully!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity2.this, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e(TAG, "Database write failed: " + task1.getException());
                        Toast.makeText(MainActivity2.this, "Account registration failed: Database error", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseAuthUserCollisionException e) {
                    emailPengguna.setError("Email is already registered");
                } catch (Exception e) {
                    Log.e(TAG, "Register failed: " + e.getMessage());
                    Toast.makeText(MainActivity2.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
