package com.example.phygen_java_1.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.phygen_java_1.R;
import com.example.phygen_java_1.model.Exam;
import com.example.phygen_java_1.network.ApiService;
import com.example.phygen_java_1.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentExamActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyText;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_exam);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerRecentExams);
        progressBar = findViewById(R.id.progressBar);
        emptyText = findViewById(R.id.emptyText);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Set up toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Bài thi gần đây");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this::loadRecentExams);

        // Load data
        loadRecentExams();
    }

    private void loadRecentExams() {
        progressBar.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);

        ApiService apiService = RetrofitClient.getInstance(RecentExamActivity.this);
        apiService.getRecentExams().enqueue(new Callback<List<Exam>>() {
            @Override
            public void onResponse(Call<List<Exam>> call, Response<List<Exam>> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<Exam> exams = response.body();
                    if (exams.isEmpty()) {
                        emptyText.setVisibility(View.VISIBLE);
                        emptyText.setText("Không có bài thi gần đây");
                    } else {
                        recyclerView.setAdapter(new ExamAdapter(exams));
                    }
                } else {
                    Toast.makeText(RecentExamActivity.this, 
                        "Không thể tải danh sách bài thi gần đây", Toast.LENGTH_SHORT).show();
                    emptyText.setVisibility(View.VISIBLE);
                    emptyText.setText("Lỗi tải dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<List<Exam>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(RecentExamActivity.this, 
                    "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                emptyText.setVisibility(View.VISIBLE);
                emptyText.setText("Lỗi kết nối");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
