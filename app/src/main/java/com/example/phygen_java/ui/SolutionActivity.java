package com.example.phygen_java.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.phygen_java.R;
import com.example.phygen_java.model.Solution;
import com.example.phygen_java.network.ApiService;
import com.example.phygen_java.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class SolutionActivity extends AppCompatActivity {

    private RecyclerView recyclerSolutions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        recyclerSolutions = findViewById(R.id.recyclerSolutions);
        recyclerSolutions.setLayoutManager(new LinearLayoutManager(this));

        ApiService apiService = RetrofitClient.getInstance(SolutionActivity.this);
        apiService.getSolutions().enqueue(new Callback<List<Solution>>() {
            @Override
            public void onResponse(Call<List<Solution>> call, Response<List<Solution>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SolutionAdapter adapter = new SolutionAdapter(response.body());
                    recyclerSolutions.setAdapter(adapter);
                } else {
                    Toast.makeText(SolutionActivity.this, "Không lấy được dữ liệu lời giải", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Solution>> call, Throwable t) {
                Toast.makeText(SolutionActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
