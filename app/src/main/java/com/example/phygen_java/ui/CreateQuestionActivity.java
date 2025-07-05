package com.example.phygen_java.ui;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.phygen_java.R;
import com.example.phygen_java.model.Question;
import com.example.phygen_java.model.User;
import com.example.phygen_java.network.ApiService;
import com.example.phygen_java.network.RetrofitClient;
import com.example.phygen_java.util.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateQuestionActivity extends AppCompatActivity {

    private EditText inputContent, inputSource, inputLessonId;
    private Spinner spinnerDifficulty;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        inputContent = findViewById(R.id.inputContent);
        inputSource = findViewById(R.id.inputSource);
        inputLessonId = findViewById(R.id.inputLessonId);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);
        btnSubmit = findViewById(R.id.btnSubmitQuestion);

        // Setup Spinner
        String[] levels = {"Easy", "Medium", "Hard"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, levels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);

        btnSubmit.setOnClickListener(v -> submitQuestion());
    }

    private void submitQuestion() {
        String content = inputContent.getText().toString().trim();
        String source = inputSource.getText().toString().trim();
        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        int lessonId;

        try {
            lessonId = Integer.parseInt(inputLessonId.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lesson ID không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = SharedPrefManager.getInstance(this).getUser();
        if (user == null) {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        Question question = new Question(content, source, difficulty, lessonId, user.getId());

        ApiService api = RetrofitClient.getInstance();
        api.createQuestion(question).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateQuestionActivity.this, "Tạo câu hỏi thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateQuestionActivity.this, "Tạo thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CreateQuestionActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
