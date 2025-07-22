package com.example.phygen_java.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phygen_java.R;
import com.example.phygen_java.model.Question;
import com.example.phygen_java.network.ApiService;
import com.example.phygen_java.network.RetrofitClient;
import com.example.phygen_java.util.SharedPrefManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateQuestionActivity extends AppCompatActivity {

    private static final String TAG = "CreateQuestionActivity";

    private EditText edtContent, edtQuestionSource, edtLessonId, edtQuestionId;
    private Button btnCreateQuestion, btnDeleteQuestion;
    private ProgressBar progressBar;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        edtContent = findViewById(R.id.edtContent);
        edtQuestionSource = findViewById(R.id.edtQuestionSource);
        edtLessonId = findViewById(R.id.edtLessonId);
        edtQuestionId = findViewById(R.id.edtQuestionId);
        btnCreateQuestion = findViewById(R.id.btnCreateQuestion);
        btnDeleteQuestion = findViewById(R.id.btnDeleteQuestion);
        progressBar = findViewById(R.id.progressBar);

        token = SharedPrefManager.getInstance(this).getToken();
        if (token == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnCreateQuestion.setOnClickListener(v -> {
            String content = edtContent.getText().toString().trim();
            String questionSource = edtQuestionSource.getText().toString().trim();
            String lessonIdStr = edtLessonId.getText().toString().trim();

            if (content.isEmpty() || questionSource.isEmpty() || lessonIdStr.isEmpty()) {
                Toast.makeText(this, "Nội dung, nguồn và lessonId là bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }

            int lessonId;
            try {
                lessonId = Integer.parseInt(lessonIdStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Lesson ID phải là số", Toast.LENGTH_SHORT).show();
                return;
            }

            createQuestion(content, questionSource, lessonId);
        });

        btnDeleteQuestion.setOnClickListener(v -> {
            String questionIdStr = edtQuestionId.getText().toString().trim();
            if (questionIdStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập Question ID để xóa", Toast.LENGTH_SHORT).show();
                return;
            }

            int questionId;
            try {
                questionId = Integer.parseInt(questionIdStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Question ID phải là số", Toast.LENGTH_SHORT).show();
                return;
            }

            deleteQuestion(questionId);
        });
    }

    private void createQuestion(String content, String questionSource, int lessonId) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getInstance(this);
        int createdByUserId = SharedPrefManager.getInstance(this).getUserId();
        Question newQuestion = new Question(content, questionSource, "Easy", lessonId, createdByUserId);

        apiService.createQuestion(newQuestion, "Bearer " + token).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreateQuestionActivity.this, "Tạo câu hỏi thành công", Toast.LENGTH_SHORT).show();
                    clearForm();
                    // Quay lại QuestionActivity và làm mới
                    finish();
                } else {
                    Log.e(TAG, "Create failed: HTTP " + response.code() + " - " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            String errorMsg = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorMsg);
                            Toast.makeText(CreateQuestionActivity.this, "Lỗi: " + response.code() + " - " + errorMsg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CreateQuestionActivity.this, "Tạo thất bại: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to read error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "API failure: " + t.getMessage());
                Toast.makeText(CreateQuestionActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CreateQuestionActivity.this, "Xóa câu hỏi thành công", Toast.LENGTH_SHORT).show();
                    clearForm();
                    // Quay lại QuestionActivity và làm mới
                    finish();
                } else {
                    Log.e(TAG, "Delete failed: HTTP " + response.code() + " - " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            String errorMsg = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorMsg);
                            Toast.makeText(CreateQuestionActivity.this, "Lỗi: " + response.code() + " - " + errorMsg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CreateQuestionActivity.this, "Xóa thất bại: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CreateQuestionActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearForm() {
        edtContent.setText("");
        edtQuestionSource.setText("");
        edtLessonId.setText("");
        edtQuestionId.setText("");
    }
}