package com.example.phygen_java.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.phygen_java.R;
import com.example.phygen_java.model.User;
import com.example.phygen_java.util.SharedPrefManager;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        btnLogout = findViewById(R.id.btnLogout);

        // Lấy dữ liệu user từ SharedPref
        User user = SharedPrefManager.getInstance(this).getUser();
        if (user != null) {
            tvName.setText("Tên: " + user.getName());
            tvEmail.setText("Email: " + user.getEmail());
        } else {
            tvName.setText("Không có người dùng");
            tvEmail.setText("");
        }

        btnLogout.setOnClickListener(v -> {
            SharedPrefManager.getInstance(this).clear();
            Intent intent = new Intent(this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}