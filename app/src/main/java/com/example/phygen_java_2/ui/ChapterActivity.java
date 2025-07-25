package com.example.phygen_java_2.ui;

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
import com.example.phygen_java_2.model.Chapter;
import com.example.phygen_java_2.model.GenericResponse;
import com.example.phygen_java_2.network.ApiService;
import com.example.phygen_java_2.network.RetrofitClient;
import com.example.phygen_java_2.util.SharedPrefManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterActivity extends AppCompatActivity {
    private static final String TAG = "ChapterActivity";

    private RecyclerView recyclerChapters;
    private EditText edtChapterName, edtSemesterId;
    private Button btnCreate, btnUpdate, btnDelete;
    private ProgressBar progressBar;

    private ChapterAdapter adapter;
    private List<Chapter> chapterList = new ArrayList<>();
    private Chapter selectedChapter = null;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        recyclerChapters = findViewById(R.id.recyclerChapters);
        edtChapterName = findViewById(R.id.edtChapterName);
        edtSemesterId = findViewById(R.id.edtSemesterId);
        btnCreate = findViewById(R.id.btnCreateChapter);
        btnUpdate = findViewById(R.id.btnUpdateChapter);
        btnDelete = findViewById(R.id.btnDeleteChapter);
        progressBar = findViewById(R.id.progressBar);

        recyclerChapters.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChapterAdapter(chapterList, this::onChapterSelected);
        recyclerChapters.setAdapter(adapter);

        token = SharedPrefManager.getInstance(this).getToken();
        if (token == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchChapters();

        btnCreate.setOnClickListener(v -> {
            String name = edtChapterName.getText().toString().trim();
            String semesterIdStr = edtSemesterId.getText().toString().trim();

            if (name.isEmpty() || semesterIdStr.isEmpty()) {
                Toast.makeText(this, "Tên và Semester ID là bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }

            int semesterId;
            try {
                semesterId = Integer.parseInt(semesterIdStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Semester ID phải là số", Toast.LENGTH_SHORT).show();
                return;
            }

            createChapter(name, semesterId);
        });

        btnUpdate.setOnClickListener(v -> {
            if (selectedChapter == null) {
                Toast.makeText(this, "Vui lòng chọn chương để cập nhật", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = edtChapterName.getText().toString().trim();
            String semesterIdStr = edtSemesterId.getText().toString().trim();
            if (name.isEmpty() || semesterIdStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ tên và Semester ID", Toast.LENGTH_SHORT).show();
                return;
            }

            int semesterId = Integer.parseInt(semesterIdStr);
            selectedChapter.setName(name);
            selectedChapter.setSemesterId(semesterId);

            updateChapter(selectedChapter);
        });

        btnDelete.setOnClickListener(v -> {
            if (selectedChapter == null) {
                Toast.makeText(this, "Vui lòng chọn chương để xóa", Toast.LENGTH_SHORT).show();
                return;
            }

            deleteChapter(selectedChapter.getId());
        });
    }

    private void onChapterSelected(Chapter chapter) {
        selectedChapter = chapter;
        edtChapterName.setText(chapter.getName());
        edtSemesterId.setText(String.valueOf(chapter.getSemesterId()));
    }

    private void fetchChapters() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getInstance(this);
        apiService.getChapters("Bearer " + token)
                .enqueue(new Callback<List<Chapter>>() {
                    @Override
                    public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            chapterList.clear();
                            chapterList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "Failed to fetch chapters: HTTP " + response.code() + ", Message: " + response.message());
                            Toast.makeText(ChapterActivity.this, "Không lấy được danh sách chương: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Chapter>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "API failure: " + t.getMessage());
                        Toast.makeText(ChapterActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createChapter(String name, int semesterId) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getInstance(this);
        Chapter newChapter = new Chapter();
        newChapter.setName(name);
        newChapter.setSemesterId(semesterId);
        newChapter.setUserId(SharedPrefManager.getInstance(this).getUserId());

        apiService.createChapter(newChapter, "Bearer " + token).enqueue(new Callback<Chapter>() {
            @Override
            public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Chapter createdChapter = response.body();
                    Toast.makeText(ChapterActivity.this, "Tạo chương thành công", Toast.LENGTH_SHORT).show();
                    clearInput();
                    // Thêm chapter mới vào danh sách từ phản hồi
                    chapterList.add(createdChapter);
                    adapter.notifyDataSetChanged();
                    // (Tùy chọn) Đồng bộ lại với server
                    fetchChapters();
                } else {
                    Log.e(TAG, "Create failed: HTTP " + response.code() + " - " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            String errorMsg = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorMsg);
                            Toast.makeText(ChapterActivity.this, "Lỗi: " + response.code() + " - " + errorMsg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChapterActivity.this, "Tạo thất bại: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to read error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Chapter> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "API failure: " + t.getMessage());
                Toast.makeText(ChapterActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateChapter(Chapter chapter) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getInstance(this);
        chapter.setUserId(SharedPrefManager.getInstance(this).getUserId());

        apiService.updateChapter(chapter.getId(), chapter, "Bearer " + token).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    GenericResponse genericResponse = response.body();
                    Log.d(TAG, "Update response: success=" + genericResponse.isSuccess() + ", message=" + genericResponse.getMessage());
                    String message = genericResponse.getMessage();
                    if (genericResponse.isSuccess() || (message != null && message.toLowerCase().contains("successfully"))) {
                        Toast.makeText(ChapterActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        clearInput();
                        fetchChapters(); // Cập nhật danh sách
                    } else {
                        Toast.makeText(ChapterActivity.this, "Cập nhật thất bại: " + message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Update failed: HTTP " + response.code() + " - " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            String errorMsg = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorMsg);
                            Toast.makeText(ChapterActivity.this, "Lỗi: " + response.code() + " - " + errorMsg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChapterActivity.this, "Lỗi cập nhật: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to read error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "API failure: " + t.getMessage());
                Toast.makeText(ChapterActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteChapter(int chapterId) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getInstance(this);

        apiService.deleteChapter(chapterId, "Bearer " + token).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.code() == 204 || (response.body() != null && response.body().isSuccess())) {
                        Toast.makeText(ChapterActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        clearInput();
                        fetchChapters(); // Cập nhật danh sách
                    } else if (response.body() != null) {
                        Toast.makeText(ChapterActivity.this, "Xóa thất bại: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChapterActivity.this, "Xóa thất bại: Không có phản hồi", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Delete failed: HTTP " + response.code() + " - " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            String errorMsg = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorMsg);
                            Toast.makeText(ChapterActivity.this, "Lỗi: " + response.code() + " - " + errorMsg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChapterActivity.this, "Lỗi xóa: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to read error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "API failure: " + t.getMessage());
                Toast.makeText(ChapterActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearInput() {
        edtChapterName.setText("");
        edtSemesterId.setText("");
        selectedChapter = null;
    }
}