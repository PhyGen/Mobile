package com.example.phygen_java.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.phygen_java.R;
import com.example.phygen_java.model.Semester;
import com.example.phygen_java.network.ApiService;
import com.example.phygen_java.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SemesterActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SemesterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester);
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerSemesters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiService api = RetrofitClient.getInstance();
        api.getAllSemesters().enqueue(new Callback<List<Semester>>() {
            @Override
            public void onResponse(Call<List<Semester>> call, Response<List<Semester>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new SemesterAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(SemesterActivity.this, "Lỗi tải học kỳ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Semester>> call, Throwable t) {
                Toast.makeText(SemesterActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
