package com.example.phygen_java_2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phygen_java_2.R;
import com.example.phygen_java_2.model.Lesson;
import com.example.phygen_java_2.model.LessonListResponse;
import com.example.phygen_java_2.network.ApiService;
import com.example.phygen_java_2.network.RetrofitClient;
import com.example.phygen_java_2.util.SharedPrefManager;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonActivity extends AppCompatActivity {
    private static final String TAG = "LessonActivity";

    private RecyclerView recyclerView;
    private EditText edtLessonName, edtChapterId;
    private Button btnCreateLesson, btnUpdateLesson, btnDeleteLesson;
    private ProgressBar progressBar;

    private Lesson selectedLesson = null;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        recyclerView = findViewById(R.id.recyclerLessons);
        edtLessonName = findViewById(R.id.edtLessonName);
        edtChapterId = findViewById(R.id.edtChapterId);
        btnCreateLesson = findViewById(R.id.btnCreateLesson);
        btnUpdateLesson = findViewById(R.id.btnUpdateLesson);
        btnDeleteLesson = findViewById(R.id.btnDeleteLesson);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        token = SharedPrefManager.getInstance(this).getToken();
        if (token == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchLessons(token);

        btnCreateLesson.setOnClickListener(v -> {
            String name = edtLessonName.getText().toString().trim();
            String chapterIdStr = edtChapterId.getText().toString().trim();

            if (name.isEmpty() || chapterIdStr.isEmpty()) {
                Toast.makeText(this, "Tên và Chapter ID là bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }

            int chapterId;
            try {
                chapterId = Integer.parseInt(chapterIdStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Chapter ID phải là số", Toast.LENGTH_SHORT).show();
                return;
            }

            createLesson(name, chapterId);
        });

        btnUpdateLesson.setOnClickListener(v -> {
            if (selectedLesson == null) {
                Toast.makeText(this, "Vui lòng chọn bài học để cập nhật", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = edtLessonName.getText().toString().trim();
            String chapterIdStr = edtChapterId.getText().toString().trim();
            if (name.isEmpty() || chapterIdStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ tên và Chapter ID", Toast.LENGTH_SHORT).show();
                return;
            }

            int chapterId = Integer.parseInt(chapterIdStr);
            selectedLesson.setName(name);
            selectedLesson.setChapterId(chapterId);

            updateLesson(selectedLesson);
        });

        btnDeleteLesson.setOnClickListener(v -> {
            if (selectedLesson == null) {
                Toast.makeText(this, "Vui lòng chọn bài học để xoá", Toast.LENGTH_SHORT).show();
                return;
            }

            deleteLesson(selectedLesson.getId());
        });
    }

    private void fetchLessons(String token) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getInstance(this);
        apiService.getLessons("Bearer " + token).enqueue(new Callback<LessonListResponse>() {
            @Override
            public void onResponse(Call<LessonListResponse> call, Response<LessonListResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Lesson> lessons = response.body().getItems();
                    if (lessons != null && !lessons.isEmpty()) {
                        for (Lesson lesson : lessons) {
                            Log.d(TAG, "Lesson fetched - ID: " + lesson.getId() + ", Name: " + lesson.getName());
                        }
                        recyclerView.setAdapter(new LessonAdapter(lessons, lesson -> {
                            if (lesson != null && lesson.getId() > 0) {
                                Log.d(TAG, "Navigating to QuestionActivity with lessonId: " + lesson.getId());
                                Intent intent = new Intent(LessonActivity.this, QuestionActivity.class);
                                intent.putExtra("lessonId", lesson.getId());
                                startActivity(intent);
                            } else {
                                Log.e(TAG, "Invalid lesson data - ID: " + (lesson != null ? lesson.getId() : "null"));
                                Toast.makeText(LessonActivity.this, "Lỗi: Dữ liệu bài học không hợp lệ", Toast.LENGTH_SHORT).show();
                            }
                        }));
                    } else {
                        Toast.makeText(LessonActivity.this, "Không có bài học nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Failed to fetch lessons: HTTP " + response.code());
                    Toast.makeText(LessonActivity.this, "Không lấy được danh sách bài học: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LessonListResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "API failure: " + t.getMessage());
                Toast.makeText(LessonActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createLesson(String name, int chapterId) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getInstance(this);
        Lesson newLesson = new Lesson();
        newLesson.setName(name);
        newLesson.setChapterId(chapterId);
        newLesson.setUserId(SharedPrefManager.getInstance(this).getUserId());

        apiService.createLesson(newLesson, "Bearer " + token).enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(LessonActivity.this, "Tạo bài học thành công", Toast.LENGTH_SHORT).show();
                    clearForm();
                    fetchLessons(token);
                } else {
                    Log.e(TAG, "Create failed: HTTP " + response.code() + " - " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            String errorMsg = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorMsg);
                            Toast.makeText(LessonActivity.this, "Lỗi: " + response.code() + " - " + errorMsg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LessonActivity.this, "Tạo thất bại: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to read error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "API failure: " + t.getMessage());
                Toast.makeText(LessonActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLesson(Lesson lesson) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getInstance(this);
        lesson.setUserId(SharedPrefManager.getInstance(this).getUserId());

        apiService.updateLesson(lesson.getId(), lesson, "Bearer " + token).enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(LessonActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    clearForm();
                    fetchLessons(token);
                } else {
                    Log.e(TAG, "Update failed: HTTP " + response.code() + " - " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            String errorMsg = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorMsg);
                            Toast.makeText(LessonActivity.this, "Lỗi: " + response.code() + " - " + errorMsg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LessonActivity.this, "Lỗi cập nhật: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to read error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "API failure: " + t.getMessage());
                Toast.makeText(LessonActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteLesson(int lessonId) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getInstance(this);

        apiService.deleteLesson(lessonId, "Bearer " + token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(LessonActivity.this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                    clearForm();
                    fetchLessons(token);
                } else {
                    Toast.makeText(LessonActivity.this, "Lỗi xoá: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LessonActivity.this, "Lỗi xoá: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearForm() {
        edtLessonName.setText("");
        edtChapterId.setText("");
        selectedLesson = null;
    }
}