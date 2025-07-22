package com.example.phygen_java_1.ui;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phygen_java_1.R;
import com.example.phygen_java_1.model.Exam;
import com.example.phygen_java_1.model.ExamDraft;
import com.example.phygen_java_1.model.GenericResponse;
import com.example.phygen_java_1.network.ApiService;
import com.example.phygen_java_1.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateExamActivity extends AppCompatActivity {

    private ExamDraft draft = new ExamDraft(); // Lưu dữ liệu từng bước
    private int currentStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showStep(currentStep);
    }

    private void showStep(int step) {
        switch (step) {
            case 1:
                setContentView(R.layout.step1_grade);
                setupGradeStep();
                break;
            case 2:
                setContentView(R.layout.step2_semester);
                setupSemesterStep();
                break;
            case 3:
                setContentView(R.layout.step3_type);
                setupTypeStep();
                break;
            case 4:
                setContentView(R.layout.step4_questions);
                setupQuestionStep();
                break;
            case 5:
                setContentView(R.layout.step5_difficulty);
                setupDifficultyStep();
                break;
        }
    }

    private void goToNextStep() {
        if (currentStep < 5) {
            currentStep++;
            showStep(currentStep);
        } else {
            submitExam();
        }
    }

    // BƯỚC 1: CHỌN KHỐI
    private void setupGradeStep() {
        RadioGroup group = findViewById(R.id.radioGroupGrades);
        MaterialButton btnNext = findViewById(R.id.btnNextGrade);

        btnNext.setOnClickListener(v -> {
            int selectedId = group.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Vui lòng chọn khối", Toast.LENGTH_SHORT).show();
                return;
            }

            int grade = 10;
            if (selectedId == R.id.grade11) grade = 11;
            else if (selectedId == R.id.grade12) grade = 12;

            draft.setGradeId(grade);
            goToNextStep();
        });
    }

    // BƯỚC 2: CHỌN HỌC KỲ
    private void setupSemesterStep() {
        RadioGroup group = findViewById(R.id.radioGroupSemester);
        MaterialButton btnNext = findViewById(R.id.btnNextSemester);

        btnNext.setOnClickListener(v -> {
            int selectedId = group.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Vui lòng chọn học kỳ", Toast.LENGTH_SHORT).show();
                return;
            }

            int semester = selectedId == R.id.semester2 ? 2 : 1;
            draft.setSemesterId(semester);
            goToNextStep();
        });
    }

    // BƯỚC 3: CHỌN LOẠI ĐỀ
    private void setupTypeStep() {
        RadioGroup group = findViewById(R.id.radioGroupExamType);
        MaterialButton btnNext = findViewById(R.id.btnNextType);

        btnNext.setOnClickListener(v -> {
            int typeId = 1;
            if (group.getCheckedRadioButtonId() == R.id.typeSemester) {
                typeId = 2;
            }
            draft.setExamTypeId(typeId);
            goToNextStep();
        });
    }

    // BƯỚC 4: CHỌN CÂU HỎI
    private void setupQuestionStep() {
        CheckBox cb1 = findViewById(R.id.checkboxQuestion1);
        CheckBox cb2 = findViewById(R.id.checkboxQuestion2);
        CheckBox cb3 = findViewById(R.id.checkboxQuestion3);
        MaterialButton btnNext = findViewById(R.id.btnNextQuestions);

        btnNext.setOnClickListener(v -> {
            List<Integer> selected = new ArrayList<>();
            if (cb1.isChecked()) selected.add(1);
            if (cb2.isChecked()) selected.add(2);
            if (cb3.isChecked()) selected.add(3);

            if (selected.isEmpty()) {
                Toast.makeText(this, "Chọn ít nhất 1 câu hỏi", Toast.LENGTH_SHORT).show();
                return;
            }

            draft.setQuestionIds(selected); // sửa đúng biến
            goToNextStep();
        });
    }

    // BƯỚC 5: CHỌN ĐỘ KHÓ
    private void setupDifficultyStep() {
        RadioGroup group = findViewById(R.id.radioGroupDifficulty);
        MaterialButton btnSubmit = findViewById(R.id.btnSubmitExam);

        btnSubmit.setOnClickListener(v -> {
            int selectedId = group.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Vui lòng chọn độ khó", Toast.LENGTH_SHORT).show();
                return;
            }

            String level = "Dễ";
            if (selectedId == R.id.medium) level = "Trung bình";
            else if (selectedId == R.id.hard) level = "Khó";

            draft.setDifficulty(level);
            submitExam();
        });
    }

    // GỬI LÊN API
    private void submitExam() {
        Exam exam = new Exam();
        exam.setLessonId(draft.getGradeId()); // tạm dùng grade là lesson
        exam.setExamTypeId(draft.getExamTypeId());

        ApiService apiService = RetrofitClient.getInstance(CreateExamActivity.this);
        apiService.createExam(exam).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateExamActivity.this, "Tạo đề thành công", Toast.LENGTH_SHORT).show();
                    finish(); // hoặc chuyển sang màn MyExamActivity
                } else {
                    Toast.makeText(CreateExamActivity.this, "Lỗi tạo đề", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(CreateExamActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
