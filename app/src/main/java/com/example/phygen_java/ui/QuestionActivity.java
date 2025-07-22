package com.example.phygen_java.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phygen_java.R;
import com.example.phygen_java.model.Question;
import com.example.phygen_java.model.QuestionResponse;
import com.example.phygen_java.network.ApiService;
import com.example.phygen_java.network.RetrofitClient;
import com.example.phygen_java.util.SharedPrefManager;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = "QuestionActivity";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button btnCreateQuestion, btnDeleteQuestion;
    private int selectedQuestionId = -1; // Lưu ID câu hỏi được chọn để xóa
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        recyclerView = findViewById(R.id.recyclerQuestions);
        progressBar = findViewById(R.id.progressBar);
        btnCreateQuestion = findViewById(R.id.btnCreateQuestion);
        btnDeleteQuestion = findViewById(R.id.btnDeleteQuestion);

        if (recyclerView == null || progressBar == null || btnCreateQuestion == null || btnDeleteQuestion == null) {
            Log.e(TAG, "Layout chưa khởi tạo đúng các thành phần.");
            finish();
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        token = SharedPrefManager.getInstance(this).getToken();
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Thiết lập adapter với khả năng chọn câu hỏi
        fetchQuestions(token);

        // Xử lý nút Tạo Câu Hỏi
        btnCreateQuestion.setOnClickListener(v -> {
            Intent intent = new Intent(QuestionActivity.this, CreateQuestionActivity.class);
            startActivity(intent);
        });

        // Xử lý nút Xóa Câu Hỏi
        btnDeleteQuestion.setOnClickListener(v -> {
            if (selectedQuestionId == -1) {
                Toast.makeText(this, "Vui lòng chọn một câu hỏi để xóa", Toast.LENGTH_SHORT).show();
                return;
            }
            deleteQuestion(selectedQuestionId);
        });
    }

    private void fetchQuestions(String token) {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "Fetching all questions");
        ApiService apiService = RetrofitClient.getInstance(this);
        Call<QuestionResponse> call = apiService.getAllQuestions("Bearer " + token);

        call.enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Question> questions = response.body().getItems();
                    Log.d(TAG, "Total questions from API: " + (questions != null ? questions.size() : 0));
                    if (!questions.isEmpty()) {
                        recyclerView.setAdapter(new QuestionAdapter(questions, question -> {
                            selectedQuestionId = question.getId();
                            Toast.makeText(QuestionActivity.this, "Đã chọn câu hỏi ID: " + selectedQuestionId, Toast.LENGTH_SHORT).show();
                        }));
                    } else {
                        Toast.makeText(QuestionActivity.this, "Không có câu hỏi nào", Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(null); // Xóa adapter nếu không có dữ liệu
                    }
                } else {
                    String errorMsg = "Không thể lấy câu hỏi: HTTP " + response.code();
                    try {
                        if (response.errorBody() != null) errorMsg = response.errorBody().string();
                    } catch (IOException e) {
                        Log.e(TAG, "Lỗi khi đọc errorBody: " + e.getMessage());
                    }
                    Log.e(TAG, errorMsg);
                    Toast.makeText(QuestionActivity.this, "Lỗi: " + errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "API lỗi: " + t.getMessage());
                Toast.makeText(QuestionActivity.this, "Lỗi mạng hoặc máy chủ: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteQuestion(int questionId) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getInstance(this);
        apiService.deleteQuestion(questionId, "Bearer " + token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(QuestionActivity.this, "Xóa câu hỏi thành công", Toast.LENGTH_SHORT).show();
                    selectedQuestionId = -1; // Reset sau khi xóa
                    fetchQuestions(token); // Làm mới danh sách
                } else {
                    Log.e(TAG, "Delete failed: HTTP " + response.code() + " - " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            String errorMsg = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorMsg);
                            Toast.makeText(QuestionActivity.this, "Lỗi: " + response.code() + " - " + errorMsg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(QuestionActivity.this, "Xóa thất bại: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to read error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "API failure: " + t.getMessage());
                Toast.makeText(QuestionActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Làm mới danh sách khi quay lại từ CreateQuestionActivity
        if (token != null) fetchQuestions(token);
    }
}