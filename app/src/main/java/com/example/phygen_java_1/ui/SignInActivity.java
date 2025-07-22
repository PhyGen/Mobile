package com.example.phygen_java_1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phygen_java_1.R;
import com.example.phygen_java_1.model.GoogleLoginRequest;
import com.example.phygen_java_1.model.LoginRequest;
import com.example.phygen_java_1.model.LoginResponse;
import com.example.phygen_java_1.model.User;
import com.example.phygen_java_1.network.ApiService;
import com.example.phygen_java_1.network.RetrofitClient;
import com.example.phygen_java_1.util.SharedPrefManager;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    private EditText emailInput, passwordInput;
    private Button signInBtn, googleBtn;
    private TextView goToSignUp, forgotPassword;

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    private final ActivityResultLauncher<IntentSenderRequest> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                        String idToken = credential.getGoogleIdToken();

                        if (idToken != null) {
                            Log.d(TAG, "ID Token received from Google: " + idToken);
                            sendIdTokenToBackend(idToken);
                        } else {
                            Log.e(TAG, "ID Token is null");
                            Toast.makeText(this, "Không nhận được ID Token", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ApiException e) {
                        Log.e(TAG, "Google Sign-In failed", e);
                        Toast.makeText(this, "Đăng nhập Google thất bại: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.w(TAG, "Sign-in result failed or canceled, resultCode: " + result.getResultCode());
                    Toast.makeText(this, "Đăng nhập Google bị hủy", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharedPrefManager.getInstance(this).getToken() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_sign_in);

        emailInput = findViewById(R.id.inputEmail);
        passwordInput = findViewById(R.id.inputPassword);
        signInBtn = findViewById(R.id.btnSignIn);
        googleBtn = findViewById(R.id.btnGoogle);
        goToSignUp = findViewById(R.id.goToSignUp);
        forgotPassword = findViewById(R.id.forgotPassword);

        signInBtn.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(email, password);
        });

        googleBtn.setOnClickListener(view -> startGoogleSignIn());

        goToSignUp.setOnClickListener(view ->
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class))
        );

        if (forgotPassword != null) {
            forgotPassword.setOnClickListener(view ->
                    Toast.makeText(this, "Chức năng quên mật khẩu chưa được triển khai", Toast.LENGTH_SHORT).show()
            );
        }

        oneTapClient = Identity.getSignInClient(this);

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(getString(R.string.default_web_client_id))
                                .setFilterByAuthorizedAccounts(false)
                                .build()
                )
                .setAutoSelectEnabled(true)
                .build();
    }

    private void startGoogleSignIn() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(result -> {
                    try {
                        IntentSenderRequest request = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                        googleSignInLauncher.launch(request);
                    } catch (Exception e) {
                        Log.e(TAG, "Launch SignIn Intent failed", e);
                        Toast.makeText(this, "Không thể khởi động Google Sign-In", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "One Tap Sign-in failed", e);
                    Toast.makeText(this, "Không thể khởi động Google Sign-In", Toast.LENGTH_SHORT).show();
                });
    }

    private void sendIdTokenToBackend(String idToken) {
        GoogleLoginRequest request = new GoogleLoginRequest(idToken);
        ApiService apiService = RetrofitClient.getInstance(this);

        apiService.googleLogin(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    int userId = response.body().getUserId();
                    SharedPrefManager.getInstance(SignInActivity.this).saveToken(token);
                    SharedPrefManager.getInstance(SignInActivity.this).saveUserId(userId);
                    Log.d(TAG, "Login successful, token: " + token + ", userId: " + userId);
                    fetchUserId(token);
                    Toast.makeText(SignInActivity.this, "Đăng nhập Google thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    int statusCode = response.code();
                    String errorMsg = "Lỗi đăng nhập Google: HTTP " + statusCode;
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            errorMsg += " - " + errorBody;
                            Log.e(TAG, "Error body: " + errorBody);
                        } catch (IOException e) {
                            errorMsg += " - Không thể đọc lỗi chi tiết";
                            Log.e(TAG, "Error reading error body", e);
                        }
                    }
                    Log.e(TAG, errorMsg);
                    Toast.makeText(SignInActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Google login error: " + t.getMessage());
                Toast.makeText(SignInActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginUser(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password, null);
        ApiService apiService = RetrofitClient.getInstance(this);
        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    int userId = response.body().getUserId();
                    SharedPrefManager.getInstance(SignInActivity.this).saveToken(token);
                    SharedPrefManager.getInstance(SignInActivity.this).saveUserId(userId);
                    fetchUserId(token);
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
    }

    private void fetchUserId(String token) {
        ApiService apiService = RetrofitClient.getInstance(this);
        apiService.getUserById("Bearer " + token, 0).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int userId = response.body().getId();
                    SharedPrefManager.getInstance(SignInActivity.this).saveUserId(userId);
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e(TAG, "Failed to fetch userId: HTTP " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Fetch userId error: " + t.getMessage());
            }
        });
    }
}