package com.example.phygen_java_2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phygen_java_2.R;
import com.example.phygen_java_2.model.User;
import com.example.phygen_java_2.network.ApiService;
import com.example.phygen_java_2.network.RetrofitClient;
import com.example.phygen_java_2.util.SharedPrefManager;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private TextView tvName, tvEmail, tvUserId, tvRoleId;
    private MaterialButton btnLogout, btnRefresh, btnEditProfile;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvUserId = findViewById(R.id.tvUserId);
        tvRoleId = findViewById(R.id.tvRoleId);
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
            tvRoleId.setText("Role ID: " + user.getRoleId());
            Log.d(TAG, "Displayed ID: " + user.getId());
        } else {
            tvName.setText("Tên: Không có dữ liệu");
            tvEmail.setText("Email: Không có dữ liệu");
            tvUserId.setText("ID: Không có dữ liệu");
            tvRoleId.setText("Role ID: Không có dữ liệu");
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

        int userId = SharedPrefManager.getInstance(this).getUserId();
        if (userId == 0) {
            Log.w(TAG, "UserId is 0, attempting to extract from token");
            String tokenUserId = getUserIdFromToken(token);
            if (tokenUserId != null) {
                try {
                    userId = Integer.parseInt(tokenUserId); // Gán giá trị mới nhưng không sử dụng trực tiếp trong inner class
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Invalid userId from token: " + tokenUserId, e);
                    Toast.makeText(this, "ID từ token không hợp lệ", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(ProgressBar.GONE);
                    return;
                }
            } else {
                Log.e(TAG, "Could not extract user ID from token");
                Toast.makeText(this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                displayUserInfo(null);
                progressBar.setVisibility(ProgressBar.GONE);
                return;
            }
        }

        final int finalUserId = userId; // Khai báo biến final để sử dụng trong inner class
        Log.d(TAG, "Using user ID: " + finalUserId);

        ApiService apiService = RetrofitClient.getInstance(this);
        apiService.getUserById(finalUserId, "Bearer " + token)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        progressBar.setVisibility(ProgressBar.GONE);
                        Log.d(TAG, "GET response code: " + response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            User freshUser = response.body();
                            int apiUserId = freshUser.getId();
                            Log.d(TAG, "API returned user ID: " + apiUserId);
                            if (apiUserId != finalUserId) {
                                Log.w(TAG, "Mismatch: Stored ID (" + finalUserId + ") != API ID (" + apiUserId + ")");
                            }
                            boolean saved = SharedPrefManager.getInstance(ProfileActivity.this).saveUser(freshUser);
                            if (!saved) {
                                Log.e(TAG, "Failed to save user to SharedPref");
                            }
                            displayUserInfo(freshUser);
                            Log.d(TAG, "User fetched: " + freshUser.getEmail());
                        } else {
                            Log.e(TAG, "Failed to fetch user: HTTP " + response.code() + " - " + response.message());
                            Toast.makeText(ProfileActivity.this, "Không thể lấy thông tin người dùng: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                            displayUserInfo(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        progressBar.setVisibility(ProgressBar.GONE);
                        Log.e(TAG, "Error fetching user: " + t.getMessage(), t);
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
            if (jsonObject.has("id")) {
                String id = jsonObject.getString("id");
                Log.d(TAG, "Extracted ID from token: " + id);
                return id;
            } else {
                Log.e(TAG, "Token payload does not contain 'id' field");
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error decoding token payload: " + e.getMessage(), e);
            return null;
        }
    }
}