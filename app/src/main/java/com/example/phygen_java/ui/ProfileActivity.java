package com.example.phygen_java.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phygen_java.R;
import com.example.phygen_java.model.User;
import com.example.phygen_java.network.ApiService;
import com.example.phygen_java.network.RetrofitClient;
import com.example.phygen_java.util.SharedPrefManager;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private TextView tvName, tvEmail, tvUserId;
    private MaterialButton btnLogout, btnRefresh, btnEditProfile;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvUserId = findViewById(R.id.tvUserId);
        btnLogout = findViewById(R.id.btnLogout);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        progressBar = findViewById(R.id.progressBar);

        fetchAndDisplayUser();

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        btnRefresh.setOnClickListener(v -> fetchAndDisplayUser());

        btnLogout.setOnClickListener(v -> {
            SharedPrefManager.getInstance(this).clear();
            Intent intent = new Intent(this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAndDisplayUser();
    }

    private void displayUserInfo(User user) {
        if (user != null) {
            tvName.setText("Tên: " + (user.getName() != null ? user.getName() : "N/A"));
            tvEmail.setText("Email: " + (user.getEmail() != null ? user.getEmail() : "N/A"));
            tvUserId.setText("ID: " + user.getId());
        } else {
            tvName.setText("Tên: Không có dữ liệu");
            tvEmail.setText("Email: Không có dữ liệu");
            tvUserId.setText("ID: Không có dữ liệu");
            Log.w(TAG, "No user data to display");
        }
    }

    private void fetchAndDisplayUser() {
        progressBar.setVisibility(ProgressBar.VISIBLE);

        String token = SharedPrefManager.getInstance(this).getToken();
        if (token == null) {
            Log.e(TAG, "Token is null");
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        Log.d(TAG, "Token: " + token.substring(0, Math.min(token.length(), 20)) + "...");

        String userId = getUserIdFromToken(token);
        if (userId == null) {
            Log.e(TAG, "Could not extract user ID from token");
            Toast.makeText(this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            displayUserInfo(null);
            progressBar.setVisibility(ProgressBar.GONE);
            return;
        }
        Log.d(TAG, "Fetched user ID: " + userId);

        ApiService apiService = RetrofitClient.getInstance(this);
        apiService.getUserById(Integer.parseInt(userId), "Bearer " + token)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        progressBar.setVisibility(ProgressBar.GONE);
                        Log.d(TAG, "GET response code: " + response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            User freshUser = response.body();
                            boolean saved = SharedPrefManager.getInstance(ProfileActivity.this).saveUser(freshUser);
                            if (!saved) {
                                Log.e(TAG, "Failed to save user to SharedPref");
                            }
                            displayUserInfo(freshUser);
                            Log.d(TAG, "User fetched: " + freshUser.getEmail());
                        } else {
                            Log.e(TAG, "Failed to fetch user: HTTP " + response.code());
                            Toast.makeText(ProfileActivity.this, "Không thể lấy thông tin người dùng: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                            displayUserInfo(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        progressBar.setVisibility(ProgressBar.GONE);
                        Log.e(TAG, "Error fetching user: " + t.getMessage());
                        Toast.makeText(ProfileActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        displayUserInfo(null);
                    }
                });
    }

    private String getUserIdFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                Log.e(TAG, "Invalid JWT format, parts length: " + parts.length);
                return null;
            }
            String payload = new String(android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT));
            JSONObject jsonObject = new JSONObject(payload);
            String id = jsonObject.getString("id");
            Log.d(TAG, "Extracted ID from token: " + id);
            return id;
        } catch (Exception e) {
            Log.e(TAG, "Error decoding token payload: " + e.getMessage());
            return null;
        }
    }
}