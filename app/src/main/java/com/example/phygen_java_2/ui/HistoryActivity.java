package com.example.phygen_java_2.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phygen_java_2.R;
import com.example.phygen_java_2.model.ExamHistory;
import com.example.phygen_java_2.network.ApiService;
import com.example.phygen_java_2.network.RetrofitClient;
import com.example.phygen_java_2.util.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerHistory);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy vai trò và userId từ SharedPref
        String role = SharedPrefManager.getInstance(this).getRole();
        int userId = SharedPrefManager.getInstance(this).getUserId();
        String currentUserId = String.valueOf(userId);
        String token = SharedPrefManager.getInstance(this).getToken();

        Log.d(TAG, "Role: " + (role != null ? role : "null"));
        Log.d(TAG, "UserId: " + currentUserId);
        Log.d(TAG, "Token: " + (token != null ? token.substring(0, 10) + "..." : "null"));

        if (role == null || token == null || userId < 0) { // Chỉ từ chối khi userId < 0
            Log.e(TAG, "Login check failed - role: " + (role != null ? role : "null") + ", token: " + (token != null ? "not null" : "null") + ", userId: " + userId);
            Toast.makeText(this, "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getInstance(this);

        List<String> userIdsToFetch = new ArrayList<>();
        if ("admin".equals(role)) {
            // TODO: Cập nhật logic để lấy tất cả userId cho admin (cần API hỗ trợ)
            userIdsToFetch.add(currentUserId); // Placeholder, cần thay bằng danh sách thực tế
        } else {
            userIdsToFetch.add(currentUserId);
        }

        fetchHistory(apiService, token, userIdsToFetch);
    }

    private void fetchHistory(ApiService apiService, String token, List<String> userIds) {
        List<ExamHistory> allHistory = new ArrayList<>();
        for (String userId : userIds) {
            Call<List<ExamHistory>> call = apiService.getExamHistory("Bearer " + token, userId);
            call.enqueue(new Callback<List<ExamHistory>>() {
                @Override
                public void onResponse(Call<List<ExamHistory>> call, Response<List<ExamHistory>> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        allHistory.addAll(response.body());
                        adapter = new HistoryAdapter(allHistory);
                        recyclerView.setAdapter(adapter);
                        Log.d(TAG, "History loaded for userId: " + userId + ", count: " + response.body().size());
                    } else {
                        int statusCode = response.code();
                        Log.e(TAG, "Lỗi API cho userId " + userId + ": HTTP " + statusCode + ", message: " + response.message());
                        if (statusCode == 404) { // Giả định 404 nghĩa là chưa tạo đề
                            Toast.makeText(HistoryActivity.this, "Bạn chưa tạo đề", Toast.LENGTH_SHORT).show();
                        } else if (statusCode == 401 || statusCode == 403) { // Unauthorized
                            Log.e(TAG, "Unauthorized access, token may be invalid");
                            Toast.makeText(HistoryActivity.this, "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(HistoryActivity.this, "Lỗi tải lịch sử cho user " + userId, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<ExamHistory>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Lỗi mạng cho userId " + userId + ": " + t.getMessage(), t);
                    Toast.makeText(HistoryActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}