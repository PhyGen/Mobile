package com.example.phygen_java.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phygen_java.R;
import com.example.phygen_java.model.LoginRequest;
import com.example.phygen_java.model.LoginResponse;
import com.example.phygen_java.network.ApiService;
import com.example.phygen_java.network.RetrofitClient;
import com.example.phygen_java.util.LocaleHelper;
import com.example.phygen_java.util.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button signInBtn, googleBtn;
    TextView goToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Đổi ngôn ngữ
        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        String lang = pref.getLanguage();
        LocaleHelper.setLocale(this, lang);

        setContentView(R.layout.activity_sign_in);

        emailInput = findViewById(R.id.inputEmail);
        passwordInput = findViewById(R.id.inputPassword);
        signInBtn = findViewById(R.id.btnSignIn);
        googleBtn = findViewById(R.id.btnGoogle);
        goToSignUp = findViewById(R.id.goToSignUp);

        signInBtn.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String pass = passwordInput.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest loginRequest = new LoginRequest(email, pass);
            ApiService apiService = RetrofitClient.getInstance();

            apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String token = response.body().getToken();
                        SharedPrefManager.getInstance(SignInActivity.this).saveToken(token);

                        // Nếu response có user, bạn có thể lưu luôn:
                        if (response.body().getUser() != null) {
                            SharedPrefManager.getInstance(SignInActivity.this).saveUser(response.body().getUser());
                        }

                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(SignInActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        googleBtn.setOnClickListener(view ->
                Toast.makeText(this, "Google Sign-In chưa hỗ trợ", Toast.LENGTH_SHORT).show()
        );

        goToSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
