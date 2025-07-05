package com.example.phygen_java.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.phygen_java.R;
import com.example.phygen_java.model.Chapter;
import com.example.phygen_java.network.ApiService;
import com.example.phygen_java.network.RetrofitClient;
import com.example.phygen_java.util.LocaleHelper;
import com.example.phygen_java.util.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        recyclerView = findViewById(R.id.recyclerChapters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiService api = RetrofitClient.getInstance();
        api.getChapters().enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChapterAdapter adapter = new ChapterAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ChapterActivity.this, "Không lấy được danh sách chương", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                Toast.makeText(ChapterActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

