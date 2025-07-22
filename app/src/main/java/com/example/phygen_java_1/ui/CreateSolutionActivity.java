package com.example.phygen_java_1.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phygen_java_1.R;
import com.example.phygen_java_1.model.Solution;
import com.example.phygen_java_1.network.ApiService;
import com.example.phygen_java_1.network.RetrofitClient;
import com.example.phygen_java_1.util.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateSolutionActivity extends AppCompatActivity {

    private EditText edtQuestionId, edtContent, edtExplanation;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_solution);

        edtQuestionId = findViewById(R.id.edtQuestionId);
        edtContent = findViewById(R.id.edtContent);
        edtExplanation = findViewById(R.id.edtExplanation);
        btnSubmit = findViewById(R.id.btnSubmitSolution);

        btnSubmit.setOnClickListener(v -> {
            int questionId = Integer.parseInt(edtQuestionId.getText().toString().trim());
            String content = edtContent.getText().toString().trim();
            String explanation = edtExplanation.getText().toString().trim();
            int createdByUserId = SharedPrefManager.getInstance(this).getUser().getId();

            Solution solution = new Solution(questionId, content, explanation, createdByUserId);

            ApiService apiService = RetrofitClient.getInstance(CreateSolutionActivity.this);

            apiService.createSolution(solution).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(CreateSolutionActivity.this, "Tạo lời giải thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(CreateSolutionActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
