package com.example.phygen_java.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phygen_java.R;
import com.example.phygen_java.model.UpdateProfileRequest;
import com.example.phygen_java.model.User;
import com.example.phygen_java.network.ApiService;
import com.example.phygen_java.network.RetrofitClient;
import com.example.phygen_java.util.SharedPrefManager;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";
    private EditText edtFullName, edtEmail, edtPhone, edtAvatarUrl;
    private Button btnSave;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtAvatarUrl = findViewById(R.id.edtAvatarUrl);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);

        fetchUserInfo();

        btnSave.setOnClickListener(v -> {
            String name = edtFullName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String avatar = edtAvatarUrl.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Tên và email là bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }

            String token = SharedPrefManager.getInstance(this).getToken();
            if (token == null) {
                Toast.makeText(this, "Token không hợp lệ, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = getUserIdFromToken(token);
            if (userId == null) {
                Toast.makeText(this, "Không thể xác định người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Updating profile for user ID: " + userId);
            Log.d(TAG, "Token: " + token.substring(0, Math.min(token.length(), 20)) + "...");
            Log.d(TAG, "Request payload: fullName=" + name + ", email=" + email + ", phone=" + phone + ", avatar=" + avatar);

            UpdateProfileRequest request = new UpdateProfileRequest(name, email, phone, avatar);
            ApiService apiService = RetrofitClient.getInstance(this);

            progressBar.setVisibility(ProgressBar.VISIBLE);
            apiService.updateUserProfile("Bearer " + token, Integer.parseInt(userId), request)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            progressBar.setVisibility(ProgressBar.GONE);
                            Log.d(TAG, "PUT response code: " + response.code());
                            if (response.isSuccessful()) {
                                Log.d(TAG, "Profile update API call successful");
                                Toast.makeText(EditProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                updateSharedPreferencesAndVerify(userId, token);
                            } else {
                                Log.e(TAG, "Profile update failed: HTTP " + response.code());
                                Toast.makeText(EditProfileActivity.this, "Lỗi cập nhật: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            progressBar.setVisibility(ProgressBar.GONE);
                            Log.e(TAG, "Error updating profile: " + t.getMessage());
                            Toast.makeText(EditProfileActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private void fetchUserInfo() {
        progressBar.setVisibility(ProgressBar.VISIBLE);

        String token = SharedPrefManager.getInstance(this).getToken();
        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        String userId = getUserIdFromToken(token);
        if (userId == null) {
            Log.e(TAG, "Could not extract user ID from token");
            Toast.makeText(this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(ProgressBar.GONE);
            return;
        }

        ApiService apiService = RetrofitClient.getInstance(this);
        apiService.getUserById(Integer.parseInt(userId), "Bearer " + token)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        progressBar.setVisibility(ProgressBar.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            User user = response.body();
                            boolean saved = SharedPrefManager.getInstance(EditProfileActivity.this).saveUser(user);
                            if (!saved) {
                                Log.e(TAG, "Failed to save user to SharedPref");
                            }
                            edtFullName.setText(user.getName() != null ? user.getName() : "");
                            edtEmail.setText(user.getEmail() != null ? user.getEmail() : "");
                            edtPhone.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
                            edtAvatarUrl.setText(user.getAvatarUrl() != null ? user.getAvatarUrl() : "");
                            Log.d(TAG, "User fetched: " + user.getEmail());
                        } else {
                            Log.e(TAG, "Failed to fetch user: HTTP " + response.code());
                            Toast.makeText(EditProfileActivity.this, "Không thể lấy thông tin người dùng: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        progressBar.setVisibility(ProgressBar.GONE);
                        Log.e(TAG, "Error fetching user: " + t.getMessage());
                        Toast.makeText(EditProfileActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updateSharedPreferencesAndVerify(String userId, String token) {
        ApiService apiService = RetrofitClient.getInstance(this);
        apiService.getUserById(Integer.parseInt(userId), "Bearer " + token)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        progressBar.setVisibility(ProgressBar.GONE);
                        Log.d(TAG, "GET response code: " + response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            User freshUser = response.body();
                            boolean saved = SharedPrefManager.getInstance(EditProfileActivity.this).saveUser(freshUser);
                            if (!saved) {
                                Log.e(TAG, "Failed to save updated user to SharedPref");
                            }
                            Log.d(TAG, "Verified updated user: " + freshUser.getName() + ", " + freshUser.getEmail());
                        } else {
                            Log.e(TAG, "Failed to verify updated user: HTTP " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        progressBar.setVisibility(ProgressBar.GONE);
                        Log.e(TAG, "Error verifying updated user: " + t.getMessage());
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