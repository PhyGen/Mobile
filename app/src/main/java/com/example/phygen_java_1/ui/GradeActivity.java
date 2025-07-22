package com.example.phygen_java_1.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phygen_java_1.R;
import com.example.phygen_java_1.model.Grade;
import com.example.phygen_java_1.network.ApiService;
import com.example.phygen_java_1.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GradeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        recyclerView = findViewById(R.id.recyclerGrades);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiService apiService = RetrofitClient.getInstance(GradeActivity.this);
        apiService.getGrades().enqueue(new Callback<List<Grade>>() {
            @Override
            public void onResponse(Call<List<Grade>> call, Response<List<Grade>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GradeAdapter adapter = new GradeAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(GradeActivity.this, "Không thể lấy danh sách khối", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Grade>> call, Throwable t) {
                Toast.makeText(GradeActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
