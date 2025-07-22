package com.example.phygen_java.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phygen_java.R;
import com.example.phygen_java.model.LoginRequest;
import com.example.phygen_java.model.LoginResponse;
import com.example.phygen_java.model.User;
import com.example.phygen_java.network.ApiService;
import com.example.phygen_java.network.RetrofitClient;
import com.example.phygen_java.util.LocaleHelper;
import com.example.phygen_java.util.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private EditText emailInput, passwordInput;
    private Button signInBtn, googleBtn;
    private TextView goToSignUp, forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra trạng thái đăng nhập
        if (SharedPrefManager.getInstance(this).getToken() != null) {
            Log.d(TAG, "Token found, redirecting to HomeActivity");
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        // Đổi ngôn ngữ
//        SharedPrefManager pref = SharedPrefManager.getInstance(this);
//        String lang = pref.getLanguage();
//        LocaleHelper.setLocale(this, lang);

        setContentView(R.layout.activity_sign_in);

        // Khởi tạo các thành phần giao diện
        emailInput = findViewById(R.id.inputEmail);
        passwordInput = findViewById(R.id.inputPassword);
        signInBtn = findViewById(R.id.btnSignIn);
        googleBtn = findViewById(R.id.btnGoogle);
        goToSignUp = findViewById(R.id.goToSignUp);
        forgotPassword = findViewById(R.id.forgotPassword);

        // Xử lý sự kiện nhấn nút đăng nhập
        signInBtn.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(email, password);
        });

        // Xử lý sự kiện nhấn nút Google Sign-In
        googleBtn.setOnClickListener(view ->
                Toast.makeText(this, "Google Sign-In chưa hỗ trợ", Toast.LENGTH_SHORT).show()
        );

        // Chuyển sang màn hình đăng ký
        goToSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện nhấn "Quên mật khẩu"
        if (forgotPassword != null) {
            forgotPassword.setOnClickListener(view ->
                    Toast.makeText(this, "Chức năng quên mật khẩu chưa được triển khai", Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void loginUser(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        ApiService apiService = RetrofitClient.getInstance(SignInActivity.this);

        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    SharedPrefManager.getInstance(SignInActivity.this).saveToken(token);
                    Log.d(TAG, "Login successful, token: " + token);
                    fetchUserId(token);
                    // Chuyển hướng đến HomeActivity
                    Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg;
                    switch (response.code()) {
                        case 400:
                            errorMsg = "Yêu cầu không hợp lệ";
                            break;
                        case 401:
                            errorMsg = "Sai tài khoản hoặc mật khẩu";
                            break;
                        case 500:
                            errorMsg = "Lỗi server, vui lòng thử lại sau";
                            break;
                        default:
                            errorMsg = "Lỗi không xác định: HTTP " + response.code();
                    }
                    Log.e(TAG, "Login failed: " + errorMsg);
                    Toast.makeText(SignInActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Login error: " + t.getMessage());
                Toast.makeText(SignInActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchUserId(String token) {
        if (isFinishing() || isDestroyed()) {
            Log.w(TAG, "Activity is finishing or destroyed, aborting API call");
            return;
        }
        ApiService apiService = RetrofitClient.getInstance(SignInActivity.this);
        Call<User> call = apiService.getUserById("Bearer " + token, 0); // Điều chỉnh userId nếu cần
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    int userId = user.getId();
                    SharedPrefManager.getInstance(SignInActivity.this).saveUserId(userId);
                    Log.d(TAG, "Saved userId: " + userId);
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e(TAG, "Failed to fetch userId: HTTP " + response.code() + " - " + response.message());
                    // Loại bỏ thông báo lỗi
                    //Toast.makeText(SignInActivity.this, "Lỗi lấy userId: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Fetch userId error: " + t.getMessage());
                // Loại bỏ thông báo lỗi
                Toast.makeText(SignInActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}